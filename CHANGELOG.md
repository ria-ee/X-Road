# Change Log

## 6.11.1 - 2017-03-13
- XTE-99 / Joint development issue #79: Security Server UI: Added uniqueness check of the entered security server code when initializing the server.
- XTE-252 / Joint development issue #53: Security Server: Upgraded embedded Jetty to the version 9.4.2.
- XTE-293: Security Server: A field set used to generate the token ID of the SSCD has been made configurable.
- XTE-294 / Joint development issue #84: Security Server: Added configuration file for the OCSP responder Jetty server (and increased max threads size of the thread pool).
- XTE-307 / Joint development issue #131: Security Server bugfix: Added missing HTTP header "Connection: close" into the server proxy response in cases error occurs before parsing a service provider's response.
- XTE-308 / Joint development issue #132: Security Server bugfix: Added missing read timeout for OCSP responder client.
- XTE-310 / Joint development issue #125: Security Server bugfix: SOAP messages with attachments caused in some cases a temopray file handle leak.
- XTE-333 / Joint development issue #128: Security Server bugfix: Fixed parsing SOAP messages containing &amp; or &lt; entities.
- Security Server: TCP socket SO_LINGER values in the proxy configuration file (proxy.ini) set to -1 according to avoid unexpected data stream closures.

## 6.11.0 - 2017-03-01
- PVAYLADEV-609 / PVAYLADEV-703 / Joint development issue #120: Added a partial index to the messagelog database to speed up retrieval of messages requiring timestamping. This should increase proxy performance in cases where the logrecord table is large.
- PVAYLADEV-685 / Joint development issue #121: Added a system property to deactivate signer's periodic OCSP-response retrieval on both central server and configuration proxy.

## 6.10.0 - 2017-02-15
- PVAYLADEV-684: Change source code directory structure so that doc folder moves to root level and xtee6 folder is renamed to src. Checkstyle configuration moves to src from doc.
- PVAYLADEV-670: The document DM-CS central server data model was converted to markdown format and the included ER diagram was done with draw.io tool.
- PVAYLADEV-253: Serverproxy ensures that client certificate belongs to registered security server before reading the SOAP message.
- PVAYLADEV-369: Environmental monitoring port configuration system property group monitor has been renamed to env-monitor. If the system property monitor.port was previously configured, it has to be done again using env-monitor.port. Monitor sensor intervals are now also configurable as system properties.
- PVAYLADEV-657: Added version history table and license text to markdown documentation.
- PVAYLADEV-661: Packaging the software is done in Docker container. This includes both deb and rpm packaging.
- PVAYLADEV-675: Fixed problem in central server and security server user interface's file upload component. The problem caused the component not to clear properly on close.
- PVAYLADEV-680: Fixed problem in Debian changelog that caused warnings on packaging.
- PVAYLADEV-682: Added Ansible scripts to create test automation environment.
- PVAYLADEV-547: Added Vagrantfile for creating X-Road development boxes. It is possible to run X-Road servers in LXD containers inside the development box.

## 6.9.4 - 2017-02-13
- XTE-301: Security Server UI bugfix: race condition of the adding a new client caused duplicates
- XTE-319: Security Server UI bugfix: WSDL deletion caused incorrect ACL removal
- XTE-322: Security Server bugfix: a typo in the configuration file proxy.ini (client-timeout)

## 6.9.3 - 2017-02-10
- PVAYLADEV-691: Hotfix for ExecListingSensor init. (Fixes package listing information, etc)

## 6.9.2 - 2017-01-23
- PVAYLADEV-662: Fixed proxy memory parameters
- PVAYLADEV-662: Altered OCSP fetch interval default value from 3600 to 1200 seconds
- PVAYLADEV-662: Converted DM-ML document to markdown format
- PVAYLADEV-662: Fixed bug in handling HW token's PIN code
- PVAYLADEV-662: Fixed bug in prepare_buildhost.sh script that caused build to fail on a clean machine
- PVAYLADEV-656: Added a reading timeout of 60 seconds for OcspClient connections
- PVAYLADEV-666: Fixed bug that caused metaservices and environmental monitoring replies to be returned in multipart format

## 6.9.1 - 2017-01-13
- Updated documents: ARC-OPMOND, UG-OPMONSYSPAR, UG-SS, PR-OPMON, PR-OPMONJMX, TEST_OPMON, TEST_OPMONSTRAT, UC-OPMON
- Updated example Zabbix related scripts and configuration files

## 6.9.0 - 2017-01-06

- PVAYLADEV-505: Fixed a bug in Security Server's UI that was causing the text in the pop-window to be messed with HTML code in some cases when "Remove selected" button was clicked.
- PVAYLADEV-484: Changed the value of Finnish setting for time-stamping of messages (acceptable-timestamp-failure-period parameter). Value was increased from value 1800s to value 18000s. By this change faults related to time-stamping functionality will be decreased.
- PVAYLADEV-475 / Joint Development issue #70: Security Server's connection pool functionality improved so that existing and already opened connections will re-used more effectively. Old implementation was always opening a new connection when a new message was sent. This new functionality will boost performance of Security Server several percents. Global settings use the old functionality by default but the Finnish installation packages override the global setting, opting to use the improvements. See the system parameters documentation UG-SYSPAR for more details.  
- PVAYLADEV-523: An Ansible script for installing the test-CA was created and published in the Github repository. The script will execute the installation automatically without any manual actions needed.
- PVAYLADEV-536: Improved OCSP diagnostics and logging when handling a certificate issued by an intermediate (non-root) certificate authority: The certificates are now listed in OCSP diagnostics and a false positive error message is no longer logged.
- PVAYLADEV-467 / PVAYLADEV-468 / PVAYLADEV-469 / PVAYLADEV-553 / Joint Development issue #81: Several improvements and checks to make sure there are no security threats related to Security Server's user rights and system generated files:
  - /var/tmp/xroad and /var/log/xroad folders checked and user rights validated
  - /var/lib/xroad folder user rights restricted
  - /var/log/secure and /var/log/up2date folders checked through and validated if files generated here are necessary
  - /var/log/messages folder checked through and validated if files generated here are necessary. Fixed functionality so that xroad-confclient is not writing log files to this folder if it is running as a service.
  - N.B! if there are monitoring actions and processed related to xroad-confclient, that are using log files of this folder, the configuration of monitoring must be changed so that the source of logs is from now on /var/log/xroad folder.
- PVAYLADEV-556: All installed additional parts of Central Server are seen on UI of Central Server. Earlier some parts that where installed could not be seen on UI.
- PVAYLADEV-531: Fixed the bug in functionality of "Unregister" dialog window in security server's "Keys and Certificates" -view so that no nonsensical error messages are shown to user. Erroneous notification was shown if user had created an authentication certificate and then made a request to register it and immediately canceled the request before it was accepted. This caused an unexpected error text from the Keys -table to be translated and the subsequent message to be shown to the user. The underlying error was a fixed removing any unnecessary error messages.
- PVAYLADEV-560 / Joint Development issue #65: Improved the handling of OCSP responses at startup phase of Security Server. If at startup the global configuration is expired then the next OCSP validation is scheduled within one minute. In earlier versions this was scheduled within one hour and caused extra delay until OCSP status was 'good'. Also, error message 'Server has no valid authentication' was generated.
- PVAYLADEV-489 / PVAYLADEV-571 / Joint Development issue #69: From version 6.9.0 Security Server is supporting new XML schema that makes possible to use a set of different Global Configuration versions. This makes possible that Global Configuration can be updated without breaking the compatibility to the Security Servers that are still using the older version of Global Configuration. Each Security Server knows the correct Global Configuration version it is using and based on this information is able to request that version from the Central Server. Central Server in turn is able to distribute all the Global Configurations that might be in use.
- PVAYLADEV-570 / Joint Development issue #69: From version 6.9.0 Configuration Proxy supports a new XML schema that makes it possible to use a set of different Global Configuration versions. Configuration Proxy can download, cache and distribute all the Global Configurations that might be in use.
- PVAYLADEV-588 / Joint Development issue #64: Fixed a bug that caused Security Server to start doing duplicate OCSP fetches at the same time. This happened if two or more OCSP related parameter values were changed (almost) at the same time.
- PVAYLADEV-457: Improved the INFO level logging information of Signer module so that more information will be written to log. It makes easier to see afterward what Signer has been doing.
- PVAYLADEV-607 / Joint Development issue #69: Global Configuration version that is generated and distributed by default can be set both in Central Server and Configuration Proxy (using a parameter value).
- PVAYLADEV-616: Fixed a bug in environment monitoring causing file handles not to be closed properly.
- PVAYLADEV-618 / Joint Development issue #89: Partial index is created to messagelog database so that non-archived messages will be fetched faster. This change will make the archiver process much faster.
- PVAYLADEV-634 / Joint Development issue #96: Fixed a bug in 'Generate certificate request' dialog. A refactored method was not updated to certificate request generation from System Parameters -view which caused the certificate generation to fail with the error message "Undefined method".

## 6.8.11 - 2016-12-20
- Added documents: PR-OPMON, PR-OPMONJMX, ARC-OPMOND, UG-OPMONSYSPAR
- Updated documents: ARC-G, ARC-SS, UC-OPMON, UG-SS, TEST_OPMON, TEST_OPMONSTRAT

## 6.8.10 - 2016-12-12
- Operational data monitoring improvements and bug fixes

## 6.8.9 - 2016-12-05
- Operational data monitoring

## 6.8.8 - 2016-12-01
- CDATA parsing fix (XTE-262)
- Converting PR-MESS to Markdown

## 6.8.7 - 2016-10-21
- DOM parser replacement with SAX parser (XTE-262)

## 6.8.6 - 2016-09-30
- Fixed: security server processes MIME message incorrectly if there is a "\t" symbol before boundary parameter (XTE-265)
- Documentation update: apt-get upgrade does not upgrade security server from 6.8.3 to 6.8.5 (XTE-278)
- Added xroad-securityserver conflicts uxp-addon-monitoring <= 6.4.0

## 6.7.13 - 2016-09-20
 - PVAYLADEV-485: Fixed a bug in the message archiver functionality that caused very long filenames or filenames including XML to crash the archiver process.
 - PVAYLADEV-398: Security Server backup/restore functionality fixed to work also when the existing backup is restored to a clean environment (new installation of a Security Server to a new environment).
 - PVAYLADEV-238: OCSP dianostic UI now shows the connection status. Color codes:
   - Green: Ok
   - Yellow: Unknown status
   - Red: Connection cannot be established to OCSP service or OCSP response could not be interpreted or no response from OCSP service
 - PVAYLADEV-360: Namespace prefixes other than 'SOAP-ENV' for SOAP fault messages are now supported. Previously other prefixes caused an error.
 - PVAYLADEV-424: Improved the messagelog archiver functionality so that it cannot consume an unlimited amount of memory even if there would be tens of thousands of messages to archive.
 - PVAYLADEV-304: Fixed a situation that caused time-stamping to get stuck if some (rare) preconditions were realized.
 - PVAYLADEV-351: Performance of Security Server improved by optimizing database queries at the time when Security Server's configuration data is fetched.
 - PVAYLADEV-454: Adjusted the amount of metaspace memory available for xroad processes so that the signer service does not run out of memory.
 - PVAYLADEV-416: Added Security Server support for the old Finnish certificate profile used in the FI-DEV environment.
 - PVAYLADEV-459 / PVAYLADEV-352 / PVAYLADEV-460 / PVAYLADEV-461: Several improvements to OCSP protocol related functionalities:
    - After a failure to fetch an OCSP respose, fetch retries are now scheduled based on the Fibonacci Sequence. The first retry is done after 10 seconds, then after 20 seconds, then after 30 seconds, 50 seconds, 80 seconds, 130 seconds etc. until a successful OCSP response is fetched.
    - Validation of OCSP response is done only once per message and the validation result is then stored to cache. If result is needed later, cache will be used for checking the result. This change will make the checking faster.
    - OCSP responses are fetched by the time interval defined in Global Configuration so that the Security Server is able to operate longer in a case on OCSP service is down.
    - OCSP response is no longer fetched immediately if ocspFreshnessSeconds or nextUpdate parameter values are changed. This allows the Security Server to operate longer in case the OCSP service is down and the parameters are changed to avoid calling the unreachable OCSP service.
 - PVAYLADEV-353: Added system parameters to stop the security servers from leaking TCP connections between them. These settings maintain status quo by default so connections are leaked without configuration changes. The prevention is overridden to be enabled in the Finnish installation package. The added configuration options are described in the UG-SYSPAR document. In short, configuring the [proxy] on the server side with server-connector-max-idle-time=120000 will close idle connections after 120 seconds.
 - PVAYLADEV-455 / PVAYLADEV-458: Updated several Security Server dependency packages to the latest version so that possible vulnerabilities are fixed.
 - PVAYLADEV-400: Security Server UI: when adding a new subsystem only subsystems that are owned by the organization are visible by default. Previously all the subsystems were visible (all the registered subsystems).
 - PVAYLADEV-464: Security Server UI: implemented 'Certificate Profile Info' feature for Finnish certificates which allows the UI to automatically fill most of the necessary fields when creating a new certificate request (either sign or auth certificate request).
 - PVAYLADEV-476: Improved the performance of Security Server by optimizing the caching of global configuration.
 - Bug fixes and minor enhancements

## 6.8.5 - 2016-08-16
- Bugfix: it's not possible to send duplicate client registration requests (https://github.com/vrk-kpa/xroad-joint-development/issues/48)
- Bugfix: added one missing translation
- Updated configuration client test data
- Some minor corrections

## 6.8.4 - 2016-05-11
- Merged with XTEE6 repo

## 6.7.12 - 2016-04-25
- Fixed security server not starting if xroad-addon-messagelog is not installed
- Added connection timeouts to configuration client to prevent hanging problems
- In security server's keys and certificates view delete button now removes both key and certificates
- Signer reacts to ocspFreshnessSeconds parameter change immediately
- OCSP nextUpdate verification can be switched off with optional global configuration part
- Fixed bug in xroad-create-cluster.sh script that created the certificates with 30 days expiry period
- Fix software token batch signing setting

## 6.7.11 - 2016-02-08
- Minor documentation changes

## 6.7.10 - 2016-01-15
- Change configuration client admin port default to 5675 and make it configurable
- Security server message exchange performance optimizations
- Security server environmental monitoring

## 6.7.9 - 2016-01-15
- Fix timestamper connection checking
- Fix timestamper status when batch timestamping is used
- Timestamping diagnostics displays status for all defined timestamping services

## 6.7.8 - 2016-01-07
- Security server offers diagnostics information for time stamping service
- Fixed configuration restore (RHEL)
- Fixed database backup to support generated passwords

## 6.7.7 - 2015-12-09
- Fixed critical errors found by SonarQube
- Added option for requiring strong password for PIN-code
- Security server offers diagnostics information for global configuration fetching
- Taken to use HTML-attribute data-name to improve testability

## 6.7.6 - 2015-11-27
- Fixed changing of security server configuration anchor

## 6.7.5 - 2015-11-26
- Updated member code/identifier extractor for Finnish instance
- Fixed XSS vulnerabilities in central server and security server user interfaces
- RHEL installation does not redirect clientproxy ports automatically
- Security server's internal TLS certificate can be replaced from the UI

## 6.7.4 - 2015-11-12
- Add MIT license header to security server source code
- Add LICENSE.info file to security server source code and binaries
- Add LICENSE.info file to central server source code and binaries
- Add LICENSE.info file to configuration proxy source code and binaries
- Add LICENSE file containing MIT license to security server source code and binaries
- Fixed 'Global configuration expired' error occurring under heavy load
- The password for messagelog and serverconf databases is generated during installation
- Remove hard-coded iface from Red Hat security server port redirection and make it configurable

## 6.7.3 - 2015-10-26
- Add license information
- Refactor proxy setup scripts (RHEL)

## 6.7.2 - 2015-10-19
- Fix nginx configuration (remove X-Frame-Options)

## 6.7.1 - 2015-10-14
- Finnish settings set SHA256withRSA as default signature algorithm
- Finnish settings set SHA256withRSA as default CSR signature algorithm
- Configurable message body logging
- Perfect forward secrecy management services
- Security server user interface uses TLSv1.2
- Central server user interface uses TLSv1.2
- Security server communicates with backend services using TLSv1.2
- Perfect forward secrecy for security server user interface
- Perfect forward secrecy for central server user interface
- Perfect forward secrecy for security server communications with backend services
- Fixed SonarQube static analysis blocker issues
- Management services use TLSv1.2

## 6.7.0 - 2015-09-29
- Partial support for RHEL 7
  - Security server can be installed on RHEL 7
  - rpm packages for security server
    - xroad-securityserver-fi (meta-package for Finnish instances), xroad-securityserver, xroad-proxy, xroad-common, xroad-jetty9, xroad-addon-messagelog, xroad-addon-metaservices
    - Note. optional package xroad-addon-hwtokens is not included in this release
- Member Code/Identifier Extractor for Finnish instance (PVAYLADEV-94)
  - Member Code/Identifier Extractor Method: ee.ria.xroad.common.util.FISubjectClientIdDecoder.getSubjectClientId
  - Signing certificate subject DN format supported by the decoder: C=FI,O=<instanceIdentifier>, OU=<memberClass>, CN=<memberCode> (e.g. C=FI, O=FI-DEV, OU=PUB, CN=1234567-8)
- Configurable key size for signing and authentication RSA keys (PVAYLADEV-28)
  - New configuration parameter signer.key-length (default 2048)
- Configurable certificate signing request signing algorithm (PVAYLADEV-29)
  - New configuration parameter signer.csr-signature-algorithm (default: SHA1withRSA)
- New security server metapackage with default configuration for Finnish instance
  - xroad-securityserver-fi
  - uses SHA256withRSA as signer.csr-signature-algorithm
- Fixed atomic save to work between separate file systems (PVAYLADEV-125)
  - OS temp directory and X-Road software can now reside on different file systems
