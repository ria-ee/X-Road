![](img/eu_regional_development_fund_horizontal_div_15.png "European Union | European Regional Development Fund | Investing in your future")

---


# SECURITY SERVER USER GUIDE
**X-ROAD 6**

Version: 2.17  
15.06.2017
Doc. ID: UG-SS

---


## Version history

 Date       | Version | Description                                                     | Author
 ---------- | ------- | --------------------------------------------------------------- | --------------------
 05.09.2014 | 0.1     | Initial draft
 24.09.2014 | 0.2     | Translation to English
 10.10.2014 | 0.3     | Update
 14.10.2014 | 0.4     | Title page, header, footer added
 16.10.2014 | 0.5     | Minor corrections done
 12.11.2014 | 0.6     | Asynchronous messages section removed. Global Configuration distributors section replaced with Configuration Anchor section ([10.1](#101-managing-the-configuration-anchor)). Added Logback information (Chapter [16](#16-logs-and-system-services)). A note added about the order of timestamping services (Section [10.2](#102-managing-the-timestamping-services)).
 1.12.2014  | 1.0     | Minor corrections done
 19.01.2015 | 1.1     | License information added
 27.01.2015 | 1.2     | Minor corrections done
 30.04.2015 | 1.3     | “sdsb” changed to “xroad”
 29.05.2015 | 1.4     | Message Log chapter added (Chapter [11](#11-message-log))
 30.06.2015 | 1.5     | Minor corrections done
 3.07.2015  | 1.6     | Audit Log chapter added (Chapter [12](#12-audit-log))
 7.09.2015  | 1.7     | Message Log – how to use remote database (Section [11.3](#113-using-a-remote-database))
 14.09.2015 | 1.8     | Reference to the audit log events added
 18.09.2015 | 1.9     | Minor corrections done
 21.09.2015 | 2.0     | References fixed
 07.10.2015 | 2.1     | Default value of the parameter *acceptable-timestamp-failure-period* set to 14400
 14.10.2015 | 2.2     | Instructions for using an external database for the message log corrected
 05.11.2015 | 2.3     | Updates related to backup and restore (Chapter [13](#13-back-up-and-restore))
 30.11.2015 | 2.4     | X-Road concepts updated (Section [1.2](#12-x-road-concepts)). Security server registration updated (Chapter [3](#3-security-server-registration)). Security server clients updated (Chapter [4](#4-security-server-clients)); only subsystems (and not members) can be registered as security server clients and have services or access rights configured. Cross-references fixed. Editorial changes made.
 09.12.2015 | 2.5     | Security server client deletion updated (Section [4.5.2](#452-deleting-a-client)). Editorial changes made.
 14.12.2015 | 2.6     | Message log updated (Chapter [11](#11-message-log))
 14.01.2016 | 2.7     | Logs updated (Chapter [16](#16-logs-and-system-services))
 08.02.2016 | 2.8     | Corrections in chapter [16](#16-logs-and-system-services)
 20.05.2016 | 2.9     | Merged changes from xtee6-doc repo. Added Chapter [14](#14-diagnostics) Diagnostics and updated content of [10.3](#103-changing-the-internal-tls-key-and-certificate) Changing the Internal TLS Key and Certificate.
 29.11.2016 | 2.10    | User Management updated (Chapter [2](#2-user-management)). XTE-297: Internal Servers tab is displayed to security server owner (Chapter [9](#9-communication-with-the-client-information-systems)). |
 19.12.2016 | 2.11    | Added Chapter [15](#15-operational-monitoring) Operational Monitoring
 20.12.2016 | 2.12    | Minor corrections in Chapter [15](#15-operational-monitoring)
 22.12.2016 | 2.13    | Corrections in Chapter [15.2.5](#1525-configuring-an-external-operational-monitoring-daemon-and-the-corresponding-security-server)
 04.01.2016 | 2.14    | Corrections in Chapter [15.2.5](#1525-configuring-an-external-operational-monitoring-daemon-and-the-corresponding-security-server)
 20.02.2017 | 2.15    | Converted to Github flavoured Markdown, added license text, adjusted tables for better output in PDF | Toomas Mölder
 16.03.2017 | 2.16    | Added observer role to Chapters [2.1](#21-user-roles) and [2.2](#22-managing-the-users) | Tatu Repo
 15.06.2017 | 2.17    | Added [Chapter 17](#17-federation) on federation | Olli Lindgren

## Table of Contents

<!-- toc -->

- [License](#license)
- [1 Introduction](#1-introduction)
  * [1.1 The X-Road Security Server](#11-the-x-road-security-server)
  * [1.2 X-Road Concepts](#12-x-road-concepts)
  * [1.3 References](#13-references)
- [2 User Management](#2-user-management)
  * [2.1 User Roles](#21-user-roles)
  * [2.2 Managing the Users](#22-managing-the-users)
- [3 Security Server Registration](#3-security-server-registration)
  * [3.1 Configuring the Signing Key and Certificate for the Security Server Owner](#31-configuring-the-signing-key-and-certificate-for-the-security-server-owner)
    + [3.1.1 Generating a Signing Key](#311-generating-a-signing-key)
    + [3.1.2 Generating a Certificate Signing Request for a Signing Key](#312-generating-a-certificate-signing-request-for-a-signing-key)
    + [3.1.3 Importing a Certificate from the Local File System](#313-importing-a-certificate-from-the-local-file-system)
    + [3.1.4 Importing a Certificate from a Security Token](#314-importing-a-certificate-from-a-security-token)
  * [3.2 Configuring the Authentication Key and Certificate for the Security Server](#32-configuring-the-authentication-key-and-certificate-for-the-security-server)
    + [3.2.1 Generating an Authentication Key](#321-generating-an-authentication-key)
    + [3.2.2 Generating a Certificate Signing Request for an Authentication Key](#322-generating-a-certificate-signing-request-for-an-authentication-key)
    + [3.2.3 Importing an Authentication Certificate from the Local File System](#323-importing-an-authentication-certificate-from-the-local-file-system)
  * [3.3 Registering the Security Server in the X-Road Governing Authority](#33-registering-the-security-server-in-the-x-road-governing-authority)
    + [3.3.1 Registering an Authentication Certificate](#331-registering-an-authentication-certificate)
- [4 Security Server Clients](#4-security-server-clients)
  * [4.1 Security Server Client States](#41-security-server-client-states)
  * [4.2 Adding a Security Server Client](#42-adding-a-security-server-client)
  * [4.3 Configuring a Signing Key and Certificate for a Security Server Client](#43-configuring-a-signing-key-and-certificate-for-a-security-server-client)
  * [4.4 Registering a Security Server Client in the X-Road Governing Authority](#44-registering-a-security-server-client-in-the-x-road-governing-authority)
    + [4.4.1 Registering a Security Server Client](#441-registering-a-security-server-client)
  * [4.5 Deleting a Client from the Security Server](#45-deleting-a-client-from-the-security-server)
    + [4.5.1 Unregistering a Client](#451-unregistering-a-client)
    + [4.5.2 Deleting a Client](#452-deleting-a-client)
- [5 Security Tokens, Keys, and Certificates](#5-security-tokens-keys-and-certificates)
  * [5.1 Availability States of Security Tokens, Keys, and Certificates](#51-availability-states-of-security-tokens-keys-and-certificates)
  * [5.2 Registration States of Certificates](#52-registration-states-of-certificates)
    + [5.2.1 Registration States of the Signing Certificate](#521-registration-states-of-the-signing-certificate)
    + [5.2.2 Registration States of the Authentication Certificate](#522-registration-states-of-the-authentication-certificate)
  * [5.3 Validity States of Certificates](#53-validity-states-of-certificates)
  * [5.4 Activating and Disabling the Certificates](#54-activating-and-disabling-the-certificates)
  * [5.5 Configuring and Registering an Authentication key and Certificate](#55-configuring-and-registering-an-authentication-key-and-certificate)
  * [5.6 Deleting a Certificate](#56-deleting-a-certificate)
    + [5.6.1 Unregistering an Authentication Certificate](#561-unregistering-an-authentication-certificate)
    + [5.6.2 Deleting a Certificate or a certificate Signing Request notice](#562-deleting-a-certificate-or-a-certificate-signing-request-notice)
  * [5.7 Deleting a Key](#57-deleting-a-key)
- [6 X-Road Services](#6-x-road-services)
  * [6.1 Adding a WSDL](#61-adding-a-wsdl)
  * [6.2 Refreshing a WSDL](#62-refreshing-a-wsdl)
  * [6.3 Enabling and Disabling a WSDL](#63-enabling-and-disabling-a-wsdl)
  * [6.4 Changing the Address of a WSDL](#64-changing-the-address-of-a-wsdl)
  * [6.5 Deleting a WSDL](#65-deleting-a-wsdl)
  * [6.6 Changing the Parameters of a Service](#66-changing-the-parameters-of-a-service)
- [7 Access Rights](#7-access-rights)
  * [7.1 Changing the Access Rights of a Service](#71-changing-the-access-rights-of-a-service)
  * [7.2 Adding a Service Client](#72-adding-a-service-client)
  * [7.3 Changing the Access Rights of a Service Client](#73-changing-the-access-rights-of-a-service-client)
- [8 Local Access Right Groups](#8-local-access-right-groups)
  * [8.1 Adding a Local Group](#81-adding-a-local-group)
  * [8.2 Displaying and Changing the Members of a Local Group](#82-displaying-and-changing-the-members-of-a-local-group)
  * [8.3 Changing the description of a Local Group](#83-changing-the-description-of-a-local-group)
  * [8.4 Deleting a Local Group](#84-deleting-a-local-group)
- [9 Communication with the Client Information Systems](#9-communication-with-the-client-information-systems)
- [10 System Parameters](#10-system-parameters)
  * [10.1 Managing the Configuration Anchor](#101-managing-the-configuration-anchor)
  * [10.2 Managing the Timestamping Services](#102-managing-the-timestamping-services)
  * [10.3 Changing the Internal TLS Key and Certificate](#103-changing-the-internal-tls-key-and-certificate)
- [11 Message Log](#11-message-log)
  * [11.1 Changing the Configuration of the Message Log](#111-changing-the-configuration-of-the-message-log)
    + [11.1.1 Common parameters](#1111-common-parameters)
    + [11.1.2 Timestamping parameters](#1112-timestamping-parameters)
    + [11.1.3 Archiving parameters](#1113-archiving-parameters)
  * [11.2 Transferring the Archive Files from the Security Server](#112-transferring-the-archive-files-from-the-security-server)
  * [11.3 Using a Remote Database](#113-using-a-remote-database)
- [12 Audit Log](#12-audit-log)
  * [12.1 Changing the Configuration of the Audit Log](#121-changing-the-configuration-of-the-audit-log)
  * [12.2 Archiving the Audit Log](#122-archiving-the-audit-log)
- [13 Back up and Restore](#13-back-up-and-restore)
  * [13.1 Back up and Restore in the User Interface](#131-back-up-and-restore-in-the-user-interface)
  * [13.2 Restore from the Command Line](#132-restore-from-the-command-line)
- [14 Diagnostics](#14-diagnostics)
  * [14.1 Examine security server services status information](#141-examine-security-server-services-status-information)
- [15 Operational Monitoring](#15-operational-monitoring)
  * [15.1 Operational Monitoring Buffer](#151-operational-monitoring-buffer)
    + [15.1.1 Stopping the Collecting of Operational Data](#1511-stopping-the-collecting-of-operational-data)
  * [15.2 Operational Monitoring Daemon](#152-operational-monitoring-daemon)
    + [15.2.1 Configuring the Health Statistics Period](#1521-configuring-the-health-statistics-period)
    + [15.2.2 Configuring the Parameters Related to Database Cleanup](#1522-configuring-the-parameters-related-to-database-cleanup)
    + [15.2.3 Configuring the Parameters related to the HTTP Endpoint of the Operational Monitoring Daemon](#1523-configuring-the-parameters-related-to-the-http-endpoint-of-the-operational-monitoring-daemon)
    + [15.2.4 Installing an External Operational Monitoring Daemon](#1524-installing-an-external-operational-monitoring-daemon)
    + [15.2.5 Configuring an External Operational Monitoring Daemon and the Corresponding Security Server](#1525-configuring-an-external-operational-monitoring-daemon-and-the-corresponding-security-server)
    + [15.2.6 Monitoring Health Data over JMXMP](#1526-monitoring-health-data-over-jmxmp)
- [16 Logs and System Services](#16-logs-and-system-services)
  * [16.1 System Services](#161-system-services)
  * [16.2 Logging configuration](#162-logging-configuration)
  * [16.3 Fault Detail UUID](#163-fault-detail-uuid)
- [17 Federation](#17-federation)

<!-- tocstop -->

## License

This document is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License. To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/

## 1 Introduction

This document describes the management and maintenance of an X-Road version 6 security server.


### 1.1 The X-Road Security Server

The main function of a security server is to mediate requests in a way that preserves their evidential value.

The security server is connected to the public Internet from one side and to the information system within the organization's internal network from the other side. In a sense, the security server can be seen as a specialized application-level firewall that supports the SOAP protocol; hence, it should be set up in parallel with the organization's firewall, which mediates other protocols.

The security server is equipped with the functionality needed to secure the message exchange between a client and a service provider.

-   Messages transmitted over the public Internet are secured using digital signatures and encryption.

-   The service provider's security server applies access control to incoming messages, thus ensuring that only those users that have signed an appropriate agreement with the service provider can access the data.

To increase the availability of the entire system, the service user's and service provider's security servers can be set up in a redundant configuration as follows.

-   One service user can use multiple security servers in parallel to perform requests.

-   If a service provider connects multiple security servers to the network to provide the same services, the requests are load-balanced between the security servers.

-   If one of the service provider's security servers goes offline, the requests are automatically redirected to other available security servers.

The security server also depends on a central server, which provides the global configuration.


### 1.2 X-Road Concepts

-   **Global configuration** consists of XML files, which are regularly downloaded by security servers from the X-Road central server. The global configuration includes, among other data, the following:

    -   the addresses and public keys of trust anchors (certification service CAs and timestamping services);

    -   the public keys of intermediate CAs;

    -   the addresses and public keys of OCSP services (if not already available through the certificates' *Authority Information Access* extension*);*

    -   information about X-Road members and their subsystems;

    -   the addresses of the members' security servers registered in X-Road;

    -   information about the security servers' authentication certificates registered in X-Road;

    -   information about the security servers' clients registered in X-Road;

    -   information about global access rights groups;

    -   X-Road system parameters.

-   **Member class** groups X-Road members with similar properties under a common unit. E.g., state agencies are grouped under the member class “GOV”, private organizations are grouped under the member class “COM”, etc.

-   **Member code**, associated uniquely with a certain X-Road member, is a unique character combination within its particular member class. The member code remains unchanged during the entire lifetime of the member. For example, the member code for organizations and state agencies in Estonia is the Business Registry code.

-   **Security server client** is a subsystem of an X-Road member, whose association with a security server is registered in the X-Road governing authority and that uses the security server for using and/or providing X-Road services.

-   **Security server owner** is an X-Road member legally responsible for a particular security server. The security server owner is displayed in the list of security server clients ("Configuration" -&gt; "Security Server Clients") in bold font style.

-   **Subsystem** represents a part of an X-Road member's information system. X-Road members must declare parts of its information system as subsystems to use or provide X-Road services.

    Subsystems are autonomous in terms of providing and using X-Road services.

    -   The access rights of an X-Road members’ subsystems are independent – access rights given to one subsystem do not affect the access rights of the members’ other subsystems.

    -   Services provided by a subsystem are independent of the services provided by the members’ other subsystems.

    To sign the messages sent by a subsystem when using or providing X-Road services, the signing certificate of the member that manages the subsystem is used. An X-Road member can associate several different subsystems with one security server, and one subsystem can be associated with several security servers.

-   **X-Road certificate** is issued by a certification service provider that has been approved in the X-Road governing authority. An X-Road certificate is either:

    -   a **signing certificate**, which is issued to X-Road members and which the security servers use to digitally sign the mediated data or

    -   an **authentication certificate**, which is issued to security servers and which is used to establish the secure communications channel between security servers.

-   **X-Road instance** identifier helps to distinguish between different X-Road instances. Each instance is assigned an identifying code. E.g. the code for the Estonian development instance is “ee-dev” and the code for production instance is “EE”.

-   **X-Road member** is a legal (or physical) person who has joined the X-Road and uses the functionality provided by the X-Road in the capability of service provider and/or user.

-   **X-Road messages** are service requests and responses described according to the X-Road Message Protocol (see \[[PR-MESS](#Ref_PR-MESS)\]), that are exchanged between the information systems using or providing services and the security servers.


### 1.3 References

1.  <a id="Ref_ASiC" class="anchor"></a>\[ASiC\] ETSI TS 102 918, Electronic Signatures and Infrastructures (ESI); Associated Signature Containers (ASiC)

2.  <a id="Ref_CRON" class="anchor"></a>\[CRON\] Quartz Scheduler CRON expression,
    <http://www.quartz-scheduler.org/generated/2.2.1/html/qs-all/#page/Quartz_Scheduler_Documentation_Set%2Fco-trg_crontriggers.html>

3.  <a id="Ref_INI" class="anchor"></a>\[INI\] INI file, <http://en.wikipedia.org/wiki/INI_file>

4.  <a id="Ref_JDBC" class="anchor"></a>\[JDBC\] Connecting to the Database, <https://jdbc.postgresql.org/documentation/93/connect.html>

5.  <a id="Ref_JSON" class="anchor"></a>\[JSON\] Introducing JSON, <http://json.org/>

6.  <a id="Ref_PR-MESS" class="anchor"></a>\[PR-MESS\] Cybernetica AS. X-Road: Message Protocol v4.0. Document ID: PR-MESS

7.  <a id="Ref_SPEC-AL" class="anchor"></a>\[SPEC-AL\] Cybernetica AS. X-Road: Audit log events. Document ID: SPEC-AL

8.  <a id="Ref_PR-OPMON" class="anchor"></a>\[PR-OPMON\] Cybernetica AS. X-Road: Operational Monitoring Protocol. Document ID: PR-OPMON

9.  <a id="Ref_PR-OPMONJMX" class="anchor"></a>\[PR-OPMONJMX\] Cybernetica AS. X-Road: Operational Monitoring JMX Protocol. Document ID: PR-OPMONJMX

10. <a id="Ref_UG-OPMONSYSPAR" class="anchor"></a>\[UG-OPMONSYSPAR\] Cybernetica AS. X-Road: Operational Monitoring System Parameters. Document ID: PR-OPMONSYSPAR

11. <a id="Ref_IG-SS" class="anchor"></a>\[IG-SS\] Cybernetica AS. X-Road: Security Server Installation Guide. Document ID: IG-SS

12. <a id="Ref_JMX" class="anchor"></a>\[JMX\] Monitoring and Management Using JMX Technology, <http://docs.oracle.com/javase/8/docs/technotes/guides/management/agent.html>

13. <a id="Ref_ZABBIX-GATEWAY" class="anchor"></a>\[ZABBIX-GATEWAY\] Zabbix Java Gateway, <https://www.zabbix.com/documentation/3.0/manual/concepts/java>

14. <a id="Ref_ZABBIX-JMX" class="anchor"></a>\[ZABBIX-JMX\] Zabbix JMX Monitoring, <https://www.zabbix.com/documentation/3.0/manual/config/items/itemtypes/jmx_monitoring>

15. <a id="Ref_ZABBIX-API" class="anchor"></a>\[ZABBIX-API\] Zabbix API, <https://www.zabbix.com/documentation/3.0/manual/api>


## 2 User Management


### 2.1 User Roles

Security servers support the following user roles:

-   <a id="xroad-security-officer" class="anchor"></a>**Security Officer** (`xroad-security-officer`) is responsible for the application of the security policy and security requirements, including the management of key settings, keys, and certificates.

-   <a id="xroad-registration-officer" class="anchor"></a>**Registration Officer** (`xroad-registration-officer`) is responsible for the registration and removal of security server clients.

-   <a id="xroad-service-administrator" class="anchor"></a>**Service Administrator** (`xroad-service-administrator`) manages the data of and access rights to services

-   <a id="xroad-system-administrator" class="anchor"></a>**System Administrator** (`xroad-system-administrator`) is responsible for the installation, configuration, and maintenance of the security server.

-   <a id="xroad-securityserver-observer" class="anchor"></a>**Security Server Observer** (`xroad-securityserver-observer`) can view the status of the security server without having access rights to edit the configuration. This role can be used to offer users read-only access to the security server admin user interface.

One user can have multiple roles and multiple users can be in the same role. Each role has a corresponding system group, created upon the installation of the system.

Henceforth each applicable section of the guide indicates, which user role is required to perform a particular action. For example:

**Access rights:** [Security Officer](#xroad-security-officer)

If the logged-in user does not have a permission to carry out a particular task, the button that would initiate the action is hidden (and neither is it possible to run the task using its corresponding keyboard combinations or mouse actions). Only the permitted data and actions are visible and available to the user.


### 2.2 Managing the Users

User management is carried out on command line in root user permissions.

To add a new user, enter the command:

    adduser username

To grant permissions to the user you created, add it to the corresponding system groups, for example:

    adduser username xroad-security-officer
    adduser username xroad-registration-officer
    adduser username xroad-service-administrator
    adduser username xroad-system-administrator
    adduser username xroad-securityserver-observer

To remove a user permission, remove the user from the corresponding system group, for example:

    deluser username xroad-security-officer

User permissions are applied only after restart of the xroad-jetty service (see Section [16.1](#161-system-services)).

To remove a user, enter:

    deluser username


## 3 Security Server Registration

To use a security server for mediating (exchanging) messages, the security server and its owner must be certified by a certification service provider approved by the X-Road governing authority, and the security server has to be registered in the X-Road governing authority.


### 3.1 Configuring the Signing Key and Certificate for the Security Server Owner

The signing keys used by the security servers for signing X-Road messages can be stored on software or hardware based (a Hardware Security Module or a smartcard) security tokens, according to the security policy of the X-Road instance.

Depending on the certification policy, the signing keys are generated either in the security server or by the certification service provider. Sections [3.1.1](#311-generating-a-signing-key) to [3.1.3](#313-importing-a-certificate-from-the-local-file-system) describe the actions necessary to configure the signing key and certificate in case the key is generated in the security server. Section [3.1.4](#314-importing-a-certificate-from-a-security-token) describes the importing of the signing key and certificate in case the key is generated by the certification service provider.

The **background colors** of the devices, keys and certificate are explained in Section [5.1](#51-availability-states-of-security-tokens-keys-and-certificates).


#### 3.1.1 Generating a Signing Key

**Access rights:**

-   All activities: [Security Officer](#xroad-security-officer)

-   All activities except logging into the key device: [Registration Officer](#xroad-registration-officer)

-   Logging in to the key device: [System Administrator](#xroad-system-administrator)

To generate a signing key, follow these steps.

1.  On the **Management** menu, select **Keys and Certificates**.

2.  If you are using a hardware security token, ensure that the device is connected to the security server. The device information must be displayed in the **Keys and Certificates** table.

3.  To log in to the token, click **Enter PIN** on the token’s row in the table and enter the PIN code. Once the correct PIN is entered, the **Enter PIN** button changes to **Logout**.

4.  To generate a signing key, select the token from the table by clicking the respective row, and click **Generate key**. Enter the label value for the key and click **OK**. The generated key appears under the token’s row in the table. The label value is displayed as the name of the key.


#### 3.1.2 Generating a Certificate Signing Request for a Signing Key

**Access rights:** [Security Officer](#xroad-security-officer), [Registration Officer](#xroad-registration-officer)

To generate a certificate signing request (CSR) for the signing key, follow these steps.

1.  On the **Management** menu, select **Keys and Certificates**.

2.  Select a key from the table and click **Generate CSR**. In the dialog that opens

    2.1  Select the certificate usage policy from the **Usage** drop down list (SIGN for signing certificates);

    2.2  select the X-Road member the certificate will be issued for from the **Client** drop-down list;

    2.3  select the issuer of the certificate from the **Certification Service** drop-down list;

    2.4  select the format of the certificate signing request (PEM or DER), according to the certification service provider’s requirements

    2.5  click **OK**;

3.  In the form that opens, review the certificate owner’s information that will be included in the CSR and fill in the empty fields, if needed.

4.  Click **OK** to complete the generation of the CSR and save the prompted file to the local file system.

After the generation of the CSR, a “Request” record is added under the key’s row in the table, indicating that a certificate signing request has been created for this key. The record is added even if the request file was not saved to the local file system.

**To certify the signing key, transmit the certificate signing request to the approved certification service provider and accept the signing certificate created from the certificate signing request.**


#### 3.1.3 Importing a Certificate from the Local File System

**Access rights:** [Security Officer](#xroad-security-officer), [Registration Officer](#xroad-registration-officer)

To import the signing certificate to the security server, follow these steps.

1.  On the **Management** menu, select **Keys and Certificates**.

2.  Click **Import certificate**.

3.  Locate the certificate file from the local file system and click **OK**. After importing the certificate, the "Request" record under the signing key's row is replaced with the information from the imported certificate. By default, the signing certificate is imported in the "Registered" state.


#### 3.1.4 Importing a Certificate from a Security Token

**Access rights:** [Security Officer](#xroad-security-officer), [Registration Officer](#xroad-registration-officer)

To import a certificate from a security token, follow these steps.

1.  On the **Management** menu, select **Keys and Certificates**.

2.  Make sure that a key device containing the signing key and the signing certificate is connected to the security server. The device and the keys and certificates stored on the device must be displayed in the **Keys and Certificates** view.

3.  To log in to the security token, click **Enter PIN** on the token’s row in the table and enter the PIN. Once the correct PIN is entered, the **Enter PIN** button changes to **Logout**.

4.  Click the **Import** button on the row of the certificate. By default, the certificate is imported in the "Registered" state.


### 3.2 Configuring the Authentication Key and Certificate for the Security Server

The **background colors** of the devices, keys and certificate are explained in Section [5.1](#51-availability-states-of-security-tokens-keys-and-certificates).


#### 3.2.1 Generating an Authentication Key

**Access rights**

-   All activities: [Security Officer](#xroad-security-officer)

-   Logging in to the key device: [System Administrator](#xroad-system-administrator)

**The security server's authentication keys can only be generated on software security tokens.**

1.  On the **Management** menu, select **Keys and Certificates**.

2.  To log in to the software token, click **Enter PIN** on the token’s row in the table and enter the token’s PIN code. Once the correct PIN is entered, the **Enter PIN** button changes to **Logout**.

3.  To generate an authentication key, select the software token from the table by clicking the respective row, and click **Generate key**. Enter the label value for the key and click **OK**. The generated key appears under the token’s row in the table. The label value is displayed as the name of the key.


#### 3.2.2 Generating a Certificate Signing Request for an Authentication Key

**Access rights:** [Security Officer](#xroad-security-officer)

To generate a certificate signing request (CSR) for the authentication key, follow these steps.

1.  On the **Management** menu, select **Keys and Certificates**.

2.  Select the authentication key from the table and click **Generate CSR**. In the dialog that opens

    2.1  Select the certificate usage policy from the **Usage** drop down list (AUTH for authentication certificates);

    2.2  select the issuer of the certificate from the **Certification Service** drop-down list;

    2.3  select the format of the certificate signing request (PEM or DER), according to the certification service provider’s requirements

    2.4  click **OK**;

3.  In the form that opens, review the information that will be included in the CSR and fill in the empty fields, if needed.

4.  Click **OK** to complete the generation of the CSR and save the prompted file to the local file system.

After the generation of the CSR, a “Request” record is added under the key’s row in the table, indicating that a certificate signing request has been created for this key. The record is added even if the request file was not saved to the local file system.

**To certify the authentication key, transmit the certificate signing request to the approved certification service provider and accept the authentication certificate created from the certificate signing request.**


#### 3.2.3 Importing an Authentication Certificate from the Local File System

**Access rights:** [Security Officer](#xroad-security-officer)

To import the authentication certificate to the security server, follow these steps.

1.  On the Management menu, select Keys and Certificates.

2.  Click Import certificate.

3.  Locate the certificate file from the local file system and click OK. After importing the certificate, the "Request" record under the authentication key's row is replaced with the information from the imported certificate. By default, the certificate is imported in the “Saved” (see Section [5.2.2](#522-registration-states-of-the-authentication-certificate)) and “Disabled” states (see Section [5.3](#53-validity-states-of-certificates)).


### 3.3 Registering the Security Server in the X-Road Governing Authority

To register the security server in the X-Road governing authority, the following actions must be completed.

-   The authentication certificate registration request must be submitted from the security server (see [3.3.1](#331-registering-an-authentication-certificate)).

-   A request for registering the security server must be submitted to the X-Road governing authority according to the organizational procedures of the X-Road instance.

-   The registration request must be approved by the X-Road governing authority.


#### 3.3.1 Registering an Authentication Certificate

**Access rights:** [Security Officer](#xroad-security-officer)

The security server's registration request is signed in the security server with the server owner's signing key and the server's authentication key. Therefore, ensure that the corresponding certificates are imported to the security server and are in a usable state (the tokens holding the keys are in logged in state and the OCSP status of the certificates is “good”).

To submit an authentication certificate registration request, follow these steps.

1.  On the Management menu, select Keys and Certificates.

2.  Select an authentication certificate to be registered (it must be in the "saved" state) and click **Register**.

3.  In the dialog that opens, enter the security server's public DNS name or its external IP address and click **OK**.

On submitting the request, the message "Request sent" is displayed, and the authentication certificate’s state is set to "Registration in process".

After the X-Road governing authority has accepted the registration, the registration state of the authentication certificate is set to “Registered” and the registration process is completed.


## 4 Security Server Clients

**Important: to use or provide X-Road services, a security server client needs to be certified by a certification service provider approved by the X-Road governing authority, and the association between the client and the security server used by the client must be registered at the X-Road governing authority.**

**This section does not address managing the owner to a security server.** The owner's information has been already added to the security server upon the installation, and registered upon the security server's registration. The owner's registration status can be looked up by selecting **Security Server Clients** on the **Configuration** menu. The security server's owner is displayed in bold. Before the registration of the security server, the owner is in the "Saved" state and after the completion of the registration process, in the "Registered" state.

The registration of the security server's owner does not extend to the owner's subsystems. The subsystems must be registered as individual clients.


### 4.1 Security Server Client States

The security server distinguishes between the following client states.

![](img/ug-ss_saved.png) **Saved** – the client's information has been entered and saved into the security server's configuration (see [4.2](#42-adding-a-security-server-client)), but the association between the client and the security server is not registered in the X-Road governing authority. (If the association is registered in the central server prior to the entry of data, the client will move to the "Registered" state upon data entry.) From this state, the client can move to the following states:

-   “Registration in progress”, if a registration request for the client is submitted from the security server (see [4.4.1](#441-registering-a-security-server-client));

-   “Deleted”, if the client's information is deleted from the security server configuration (see [4.5.2](#452-deleting-a-client)).

![](img/ug-ss_registration_in_progress.png) **Registration in progress** – a registration request for the client is submitted from the security server to the central server, but the association between the client and the security server is not yet approved by the X-Road governing authority. From this state, the client can move to the following states:

-   "Registered", if the association between the client and the security server is approved by the X-Road governing authority (see [4.4.1](#441-registering-a-security-server-client));

-   "Deletion in progress", if a client deletion request is submitted from the security server (see [4.5.1](#451-unregistering-a-client)).

![](img/ug-ss_registered.png) **Registered** – the association between the client and the security server has been approved in the X-Road governing authority. In this state, the client can provide and use X-Road services (assuming all other prerequisites are fulfilled). From this state, the client can move to the following states:

-   "Global error", if the association between the client and the security server has been revoked by the X-Road governing authority;

-   "Deletion in progress", if a client deletion request is submitted from the security server (see [4.5.1](#451-unregistering-a-client)).

![](img/ug-ss_global_error.png) **Global error** – the association between the client and the security server has been revoked in the central server. From this state, the client can move to the following states:

-   "Registered", if the association between the client and the security server has been restored in the central server (e.g., the association between the client and the security server was lost due to an error);

-   "Deleted", if the client's information is deleted from the security server's configuration (see [4.5.2](#452-deleting-a-client)).

![](img/ug-ss_deletion_in_progress.png) **Deletion in progress** – a client deletion request has been submitted from the security server. From this state, the client can move to the following state:

-   "Deleted", if the client's information is deleted from the security server's configuration (see [4.5.2](#452-deleting-a-client)).

**Deleted** – the client's information has been deleted from the security server's configuration.


### 4.2 Adding a Security Server Client

**Access rights:** [Registration Officer](#xroad-registration-officer)

Follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**.

2.  Click **Add Client**. In the window that opens, either enter the client's information manually or click **Select Client from Global List** and locate the client's information from within all X-Road members and their subsystems.

3.  Click **OK** once the client’s information has been entered.

The new client is added to the list of security server clients in the "Saved" state.


### 4.3 Configuring a Signing Key and Certificate for a Security Server Client

A signing key and certificate must be configured for the security server client to sign messages exchanged over the X-Road.

Certificates are not issued to subsystems; therefore, the certificate of the subsystem’s owner (that is, an X-Road member) is used for the subsystem.

All particular X-Road member’s subsystems that are registered in the same security server use the same signing certificate for signing messages. Hence, if the security server already contains the member's signing certificate, it is not necessary to configure a new signing key and/or certificate when adding a subsystem of that member.

The process of configuring the signing key and certificate for a security server client is the same as for the security server owner. The process is described in Section [3.1](#31-configuring-the-signing-key-and-certificate-for-the-security-server-owner).


### 4.4 Registering a Security Server Client in the X-Road Governing Authority

To register a security server client in the X-Road governing authority, the following actions must be completed.

-   The security server client registration request must be submitted from the security server (see [4.4.1](#441-registering-a-security-server-client)).

-   A request for registering the client must be submitted to the X-Road governing authority according to the organizational procedures of the X-Road instance.

-   The registration request must be approved by the X-Road governing authority.


#### 4.4.1 Registering a Security Server Client

**Access rights:** [Registration Officer](#xroad-registration-officer)

To submit a client registration request follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**.

2.  Select the client in the "Saved" state from the list of security server clients.

3.  Click the **Details** icon and in the window that opens, click **Register**.

4.  Click **Confirm** to submit the request.

On submitting the request, the message "Request sent" is displayed, and the client’s state is set to "Registration in process".

After the X-Road governing authority has accepted the registration, the state of the client is set to “Registered” and the registration process is completed.


### 4.5 Deleting a Client from the Security Server

If a client is deleted from the security server, all the information related to the client is deleted from the server as well – that is, the WSDLs, services, access rights, and, if necessary, the certificates.

When one of the clients is deleted, it is not advisable to delete the signing certificate if the certificate is used by other clients registered to the security server, e.g., other subsystems belonging the same X-Road member as the deleted subsystem.

A client registered or submitted for registration in the X-Road governing authority (indicated by the "Registered" or "Registration in progress" state) must be unregistered before it can be deleted. The unregistering event sends a security server client deletion request from the security server to the central server.


#### 4.5.1 Unregistering a Client

**Access rights:** [Registration Officer](#xroad-registration-officer)

To unregister a client, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**.

2.  Select the client that you wish to remove from the server and click the **Details** icon on the client’s row.

3.  In the window that opens, click **Unregister** and then click **Confirm**. The security server automatically sends a client deletion request to the X-Road central server, upon the receipt of which the association between the security server and the client is revoked.

4.  Next, a notification is displayed about sending a deletion request to the central server, and a confirmation is presented about deleting the client's information (except its certificates).

  -   If you wish to delete the client's information immediately, click **Confirm**. Next, an option is presented to delete the client's certificates. To delete the certificates, click **Confirm** again.

  -   If you wish to retain the client's information, click **Cancel**. In that case, the client is moved to the "Deletion in progress" state, wherein the client cannot mediate messages and cannot be registered again in the X-Road governing authority.

5.  To delete the information of a client in the "Deletion in progress" state, select the client by clicking the **Details** icon on its row, click **Delete** in the window that opens, and then click **Confirm**.

*Note:* It is possible to unregister a registered client from the central server without sending a deletion request through the security server. In this case, the security server's administrator responsible for the client must transmit a request containing information about the client to be unregistered to the central server's administrator. If the client has been deleted from the central server without a prior deletion request from the security server, the client is shown in the "Global error" state in the security server.


#### 4.5.2 Deleting a Client

**Access rights:** [Registration Officer](#xroad-registration-officer)

A security server client can be deleted if its state is "Saved", "Global error" or "Deletion in progress". Clients that are in states "Registered" or "Registration in progress" need to be unregistered before they can be deleted (see Section [4.5.1](#451-unregistering-a-client)).

To delete a client, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**.

2.  Select from the table a client that you wish to remove from the security server and click the **Details** icon on that row.

3.  In the window that opens, click **Delete**. Confirm the deletion by clicking **Confirm**.


## 5 Security Tokens, Keys, and Certificates


### 5.1 Availability States of Security Tokens, Keys, and Certificates

To display the availability of objects (that is, tokens, keys or certificates), the following background colors are used in the "Keys and Certificates" view.

-   **Yellow** background – the object is available to the security server, but the object's information has not been saved to the security server configuration. For example, a smartcard could be connected to the server, but the certificates on the smartcard may not have been imported to the server. Certificates on the yellow background cannot be used for mediating messages.

-   **White** background – the object is available to the security server and the object's information has been saved to the security server's configuration. **Certificates on the white background can be used for mediating messages.**

-   **Gray** background – the object is not available for the security server. Certificates on the gray background cannot be used for mediating messages.

**Caution:** The key device's and key's information is automatically saved to the configuration when a certificate associated with either of them is imported to the security server, or when a certificate signing request is generated for the key. Similarly, the key device's and key's information is deleted from the security server configuration automatically upon the deletion of the last associated certificate and/or certificate signing request.


### 5.2 Registration States of Certificates

Registration states indicate if and how a certificate can be used in the X-Road system. In the "Keys and Certificates" view, a certificate's registration states (except "Deleted") are displayed in the "Status" column.


#### 5.2.1 Registration States of the Signing Certificate

A security server signing certificate can be in one of the following registration states.

-   **Registered** – the certificate has been imported to the security server and saved to its configuration. A signing certificate in a “Registered” state can be used for signing X-Road messages.

-   **Deleted** – the certificate has been deleted from the server configuration. If the certificate is in the “Deleted” state and stored on a hardware key device connected to the security server, the certificate is displayed on a yellow background.


#### 5.2.2 Registration States of the Authentication Certificate

A security server authentication certificate can be in one of the following registration states.

**Saved** – the certificate has been imported to the security server and saved to its configuration, but the certificate has not been submitted for registration. From this state, the certificate can move to the following states:

-   "Registration in progress", if the authentication certificate registration request is sent from the security server to the central server (see [3.3.1](#331-registering-an-authentication-certificate));

-   "Deleted", if the authentication certificate's information is deleted from the security server configuration (see Section [5.6](#56-deleting-a-certificate)).

**Registration in progress** – an authentication certificate registration request has been created and sent to the central server, but the association between the certificate and the security server has not yet been approved. From this state, the certificate can move to the following states:

-   "Registered", if the association between the authentication certificate and the security server is approved by the X-Road governing authority (see [3.3](#33-registering-the-security-server-in-the-x-road-governing-authority));

-   "Deletion in progress", if the certificate deletion request has been submitted to the central server (see [5.6.1](#561-unregistering-an-authentication-certificate)). The user can force this state transition even if the sending of the authentication certificate deletion request fails.

**Registered** – the association between the authentication certificate and the security server has been approved in the central server. An authentication certificate in this state can be used to establish a secure data exchange channel for exchanging X-Road messages. From this state, the certificate can move to the following states:

-   "Global error", if the association between the authentication certificate and the security server has been revoked in the central server;

-   "Deletion in progress", if the certificate deletion request has been transmitted to the central server (see [5.6.1](#561-unregistering-an-authentication-certificate)). The user can force this state transition even if the sending of the authentication certificate deletion request fails.

**Global error** – the association between the authentication certificate and the security server has been revoked in the central server. From this state, the certificate can move to the following states:

-   "Registered", if the association between the authentication certificate and the security server has been restored in the central server (e.g., the association between the client and the security server was lost due to an error);

-   "Deleted", if the authentication certificate's information is deleted from the security server configuration (see [5.6](#56-deleting-a-certificate)).

**Deletion in progress** – an authentication certificate registration request has been created for the certificate and sent to the central server. From this state, the certificate can move to the following state:

-   "Deleted", if the authentication certificate's information is deleted from the security server configuration (see [5.6](#56-deleting-a-certificate)).

**Deleted** – the certificate has been deleted from the security server configuration.


### 5.3 Validity States of Certificates

Validity states indicate if and how a certificate can be used independent of the X-Road system. In the "Keys and Certificates" view, the certificate's validity states are displayed in the "OCSP response" column. Validity states (except "Disabled") are displayed for certificates that are in the "Registered" registration state.

A security server certificate can be in one of the following validity states.

-   **Unknown** (validity information missing) – the certificate does not have a valid OCSP response (the OCSP response validity period is set by the X-Road governing authority) or the last OCSP response was either “unknown” (the responder doesn't know about the certificate being requested) or an error.

-   **Suspended** – the last OCSP response about the certificate was "suspended".

-   **Good** (valid) – the last OCSP response about the certificate was "good". Only certificates in the "good" (valid) state can be used to sign messages or establish a connection between security servers.

-   **Expired** – the certificate's validity end date has passed. The certificate is not active and OCSP queries are not performed about it.

-   **Revoked** – the last OCSP response about the certificate was "revoked". The certificate is not active and OCSP queries are not performed about it.

-   **Disabled** – the user has marked the certificate as disabled. The certificate is not active and OCSP queries are not performed about it.


### 5.4 Activating and Disabling the Certificates

**Access rights**

-   For authentication certificates: [Security Officer](#xroad-security-officer)

-   For signing certificates: [Security Officer](#xroad-security-officer), [Registration Officer](#xroad-registration-officer)

Disabled certificates are not used for signing messages or for establishing secure channels between security servers (authentication). If a certificate is disabled, its status in the “OCSP response” column in the “Keys and Certificates” table is “Disabled”.

To activate or disable a certificate, follow these steps.

1.  On the **Management** menu, select **Keys and Certificates**.

2.  To activate a certificate, select an inactive certificate from the table and click **Activate**. To deactivate a certificate, select an active certificate from the table and click **Disable**.


### 5.5 Configuring and Registering an Authentication key and Certificate

A Security server can have multiple authentication keys and certificates (e.g., during authentication key change).

The process of configuring another authentication key and certificate is described in Section [3.2](#32-configuring-the-authentication-key-and-certificate-for-the-security-server).

The process of registering an authentication certificate is described in Section [3.3.1](#331-registering-an-authentication-certificate).


### 5.6 Deleting a Certificate

An authentication certificate registered or submitted for registration in the X-Road governing authority (indicated by the "Registered" or "Registration in progress" state) must be unregistered before it can be deleted. The unregistering event sends an authentication certificate deletion request from the security server to the central server.


#### 5.6.1 Unregistering an Authentication Certificate

**Access rights:** [Security Officer](#xroad-security-officer)

To unregister an authentication certificate, follow these steps.

1.  On the **Management** menu, select **Keys and Certificates**.

2.  Select an authentication certificate in the state "Registered" or "Registration in progress" and click **Unregister**.

    Next, an authentication certificate deletion request is automatically sent to the X-Road central server, upon the receipt of which the associated authentication certificate is deleted from the central server. If the request was successfully sent, the message "Request sent" is displayed and the authentication certificate is moved to the "Deletion in progress" state.

A registered authentication certificate can be deleted from the central server without sending a deletion request through the security server. In this case, the security server's administrator must transmit a request containing information about the authentication certificate to be deleted to the central server's administrator. If the authentication certificate has been deleted from the central server without a deletion request from the security server, the certificate is shown in the "Global error" state in the security server.


#### 5.6.2 Deleting a Certificate or a certificate Signing Request notice

**Access rights**

-   For authentication certificates: [Security Officer](#xroad-security-officer)

-   For signing certificates: [Security Officer](#xroad-security-officer), [Registration Officer](#xroad-registration-officer)

An authentication certificate saved in the system configuration can be deleted if its state is "Saved", "Global error" or "Deletion in progress". The signing certificate and request notices can always be deleted from the system configuration.

**If a certificate is stored on a hardware security token, then the deletion works on two levels:**

-   if the certificate is saved in the server configuration, then the deletion **deletes the certificate from server configuration**, but not from the security token;

-   if the certificate is not saved in the server configuration (the background of the certificate is yellow), then the deletion deletes the certificate from the security token (assuming the token supports this operation).

**To delete a certificate or a signing request notice, follow these steps.**

1.  On the **Management** menu, select **Keys and Certificates**.

2.  Select from the table a certificate or a certificate signing request notice and click **Delete**. Confirm the deletion by clicking **Confirm**.


### 5.7 Deleting a Key

**Warning:** Deleting a key from the server configuration also deletes all certificates (and certificate signing request notices) associated with the key.

**Access rights**

-   For authentication keys: [Security Officer](#xroad-security-officer)

-   For signing keys: [Security Officer](#xroad-security-officer), [Registration Officer](#xroad-registration-officer)

-   For keys without a role: [Security Officer](#xroad-security-officer), [Registration Officer](#xroad-registration-officer)

**The deletion of keys works on two levels:**

-   if the key is saved in the server configuration, then the deletion **deletes the key** (and associated certificates) **from server configuration**, but not from the security token;

-   if the key is not saved in the server configuration (the background of the key is yellow), then the deletion **deletes the key from the security token** (assuming the token supports this operation).

To delete a key, follow these steps.

1.  On the **Management** menu, select **Keys and Certificates**.

2.  Select a key and click **Delete**. Confirm the deletion of the key (and its associated certificates) by clicking **Confirm**.


## 6 X-Road Services

The services are managed on two levels:

-   the addition, deletion, and deactivation of services is carried out on the WSDL level;

-   the service address, internal network connection method, and the service timeout values are configured at the service level. However, it is easy to extend the configuration of one service to all the other services in the same WSDL.


### 6.1 Adding a WSDL

**Access rights:** [Service Administrator](#xroad-service-administrator)

When a new WSDL file is added, the security server reads service information from it and displays the information in the table of services. The service code, title and address are read from the WSDL.

**To add a WSDL**, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Services** icon on that row.

2.  Click **Add WSDL**, enter the WSDL address in the window that opens and click **OK**. The WSDL and the information about the services it contains are added to the table. By default, the WSDL is added in disabled state (see [6.3](#63-enabling-and-disabling-a-wsdl)).

**To see a list of services contained in the WSDL**

-   click the “**+**” symbol in front of the WSDL row to expand the list.


### 6.2 Refreshing a WSDL

**Access rights:** [Service Administrator](#xroad-service-administrator)

Upon refreshing, the security server reloads the WSDL file from the WSDL address to the security server and checks the service information in the reloaded file against existing services. If the composition of services in the new WSDL has changed compared to the current version, a warning is displayed and you can either continue with the refresh or cancel.

To refresh the WSDL, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Services** icon on that row.

2.  Select from the table a WSDL to be refreshed and click **Refresh**.

3.  If the new WSDL contains changes compared to the current WSDL in the security server, a warning is displayed. To proceed with the refresh, click **Continue**.

When the WSDL is refreshed, the existing services’ settings are not overwritten.


### 6.3 Enabling and Disabling a WSDL

**Access rights:** [Service Administrator](#xroad-service-administrator)

A disabled WSDL is displayed in the services’ table in red with a "Disabled" note.

Services described by a disabled WSDL cannot be accessed by the service clients – if an attempt is made to access the service, an error message is returned, containing the information entered by the security server's administrator when the WSDL was disabled.

If a WSDL is enabled, the services described there become accessible to users. Therefore it is necessary to ensure that before enabling the WSDL, the parameters of all its services are correctly configured (see [6.6](#66-changing-the-parameters-of-a-service)).

To **enable** a WSDL, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Services** icon on that row.

2.  Select a disabled WSDL from the table and click **Enable**.

To **disable** a WSDL, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Services** icon on that row.

2.  To enable a WSDL, select an enabled WSDL from the table and click **Disable**.

3.  In the window that opens, enter an error message, which is shown to clients who try to access any of the services in the WSDL, and click **OK**.


### 6.4 Changing the Address of a WSDL

**Access rights:** [Service Administrator](#xroad-service-administrator)

To change the WSDL address, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Services** icon on that row.

2.  Select from the table a WSDL whose address you wish to change and click **Edit**.

3.  In the window that opens, edit the WSDL address and click **OK**. When the address is changed, the WSDL is refreshed (see section [6.2](#62-refreshing-a-wsdl)).


### 6.5 Deleting a WSDL

**Access rights:** [Service Administrator](#xroad-service-administrator)

When a WSDL is deleted, all information related to the services described in the WSDL, including access rights, are deleted.

To delete a WSDL, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Services** icon on that row.

2.  Select from the table a WSDL to be deleted and click **Delete**.

3.  Confirm the deletion by clicking **Confirm** in the window that opens.


### 6.6 Changing the Parameters of a Service

**Access rights:** [Service Administrator](#xroad-service-administrator)

Service parameters are

-   "Service URL" – the URL where requests targeted at the service are directed;

-   "Timeout (s)" – the maximum duration of a request to the database, in seconds;

-   "Verify TLS certificate" – toggles the verification of the certificate when a TLS connection is established.

To change service parameters, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Services** icon on that row.

2.  Select a service from the table and click **Edit**.

3.  In the window that opens, configure the service parameters. To apply the selected parameter to all services described in the same WSDL, select the checkbox adjacent to this parameter in the **Apply to All in WSDL** column. To apply the configured parameters, click **OK**.


## 7 Access Rights

Access rights can be granted to the following access right subjects.

-   **An X-Road member's subsystem.**

-   **A global access rights group.** Global groups are created in the X-Road governing authority. If a group is granted an access right, it extends to all group members.

-   **A local access rights group.** To simplify access rights management, each client in the security server can create local access rights groups (see section [8](#8-local-access-right-groups)). If a group is granted an access right, it extends to all group members.

There are two options for managing access rights in a security server.

-   Service-based access rights management – if a single service needs to be opened/closed to multiple service clients (see [7.1](#71-changing-the-access-rights-of-a-service)).

-   Service client-based access rights management – if a single service client needs multiple services opened/closed (see [7.2](#72-adding-a-service-client)).


### 7.1 Changing the Access Rights of a Service

**Access rights:** [Service Administrator](#xroad-service-administrator)

To change the access rights to a service, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Services** icon on that row.

2.  Select a service from the table and click **Access Rights**.

3.  In the window that opens, the access rights table displays information about all X-Road subsystems and groups that have access to the selected service.

4.  To add one or more access right subjects to the service, click **Add Subjects**. The subject search window appears. You can search among all subsystems and global groups registered in the X-Road governing authority and among the security server client's local groups.
    Select one or more subjects from the table and click **Add Selected to ACL**. To grant the access right to all subjects in the search results, click **Add All to ACL**.

5.  To remove service access rights subjects, select the respective rows in the access rights table and click **Remove Selected**. To clear the access rights list (that is, remove all subjects), click **Remove All**.


### 7.2 Adding a Service Client

**Access rights:** [Service Administrator](#xroad-service-administrator)

The service client view (**Configuration** -&gt; **Security Server Clients** -&gt; **Service Clients**) displays all the access rights subjects of the services mediated by this security server client. In other words, if an X-Road subsystem or group has been granted the access right to a service of this client, then the subject is shown in this view.

To add a service client, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**.

2.  Select a client from the table and click the **Service Clients** icon, then click **Add**.

3.  In the window that opens, locate and select a subject (a subsystem, or a local or global group) to which you want to grant access rights to and click **Next**.

4.  Locate the service(s) whose access rights you want to grant to the selected subject. Click **Add Selected to ACL** to grant access rights to the selected services to this subject. Click **Add All to ACL** to grant access rights to all services in the filter to the subject.

The subject is added to the list of service clients, after which the service client's access rights view is displayed where the access rights can be changed.


### 7.3 Changing the Access Rights of a Service Client

**Access rights:** [Service Administrator](#xroad-service-administrator)

To change the service client's access rights, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Service Clients** icon on that row.

2.  In the window that opens, locate and select a subject (a subsystem, or a local or global group) whose access rights you want to change and click **Access Rights**.

3.  In the window that opens, a list of services opened in the security server to the selected subject is displayed.

-   To remove access rights to a service from the service client, select one or more services from the table and click **Remove Selected**, then click **Confirm**.

-   To remove all access rights from the service client, click **Remove All** and then click **Confirm**.

-   To add access rights to a service client, start by clicking **Add Service**. In the window that opens, select the service(s) that you wish to grant to the subject (already granted services are displayed in gray) and click **Add Selected to ACL**. To add all services found by the search, click **Add All to ACL**.

**Caution:** If you refresh the page, all service clients that do not have access rights to any services are removed from the service clients’ view.


## 8 Local Access Right Groups

A local access rights group can be created for a security server client in order to facilitate the management of service access rights for a group of X-Road subsystems that use the same services. The access rights granted for a group apply for all the members of the group. Local groups are client-based, that is, a local group can only be used to manage the service access rights of one security server client in one security server.


### 8.1 Adding a Local Group

**Access rights:** [Service Administrator](#xroad-service-administrator)

To create a local group for a security server client, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client and click the **Local Groups** icon on that row. In the window that opens, a list of the client's local groups is displayed.

2.  To create a new group, click **Add Group**. In the window that opens, enter the code and description for the new group and click **OK**.


### 8.2 Displaying and Changing the Members of a Local Group

**Access rights:** [Service Administrator](#xroad-service-administrator)

To **view the members** of a local group, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client and click the **Local Groups** icon on that row.

2.  In the window that opens, select a group whose members you want to view or change, and click **Details** to open the detail view.

To **add one or more members** to a local group, follow these steps in the group’s detail view.

1.  Click **Add Members**.

2.  In the window that opens, locate and select the subsystems that you wish to add to the group and click **Add Selected to Group**. To add all subsystems found by the search function to the group, click **Add All to Group**.

To **remove members** from a local group, select the members to be deleted in the group’s detail view and click **Remove Selected Members**. To remove all group members from the group, click **Remove All Members**.


### 8.3 Changing the description of a Local Group

**Access rights:** [Service Administrator](#xroad-service-administrator)

To change the description of a local group, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Local Groups** icon on that row.

2.  Select a group from the local groups table and click **Details**.

3.  In the group detail view, click **Edit** to change the description.

4.  Enter the group description and click **OK**.


### 8.4 Deleting a Local Group

**Access rights:** [Service Administrator](#xroad-service-administrator)

**Warning:** When a local group is deleted, all the group members' access rights, which were granted through belonging to the group, are revoked.

To delete a local group, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Local Groups** icon on that row.

2.  Select a group from the local groups table and click **Details**.

3.  In the group detail view, click **Delete Group** and confirm the deletion by clicking **Confirm** in the window that opens.


## 9 Communication with the Client Information Systems

**Access rights:** [Registration Officer](#xroad-registration-officer), [Service Administrator](#xroad-service-administrator)

A security server can use either the HTTP, HTTPS, or HTTPS NOAUTH protocol to communicate with information system servers which provide and use services.

-   The HTTP protocol should be used if the information system server and the security server communicate in a private network segment where no other computers are connected to. Furthermore, the information system server must not allow interactive log-in.

-   The HTTPS protocol should be used if it is not possible to provide a separate network segment for the communication between the information system server and the security server. In that case, cryptographic methods are used to protect their communication against potential eavesdropping and interception. Before HTTPS can be used, internal TLS certificates must be created for the information system server(s) and loaded to the security server.

-   The HTTPS NOAUTH protocol should be used if you want the security server to skip the verification of the information system TLS certificate.

   *Note:* If the HTTP connection method is selected, but the information system connects to the security server over HTTPS, then the connection is accepted, but the client’s internal TLS certificate is not verified (same behavior as with HTTPS NOAUTH).

**By default the connection type for the security server owner is set to HTTPS to prevent security server clients from making operational monitoring data requests as a security server owner.**

To set the connection method for internal network servers in the **service consumer role**, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a security server owner or a client from the table and click the **Internal Servers** icon on that row.

2.  On the **Connection Type** drop-down, select the connection method and click **Save**.

Depending on the configured connection method, the request URL for information system is **`http://SECURITYSERVER/`** or **`https://SECURITYSERVER/`**. When making the request, the address `SECURITYSERVER` must be replaced with the actual address of the security server.

The connection method for internal network servers in the **service provider role** is determined by the protocol in the URL. To change the connection method, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a client from the table and click the **Services** icon on that row.

2.  Select a service from the table and click **Edit**.

3.  Change the protocol in the service URL to HTTP or HTTPS.
    If the HTTPS protocol was selected, select the **Verify TLS certificate** checkbox if needed (see section [6.6](#66-changing-the-parameters-of-a-service)). According to the service parameters, the connection with the internal network server is created using one the following protocols:

-   HTTP – the service/adapter URL begins with “**http:**//...”.

-   HTTPS – the service/adapter URL begins with “**https**://” and the **Verify TLS certificate** checkbox is selected.

-   HTTPS NOAUTH – the service/adapter URL begins with "**https**://" and the **Verify TLS certificate** checkbox is not selected.

To add an internal TLS certificate for a security server owner or security server client (for HTTPS connections), follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a security server owner or a client from the table and click the **Internal Servers** icon on that row.

2.  To add a certificate, click **Add** in the **Internal TLS Certificates** section, select a certificate file from the local file system and click **OK**. The certificate fingerprint appears in the “Internal TLS Certificates” table.

To display the detailed information of an internal TLS certificate, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a security server owner or a client from the table and click the **Internal Servers** icon on that row.

2.  Select a certificate from the “Internal TLS Certificates” table and click **Details**.

To delete an internal TLS certificate, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a security server owner or a client from the table and click the **Internal Servers** icon on that row.

2.  Select a certificate from the “Internal TLS Certificates” table and click **Delete**.

3.  Confirm the deletion by clicking **Confirm** in the window that opens.

To export the security server's internal TLS certificate, follow these steps.

1.  On the **Configuration** menu, select **Security Server Clients**, select a security server owner or a client from the table and click the **Internal Servers** icon on that row.

2.  Click **Export** and save the prompted file to the local file system.


## 10 System Parameters

The security server system parameters are:

-   **Configuration anchor's information.** The configuration anchor contains data that is used to periodically download signed configuration from the central server and to verify the signature of the downloaded configuration.

-   **Timestamping service information.** Timestamping is used to preserve the evidential value of messages exchanged over X-Road.

-   **The internal TLS key and certificate.** The internal TLS certificate is used to establish a TLS connection with the security server client's information system if the "HTTPS" connection method is chosen for the client's servers.


### 10.1 Managing the Configuration Anchor

**Access rights**

-   For uploading the configuration anchor: [Security Officer](#xroad-security-officer)

-   For downloading the configuration anchor: [Security Officer](#xroad-security-officer), [System Administrator](#xroad-system-administrator)

To upload the configuration anchor, follow these steps.

1.  On the **Configuration** menu, select **System Parameters**. The system parameters view is opened.

2.  In the **Configuration Anchor** section, click **Upload**.

3.  Find the anchor file from the local file system and click **Upload**.

4.  Ensure that the anchor file you are uploading is valid by comparing the hash value of the uploaded file with the hash value of the currently valid anchor published by the X-Road governing authority. If the hash values match, confirm the upload by clicking **Confirm**.

To download the configuration anchor, follow these steps.

1.  On the **Configuration** menu, select **System Parameters**. The system parameters view is opened.

2.  On the **Configuration Anchor** section, click **Download** and save the prompted file.


### 10.2 Managing the Timestamping Services

**Access rights:** [Security Officer](#xroad-security-officer)

To add a timestamping service, follow these steps.

1.  On the **Configuration** menu, select **System Parameters**. The system parameters view is opened.

2.  In the **Timestamping Services** section, click **Add**.

3.  In the window that opens, select a service and click **OK**.

To delete a timestamping service, follow these steps.

1.  On the **Configuration** menu, select **System Parameters**. The system parameters view is opened.

2.  In the **Timestamping Services** section, select the service to be deleted and click **Delete**.

*Note*: If more than one time stamping service is configured, the security server will try to get a timestamp from the topmost service in the table, moving down to the next service if the try was unsuccessful.


### 10.3 Changing the Internal TLS Key and Certificate

**Access rights:** [Security Officer](#xroad-security-officer), [System Administrator](#xroad-system-administrator)

To change the security server’s internal TLS key and certificate, follow these steps.

1.  On the **Configuration** menu, select **System Parameters**. The system parameters view is opened.

2.  In the **Internal TLS Certificate** section, click **Generate New TLS Key** and in the window that opens, click **Confirm**.

The security server generates a key used for communication with the client information systems, and the corresponding self-signed certificate. The security server's certificate fingerprint will also change. The security server's domain name is saved to the certificate's *Common Name* field, and the internal IP address to the *subjectAltName* extension field.

To generate a new certificate request, follow these steps.

1.  On the **Configuration** menu, select **System Parameters**. The system parameters view is opened.

2.  In the “Internal TLS Certificate” section, click **Generate Certificate Request**, input the **Distinguished Name** and save the certificate request file to the local file system.

The security server generates a certificate request using the current key and the provided **Distinguished Name**.

To import a new TLS certificate, follow these steps.

1.  On the **Configuration** menu, select **System Parameters**. The system parameters view is opened.

2.  In the “Internal TLS Certificate” section, click **Import Certificate** and point to the file to be imported.

The imported certificate must be in PEM-format to be accepted. Note that importing a new TLS certificate will restart the xroad-proxy and thus affects providing services from the security server.

To export the security server’s internal TLS certificate, follow these steps.

1.  On the **Configuration** menu, select **System Parameters**. The system parameters view is opened.

2.  In the **Internal TLS Certificate** section, click **Export** and save the prompted file to the local file system.

To view the detailed information of the security server’s internal TLS certificate, follow these steps.

1.  On the **Configuration** menu, select **System Parameters**. The system parameters view is opened.

2.  In the **Internal TLS Certificate** section, click **Certificate Details**.


## 11 Message Log

The purpose of the message log is to provide means to prove the reception of a regular request or response message to a third party. Messages exchanged between security servers are signed and encrypted. For every regular request and response, the security server produces a complete signed and timestamped document (Associated Signature Container \[[ASiC](#Ref_ASiC)\]).

Message log data is stored to the database of the security server during message exchange. According to the configuration (see [11.1](#111-changing-the-configuration-of-the-message-log)), the timestamping of the signatures of the exchanged messages is either synchronous to the message exchange process or is done asynchronously using the time period set by the X-Road governing agency.

In case of synchronous timestamping, the timestamping is an integral part of the message exchange process (one timestamp is taken for the request and another for the response). If the timestamping fails, the message exchange fails as well and the security server responds with an error message.

In case of asynchronous timestamping, all the messages (maximum limit is determined in the configuration, see [11.1](#111-changing-the-configuration-of-the-message-log)) stored in the message log since the last periodical timestamping event are timestamped with a single (batch) timestamp. By default, the security server uses asynchronous timestamping for better performance and availability.

The security server periodically composes signed (and timestamped) documents from the message log data and archives them in the local file system. Archive files are ZIP containers containing one or more signed documents and a special linking information file for additional integrity verification purpose.


### 11.1 Changing the Configuration of the Message Log

Configuration parameters are defined in INI files \[[INI](#Ref_INI)\], where each section contains the parameters for a particular security server component. The default message log configuration is located in the file

    /etc/xroad/conf.d/addons/message-log.ini

In order to override default values, create or edit the file

    /etc/xroad/conf.d/local.ini

Create the `[message-log]` section (if not present) in the file. Below the start of the section, list the values of the parameters, one per line.

For example, to configure the parameters `archive-path` and `archive-max-filesize`, the following lines must be added to the configuration file:

    [message-log]
    archive-path=/my/arhcive/path/
    archive-max-filesize=67108864


#### 11.1.1 Common parameters

1.  `hash-algo-id` – the hash algorithm that is used for hashing in the message log. Possible choices are `SHA-256`, `SHA-384`, `SHA-512`. Defaults to `SHA-512`.


#### 11.1.2 Timestamping parameters

1.  `timestamp-immediately` – if set to true, the timestamps are created synchronously with the message exchange, i.e., one timestamp is created for a request and another for a response. This is a security policy to guarantee the timestamp at the time of logging the message, but if the timestamping fails, the message exchange fails as well, and if load to the security server increases, then the load to the timestamping service increases as well. The value of this parameter defaults to false for better performance and availability. In case the value of the parameter is false then the timestamping is performed as a periodic background process (the time period is determined in the X-Road governing agency and propagated to the security servers by global configuration) and signatures stored during the time period (see parameter `timestamp-records-limit`) are timestamped in one batch.

2.  `timestamp-records-limit` – maximum number of signed messages that can be timestamped in one batch. The message exchanging load (messages per minute) and the timestamping interval of the security server must be taken into account when changing the default value of this parameter. Do not modify this parameter without a good reason. Defaults to `10000`.

3.  `acceptable-timestamp-failure-period` – time period in seconds, for how long the asynchronous timestamping is allowed to fail before message exchange between security servers is stopped. Set to `0` to disable this check. Defaults to `14400`.


#### 11.1.3 Archiving parameters

1.  `keep-records-for` – time in days for which to keep timestamped and archived records in the database. Defaults to `30`.

2.  `archive-max-filesize` – maximum size for archived files in bytes. Reaching the maximum value triggers the file rotation. Defaults to `33554432` (32 MB).

3.  `archive-interval` – time interval as Cron expression \[[CRON](#Ref_CRON)\] for archiving timestamped records. Defaults to `0 0 0/6 1/1 * ? *` (fire every 6 hours).

4.  `archive-path` – the directory where the timestamped log records are archived. Defaults to `/var/lib/xroad/`.

5.  `clean-interval` – time interval as Cron expression \[[CRON](#Ref_CRON)\] for cleaning archived records from the database. Defaults to `0 0 0/12 1/1 * ? *` (fire every 12 hours).

6.  `archive-transfer-command` – the command executed after the (periodic) archiving process. This enables one to configure an external script to transfer archive files automatically from the security server. Defaults to no operation.


### 11.2 Transferring the Archive Files from the Security Server

In order to save hard disk space, it is recommended to transfer archive files periodically from the security server (manually or automatically) to an external location.

Archive files (ZIP containers) are located in the directory specified by the configuration parameter archive-path. File names are in the format `mlog-X-Y-Z.zip`, where X is the timestamp (UTC time in the format `YYYYMMDDHHmmss`) of the first message log record, Y is the timestamp of the last message log record (records are processed in chronological order) and Z is 10 characters long alphanumeric random. An example of an archive file name is:

    mlog-20150504152559-20150504152559-a7JS05XAJC.zip

The message log package provides a helper script `/usr/share/xroad/scripts/archive-http-transporter.sh` for transferring archive files. This script uses the HTTP/HTTPS protocol (the POST method, the form name is file) to transfer archive files to an archiving server.

Usage of the script:

 Options:          | &nbsp;
------------------ | -----------------------------------------------------------------------------------------------
 `-d, --dir DIR`   | Archive directory. Defaults to '/var/lib/xroad'
 `-r, --remove`    | Remove successfully transported files form the archive directory.
 `-k, --key KEY`   | Private key file name in PEM format (TLS). Defaults to '/etc/xroad/ssl/internal.key'
 `-c, --cert CERT` | Client certificate file in PEM format (TLS). Defaults to '/etc/xroad/ssl/internal.crt'
 `-cacert FILE`    | CA certificate file to verify the peer (TLS). The file may contain multiple CA certificates. The certificate(s) must be in PEM format.
 `-h, --help`      | This help text.

The archive file has been successfully transferred when the archiving server returns the HTTP status code `200`.

Override the configuration parameter archive-transfer-command (create or edit the file `etc/xroad/conf.d/local.ini`) to set up a transferring script. For example:

    [message-log]
    archive-transfer-command=/usr/share/xroad/scripts/archive-http-transporter.sh -r http://my-archiving-server/cgi-bin/upload

The message log package contains the CGI script /usr/share/doc/xroad-addon-messagelog/archive-server/demo-upload.pl for a demo archiving server for the purpose of testing or development.


### 11.3 Using a Remote Database

The message log database can be located outside of the security server. The following guide describes how to configure and populate a remote database schema for the message log. It is assumed that access to the database from the security server has been configured. For detailed information about the configuration of database connections, refer to \[[JDBC](#Ref_JDBC)\].

1.  Create a database user at remote database host:

        postgres@db_host:~$ createuser -P messagelog_user
        Enter password for new role: <messagelog_password>
        Enter it again: <messagelog_password>

2.  Create a database owned by the message log user at remote database host:

        postgres@db_host:~$ createdb messagelog_dbname -O messagelog_user -E UTF-8

3.  Verify connectivity from security server to the remote database:

        user@security_server:~$ psql -h db_host -U messagelog_user messagelog_dbname
        Password for user messagelog_user: <messagelog_password>
        psql (9.3.9)
        SSL connection (cipher: DHE-RSA-AES256-GCM-SHA384, bits: 256)
        Type "help" for help.
        messagelog_dbname=>

4.  Stop xroad-proxy service for reconfiguration:

        root@security_server:~ # service xroad-proxy stop

5.  Configure the database connection parameters to achieve encrypted connections, in `/etc/xroad/db.properties`:

        messagelog.hibernate.jdbc.use_streams_for_binary = true
        messagelog.hibernate.dialect = ee.ria.xroad.common.db.CustomPostgreSQLDialect
        messagelog.hibernate.connection.driver_class = org.postgresql.Driver
        messagelog.hibernate.connection.url = jdbc:postgresql://db_host:5432/messagelog_dbname?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory
        messagelog.hibernate.connection.username = messagelog_user
        messagelog.hibernate.connection.password = messagelog_password

6.  Populate database schema by reinstalling messagelog addon package (it will start xroad-proxy service also):

        root@security_server:~ # apt-get install --reinstall xroad-addon-messagelog


## 12 Audit Log

The security server keeps an audit log. The audit log events are generated by the user interface when the user changes the system’s state or configuration. The user actions are logged regardless of whether the outcome was a success or a failure. The complete list of the audit log events is described in \[[SPEC-AL](#Ref_SPEC-AL)\].

Actions that change the system state or configuration but are not carried out using the user interface are not logged (for example, X-Road software installation and upgrade, user creation and permission granting, and changing the configuration files).

An audit log record contains

-   the description of the user action,

-   the date and time of the event,

-   the username of the user performing the action, and

-   the data related to the event.

For example, registering a new client in the security server produces the following log record:

  `2015-07-03T10:21:59+03:00 my-security-server-host INFO [X-Road Proxy UI] 2015-07-03 10:21:59+0300 - {"event":"Register client", "user":"admin1", "data":{"clientIdentifier":{"xRoadInstance":"EE", "memberClass":"COM",  "memberCode":"member1"}, "clientStatus":"registration in progress"}}`

The event is present in JSON \[[JSON](#Ref_JSON)\] format, in order to ensure machine processability. The field event represents the description of the event, the field user represents the user name of the performer, and the field data represents data related with the event. The failed action event record contains an additional field reason for the error message. For example:

  `2015-07-03T11:55:39+03:00 my-security-server-host INFO [X-Road Proxy UI] 2015-07-03 11:55:39+0300 - {"event":"Log in to token failed", "user":"admin1", "reason":"PIN incorrect", "data":{"tokenId":"0", "tokenSerialNumber":null, "tokenFriendlyName":"softToken-0"}}`

By default, audit log is located in the file

    /var/log/xroad/audit.log


### 12.1 Changing the Configuration of the Audit Log

The X-Road software writes the audit log to the *syslog* (*rsyslog*) using UDP interface (default port is 514). Corresponding configuration is located in the file

    /etc/rsyslog.d/90-udp.conf

The audit log records are written with level INFO and facility LOCAL0. By default, log records of that level and facility are saved to the X-Road audit log file

    /var/log/xroad/audit.log

The default behavior can be changed by editing the *rsyslog* configuration file

    /etc/rsyslog.d/40-xroad.conf

Restart the *rsyslog* service to apply the changes made to the configuration file

    restart rsyslog

The audit log is rotated monthly by *logrotate*. To configure the audit log rotation, edit the *logrotate* configuration file

    /etc/logrotate.d/xroad-proxy


### 12.2 Archiving the Audit Log

In order to save hard disk space and avoid loss of the audit log records during security server crash, it is recommended to archive the audit log files periodically to an external storage or a log server.

The X-Road software does not offer special tools for archiving the audit log. The *rsyslog* can be configured to redirect the audit log to an external location.


## 13 Back up and Restore


### 13.1 Back up and Restore in the User Interface

**Access rights:** [System Administrator](#xroad-system-administrator)

The backup and restore view can be accessed from the **Management** menu by selecting **Back Up and Restore**.

To **back up configuration**, follow these steps.

1.  Click **Back Up Configuration**.

2.  A window opens displaying the output of the backup script; click **OK** to close it. The configuration backup file appears in the list of configuration backup files.

3.  To save the configuration backup file to the local file system, click **Download** on the configuration file’s row and save the prompted file.

To **restore configuration**, follow these steps.

1.  Click **Restore** on the appropriate row in the list of configuration backup files and click **Confirm**.

2.  A window opens displaying the output of the restore script; click **OK** to close it.

To **delete a configuration backup file**, click **Delete** on the appropriate row in the configuration backup file list and then click **Confirm**.

To **upload a configuration backup file** from the local file system to the security server, click **Upload Backup File**, select a file and click **OK**. The uploaded configuration file appears in the list of configuration files.


### 13.2 Restore from the Command Line

To restore configuration from the command line, the following data must be available:

-   The X-Road ID of the security server

It is expected that the restore command is run by the xroad user.

In order to restore configuration, the following command should be used:

    /usr/share/xroad/scripts/restore_xroad_proxy_configuration.sh \
    -s <security server ID> -f <path + filename>

For example (all on one line):

    /usr/share/xroad/scripts/restore_xroad_proxy_configuration.sh \
    -s AA/GOV/TS1OWNER/TS1 \
    –f /var/lib/xroad/backup/conf_backup_20140703-110438.tar

If it is absolutely necessary to restore the system from a backup made on a different security server, the forced mode of the restore command can be used with the –F option. For example (all on one line):

    /usr/share/xroad/scripts/restore_xroad_proxy_configuration.sh \
    -F –f /var/lib/xroad/backup/conf_backup_20140703-110438.tar


## 14 Diagnostics


### 14.1 Examine security server services status information

**Access rights:** [System Administrator](#xroad-system-administrator)

Open the **Management** menu and select **Diagnostics**.

On this page you can examine the statuses of the following services:

 Service              | Status           | Message        | Previous Update          | Next Update
--------------------- | ---------------- | -------------- | ------------------------ | ------------------------
 Global configuration | Green/yellow/red | Status message | The time of the global configuration client’s last run | The estimated time of the global configuration client’s next run
 Timestamping         | Green/yellow/red | Status message | The time of the last timestamping operation            | Not used                                   
 OCSP-responders      | Green/yellow/red | Status message | The time of the last contact with the OCSP-responder   | The latest possible time for the next OCSP-refresh
 
To refresh the service statuses click the **Diagnostics** item on the **Management** menu.

The status colors indicate the following:
- **Red indicator** – service cannot be contacted or is not operational
- **Yellow indicator** – service has been contacted but is yet to have been used to verify its status
- **Green indicator** – service has been successfully contacted and used to verify it is operational

The status message offers more detailed information on the current status.

If a section of the diagnostics view appears empty, it means that there either is no configured service available or that checking the service status has failed. If sections are empty, try refreshing the diagnostics view or check the service configuration.

## 15 Operational Monitoring

**Operational monitoring data** contains data about request exchange (such as the ID-s of the client and the service, various attributes of the message read from the message header, request and response timestamps, SOAP sizes etc.) of the X-Road security server(s).

**The operational monitoring daemon** collects and shares operational monitoring data of the X-Road security server(s) as part of request exchange, shares this data, calculates and shares health statistics (the timestamps and number of successful/unsuccessful requests, various metrics of the duration and the SOAP message size of the requests, etc.). The data fields that are stored and shared are described in \[[PR-OPMON](#Ref_PR-OPMON)\].

The security server caches operational monitoring data in the **operational monitoring buffer**. One operational data record is created for each request during the message exchange. Security server forwards operational data cached in the operational monitoring buffer to the operational monitoring daemon. Successfully forwarded records are removed from the operational monitoring buffer.

The operational monitoring daemon makes operational and health data available to the owner of the security server, regular clients and the central monitoring client via the security server. Local health data are available for external monitoring systems (e.g. Zabbix) over the JMXMP interface described in \[[PR-OPMONJMX](#Ref_PR-OPMONJMX)\].

The owner of the security server and the central monitoring client are able to query the records of all clients. For a regular client, only the records associated with that client are available. The internal IP of the security server is included in the response only for the owner of the security server and central monitoring client.

**NOTE:** All the commands in the following sections must be carried out using root permissions.


### 15.1 Operational Monitoring Buffer

In general, the operational monitoring buffer is an internal component of the security server and thus being not directly used by the end user.

The configuration parameters available for configuring the operational monitoring buffer have been documented in \[[UG-OPMONSYSPAR](#Ref_UG-OPMONSYSPAR)\].

The default values of the parameters have been chosen to be sufficient under expected average load using the minimum hardware recommended.

All overrides to the default configuration values must be made in the file `/etc/xroad/conf.d/local.ini`, in the `[op-monitor-buffer]` section.


#### 15.1.1 Stopping the Collecting of Operational Data

If, for any reason, operational data should not be collected and forwarded to the operational monitoring daemon, the parameter size can be set to 0:

    [op-monitor-buffer]
    size = 0

After the configuration change, the xroad-proxy service must be restarted:

    service xroad-proxy restart

In addition, the operational monitoring daemon should be stopped:

    service xroad-opmonitor stop

For the service to stay stopped after reboot the following command should be run:

    echo manual > /etc/init/xroad-opmonitor.override


### 15.2 Operational Monitoring Daemon

The configuration parameters available for configuring the operational monitoring daemon have been documented in \[[UG-OPMONSYSPAR](#Ref_UG-OPMONSYSPAR)\].

Similarly to the operational monitoring buffer, the default values of the parameters have been chosen to be sufficient under expected average load using the minimum recommended hardware.

All overrides to the default configuration values must be made in the file `/etc/xroad/conf.d/local.ini`, in the `[op-monitor]` section.

In the following sections, some parameters are described which may be required to be changed more likely.


#### 15.2.1 Configuring the Health Statistics Period

By default, health statistics are provided for a period of 600 seconds (10 minutes). This means that if no request exchange has taken place for 10 minutes, all the statistical metrics are reset. Please refer to \[[PR-OPMON](#Ref_PR-OPMON)\] for a detailed overview of the health metrics available.

To change the health statistics period, the value of the parameter health-statistics-period-seconds should be set or edited in the `[op-monitor]`
section of the file `/etc/xroad/conf.d/local.ini`.


#### 15.2.2 Configuring the Parameters Related to Database Cleanup

Depending on the load and resources of the system, it may be necessary to change the interval of the removal of old database records.

The following parameters must be placed in the `[op-monitor]` section of the file `/etc/xroad/conf.d/local.ini`.

The parameter `keep-records-for-days` should be edited, for instance if the disk fills up before cleanup occurs, or alternatively, if the default period of 7 days is too short. The parameter `clean-interval` (a Cron expression \[[CRON](#Ref_CRON)\]) defines how often the system checks whether cleanup should be done. If the default period of 12 hours is too long or short it should be edited according to your needs.


#### 15.2.3 Configuring the Parameters related to the HTTP Endpoint of the Operational Monitoring Daemon

For configuring the endpoint of the operational monitoring daemon, the following parameters are available in the `[op-monitor]` section of the configuration:

**host** – listening host of the daemon (by default the value is set to *localhost*).

**port** – listening port (by default the value is set to *2080*).

**scheme** – connection type (by default the value is set to *HTTP*).

If any of these values are changed, both the proxy and the operational monitoring daemon services must be restarted:

    service xroad-proxy restart
    service xroad-opmonitor restart


#### 15.2.4 Installing an External Operational Monitoring Daemon

Technically, the operational monitoring daemon can be installed on a separate host from the security server. It is possible to configure several security servers to use that external operational monitoring daemon, but this setup is correct *only* if the security servers are identical clones installed behind a load balancer.

**NOTE:** The setup of clustered security servers is not officially supported yet and has been implemented for future compatibility.

**NOTE:** It is **strongly advised** to use HTTPS for requests between a security server and the associated external operational monitoring daemon.

For running a separate operational monitoring daemon, the xroad-opmonitor package must be installed. Please refer to \[[IG-SS](#Ref_IG-SS)\] for general instructions on obtaining X-Road packages.

As a result of installation, the following services will be running:

    xroad-confclient
    xroad-signer
    xroad-opmonitor


#### 15.2.5 Configuring an External Operational Monitoring Daemon and the Corresponding Security Server

To make a security server communicate with an external operational monitoring daemon, it is necessary to configure both the daemon and the security server.

By default, the operational monitoring daemon listens on localhost. To make the daemon available to security servers on other hosts, the listening address must be set to the IP address that is relevant in the particular network, as described in the previous section.

As advised, the scheme parameter should be set to “https”. For communication over HTTPS, the security server and the operational monitoring daemon must know each other’s TLS certificates to enable the security server to authenticate to the monitoring daemon successfully.

**NOTE:** If an external operational monitoring daemon is used, the host, scheme (and optionally, port) parameters must be changed at both hosts.

The internal TLS certificate of the security server is used for authenticating the security server to the operational monitoring daemon. This certificate has been generated beforehand, during the installation process of the security server, and is available in PEM format in the file `/etc/xroad/ssl/internal.crt`. Please refer to Section [10.3](#103-changing-the-internal-tls-key-and-certificate) for the instructions on exporting the internal TLS certificate from UI. The file must be copied to the host running the operational monitoring daemon. The system user xroad must have permissions to read this file.

In the configuration of the external daemon, the corresponding path must be set in `/etc/xroad/conf.d/local.ini`:

    [op-monitor]
    client-tls-certificate = <path/to/security/server/internal/cert>

Next, a TLS key and the corresponding certificate must be generated on the host of the external monitoring daemon as well, using the command

    generate-opmonitor-certificate

The script will prompt you for standard fields for input to TLS certificates and its output (key files and the certificate) will be generated to the directory `/etc/xroad/ssl`.

The generated certificate, in the file `opmonitor.crt`, must be copied to the corresponding security server. The system user `xroad` must have permissions to read this file. Its path at the security server must be written to the configuration (note the name of the section, although it is the proxy service that will read the configuration):

    [op-monitor]
    tls-certificate = <path/to/external/daemon/tls/cert>

For the external operational daemon to be used, the proxy service at the security server must be restarted:

    service xroad-proxy restart

In addition, on the host running the corresponding security server, the operational monitoring daemon must be stopped:

    service xroad-opmonitor stop

For the service to stay stopped after reboot the following command should be run:

    echo manual > /etc/init/xroad-opmonitor.override

The configuration anchor (renamed as `configuration-anchor.xml`) file must be manually copied into the directory `/etc/xroad` of the external monitoring daemon in order for configuration client to be able to download the global configuration (by default configuration download interval is 60 seconds). The system user xroad must have permissions to read this file.


#### 15.2.6 Monitoring Health Data over JMXMP

The operational monitoring daemon makes health data available over the JMXMP protocol. The Zabbix monitoring software can be configured to gather that data periodically, using its built in JMX interface type.

By default, the operational monitoring daemon exposes health data over JMXMP on localhost, port 9011, with no authentication configured and TLS enabled (see `/etc/xroad/services/opmonitor.conf`). This configuration has to be customized for external tools such as Zabbix to be able to access the data. Please refer to the documentation at \[[JMX](#Ref_JMX)\] for instructions on configuring access to the JMX interface of the operational monitoring daemon.

For Zabbix to be able to gather data over JMX, the Zabbix Java gateway must be installed. See \[[ZABBIX-GATEWAY](#Ref_ZABBIX-GATEWAY)\] for instructions.

The JMX interface must be configured to each host item in Zabbix, for which health data needs to be obtained. See \[[ZABBIX-JMX](#Ref_ZABBIX-JMX)\] for instructions.

Please refer to \[[PR-OPMONJMX](#Ref_PR-OPMONJMX)\] for a specification of the names and attributes of the JMX objects exposed by the operational monitoring daemon.

The xroad-opmonitor package comes with sample host data that can be imported to Zabbix, containing a JMX interface, applications related to sample services and health data items under these services. Also, a script is provided for importing health data related applications and items to several hosts using the Zabbix API. Please find the example files in the directory `/usr/share/doc/xroad-opmonitor/examples/zabbix/`. Please refer to \[[ZABBIX-API](#Ref_ZABBIX-API)\] for information on the Zabbix API.


## 16 Logs and System Services

**To read logs**, a user must have root user's rights or belong to the `xroad` and/or `adm` system group.


### 16.1 System Services

The most important system services of a security server are as follows.

 **Service**        | **Purpose**                                   | **Log**
------------------- | --------------------------------------------- | -----------------------------------------
 `xroad-confclient` | Client process for the global configuration distributor | `/var/log/xroad/configuration_client.log`
 `xroad-jetty `     | Application server running the user interface | `/var/log/xroad/jetty/`
 `xroad-proxy`      | Message exchanger                             | `/var/log/xroad/proxy.log`
 `xroad-signer`     | Manager process for key settings              | `/var/log/xroad/signer.log`
 `nginx`            | Web server that exchanges the services of the user interface's application server and the message exchanger | `/var/log/nginx/`           

System services are managed through the *upstart* facility.

**To start a service**, issue the following command as a `root` user:

    service <service> start

**To stop a service**, enter:

    service <service> stop


### 16.2 Logging configuration

For logging, the **Logback** system is used. Logback configuration files are stored in the directory `/etc/xroad/conf.d/`.

Default settings for logging are the following:

-   logging level: INFO;

-   rolling policy: whenever the file size reaches 100 MB.


### 16.3 Fault Detail UUID

In case a security server encounters an error condition during the message exchange, the security server returns a SOAP Fault message \[[PR-MESS](#Ref_PR-MESS)\] containing a UUID (a universally unique identifier, e.g. `1328e974-4fe5-412c-a4c4-f1ac36f20b14`) as the fault detail to the service client's information system. The UUID can be used to find the details of the occurred error from the `xroad-proxy` log.

## 17 Federation

Federation allows security servers of two different X-Road instances to exchange messages with each other. The instances
are federated at the central server level. After this, security servers can be configured to opt-in to the federation.
By default, federation is disabled and configuration data for other X-Road instances will not be downloaded.

The federation can be allowed for all X-Road instances that the central server offers, or a list of specific
(comma-separated) instances. The default is to allow none. The values are case-insensitive.

To override the default value, edit the file `/etc/xroad/conf.d/local.ini` and add or change the value of the system
parameter `allowed-federations` for the server component `configuration-client`. To restore the default, either remove
the system parameter entirely or set the value to `none`. X-Road services `xroad-confclient` and `xroad-proxy` need to
be restarted (in that order) for any setting changes to take effect.

Below are some examples for `/etc/xroad/conf.d/local.ini`.

To allow federation with all offered X-Road instances:
```
[configuration-client]
allowed-federations=all
```

To allow federation with specific instances `xe-test` and `ee-test`:
```
[configuration-client]
allowed-federations=xe-test,ee-test
```

To disable federation, just remove the `allowed-federations` system parameter entirely or use:
```
[configuration-client]
allowed-federations=none
```

Please note that if the keyword `all` is present in the comma-separated list, it will override the single allowed
instances. The keyword `none` will override all other values. This means that the following setting will allow all
federations:
```
[configuration-client]
allowed-federations=xe-test, all, ee-test
```
And the following will allow none:
```
[configuration-client]
allowed-federations=xe-test, all, none, ee-test
```
