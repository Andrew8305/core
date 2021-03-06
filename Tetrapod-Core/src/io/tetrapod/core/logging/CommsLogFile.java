package io.tetrapod.core.logging;

import java.io.*;
import java.util.*;

import org.slf4j.*;

import io.tetrapod.core.StructureFactory;
import io.tetrapod.core.serialize.StructureAdapter;
import io.tetrapod.core.serialize.datasources.IOStreamDataSource;
import io.tetrapod.protocol.core.*;

public class CommsLogFile {

   private static final Logger      logger = LoggerFactory.getLogger(CommsLogFile.class);

   public CommsLogFileHeader        header = new CommsLogFileHeader();
   public final List<CommsLogEntry> list   = new ArrayList<>();

   public CommsLogFile(DataInputStream in) throws IOException {
      @SuppressWarnings("unused")
      int ver = in.readInt();
      IOStreamDataSource data = IOStreamDataSource.forReading(in);
      header.read(data);
      for (StructDescription def : header.structs) {
         StructureFactory.addIfNew(new StructureAdapter(def));
      }
      boolean hasGap = false;
      while (true) {
         try {
            in.mark(1024);
            CommsLogEntry e = CommsLogEntry.read(data);
            if (e != null) {
               //logger.debug("READING {}", e);
               list.add(e);
               hasGap = false;
            }
         } catch (EOFException e) {
            break;
         } catch (IOException e) {
            // possibly a corrupt section of the file, we'll skip a byte and try again until we find something readable...
            if (!hasGap) {
               logger.error("IOException reading file ... {}", e.getMessage());
               //logger.error(e.getMessage(), e);
               hasGap = true; // log once per gap
            }
            in.reset();
            in.skipBytes(1);
         }
      }
   }

}
