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
package ee.ria.xroad.signer.protocol.handler;

import static ee.ria.xroad.common.ErrorCodes.X_KEY_NOT_FOUND;
import static ee.ria.xroad.common.util.CryptoUtils.readCertificate;
import static ee.ria.xroad.signer.util.ExceptionHelper.tokenNotActive;
import static ee.ria.xroad.signer.util.ExceptionHelper.tokenNotInitialized;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.cert.ocsp.OCSPResp;

import ee.ria.xroad.common.CodedException;
import ee.ria.xroad.common.conf.globalconf.GlobalConf;
import ee.ria.xroad.common.conf.globalconfextension.GlobalConfExtensions;
import ee.ria.xroad.common.identifier.SecurityServerId;
import ee.ria.xroad.common.ocsp.OcspVerifier;
import ee.ria.xroad.common.ocsp.OcspVerifierOptions;
import ee.ria.xroad.common.util.CertUtils;
import ee.ria.xroad.common.util.PasswordStore;
import ee.ria.xroad.signer.protocol.AbstractRequestHandler;
import ee.ria.xroad.signer.protocol.dto.AuthKeyInfo;
import ee.ria.xroad.signer.protocol.dto.CertificateInfo;
import ee.ria.xroad.signer.protocol.dto.KeyInfo;
import ee.ria.xroad.signer.protocol.dto.KeyUsageInfo;
import ee.ria.xroad.signer.protocol.dto.TokenInfo;
import ee.ria.xroad.signer.protocol.message.GetAuthKey;
import ee.ria.xroad.signer.tokenmanager.TokenManager;
import ee.ria.xroad.signer.tokenmanager.module.SoftwareModuleType;
import ee.ria.xroad.signer.tokenmanager.token.SoftwareTokenType;
import ee.ria.xroad.signer.tokenmanager.token.SoftwareTokenUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles authentication key retrieval requests.
 */
@Slf4j
public class GetAuthKeyRequestHandler
        extends AbstractRequestHandler<GetAuthKey> {

    @Override
    protected Object handle(GetAuthKey message) throws Exception {
        log.trace("Selecting authentication key for security server {}",
                message.getSecurityServer());

        if (!SoftwareTokenUtil.isTokenInitialized()) {
            throw tokenNotInitialized(SoftwareTokenType.ID);
        }

        if (!TokenManager.isTokenActive(SoftwareTokenType.ID)) {
            throw tokenNotActive(SoftwareTokenType.ID);
        }

        for (TokenInfo tokenInfo : TokenManager.listTokens()) {
            if (!SoftwareModuleType.TYPE.equals(tokenInfo.getType())) {
                log.trace("Ignoring {} module", tokenInfo.getType());
                continue;
            }

            for (KeyInfo keyInfo : tokenInfo.getKeyInfo()) {
                if (keyInfo.getUsage() != KeyUsageInfo.AUTHENTICATION) {
                    log.trace("Ignoring {} key {}", keyInfo.getUsage(),
                            keyInfo.getId());
                    continue;
                }

                if (!keyInfo.isAvailable()) {
                    log.trace("Ignoring unavailable key {}", keyInfo.getId());
                    continue;
                }

                for (CertificateInfo certInfo : keyInfo.getCerts()) {
                    if (authCertValid(certInfo, message.getSecurityServer())) {
                        log.trace("Found suitable authentication key {}",
                                keyInfo.getId());
                        return authKeyResponse(keyInfo, certInfo);
                    }
                }
            }
        }

        throw CodedException.tr(X_KEY_NOT_FOUND,
                "auth_key_not_found_for_server",
                "Could not find active authentication key for "
                        + "security server '%s'", message.getSecurityServer());
    }

    private AuthKeyInfo authKeyResponse(KeyInfo keyInfo,
            CertificateInfo certInfo) throws Exception {
        String alias = keyInfo.getId();
        String keyStoreFileName = SoftwareTokenUtil.getKeyStoreFileName(alias);
        char[] password = PasswordStore.getPassword(SoftwareTokenType.ID);

        return new AuthKeyInfo(alias, keyStoreFileName, password, certInfo);
    }

    private boolean authCertValid(CertificateInfo certInfo,
            SecurityServerId securityServer) throws Exception {
        X509Certificate cert = readCertificate(certInfo.getCertificateBytes());

        if (!certInfo.isActive()) {
            log.trace("Ignoring inactive authentication certificate {}",
                    CertUtils.identify(cert));

            return false;
        }

        if (!isRegistered(certInfo.getStatus())) {
            log.trace("Ignoring non-registered ({}) authentication certificate"
                    + " {}", certInfo.getStatus(), CertUtils.identify(cert));

            return false;
        }

        SecurityServerId serverIdFromConf = GlobalConf.getServerId(cert);
        try {
            cert.checkValidity();

            if (securityServer.equals(serverIdFromConf)) {
                verifyOcspResponse(securityServer.getXRoadInstance(), cert,
                        certInfo.getOcspBytes(), new OcspVerifierOptions(
                                GlobalConfExtensions.getInstance()
                                        .shouldVerifyOcspNextUpdate()));

                return true;
            }
        } catch (Exception e) {
            log.warn("Ignoring authentication certificate '{}' because: ",
                    cert.getSubjectX500Principal().getName(), e);

            return false;
        }

        log.trace("Ignoring authentication certificate {} because it does "
                + "not belong to security server {} "
                + "(server id from global conf: {})", new Object[] {
                        CertUtils.identify(cert),
                        securityServer, serverIdFromConf});
        
        return false;
    }

    private void verifyOcspResponse(String instanceIdentifier,
            X509Certificate subject, byte[] ocspBytes, OcspVerifierOptions verifierOptions) throws Exception {
        if (ocspBytes == null) {
            throw new CertificateException("OCSP response not found");
        }

        OCSPResp ocsp = new OCSPResp(ocspBytes);
        X509Certificate issuer =
                GlobalConf.getCaCert(instanceIdentifier, subject);
        OcspVerifier verifier =
                new OcspVerifier(GlobalConf.getOcspFreshnessSeconds(false), verifierOptions);
        verifier.verifyValidityAndStatus(ocsp, subject, issuer);
    }

    private static boolean isRegistered(String status) {
        return status != null
                && status.startsWith(CertificateInfo.STATUS_REGISTERED);
    }

}
