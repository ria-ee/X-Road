# X-Road: Message Log Data Model

Version: 1.3  
Doc. ID: DM-ML

| Date       | Version     | Description                                     | Author             |
|------------|-------------|-------------------------------------------------|--------------------|
| 21.08.2015 | 0.1         | Initial version                                 | Mait Märdin        |
| 25.08.2015 | 0.2         | Resolved a few TODOs                            | Siim Annuk         |
| 10.09.2015 | 0.3         | Added corrections and comments                  | Margus Freudenthal |
| 14.09.2015 | 0.4         | Added more informative text                     | Siim Annuk         |
| 16.09.2015 | 0.5         | Cleaned up a bit and added some documentation   | Margus Freudenthal |
| 16.09.2015 | 0.6         | PostgreSQL version 9.4 is used now              | Martin Lind        |
| 16.09.2015 | 0.7         | We still use Postgre SQL 9.3                    | Martin Lind        |
| 20.09.2015 | 1.0         | Editorial changes made                          | Imbi Nõgisto       |
| 19.10.2015 | 1.1         | Indexes added                                   | Martin Lind        |
| 16.12.2016 | 1.2         | Described index added to message log            | Martin Lind        |
| 16.02.2017 | 1.3         | Converted to markdown                           | Ilkka Seppälä      |
| 16.02.2017 | 1.4         | Added index to logrecord, fixed earlier logrecord index name  | Olli Lindgren      |

##Table of Contents

- [General](#1-general)
  - [Preamble](#11-preamble)
  - [Database Version](#12-database-version)
  - [Creating, Backing Up and Restoring the Database](#13-creating-backing-up-and-restoring-the-database)
  - [Message Logging and Timestamping](#14-message-logging-and-timestamping)
  - [Entity-Relationship Diagram](#15-entity-relationship-diagram)
- [Description of Entities](#2-description-of-entities)
  - [LOGRECORD](#21-logrecord)
    - [Indexes](#211-indexes)
    - [Attributes](#212-attributes)
  - [LAST_ARCHIVE_DIGEST](#22-last-archive-digest)
    - [Attributes](#221-attributes)
  - [DATABASECHANGELOG](#23-databasechangelog)
    - [Attributes](#231-attributes)
  - [DATABASECHANGELOGLOCK](#24-databasechangeloglock)
    - [Attributes](#241-attributes)

# 1. General

## 1.1 Preamble

This document describes database model of X-Road message log.

## 1.2 Database Version

This database assumes PostgreSQL version 9.3. Ubuntu 14.04 default settings are used.

## 1.3 Creating, Backing Up and Restoring the Database

This database is integrated into X-Road message log component.

The database, the database user and the data model is created by the component's installer. The database updates are packaged as component updates and are applied when the component is upgraded. From the technical point of view, the database structure is created and updated using Liquibase tool (see http://www.liquibase.org/). The migration scripts can be found both in component source and in file system of the installed component.

The database is used for logging purposes only and does not contain any configuration. Backing-up and restoring the database is not necessary for the functioning of the component.

## 1.4 Message Logging and Timestamping

The input to the message log component consists of a message and its corresponding signature (along with hash chain and hash chain result if the signature is a batch signature). Depending on the security policy, timestamping can be asynchronous (one or more signatures are batch timestamped) or synchronous (to guarantee the timestamp).

The process of logging and asynchronous timestamping consists of the following steps:

1. System verifies that the message and signature can be logged – if the time of last successful timestamping is older than specified by the security policy then no more messages are accepted by the system and the situation should be considered as system failure.
2. System saves the message and signature in the message log.
3. System periodically timestamps messages that have no timestamp. Batch timestamp is created if more than one message is timestamped simultaneously. Regular timestamp is created for single messages.

When timestamping synchronously, the logging call will block until the timestamp is received. The process of synchronous timestamping consists of the following steps:

1. The system saves the message and signature in the message log.
2. System timestamps the message synchronously.

## 1.5 Entity-Relationship Diagram

![Entity-Relationship Diagram](img/messagelog-er-diagram.png)

# 2. Description of Entities

## 2.1 LOGRECORD

Log record can either be a message record or a timestamp record. A message record is created when the system processes an X-Road message. A timestamp record is created when the system timestamps the message records created since the last timestamping. A message record is modified by adding a reference to a timestamp record after it has been timestamped. All the records are modified by changing the archived flag after they have been archived. All the records are eventually deleted after they have been timestamped and archived.

### 2.1.1 Indexes

| Name        | Columns           | Partial index details  |
| ----------- |:-----------------:| ----------------------:|
| LOGRECORD_TIMESTAMPRECORD_fkey | timestamprecord | N/A |
| IX_NOT_ARCHIVED_LOGRECORD | id | where discriminator = 't' and archived = false |
| IX_NOT_TIMESTAMPED_LOGRECORD | id, discriminator, signaturehash | where discriminator = 'm' and signaturehash is not null |

### 2.1.2 Attributes

| Name        | Type           | Modifiers  | Description |
| ----------- |:--------------:| ----------:| -----------:|
| id [PK] | bigint | NOT NULL | Primary key |
| discriminator | character varying(255) | NOT NULL | Technical attribute, specifying the Java class to which the log record is mapped. The possible values are “m” (MessageRecord) and “t” (TimestampRecord). The corresponding Java classes are located in the ee.ria.xroad.common.messagelog package. |
| queryid | character varying(255) | | The id field of the SOAP message header. Only present for message records. |
| response | boolean | | A flag indicating whether the message in this log record is a response message (as opposed to a request message). Only present for message records. |
| memberclass | character varying(255) | | Member class of the client who sent this message. Only present for message records. |
| membercode | character varying(255) | | Member code of the client who sent this message. Only present for message records. |
| subsystemcode | character varying(255) | | Subsystem code of the client who sent this message. Only present for message records. |
| message | text | | The SOAP message body. Only present for message records. |
| signature | text | | The signature of the message. Only present for message records. |
| signaturehash | text | | Hash of the signature of the message. Only present for message records. |
| timestamp | text | | Base64-encoded contents of the time stamp.  Only present for timestamp records. |
| timestamprecord [FK] | bigint | | Identifies the timestamp record that timestamps this message record. References id attribute of LOGRECORD entity. Only present for message records. |
| timestamphashchain | text | | If the message record is time-stamped, the base-64 encoded hash chain of the timestamp. Only present for message records. |
| hashchain | text | | If the signature is a batch signature, the base-64 encoded hash chain. Only present for message records. |
| hashchainresult | text | | If the signature is a batch signature, the base-64 encoded hash chain result. Only present for message records. |
| time | bigint | | The creation time of the log record (number of milliseconds since January 1, 1970, 00:00:00 GMT). |
| archived | boolean | | A flag indicating whether this log record has been archived. |

## 2.2 LAST_ARCHIVE_DIGEST

Records the last digest of the archive file. When archiving signatures, the message log links them together using cryptographic hash functions. When creating an archive, the last link is saved in the last_archive_digest table. This makes it possible to continue the hash chain for the next archive file.

The record is created when the first archive file is created. The record is modified every time when an archive file s created. The record is never deleted.

### 2.2.1 Attributes

| Name        | Type           | Modifiers  | Description |
| ----------- |:--------------:| ----------:| -----------:|
| id [PK] | bigint | NOT NULL | Primary key |
| digest | text | | Digest of the last archive file. |
| filename | character varying(255) | | The filename of the last archive. |

## 2.3 DATABASECHANGELOG

Liquibase migration of the database. A record is created when the administrator updates the software package containing this database and the database structure needs to be modified. The record is never modified or deleted. This table has a technical nature and is not managed by X-Road application software.

### 2.3.1 Attributes

| Name        | Type           | Modifiers  | Description |
| ----------- |:--------------:| ----------:| -----------:|
| id | character varying(255) | NOT NULL | The identifier of the migration. |
| author | character varying(255) | NOT NULL | The author of the migration. |
| filename | character varying(255) | NOT NULL | The filename containing the migration script. |
| dateexecuted | timestamp with time zone | NOT NULL | The time when the migration was executed. Used with orderexecuted to determine rollback order. |
| orderexecuted | integer | NOT NULL | The order number in which the migration was executed. Used in addition to dateexecuted to ensure order is correct even when the databases datetime supports poor resolution. |
| exectype | character varying(10) | NOT NULL | The type of the execution that was performed. Possible values are EXECUTED, FAILED, SKIPPED, RERAN, and MARK_RAN. |
| md5sum | character varying(35) | | The MD5 hash of the migration script when it was executed. Used on each run to ensure there have been no unexpected changes to the migration script. |
| description | character varying(255) | | Short auto-generated human readable description of the migration. |
| comments | character varying(255) | | The comments of the migration. |
| tag | character varying(255) | | The tag of the migration. |
| liquibase | character varying(20) | | The version of the Liquibase that performed the migration. |

## 2.4 DATABASECHANGELOGLOCK

Lock used by Liquibase to allow only one migration of the database to run at a time. This tabel has a technical nature and is not managed by X-Road application software.

### 2.4.1 Attributes

| Name        | Type           | Modifiers  | Description |
| ----------- |:--------------:| ----------:| -----------:|
| id [PK] | integer | NOT NULL | Primary key. Id of the lock. Currently there is only one lock. |
| locked | boolean | NOT NULL | Set to "1" if the Liquibase is running against this database. Otherwise set to "0". |
| lockgranted | timestamp with time zone | | Date and time when the lock was granted. |
| lockedby | character varying(255) | | Human-readable description of who the lock was granted to. |
