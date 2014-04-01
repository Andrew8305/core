package io.tetrapod.core.rpc;

import java.io.IOException;

import io.tetrapod.core.serialize.*;
import io.tetrapod.protocol.core.TetrapodContract;

public class Pending extends Response {
   public static final int STRUCT_ID = 3;

   @Override
   public void write(DataSource data) throws IOException {}

   @Override
   public void read(DataSource data) throws IOException {}

   @Override
   public int getStructId() {
      return Pending.STRUCT_ID;
   }

   public int getContractId() {
      return TetrapodContract.CONTRACT_ID;
   }

}