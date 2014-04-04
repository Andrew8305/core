package io.tetrapod.core;

import io.tetrapod.protocol.core.ServerAddress;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.Map.Entry;

/**
 * Simple service launcher. Some day it might be nice to replace with a ClusterService that was able to launch things.
 */
public class Launcher {
   private static Map<String, String> opts = null;

   public static void main(String[] args) {
      try {
         if (args.length < 1)
            usage();
         String serviceClass = args[0];
         opts = getOpts(args, 1, defaultOpts());
         System.setProperty("APPNAME", serviceClass.substring(serviceClass.lastIndexOf('.') + 1));

         ServerAddress addr = null;
         if (opts.get("host") != null) {
            int port = TetrapodService.DEFAULT_SERVICE_PORT;
            if (opts.get("port") != null) {
               port = Integer.parseInt(opts.get("port"));
            }
            addr = new ServerAddress(opts.get("host"), port);
         }

         Service service = (Service) Class.forName(serviceClass).newInstance();
         service.startNetwork(addr, opts.get("token"));
      } catch (Throwable t) {
         t.printStackTrace();
         usage();
      }
   }

   private static void usage() {
      System.err.println("\nusage:\n\t java <vmopts> " + Launcher.class.getCanonicalName()
            + " serviceClassName [-host hostname] [-port port] [-token authToken]\n");
      System.exit(0);
   }

   private static Map<String, String> defaultOpts() {
      Map<String, String> map = new HashMap<>();
      map.put("host", null);
      map.put("port", null);
      map.put("token", null);
      return map;
   }

   private static Map<String, String> getOpts(String[] array, int startIx, Map<String, String> opts) {
      for (int i = startIx; i < array.length; i += 2) {
         String key = array[i];
         String value = array[i + 1];
         if (!key.startsWith("-")) {
            throw new RuntimeException("expected option, got [" + key + "]");
         }
         opts.put(key.substring(1), value);
      }
      return opts;
   }

   public static void relaunch(String token) throws IOException {
      opts.put("token", token);
      StringBuilder sb = new StringBuilder();
      sb.append("java ");

      // java args
      for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
         sb.append(' ');
         sb.append(arg);
      }

      for (Entry<String, String> entry : opts.entrySet()) {
         if (entry.getValue() != null) {
            sb.append(' ');
            sb.append('-');
            sb.append(entry.getKey());
            sb.append(' ');
            sb.append(entry.getValue());
         }
      }
      System.out.println("EXEC: " + sb);
      Runtime.getRuntime().exec(sb.toString());
   }

}
