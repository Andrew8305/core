define([], function() { return TP_Core });

function TP_Core(server) {

   server.register("response", "Tetrapod", "ERROR", 1, 1);
   server.register("response", "Tetrapod", "SUCCESS", 1, 2);

   server.registerConst("Core", "Core", "UNADDRESSED", 0);
   server.registerConst("Core", "Core", "DIRECT", 1);
   server.registerConst("Core", "Core", "TYPE_TETRAPOD", 1);
   server.registerConst("Core", "Core", "TYPE_SERVICE", 2);
   server.registerConst("Core", "Core", "TYPE_ADMIN", 3);
   server.registerConst("Core", "Core", "TYPE_CLIENT", 4);
   server.registerConst("Core", "Core", "TYPE_ANONYMOUS", 5);
   server.registerConst("Core", "Core", "TYPE_WEBAPI", 6);
   server.registerConst("Core", "Core", "DEFAULT_PUBLIC_PORT", 9900);
   server.registerConst("Core", "Core", "DEFAULT_SERVICE_PORT", 9901);
   server.registerConst("Core", "Core", "DEFAULT_CLUSTER_PORT", 9902);
   server.registerConst("Core", "Core", "DEFAULT_HTTP_PORT", 9904);
   server.registerConst("Core", "Core", "DEFAULT_HTTPS_PORT", 9906);
   server.registerConst("Core", "Core", "DEFAULT_DIRECT_PORT", 9800);
   server.registerConst("Core", "Core", "STATUS_STARTING", 1);
   server.registerConst("Core", "Core", "STATUS_PAUSED", 2);
   server.registerConst("Core", "Core", "STATUS_GONE", 4);
   server.registerConst("Core", "Core", "STATUS_BUSY", 8);
   server.registerConst("Core", "Core", "STATUS_OVERLOADED", 16);
   server.registerConst("Core", "Core", "STATUS_FAILED", 32);
   server.registerConst("Core", "Core", "STATUS_STOPPING", 64);
   server.registerConst("Core", "Core", "STATUS_PASSIVE", 128);
   server.registerConst("Core", "Core", "STATUS_ERRORS", 256);
   server.registerConst("Core", "Core", "STATUS_WARNINGS", 512);
   server.registerConst("Core", "Core", "ENVELOPE_HANDSHAKE", 1);
   server.registerConst("Core", "Core", "ENVELOPE_REQUEST", 2);
   server.registerConst("Core", "Core", "ENVELOPE_RESPONSE", 3);
   server.registerConst("Core", "Core", "ENVELOPE_MESSAGE", 4);
   server.registerConst("Core", "Core", "ENVELOPE_BROADCAST", 5);
   server.registerConst("Core", "Core", "ENVELOPE_PING", 6);
   server.registerConst("Core", "Core", "ENVELOPE_PONG", 7);
   server.registerConst("Core", "MessageHeader", "FLAGS_ALTERNATE", 1);
   server.registerConst("Core", "TypeDescriptor", "T_BOOLEAN", 1);
   server.registerConst("Core", "TypeDescriptor", "T_BYTE", 2);
   server.registerConst("Core", "TypeDescriptor", "T_INT", 3);
   server.registerConst("Core", "TypeDescriptor", "T_LONG", 4);
   server.registerConst("Core", "TypeDescriptor", "T_DOUBLE", 5);
   server.registerConst("Core", "TypeDescriptor", "T_STRING", 6);
   server.registerConst("Core", "TypeDescriptor", "T_STRUCT", 7);
   server.registerConst("Core", "TypeDescriptor", "T_BOOLEAN_LIST", 8);
   server.registerConst("Core", "TypeDescriptor", "T_BYTE_LIST", 9);
   server.registerConst("Core", "TypeDescriptor", "T_INT_LIST", 10);
   server.registerConst("Core", "TypeDescriptor", "T_LONG_LIST", 11);
   server.registerConst("Core", "TypeDescriptor", "T_DOUBLE_LIST", 12);
   server.registerConst("Core", "TypeDescriptor", "T_STRING_LIST", 13);
   server.registerConst("Core", "TypeDescriptor", "T_STRUCT_LIST", 14);
   server.registerConst("Core", "ServiceLogEntry", "LEVEL_ALL", 0);
   server.registerConst("Core", "ServiceLogEntry", "LEVEL_TRACE", 10);
   server.registerConst("Core", "ServiceLogEntry", "LEVEL_DEBUG", 20);
   server.registerConst("Core", "ServiceLogEntry", "LEVEL_INFO", 30);
   server.registerConst("Core", "ServiceLogEntry", "LEVEL_WARN", 40);
   server.registerConst("Core", "ServiceLogEntry", "LEVEL_ERROR", 50);
   server.registerConst("Core", "ServiceLogEntry", "LEVEL_OFF", 100);
   server.registerEnumConst("Core", "RequestStatsSort", "COUNT", 1);
   server.registerEnumConst("Core", "RequestStatsSort", "TOTAL_TIME", 2);
   server.registerEnumConst("Core", "RequestStatsSort", "AVERAGE_TIME", 3);
   
   server.registerErrorConst("Core", "null", "CONNECTION_CLOSED", 7);
   server.registerErrorConst("Core", "null", "FLOOD", 12);
   server.registerErrorConst("Core", "null", "INVALID_DATA", 15);
   server.registerErrorConst("Core", "null", "INVALID_ENTITY", 9);
   server.registerErrorConst("Core", "null", "INVALID_RIGHTS", 8);
   server.registerErrorConst("Core", "null", "INVALID_TOKEN", 13);
   server.registerErrorConst("Core", "null", "NOT_CONFIGURED", 2718243);
   server.registerErrorConst("Core", "null", "PROTOCOL_MISMATCH", 5);
   server.registerErrorConst("Core", "null", "RIGHTS_EXPIRED", 10);
   server.registerErrorConst("Core", "null", "SERIALIZATION", 4);
   server.registerErrorConst("Core", "null", "SERVICE_OVERLOADED", 11);
   server.registerErrorConst("Core", "null", "SERVICE_UNAVAILABLE", 2);
   server.registerErrorConst("Core", "null", "TIMEOUT", 3);
   server.registerErrorConst("Core", "null", "UNKNOWN", 1);
   server.registerErrorConst("Core", "null", "UNKNOWN_REQUEST", 6);
   server.registerErrorConst("Core", "null", "UNSUPPORTED", 14);
   
   server.register("struct", "Core", "Core", 1, 9088168);
   server.register("struct", "Core", "RequestHeader", 1, 7165109);
   server.register("struct", "Core", "ResponseHeader", 1, 675609);
   server.register("struct", "Core", "MessageHeader", 1, 11760427);
   server.register("struct", "Core", "ServiceCommand", 1, 5461687);
   server.register("struct", "Core", "ServerAddress", 1, 14893956);
   server.register("request", "Core", "Pause", 1, 14690004);
   server.register("request", "Core", "Unpause", 1, 10620319);
   server.register("request", "Core", "Rebalance", 1, 1734199);
   server.register("request", "Core", "ReleaseExcess", 1, 12519092);
   server.register("request", "Core", "Purge", 1, 2667367);
   server.register("request", "Core", "Shutdown", 1, 8989182);
   server.register("request", "Core", "Restart", 1, 4802943);
   server.register("request", "Core", "ServiceStatsSubscribe", 1, 13519504);
   server.register("request", "Core", "ServiceStatsUnsubscribe", 1, 576067);
   server.register("request", "Core", "ServiceDetails", 1, 14458441);
   server.register("response", "Core", "ServiceDetails", 1, 12435407);
   server.register("request", "Core", "ServiceLogs", 1, 13816458);
   server.register("response", "Core", "ServiceLogs", 1, 6345878);
   server.register("struct", "Core", "StatPair", 1, 10082177);
   server.register("struct", "Core", "RequestStat", 1, 12902770);
   server.register("request", "Core", "ServiceRequestStats", 1, 16134423);
   server.register("response", "Core", "ServiceRequestStats", 1, 6312573);
   server.register("response", "Core", "ServiceRequestDetailedStats", 1, 9733533);
   server.register("request", "Core", "HostInfo", 1, 16426670);
   server.register("response", "Core", "HostInfo", 1, 7161106);
   server.register("request", "Core", "HostStats", 1, 4481593);
   server.register("response", "Core", "HostStats", 1, 15046655);
   server.register("request", "Core", "ServiceErrorLogs", 1, 16327568);
   server.register("response", "Core", "ServiceErrorLogs", 1, 9302372);
   server.register("request", "Core", "ResetServiceErrorLogs", 1, 9359779);
   server.register("request", "Core", "SetCommsLogLevel", 1, 10256079);
   server.register("request", "Core", "WebAPI", 1, 9321342);
   server.register("response", "Core", "WebAPI", 1, 9652194);
   server.register("request", "Core", "DirectConnection", 1, 1361471);
   server.register("response", "Core", "DirectConnection", 1, 16162197);
   server.register("request", "Core", "ValidateConnection", 1, 6315662);
   server.register("response", "Core", "ValidateConnection", 1, 1291890);
   server.register("request", "Core", "Dummy", 1, 6747086);
   server.register("message", "Core", "ServiceStats", 1, 469976);
   server.register("struct", "Core", "Subscriber", 1, 16013581);
   server.register("struct", "Core", "WebRoute", 1, 4890284);
   server.register("struct", "Core", "TypeDescriptor", 1, 6493266);
   server.register("struct", "Core", "ContractDescription", 1, 7323457);
   server.register("struct", "Core", "StructDescription", 1, 9642196);
   server.register("struct", "Core", "ServiceLogEntry", 1, 11222968);

}
