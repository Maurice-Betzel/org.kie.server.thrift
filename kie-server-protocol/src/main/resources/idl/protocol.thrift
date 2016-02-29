namespace php hmm.zhponline.base.thrift.protocol
namespace java org.kie.server.thrift.protocol

const string PROTOCOL_VERSION = "6.4.0.0"
const string APPLICATION_XTHRIFT = "application/x-thrift"
const string ROLE_ARCHITECT = "architect"
const string ROLE_ENGINEER = "engineer"
const string ROLE_MANAGER = "manager"
const string ROLE_ANALYST = "analyst"
const string ROLE_OWNER = "owner"

enum KieContainerStatus {
    CREATING,
    STARTED,
    FAILED,
    DISPOSING,
    STOPPED
}

enum KieScannerStatus {
    UNKNOWN,
    CREATED,
    STARTED,
    SCANNING,
    STOPPED,
    DISPOSED
}

enum AgendaFilterType {
    RULE_NAME_EQUALS;
    RULE_NAME_MATCHES;
    RULE_NAME_STARTS_WITH;
    RULE_NAME_ENDS_WITH;
}

// Drools commands, see Drools doc

struct BatchExecutionCommand {
    1: required FireAllRulesCommand fireAllRulesCommand;
    2: optional list<InsertObjectCommand> insertObjectCommands;
    3: optional list<InsertElementsCommand> insertElementsCommands;
    4: optional list<SetGlobalCommand> setGlobalCommands;
}

struct FireAllRulesCommand {
    1: required i32 max = -1
    2: optional string outIdentifier;
    3: optional AgendaFilter agendaFilter;
}

struct AgendaFilter {
    1: required AgendaFilterType agendaFilterType;
    2: required string expression;
    3: required bool accept = true
}

struct InsertObjectCommand {
    1: required binary object;
    2: required string classCanonicalName;
    3: required bool returnObject = true
    4: required string entryPoiny = "DEFAULT"
    5: optional string outIdentifier;
}

struct InsertElementsCommand {
    1: required list<binary> objects;
    2: required string classCanonicalName;
    3: required bool returnObject = true
    4: required string entryPoiny = "DEFAULT"
    5: optional string outIdentifier;
}

struct SetGlobalCommand {
    1: required binary object;
    2: required string classCanonicalName;
    3: required string identifier;
    4: optional string outIdentifier;
}

struct ExecutionResults {
    1: required list<string> outIdentifiers;
    2: optional map<string, binary> objects;
}

// KIE Server protocol

exception KieServicesException {
    1: required string type;
    2: required string message;
    3: optional string trace;
}

struct KieServicesRequest {
    1: required string protocolVersion = PROTOCOL_VERSION;
    2: required KieServicesClient kieServicesClient;
    3: required BatchExecutionCommand batchExecutionCommand;
}

struct KieServicesResponse {
    1: required string protocolVersion = PROTOCOL_VERSION;
    2: required Response response;
}

union Response {
    1: Info info;
    2: Results results;
    3: KieServicesException kieServicesException;
}

union Info {
    1: ServerInfo serverInfo;
    2: ListContainers listContainers;
    3: ContainerInfo ContainerInfo;
}

union Results {
    1: ExecutionResults executionResults;
}

// KIE Server models

struct ServerInfo {
    1: required Version version;
    2: required string serverId;
    3: required string serverName;
    4: required string kieServerLocation;
    5: required list<string> serverExtensions;
}

struct ListContainers {
    1: optional list<string> containers;
}

struct ContainerInfo {
    1: required string containerId;
    2: required ReleaseId releaseId;
    3: required KieContainerStatus kieContainerStatus;
    4: required KieScannerInfo kieScannerInfo;
}

struct KieServicesClient {
    1: required string client;
    2: optional Version version;
    3: optional Authentication authentication;
}

struct Version {
    1: required i32 major;
    2: optional i32 minor;
    3: optional i32 revision;
    4: optional string classifier;
}

struct ReleaseId {
     1: required string groupId;
     2: required string artifactId;
     3: required string version;
}

struct KieScannerInfo {
    1: required KieScannerStatus kieScannerStatus;
    2: required i64 pollInterval;
}

struct Authentication {
    1: required string user;
    2: required string password;
    3: required Role role;
}

struct Role {
    1: required string role = ROLE_OWNER
}

// Collection response model

 struct Collection {
    1: required list<binary> objects;
 }