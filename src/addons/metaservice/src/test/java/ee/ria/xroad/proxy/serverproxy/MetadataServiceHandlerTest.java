/**
 * The MIT License
 * Copyright (c) 2015 Estonian Information System Authority (RIA), Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ee.ria.xroad.proxy.serverproxy;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import ee.ria.xroad.common.CodedException;
import ee.ria.xroad.common.ErrorCodes;
import ee.ria.xroad.common.SystemProperties;
import ee.ria.xroad.common.conf.globalconf.GlobalConf;
import ee.ria.xroad.common.conf.serverconf.ServerConf;
import ee.ria.xroad.common.conf.serverconf.model.ClientType;
import ee.ria.xroad.common.conf.serverconf.model.ServerConfType;
import ee.ria.xroad.common.conf.serverconf.model.ServiceType;
import ee.ria.xroad.common.conf.serverconf.model.WsdlType;
import ee.ria.xroad.common.identifier.ClientId;
import ee.ria.xroad.common.identifier.ServiceId;
import ee.ria.xroad.common.message.SoapHeader;
import ee.ria.xroad.common.metadata.MethodListType;
import ee.ria.xroad.common.metadata.ObjectFactory;
import ee.ria.xroad.common.opmonitoring.OpMonitoringData;
import ee.ria.xroad.common.util.MimeTypes;
import ee.ria.xroad.proxy.common.WsdlRequestData;
import ee.ria.xroad.proxy.conf.KeyConf;
import ee.ria.xroad.proxy.protocol.ProxyMessage;
import ee.ria.xroad.proxy.testsuite.TestGlobalConf;
import ee.ria.xroad.proxy.testsuite.TestKeyConf;
import ee.ria.xroad.proxy.testsuite.TestServerConf;
import ee.ria.xroad.proxy.util.MetaserviceTestUtil;
import lombok.Getter;
import org.apache.http.client.HttpClient;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.AbstractContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.MimeConfig;
import org.junit.*;
import org.junit.contrib.java.lang.system.ProvideSystemProperty;
import org.junit.rules.ExpectedException;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static ee.ria.xroad.common.ErrorCodes.X_UNKNOWN_SERVICE;
import static ee.ria.xroad.common.conf.serverconf.ServerConfDatabaseCtx.doInTransaction;
import static ee.ria.xroad.common.metadata.MetadataRequests.*;
import static ee.ria.xroad.common.util.MimeTypes.TEXT_XML_UTF8;
import static ee.ria.xroad.common.util.MimeUtils.HEADER_CONTENT_TYPE;
import static ee.ria.xroad.proxy.util.MetaserviceTestUtil.CodedExceptionMatcher.faultCodeEquals;
import static ee.ria.xroad.proxy.util.MetaserviceTestUtil.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link MetadataServiceHandlerImpl}
 */
public class MetadataServiceHandlerTest {

    private static final String EXPECTED_XR_INSTANCE = "EE";
    private static final ClientId DEFAULT_CLIENT = ClientId.create(EXPECTED_XR_INSTANCE, "GOV",
            "1234TEST_CLIENT", "SUBCODE5");

    private static final String EXPECTED_WSDL_QUERY_PATH = "/wsdlMock";

    private static final int WSDL_SERVER_PORT = 9858;
    // the uri from which the WSDL can be found by the meta service
    private static final String MOCK_SERVER_WSDL_URL =
            "http://localhost:" + WSDL_SERVER_PORT + EXPECTED_WSDL_QUERY_PATH;

    private static Unmarshaller unmarshaller;
    private static MessageFactory messageFactory;
    private static Marshaller marshaller;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public final ProvideSystemProperty hibernatePropertiesProperty
            = new ProvideSystemProperty(SystemProperties.DATABASE_PROPERTIES,
            "src/test/resources/hibernate.properties");


    private HttpClient httpClientMock;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private MetaserviceTestUtil.StubServletOutputStream mockServletOutputStream;
    private ProxyMessage mockProxyMessage;
    private WireMockServer mockServer;


    /**
     * Init class-wide test instances
     */
    @BeforeClass
    public static void initCommon() throws JAXBException, SOAPException {
        unmarshaller = JAXBContext.newInstance(ObjectFactory.class, SoapHeader.class)
                .createUnmarshaller();
        messageFactory = MessageFactory.newInstance();
        marshaller = JAXBContext.newInstance(WsdlRequestData.class)
                .createMarshaller();
    }

    /**
     * Init data for tests
     */
    @Before
    public void init() throws IOException {

        GlobalConf.reload(new TestGlobalConf());
        KeyConf.reload(new TestKeyConf());
        ServerConf.reload(new TestServerConf());

        httpClientMock = mock(HttpClient.class);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);

        mockServletOutputStream = new MetaserviceTestUtil.StubServletOutputStream();
        when(mockResponse.getOutputStream()).thenReturn(mockServletOutputStream);

        mockProxyMessage = mock(ProxyMessage.class);

        when(mockProxyMessage.getSoapContentType()).thenReturn(MimeTypes.TEXT_XML_UTF8);

        this.mockServer = new WireMockServer(options().port(WSDL_SERVER_PORT));
    }

    @After
    public void tearDown() throws Exception {
        this.mockServer.stop();
        MetaserviceTestUtil.cleanDB();
    }


    @Test
    public void shouldBeAbleToHandleListMethods() throws Exception {

        // setup

        MetadataServiceHandlerImpl handlerToTest = new MetadataServiceHandlerImpl();

        ServiceId serviceId = ServiceId.create(DEFAULT_CLIENT, LIST_METHODS);

        InputStream soapContentInputStream = new TestSoapBuilder()
                .withClient(DEFAULT_CLIENT)
                .withService(serviceId)
                .withModifiedBody(
                        soapBody -> soapBody.addChildElement(LIST_METHODS_REQUEST).addChildElement(REQUEST))
                .buildAsInputStream();

        when(mockProxyMessage.getSoapContent()).thenReturn(soapContentInputStream);

        // execution & verification

        assertTrue("Wasn't able to handle list methods", handlerToTest.canHandle(serviceId, mockProxyMessage));
    }

    @Test
    public void shouldBeAbleToHandleAllowedMethodsMethods() throws Exception {

        // setup

        MetadataServiceHandlerImpl handlerToTest = new MetadataServiceHandlerImpl();

        ServiceId serviceId = ServiceId.create(DEFAULT_CLIENT, ALLOWED_METHODS);

        InputStream soapContentInputStream = new TestSoapBuilder()
                .withClient(DEFAULT_CLIENT)
                .withService(serviceId)
                .withModifiedBody(
                        soapBody -> soapBody.addChildElement(ALLOWED_METHODS_REQUEST).addChildElement(REQUEST))
                .buildAsInputStream();

        when(mockProxyMessage.getSoapContent()).thenReturn(soapContentInputStream);

        // execution & verification

        assertTrue("Wasn't able to handle allowed methods",
                handlerToTest.canHandle(serviceId, mockProxyMessage));
    }


    @Test
    public void shouldBeAbleToHandleGetWsdl() throws Exception {

        // setup

        MetadataServiceHandlerImpl handlerToTest = new MetadataServiceHandlerImpl();

        ServiceId serviceId = ServiceId.create(DEFAULT_CLIENT, GET_WSDL);

        InputStream soapContentInputStream = new TestSoapBuilder()
                .withClient(DEFAULT_CLIENT)
                .withService(serviceId)
                .withModifiedBody(
                        soapBody -> soapBody.addChildElement(GET_WSDL_REQUEST).addChildElement(REQUEST))
                .buildAsInputStream();

        when(mockProxyMessage.getSoapContent()).thenReturn(soapContentInputStream);

        // execution & verification
        assertTrue("Wasn't able to handle get wsdl",
                handlerToTest.canHandle(serviceId, mockProxyMessage));
    }

    @Test
    public void shouldHandleListMethods() throws Exception {

        // setup
        List<ServiceId> expectedServices = Arrays.asList(
                ServiceId.create(DEFAULT_CLIENT, "getNumber"),
                ServiceId.create(DEFAULT_CLIENT, "helloThere"),
                ServiceId.create(DEFAULT_CLIENT, "putThings"));

        final ClientId expectedClient = DEFAULT_CLIENT;
        final ServiceId serviceId = ServiceId.create(DEFAULT_CLIENT, LIST_METHODS);

        ServerConf.reload(new TestServerConf() {
            @Override
            public List<ServiceId> getAllServices(ClientId serviceProvider) {
                assertThat("Client id does not match expected", serviceProvider, is(expectedClient));
                return expectedServices;
            }
        });

        MetadataServiceHandlerImpl handlerToTest = new MetadataServiceHandlerImpl();

        InputStream soapContentInputStream = new TestSoapBuilder()
                .withClient(DEFAULT_CLIENT)
                .withService(serviceId)
                .withModifiedBody(
                        soapBody -> soapBody.addChildElement(LIST_METHODS_REQUEST).addChildElement(REQUEST))
                .buildAsInputStream();

        when(mockProxyMessage.getSoapContent()).thenReturn(soapContentInputStream);

        handlerToTest.canHandle(serviceId, mockProxyMessage);

        // execution

        handlerToTest.startHandling(mockRequest, mockProxyMessage,
                httpClientMock, mock(OpMonitoringData.class));

        // verification
        assertThat("Content type does not match", handlerToTest.getResponseContentType(), is(TEXT_XML_UTF8));

        final SOAPMessage message = messageFactory.createMessage(null, handlerToTest.getResponseContent());

        final SoapHeader xrHeader = unmarshaller.unmarshal(message.getSOAPHeader(), SoapHeader.class).getValue();

        List<ServiceId> resultServices = verifyAndGetSingleBodyElementOfType(message.getSOAPBody(),
                MethodListType.class).getService();

        assertThat("Response client does not match", xrHeader.getClient(), is(expectedClient));
        assertThat("Response client does not match", xrHeader.getService(), is(serviceId));

        assertThat("Wrong amount of services",
                resultServices.size(), is(expectedServices.size()));

        assertThat("Wrong services", resultServices, containsInAnyOrder(expectedServices.toArray()));
    }

    @Test
    public void shouldHandleAllowedMethods() throws Exception {

        // setup
        List<ServiceId> expectedServices = Arrays.asList(
                ServiceId.create(DEFAULT_CLIENT, "getNumber"),
                ServiceId.create(DEFAULT_CLIENT, "helloThere"),
                ServiceId.create(DEFAULT_CLIENT, "putThings"));

        final ClientId expectedClient = DEFAULT_CLIENT;
        final ServiceId serviceId = ServiceId.create(DEFAULT_CLIENT, ALLOWED_METHODS);


        ServerConf.reload(new TestServerConf() {

            @Override
            public List<ServiceId> getAllowedServices(ClientId serviceProvider, ClientId client) {

                assertThat("Wrong client in query", client, is(expectedClient));

                assertThat("Wrong service provider in query", serviceProvider, is(serviceId.getClientId()));

                return expectedServices;
            }
        });

        MetadataServiceHandlerImpl handlerToTest = new MetadataServiceHandlerImpl();

        InputStream soapContentInputStream = new TestSoapBuilder()
                .withClient(DEFAULT_CLIENT)
                .withService(serviceId)
                .withModifiedBody(
                        soapBody -> soapBody.addChildElement(ALLOWED_METHODS_REQUEST).addChildElement(REQUEST))
                .buildAsInputStream();

        when(mockProxyMessage.getSoapContent()).thenReturn(soapContentInputStream);

        handlerToTest.canHandle(serviceId, mockProxyMessage);

        // execution

        handlerToTest.startHandling(mockRequest, mockProxyMessage,
                httpClientMock, mock(OpMonitoringData.class));

        // verification
        assertThat("Content type does not match", handlerToTest.getResponseContentType(), is(TEXT_XML_UTF8));

        final SOAPMessage message = messageFactory.createMessage(null, handlerToTest.getResponseContent());

        final SoapHeader xrHeader = unmarshaller.unmarshal(message.getSOAPHeader(), SoapHeader.class).getValue();

        List<ServiceId> resultServices = verifyAndGetSingleBodyElementOfType(message.getSOAPBody(),
                MethodListType.class).getService();

        assertThat("Response client does not match", xrHeader.getClient(), is(expectedClient));
        assertThat("Response client does not match", xrHeader.getService(), is(serviceId));

        assertThat("Wrong amount of services",
                resultServices.size(), is(expectedServices.size()));

        assertThat("Wrong services", resultServices, containsInAnyOrder(expectedServices.toArray()));
    }


    @Test
    public void shouldThrowWhenMissingServiceCodeInWsdlRequestBody() throws Exception {

        final ServiceId serviceId = ServiceId.create(DEFAULT_CLIENT, GET_WSDL);

        MetadataServiceHandlerImpl handlerToTest = new MetadataServiceHandlerImpl();

        InputStream soapContentInputStream = new TestSoapBuilder()
                .withClient(DEFAULT_CLIENT)
                .withService(serviceId)
                .withModifiedBody(
                        soapBody -> soapBody.addChildElement(GET_WSDL_REQUEST).addChildElement(REQUEST))
                .buildAsInputStream();

        when(mockProxyMessage.getSoapContent()).thenReturn(soapContentInputStream);

        handlerToTest.canHandle(serviceId, mockProxyMessage);

        thrown.expect(CodedException.class);
        thrown.expect(faultCodeEquals(ErrorCodes.X_INVALID_REQUEST));
        thrown.expectMessage(containsString("Missing serviceCode in message body"));

        // execution, should throw..

        handlerToTest.startHandling(mockRequest, mockProxyMessage,
                httpClientMock, mock(OpMonitoringData.class));
    }

    @Test
    public void shouldThrowUnknownServiceWhenWsdlUrlNotFound() throws Exception {

        final ServiceId serviceId = ServiceId.create(DEFAULT_CLIENT, GET_WSDL);
        final ServiceId requestingWsdlForService = ServiceId.create(DEFAULT_CLIENT, "someServiceWithoutWsdl");

        MetadataServiceHandlerImpl handlerToTest = new MetadataServiceHandlerImpl();

        WsdlRequestData wsdlRequestData = new WsdlRequestData();
        wsdlRequestData.setServiceCode(requestingWsdlForService.getServiceCode());

        InputStream soapContentInputStream = new TestSoapBuilder()
                .withClient(DEFAULT_CLIENT)
                .withService(serviceId)
                .withModifiedBody(
                        soapBody -> marshaller.marshal(wsdlRequestData, soapBody))
                .buildAsInputStream();

        when(mockProxyMessage.getSoapContent()).thenReturn(soapContentInputStream);

        handlerToTest.canHandle(serviceId, mockProxyMessage);

        thrown.expect(CodedException.class);
        thrown.expect(faultCodeEquals(X_UNKNOWN_SERVICE));
        thrown.expectMessage(containsString("Could not find wsdl URL for service"));

        // execution, should throw..

        handlerToTest.startHandling(mockRequest, mockProxyMessage,
                httpClientMock, mock(OpMonitoringData.class));
    }

    @Test
    public void shouldThrowRuntimeExWhenWsdlUrlNotOk200() throws Exception {

        final ServiceId serviceId = ServiceId.create(DEFAULT_CLIENT, GET_WSDL);
        final ServiceId requestingWsdlForService = ServiceId.create(DEFAULT_CLIENT, "someServiceWithWsdl122");

        MetadataServiceHandlerImpl handlerToTest = new MetadataServiceHandlerImpl();

        WsdlRequestData wsdlRequestData = new WsdlRequestData();
        wsdlRequestData.setServiceCode(requestingWsdlForService.getServiceCode());

        InputStream soapContentInputStream = new TestSoapBuilder()
                .withClient(DEFAULT_CLIENT)
                .withService(serviceId)
                .withModifiedBody(
                        soapBody -> marshaller.marshal(wsdlRequestData, soapBody))
                .buildAsInputStream();

        when(mockProxyMessage.getSoapContent()).thenReturn(soapContentInputStream);

        setUpDatabase(requestingWsdlForService);


        mockServer.stubFor(WireMock.any(urlPathEqualTo(EXPECTED_WSDL_QUERY_PATH))
                .willReturn(aResponse().withStatus(HttpServletResponse.SC_FORBIDDEN)));
        mockServer.start();


        handlerToTest.canHandle(serviceId, mockProxyMessage);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(containsString("Received HTTP error: 403 - Forbidden"));

        // execution, should throw..

        handlerToTest.startHandling(mockRequest, mockProxyMessage,
                httpClientMock, mock(OpMonitoringData.class));
    }

    @Test
    public void shouldHandleGetWsdl() throws Exception {

        final ServiceId serviceId = ServiceId.create(DEFAULT_CLIENT, GET_WSDL);
        final ServiceId requestingWsdlForService = ServiceId.create(DEFAULT_CLIENT, "someServiceWithWsdl122");

        MetadataServiceHandlerImpl handlerToTest = new MetadataServiceHandlerImpl();

        WsdlRequestData wsdlRequestData = new WsdlRequestData();
        wsdlRequestData.setServiceCode(requestingWsdlForService.getServiceCode());

        InputStream soapContentInputStream = new TestSoapBuilder()
                .withClient(DEFAULT_CLIENT)
                .withService(serviceId)
                .withModifiedBody(
                        soapBody -> marshaller.marshal(wsdlRequestData, soapBody))
                .buildAsInputStream();

        when(mockProxyMessage.getSoapContent()).thenReturn(soapContentInputStream);

        setUpDatabase(requestingWsdlForService);


        mockServer.stubFor(WireMock.any(urlPathEqualTo(EXPECTED_WSDL_QUERY_PATH))
                .willReturn(aResponse().withBodyFile("wsdl.wsdl")));
        mockServer.start();


        final List<String> expectedWSDLServiceNames =
                Arrays.asList("getRandom", "helloService");

        when(mockResponse.getOutputStream()).thenReturn(mockServletOutputStream);


        handlerToTest.canHandle(serviceId, mockProxyMessage);

        // execution

        handlerToTest.startHandling(mockRequest, mockProxyMessage,
                httpClientMock, mock(OpMonitoringData.class));

        // verification
        assertThat("Content type does not match", handlerToTest.getResponseContentType(),
                containsString("multipart/related; type=\"text/xml\"; charset=UTF-8;"));

        TestMimeContentHandler handler = parseWsdlResponse(handlerToTest.getResponseContent(),
                // this response content type and the headless parsing is some super funky business
                handlerToTest.getResponseContentType());

        SoapHeader xrHeader = handler.getXrHeader();
        assertThat("Response client does not match", xrHeader.getService(), is(serviceId));

        final List<String> operationNames = handler.getOperationNames();

        assertThat("Expected to find certain operations",
                operationNames,
                containsInAnyOrder(expectedWSDLServiceNames.toArray()));
    }


    private void setUpDatabase(ServiceId serviceId) throws Exception {
        ServerConfType conf = new ServerConfType();
        conf.setServerCode("TestServer");

        ClientType client = new ClientType();
        client.setConf(conf);

        conf.getClient().add(client);

        client.setIdentifier(serviceId.getClientId());

        WsdlType wsdl = new WsdlType();
        wsdl.setClient(client);
        wsdl.setUrl(MOCK_SERVER_WSDL_URL);
        wsdl.setWsdlLocation("wsdlLocation");

        ServiceType service = new ServiceType();
        service.setWsdl(wsdl);
        service.setTitle("someTitle");
        service.setServiceCode(serviceId.getServiceCode());

        wsdl.getService().add(service);

        client.getWsdl().add(wsdl);

        doInTransaction(session -> {
            session.save(conf);
            return null;
        });

    }

    private TestMimeContentHandler parseWsdlResponse(InputStream inputStream, String headlessContentType)
            throws IOException, MimeException {
        MimeConfig config = new MimeConfig.Builder().setHeadlessParsing(headlessContentType).build();
        MimeStreamParser parser = new MimeStreamParser(config);
        TestMimeContentHandler contentHandler = new TestMimeContentHandler();
        parser.setContentHandler(contentHandler);
        parser.parse(inputStream);
        return contentHandler;
    }


    private static class TestMimeContentHandler extends AbstractContentHandler {

        @Getter
        private SoapHeader xrHeader;
        private SOAPMessage message;
        @Getter
        private List<String> operationNames;

        private String partContentType;

        @Override
        public void startHeader() throws MimeException {
            partContentType = null;
        }

        @Override
        public void field(Field field) throws MimeException {
            if (field.getName().toLowerCase().equals(HEADER_CONTENT_TYPE)) {
                partContentType = field.getBody();
            }
        }

        @Override
        public void body(BodyDescriptor bd, InputStream is) throws MimeException, IOException {
            try {
                message = (message != null) ? message : messageFactory.createMessage(null, is);
            } catch (SOAPException e) {
                throw new MimeException(e);
            }

            if (xrHeader == null) {
                try {
                    xrHeader = unmarshaller.unmarshal(message.getSOAPHeader(),
                            SoapHeader.class).getValue();
                } catch (SOAPException | JAXBException e) {
                    throw new MimeException(e);
                }
            } else {
                if (partContentType != null) {
                    try {
                        WSDLReader wsdlReader = WSDLFactory.newInstance().newWSDLReader();
                        Definition definition = wsdlReader.readWSDL(null, new InputSource(is));

                        operationNames = parseOperationNamesFromWSDLDefinition(definition);

                    } catch (WSDLException e) {
                        throw new MimeException(e);
                    }
                }
            }
        }
    }
}
