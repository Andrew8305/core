define(function() { return TP_Core });

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
   server.registerConst("Core", "Core", "DEFAULT_PUBLIC_PORT", 9900);
   server.registerConst("Core", "Core", "DEFAULT_SERVICE_PORT", 9901);
   server.registerConst("Core", "Core", "DEFAULT_CLUSTER_PORT", 9902);
   server.registerConst("Core", "Core", "DEFAULT_HTTP_PORT", 9904);
   server.registerConst("Core", "Core", "DEFAULT_HTTPS_PORT", 9906);
   server.registerConst("Core", "Core", "STATUS_STARTING", 1);
   server.registerConst("Core", "Core", "STATUS_PAUSED", 2);
   server.registerConst("Core", "Core", "STATUS_GONE", 4);
   server.registerConst("Core", "Core", "STATUS_BUSY", 8);
   server.registerConst("Core", "Core", "STATUS_OVERLOADED", 16);
   server.registerConst("Core", "Core", "STATUS_FAILED", 32);
   server.registerConst("Core", "Core", "STATUS_STOPPING", 64);
   server.registerConst("Core", "Core", "ENVELOPE_HANDSHAKE", 1);
   server.registerConst("Core", "Core", "ENVELOPE_REQUEST", 2);
   server.registerConst("Core", "Core", "ENVELOPE_RESPONSE", 3);
   server.registerConst("Core", "Core", "ENVELOPE_MESSAGE", 4);
   server.registerConst("Core", "Core", "ENVELOPE_BROADCAST", 5);
   server.registerConst("Core", "Core", "ENVELOPE_PING", 6);
   server.registerConst("Core", "Core", "ENVELOPE_PONG", 7);
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
   
   server.registerConst("Core", "null", "CONNECTION_CLOSED", 7);
   server.registerConst("Core", "null", "INVALID_ENTITY", 9);
   server.registerConst("Core", "null", "INVALID_RIGHTS", 8);
   server.registerConst("Core", "null", "PROTOCOL_MISMATCH", 5);
   server.registerConst("Core", "null", "RIGHTS_EXPIRED", 10);
   server.registerConst("Core", "null", "SERIALIZATION", 4);
   server.registerConst("Core", "null", "SERVICE_OVERLOADED", 11);
   server.registerConst("Core", "null", "SERVICE_UNAVAILABLE", 2);
   server.registerConst("Core", "null", "TIMEOUT", 3);
   server.registerConst("Core", "null", "UNKNOWN", 1);
   server.registerConst("Core", "null", "UNKNOWN_REQUEST", 6);
   
   server.register("struct", "Core", "Core", 1, 9088168);
   server.register("struct", "Core", "RequestHeader", 1, 7165109);
   server.register("struct", "Core", "ResponseHeader", 1, 675609);
   server.register("struct", "Core", "MessageHeader", 1, 11760427);
   server.register("struct", "Core", "ServiceCommand", 1, 5461687);
   server.register("struct", "Core", "ServerAddress", 1, 14893956);
   server.register("request", "Core", "Pause", 1, 14690004);
   server.register("request", "Core", "Unpause", 1, 10620319);
   server.register("request", "Core", "Shutdown", 1, 8989182);
   server.register("request", "Core", "Restart", 1, 4802943);
   server.register("request", "Core", "ServiceStatsSubscribe", 1, 13519504);
   server.register("request", "Core", "ServiceStatsUnsubscribe", 1, 576067);
   server.register("request", "Core", "ServiceDetails", 1, 14458441);
   server.register("response", "Core", "ServiceDetails", 1, 12435407);
   server.register("struct", "Core", "Subscriber", 1, 16013581);
   server.register("struct", "Core", "WebRoute", 1, 4890284);
   server.register("struct", "Core", "TypeDescriptor", 1, 6493266);
   server.register("struct", "Core", "StructDescription", 1, 9642196);

}
