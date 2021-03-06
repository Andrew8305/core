package io.tetrapod.core;

import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.tetrapod.core.tasks.Task;
import io.tetrapod.core.utils.Properties;
import io.tetrapod.core.utils.Util;
import io.tetrapod.protocol.core.ServerAddress;

/**
 * Simple service launcher. Some day it might be nice to replace with a ClusterService that was able to launch things.
 * <p>
 * Don't refer to logging in this class otherwise it gets initialized (upon class load) prior to being setup.
 * <p>
 * Arguments:
 * <ul>
 * <li>-host hostname (host to connect to, overrides cluster.properties)
 * <li>-port portNum (port to connect to, overrides cluster.properties)
 * <li>-token token (reclaim token)
 * <li>-webOnly webRootName (service will connect, set web root, and disconnect)
 * <li>-paused true|false (will start paused, overrides .properties)
 * </ul>
 */
public class Launcher {
   private Map<String, String> opts         = null;
   private final String              serviceClass;
   private Service service;

   public Launcher(String serviceClass) {
      this.serviceClass = serviceClass;
   }

   public Task<Void> init(String [] args) throws Exception {
      String appName = serviceClass.substring(serviceClass.lastIndexOf('.') + 1);
      System.setProperty("APPNAME", appName);
      Util.setProperty("APPNAME", appName);
      System.setProperty("LOGDIR", Util.getProperty("tetrapod.logs","logs"));
      opts = getOpts(args, 1, defaultOpts(appName));

      String host = Util.getProperty("service.host", "localhost");
      int port = Integer.parseInt(Util.getProperty("service.port", "9901"));
      if (opts.get("host") != null) {
         host = opts.get("host");
      }
      if (opts.get("port") != null) {
         port = Integer.parseInt(opts.get("port"));
      }
      ServerAddress addr = new ServerAddress(host, port);
      service = (Service) getClass(serviceClass).newInstance();
      service.startNetwork(addr, opts.get("token"), opts, this);
      return service.readyToServe();

   }

   public Service getService() {
      return service;
   }

   public static void main(String[] args) {
      asyncInit();
      loadProperties("cfg/service.properties");
      loadClusterProperties();
      loadSecretProperties();
      // set for logback
      System.setProperty("devMode", Util.getProperty("devMode"));
      
      try {
         if (args.length < 1)
            usage();
         deleteLogs();
         String [] serviceClasses = args[0].split(",");
         for (String serviceClass : serviceClasses) {
            Launcher launcher = new Launcher(serviceClass.trim());
            launcher.init(args);
         }
      } catch (Throwable t) {
         t.printStackTrace();
         usage();
      }
   }

   public static void asyncInit() {
      try {
         Class<?> asyncClass = Class.forName("com.ea.async.Async");
         Method initMethod = asyncClass.getMethod("init");
         initMethod.invoke(null);
      } catch (Throwable e) {
         e.printStackTrace();
      }
   }

   private static Class<?> getClass(String serviceClass) {
      // actual class
      try {
         return tryName(serviceClass);
      } catch (ClassNotFoundException e) {}

      // capitalize first letter
      serviceClass = serviceClass.substring(0, 1).toUpperCase() + serviceClass.substring(1);

      // io.tetrapod.core.X
      try {
         return tryName("io.tetrapod.core." + serviceClass);
      } catch (ClassNotFoundException e) {}

      int ix = serviceClass.indexOf("Service");
      if (ix > 0) {
         // pop off Service if it's there
         serviceClass = serviceClass.substring(0, ix);
      }

      // io.tetrapod.core.XService
      try {
         return tryName("io.tetrapod.core." + serviceClass + "Service");
      } catch (ClassNotFoundException | NoClassDefFoundError e) {}

      // io.tetrapod.lowercase(X).X
      try {
         return tryName("io.tetrapod." + serviceClass.toLowerCase() + "." + serviceClass);
      } catch (ClassNotFoundException | NoClassDefFoundError e) {}

      // io.tetrapod.lowercase(X).XService
      try {
         return tryName("io.tetrapod." + serviceClass.toLowerCase() + "." + serviceClass + "Service");
      } catch (ClassNotFoundException | NoClassDefFoundError e) {}

      // io.tetrapod.lowercase(X).uppercase(X)Service
      try {
         return tryName("io.tetrapod." + serviceClass.toLowerCase() + "." + serviceClass.toUpperCase() + "Service");
      } catch (ClassNotFoundException | NoClassDefFoundError e) {}

      return null;
   }

   private static Class<?> tryName(String name) throws ClassNotFoundException {
      //      System.out.println("trying " + name);
      return Class.forName(name);
   }

   private static void usage() {
      System.err.println("\nusage:\n\t java <vmopts> " + Launcher.class.getCanonicalName()
            + " serviceClass [-host hostname] [-port port] [-token authToken]\n");
      System.err
            .println("\nserviceClass can omit its prefix if it's io.tetrapod.{core|serviceClass.upTo(\"Service\").toLower}.serviceClass[Service]\n");
      System.exit(0);
   }

   private static Map<String, String> defaultOpts(String appName) {
      Map<String, String> map = new HashMap<>();
      map.put("host", null);
      map.put("port", null);
      map.put("token", null);
      map.put("paused", Util.getProperty(appName + ".start_paused", "false"));
      return map;
   }

   private static Map<String, String> getOpts(String[] array, int startIx, Map<String, String> opts) {
      for (int i = startIx; i < array.length; i += 2) {
         String key = array[i];
         String value = array.length > i + 1 ? array[i + 1] : null;
         if (!key.startsWith("-")) {
            throw new RuntimeException("expected option, got [" + key + "]");
         }
         opts.put(key.substring(1), value);
      }
      return opts;
   }

   public void relaunch(String token) throws IOException {
      opts.put("token", token);
      StringBuilder sb = new StringBuilder();
      sb.append("../current/scripts/launch");

      // java args?
      //      for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
      //         sb.append(' ');
      //         sb.append(arg);
      //      }

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

   public static boolean loadProperties(String fileName) {
      return loadProperties(fileName, Util.properties);
   }

   public static boolean loadProperties(String fileName, Properties properties) {
      return loadProperties(new File(fileName), properties);
   }

   public static boolean loadProperties(File file, Properties properties) {
      if (file.exists()) {
         try (Reader reader = new FileReader(file)) {
            properties.load(reader);
            return true;
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return false;
   }

   public static void loadClusterProperties() {
      loadClusterProperties(Util.properties);
   }

   public static void loadClusterProperties(Properties properties) {
      String name = System.getProperty("user.name");
      String[] locs = {
            "cluster.properties", // in prod, gets symlinked in
            "../core/Tetrapod-Core/rsc/cluster/%s.cluster.properties", "../../core/Tetrapod-Core/rsc/cluster/%s.cluster.properties",
            "../../../core/Tetrapod-Core/rsc/cluster/%s.cluster.properties", // in case services are one level deeper
      };
      for (String f : locs) {
         if (loadProperties(String.format(f, name), properties))
            return;
      }
      System.err.println("ERR: no cluster properties found");
      System.exit(0);
   }

   public static void loadSecretProperties() {
      loadSecretProperties(Util.properties);
   }

   public static void loadSecretProperties(Properties properties) {
      String file = properties.optString("secrets", null);
      if (file != null && !file.isEmpty())
         loadProperties(file, properties);
   }

   public String getOpt(String key) {
      return opts.get(key);
   }

   public String getAllOpts() {
      StringBuilder sb = new StringBuilder();
      for (Entry<String, String> e : opts.entrySet()) {
         sb.append(e.getKey());
         sb.append("=");
         sb.append(e.getValue());
         sb.append(" ");
      }
      return sb.toString();
   }

   private static void deleteLogs() {
      if (Util.getProperty("delete.logs", "false").equals("true")) {
         File logs = new File(System.getProperty("delete.logs.location", "logs"));
         if (logs.isDirectory()) {
            for (File f : logs.listFiles()) {
               f.delete();
            }
         } else {
            logs.delete();
         }
      }
   }

}
