define(function() { return TP_Tetrapod });

function TP_Tetrapod(server) {

   server.register("response", "Tetrapod", "ERROR", 1, 1);
   server.register("response", "Tetrapod", "SUCCESS", 1, 2);

   server.registerConst("Tetrapod", "Core", "UNADDRESSED", 0);
   server.registerConst("Tetrapod", "Core", "DIRECT", 1);
   server.registerConst("Tetrapod", "Core", "TYPE_TETRAPOD", 1);
   server.registerConst("Tetrapod", "Core", "TYPE_SERVICE", 2);
   server.registerConst("Tetrapod", "Core", "TYPE_ADMIN", 3);
   server.registerConst("Tetrapod", "Core", "TYPE_CLIENT", 4);
   server.registerConst("Tetrapod", "Core", "TYPE_ANONYMOUS", 5);
   server.registerConst("Tetrapod", "Core", "STATUS_STARTING", 1);
   server.registerConst("Tetrapod", "Core", "STATUS_PAUSED", 2);
   server.registerConst("Tetrapod", "Core", "STATUS_GONE", 4);
   server.registerConst("Tetrapod", "Core", "STATUS_BUSY", 8);
   server.registerConst("Tetrapod", "Core", "STATUS_OVERLOADED", 16);
   server.registerConst("Tetrapod", "Core", "STATUS_FAILED", 32);
   server.registerConst("Tetrapod", "Core", "STATUS_STOPPING", 64);
   server.registerConst("Tetrapod", "Core", "ENVELOPE_HANDSHAKE", 1);
   server.registerConst("Tetrapod", "Core", "ENVELOPE_REQUEST", 2);
   server.registerConst("Tetrapod", "Core", "ENVELOPE_RESPONSE", 3);
   server.registerConst("Tetrapod", "Core", "ENVELOPE_MESSAGE", 4);
   server.registerConst("Tetrapod", "Core", "ENVELOPE_BROADCAST", 5);
   server.registerConst("Tetrapod", "Core", "ENVELOPE_PING", 6);
   server.registerConst("Tetrapod", "Core", "ENVELOPE_PONG", 7);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_BOOLEAN", 1);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_BYTE", 2);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_INT", 3);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_LONG", 4);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_DOUBLE", 5);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_STRING", 6);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_STRUCT", 7);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_BOOLEAN_LIST", 8);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_BYTE_LIST", 9);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_INT_LIST", 10);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_LONG_LIST", 11);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_DOUBLE_LIST", 12);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_STRING_LIST", 13);
   server.registerConst("Tetrapod", "TypeDescriptor", "T_STRUCT_LIST", 14);
   server.registerConst("Identity", "Identity", "IDENTITY_EMAIL", 1);
   server.registerConst("Identity", "Identity", "IDENTITY_DEVICE", 2);
   server.registerConst("Identity", "Identity", "IDENTITY_FACEBOOK", 3);
   server.registerConst("Identity", "Identity", "IDENTITY_TWITTER", 4);
   server.registerConst("Identity", "Identity", "IDENTITY_OAUTH", 5);
   server.registerConst("Identity", "Identity", "AUTH_TOKEN_USER", 1);
   server.registerConst("Identity", "Identity", "AUTH_TOKEN_APP_1", 2);
   server.registerConst("Identity", "Identity", "AUTH_TOKEN_APP_2", 3);
   server.registerConst("Identity", "Identity", "AUTH_TOKEN_APP_3", 4);
   server.registerConst("Identity", "User", "PROPS_DEVELOPER", 1);
   server.registerConst("Identity", "User", "PROPS_ADMIN_T1", 2);
   server.registerConst("Identity", "User", "PROPS_ADMIN_T2", 4);
   server.registerConst("Identity", "User", "PROPS_ADMIN_T3", 8);
   server.registerConst("Identity", "User", "PROPS_ADMIN_T4", 16);
   server.registerConst("Identity", "User", "PROPS_BANNED_T1", 32);
   server.registerConst("Identity", "User", "PROPS_BANNED_T2", 64);
   server.registerConst("Identity", "User", "PROPS_BANNED_T3", 128);
   server.registerConst("Identity", "User", "PROPS_NO_PASSWORD", 256);
   
   server.registerConst("Tetrapod", "null", "CONNECTION_CLOSED", 7);
   server.registerConst("Tetrapod", "null", "INVALID_ENTITY", 9);
   server.registerConst("Tetrapod", "null", "INVALID_RIGHTS", 8);
   server.registerConst("Tetrapod", "null", "NOT_PARENT", 2219555);
   server.registerConst("Tetrapod", "null", "NOT_READY", 12438466);
   server.registerConst("Tetrapod", "null", "PROTOCOL_MISMATCH", 5);
   server.registerConst("Tetrapod", "null", "RIGHTS_EXPIRED", 10);
   server.registerConst("Tetrapod", "null", "SERIALIZATION", 4);
   server.registerConst("Tetrapod", "null", "SERVICE_UNAVAILABLE", 2);
   server.registerConst("Tetrapod", "null", "TIMEOUT", 3);
   server.registerConst("Tetrapod", "null", "UNKNOWN", 1);
   server.registerConst("Tetrapod", "null", "UNKNOWN_REQUEST", 6);
   server.registerConst("Identity", "null", "IDENTITY_TAKEN", 5562311);
   server.registerConst("Identity", "null", "INVALID_INPUT", 9895911);
   server.registerConst("Identity", "null", "UNKNOWN_USERNAME", 983354);
   server.registerConst("Identity", "null", "UNMODIFIABLE_IDENTITY", 548527);
   server.registerConst("Identity", "null", "VERIFICATION_ERROR", 10526271);
   server.registerConst("Identity", "null", "VERIFICATION_FAILURE", 3531687);
   
   server.register("struct", "BaseService", "ServiceCommand", 2, 5461687);
   server.register("request", "BaseService", "Pause", 2, 14690004);
   server.register("request", "BaseService", "Unpause", 2, 10620319);
   server.register("request", "BaseService", "Shutdown", 2, 8989182);
   server.register("request", "BaseService", "Restart", 2, 4802943);
   server.register("request", "BaseService", "ServiceDetails", 2, 14458441);
   server.register("response", "BaseService", "ServiceDetails", 2, 12435407);
   server.register("request", "BaseService", "ServiceStatsSubscribe", 2, 13519504);
   server.register("request", "BaseService", "ServiceStatsUnsubscribe", 2, 576067);
   server.register("message", "BaseService", "ServiceStats", 2, 469976);
   server.register("struct", "Tetrapod", "Core", 1, 9088168);
   server.register("struct", "Tetrapod", "RequestHeader", 1, 7165109);
   server.register("struct", "Tetrapod", "ResponseHeader", 1, 675609);
   server.register("struct", "Tetrapod", "MessageHeader", 1, 11760427);
   server.register("struct", "Tetrapod", "ServerAddress", 1, 14893956);
   server.register("struct", "Tetrapod", "Entity", 1, 10171140);
   server.register("struct", "Tetrapod", "Subscriber", 1, 16013581);
   server.register("struct", "Tetrapod", "WebRoute", 1, 4890284);
   server.register("struct", "Tetrapod", "TypeDescriptor", 1, 6493266);
   server.register("struct", "Tetrapod", "StructDescription", 1, 9642196);
   server.register("request", "Tetrapod", "Register", 1, 10895179);
   server.register("response", "Tetrapod", "Register", 1, 13376201);
   server.register("request", "Tetrapod", "ClusterJoin", 1, 8294880);
   server.register("response", "Tetrapod", "ClusterJoin", 1, 8947508);
   server.register("request", "Tetrapod", "Unregister", 1, 3896262);
   server.register("request", "Tetrapod", "Publish", 1, 3171651);
   server.register("response", "Tetrapod", "Publish", 1, 2698673);
   server.register("request", "Tetrapod", "RegistrySubscribe", 1, 2572089);
   server.register("request", "Tetrapod", "RegistryUnsubscribe", 1, 6168014);
   server.register("request", "Tetrapod", "ServicesSubscribe", 1, 7048310);
   server.register("request", "Tetrapod", "ServicesUnsubscribe", 1, 11825621);
   server.register("request", "Tetrapod", "ServiceStatusUpdate", 1, 4487218);
   server.register("request", "Tetrapod", "AddServiceInformation", 1, 14381454);
   server.register("request", "Tetrapod", "LogRegistryStats", 1, 10504592);
   server.register("request", "Tetrapod", "AdminLogin", 1, 14191480);
   server.register("response", "Tetrapod", "AdminLogin", 1, 4213436);
   server.register("request", "Tetrapod", "AdminAuthorize", 1, 12706146);
   server.register("message", "Tetrapod", "Entity", 1, 10913291);
   server.register("message", "Tetrapod", "ClusterMember", 1, 1076508);
   server.register("message", "Tetrapod", "EntityRegistered", 1, 1454035);
   server.register("message", "Tetrapod", "EntityUnregistered", 1, 14101566);
   server.register("message", "Tetrapod", "EntityUpdated", 1, 3775838);
   server.register("message", "Tetrapod", "TopicPublished", 1, 6873263);
   server.register("message", "Tetrapod", "TopicUnpublished", 1, 6594504);
   server.register("message", "Tetrapod", "TopicSubscribed", 1, 1498241);
   server.register("message", "Tetrapod", "TopicUnsubscribed", 1, 6934832);
   server.register("message", "Tetrapod", "EntityListComplete", 1, 15616758);
   server.register("message", "Tetrapod", "ServiceAdded", 1, 15116807);
   server.register("message", "Tetrapod", "ServiceRemoved", 1, 1629937);
   server.register("message", "Tetrapod", "ServiceUpdated", 1, 1658756);
   server.register("request", "Identity", "Login", 4, 8202985);
   server.register("response", "Identity", "Login", 4, 16389615);
   server.register("request", "Identity", "Logout", 4, 3999326);
   server.register("request", "Identity", "LoginWithToken", 4, 1987710);
   server.register("response", "Identity", "LoginWithToken", 4, 6988514);
   server.register("request", "Identity", "ModifyIdentity", 4, 7124244);
   server.register("request", "Identity", "Create", 4, 6552804);
   server.register("response", "Identity", "Create", 4, 5348608);
   server.register("request", "Identity", "Link", 4, 15857496);
   server.register("response", "Identity", "Link", 4, 14285084);
   server.register("request", "Identity", "Info", 4, 14709500);
   server.register("response", "Identity", "Info", 4, 3624488);
   server.register("request", "Identity", "UpdateProperties", 4, 1362696);
   server.register("request", "Identity", "AddUnverifiedUser", 4, 3799907);
   server.register("response", "Identity", "AddUnverifiedUser", 4, 415825);
   server.register("request", "Identity", "GetAuthSecret", 4, 7311132);
   server.register("response", "Identity", "GetAuthSecret", 4, 378568);
   server.register("struct", "Identity", "Identity", 4, 12701893);
   server.register("struct", "Identity", "User", 4, 10894876);

}
