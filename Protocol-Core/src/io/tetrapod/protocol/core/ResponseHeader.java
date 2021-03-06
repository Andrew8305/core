package io.tetrapod.protocol.core;

// This is a code generated file.  All edits will be lost the next time code gen is run.

import io.*;
import io.tetrapod.core.rpc.*;
import io.tetrapod.core.serialize.*;
import io.tetrapod.protocol.core.TypeDescriptor;
import io.tetrapod.protocol.core.StructDescription;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("all")
public class ResponseHeader extends Structure {
   
   public static final int STRUCT_ID = 675609;
   public static final int CONTRACT_ID = CoreContract.CONTRACT_ID;
   public static final int SUB_CONTRACT_ID = CoreContract.SUB_CONTRACT_ID;

   public ResponseHeader() {
      defaults();
   }

   public ResponseHeader(int requestId, int contractId, int structId, long contextId) {
      this.requestId = requestId;
      this.contractId = contractId;
      this.structId = structId;
      this.contextId = contextId;
   }   
   
   public int requestId;
   public int contractId;
   public int structId;
   public long contextId;

   public final Structure.Security getSecurity() {
      return Security.PUBLIC;
   }

   public final void defaults() {
      requestId = 0;
      contractId = 0;
      structId = 0;
      contextId = 0;
   }
   
   @Override
   public final void write(DataSource data) throws IOException {
      data.write(1, this.requestId);
      data.write(2, this.contractId);
      data.write(3, this.structId);
      data.write(4, this.contextId);
      data.writeEndTag();
   }
   
   @Override
   public final void read(DataSource data) throws IOException {
      defaults();
      while (true) {
         int tag = data.readTag();
         switch (tag) {
            case 1: this.requestId = data.read_int(tag); break;
            case 2: this.contractId = data.read_int(tag); break;
            case 3: this.structId = data.read_int(tag); break;
            case 4: this.contextId = data.read_long(tag); break;
            case Codec.END_TAG:
               return;
            default:
               data.skip(tag);
               break;
         }
      }
   }

   public final int getContractId() {
      return ResponseHeader.CONTRACT_ID;
   }

   public final int getSubContractId() {
      return ResponseHeader.SUB_CONTRACT_ID;
   }

   public final int getStructId() {
      return ResponseHeader.STRUCT_ID;
   }

   public final String[] tagWebNames() {
      // Note do not use this tags in long term serializations (to disk or databases) as
      // implementors are free to rename them however they wish.  A null means the field
      // is not to participate in web serialization (remaining at default)
      String[] result = new String[4+1];
      result[1] = "requestId";
      result[2] = "contractId";
      result[3] = "structId";
      result[4] = "contextId";
      return result;
   }

   public final Structure make() {
      return new ResponseHeader();
   }

   public final StructDescription makeDescription() {
      StructDescription desc = new StructDescription();
      desc.name = "ResponseHeader";
      desc.tagWebNames = tagWebNames();
      desc.types = new TypeDescriptor[desc.tagWebNames.length];
      desc.types[0] = new TypeDescriptor(TypeDescriptor.T_STRUCT, getContractId(), getStructId());
      desc.types[1] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      desc.types[2] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      desc.types[3] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      desc.types[4] = new TypeDescriptor(TypeDescriptor.T_LONG, 0, 0);
      return desc;
   }

   @Override
   @SuppressWarnings("RedundantIfStatement")
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      ResponseHeader that = (ResponseHeader) o;

      if (requestId != that.requestId)
         return false;
      if (contractId != that.contractId)
         return false;
      if (structId != that.structId)
         return false;
      if (contextId != that.contextId)
         return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = 0;
      result = 31 * result + requestId;
      result = 31 * result + contractId;
      result = 31 * result + structId;
      result = 31 * result + (int) (contextId ^ (contextId >>> 32));
      return result;
   }

}
