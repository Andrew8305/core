package io.tetrapod.core.tasks;

import io.tetrapod.core.ServiceException;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * This is a lock object that is safe to acquire and release from separate threads, thus is appropriate for async operations.
 * To lock this, you should call one of the lock function if this is single threaded, or lockAsync if you want an
 * entire task chain synchronized.  Alternatively you can use acquire() but this is only recommended in a try-with-resources block
 * @author paulm
 *         Created: 9/8/16
 */
public class AsyncLock implements AutoCloseable {
   private final Semaphore semaphore;

   public AsyncLock() {
      this.semaphore = new Semaphore(1, false);
   }

   /**
    * Use this to wrap a function that returns a value in a sync block.  Don't return Tasks through this method.  Use
    * lockAsync instead.
    * @param function   A function you wish to be synchronized
    * @param <T> The type of object that is being returned
    * @return The return value from the fuction
    */
   public <T> T lock(Func0<T> function) {
      acquire();
      try {
         T ret = function.apply();
         if (ret instanceof Task) {
            throw new IllegalArgumentException("You must call lockAsync if you want to return a task and expect the " +
                    "locking semantics to extend across the task chain");
         }
         return ret;
      } finally {
         release();
      }
   }

   /**
    * Use this to wrap a runnable in a sync block
    * @param runnable  The runnable to execute within the sync
    */
   public void lock(Runnable runnable) {
      acquire();
      try {
         runnable.run();
      } finally {
         release();
      }
   }


   /**
    * Use this function to sync an entire task chain, across async threads
    * @param function  The function that returns a Task
    * @param <T>  The type of value wrapped by the task.
    * @return  The task that was returned from the function
    */
   public <T> Task<T> lockAsync(Func0<Task<T>> function) {
      acquire();

      try {
         return function.apply().thenApply((resp) -> {
            release();
            return resp;
         }).exceptionally(th -> {
            release();
            throw ServiceException.wrapIfChecked(th);
         });
      } catch (Throwable e) {
         release();
         throw ServiceException.wrapIfChecked(e);
      }
   }
   private void release() {
      //maybe the extra sync is a little heavy handed, but want it in for now to make sure the assert works as expected
      synchronized (semaphore) {
         assert semaphore.availablePermits() == 0;
         semaphore.release();
      }
   }

   /**
    * @deprecated Don't ever call me!  I should only be called implicity using a try with resources block.  See comments in acquire
    * @throws RuntimeException
    */
   @Override
   public void close() throws RuntimeException {
      release();
   }

   /**
    * This allows you to acquire a lock.  This method is only exposed to allow it to work with try with resource block.
    * Do something like this:
    *
    * //async lock defined earlier
    * AsyncLock myLock = new AsyncLock();
    * ...
    *
    * try (AsyncLock l = myLock.acquire()) {
    *    //things in here are synchronized
    * }
    *
    *
    * @return  This object.  Return value required for try with resources to work but can be ignored
    */
   public AsyncLock acquire() {
      try {
         synchronized (semaphore) {
            semaphore.acquire();
            return this;
         }
      } catch (InterruptedException e) {
         throw ServiceException.wrap(e);
      }
   }

}

