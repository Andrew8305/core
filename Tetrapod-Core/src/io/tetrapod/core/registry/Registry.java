package io.tetrapod.core.registry;

import io.tetrapod.core.rpc.Message;
import io.tetrapod.protocol.core.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.*;

/**
 * The global registry of all current actors in the system and their published topics and subscriptions
 * 
 * Each tetrapod service owns a shard of the registry and has a full replica of all other shards.
 */
public class Registry implements TetrapodContract.RegistryTopic.API {

   protected static final Logger                logger          = LoggerFactory.getLogger(Registry.class);

   protected static final int                   PARENT_ID_SHIFT = 20;
   protected static final int                   PARENT_ID_MASK  = 0x7FF << PARENT_ID_SHIFT;
   protected static final int                   MAX_ID          = 0x000FFFFF;

   /**
    * Our entityId
    */
   private int                                  parentId;

   /**
    * Our local entity id counter
    */
   private int                                  counter;

   /**
    * Maps entityId => EntityInfo
    */
   private final Map<Integer, EntityInfo>       entities        = new ConcurrentHashMap<>();

   /**
    * Maps contractId => List of EntityInfos that provide that service
    */
   @SuppressWarnings("unused")
   private final Map<Integer, List<EntityInfo>> services        = new ConcurrentHashMap<>();

   public static interface RegistryBroadcaster {
      public void broadcastRegistryMessage(Message msg);
   }

   private final RegistryBroadcaster broadcaster;

   public Registry(RegistryBroadcaster broadcaster) {
      this.broadcaster = broadcaster;
   }

   public synchronized int setParentId(int id) {
      assert id < 0x07FF;
      this.parentId = id << PARENT_ID_SHIFT;
      return this.parentId;
   }

   public synchronized void register(EntityInfo entity) {
      if (entity.entityId <= 0) {
         entity.entityId = issueId();
      }
      entities.put(entity.entityId, entity);
      if (entity.isService()) {
         // register their service in our services list
      }
      if (entity.parentId == parentId) {
         broadcaster.broadcastRegistryMessage(new EntityRegisteredMessage(entity, null));
      }

   }

   public synchronized void unregister(int entityId) {
      final EntityInfo e = getEntity(entityId);
      if (e != null) {
         // Unpublish all their topics
         for (Topic topic : e.getTopics()) {
            unpublish(e, topic.topicId);
         }
         // Unsubscribe from all subscriptions
         for (Topic topic : e.getSubscriptions()) {
            unsubscribe(e, topic);
         }

         entities.remove(e.entityId);

         if (e.parentId == parentId) {
            broadcaster.broadcastRegistryMessage(new EntityUnregisteredMessage(entityId));
         }
      } else {
         logger.error("Could not find entity {} to unregister", entityId);
      }
   }

   /**
    * @return a new unused ID. If we hit our local maximum, we will reset and find the next currently unused id
    */
   private synchronized int issueId() {
      while (true) {
         int id = parentId | (++counter % MAX_ID);
         if (!entities.containsKey(id)) {
            return id;
         }
      }
   }

   public EntityInfo getEntity(int entityId) {
      return entities.get(entityId);
   }

   public Topic publish(int entityId) {
      final EntityInfo entity = getEntity(entityId);
      if (entity != null) {
         return entity.publish();
      } else {
         logger.error("Could not find entity {}", entity);
      }
      return null;
   }

   public boolean unpublish(int entityId, int topicId) {
      final EntityInfo entity = getEntity(entityId);
      if (entity != null) {
         return unpublish(entity, topicId);
      } else {
         logger.error("Could not find entity {}", entity);
      }
      return false;
   }

   public boolean unpublish(EntityInfo entity, int topicId) {
      final Topic topic = entity.unpublish(topicId);
      if (topic != null) {
         // clean up all the subscriptions to this topic
         for (Subscriber sub : topic.getSubscribers()) {
            final EntityInfo e = getEntity(sub.entityId);
            if (e != null) {
               unsubscribe(e, topic);
            }
         }
         return true;
      }
      return false;
   }

   public void subscribe(EntityInfo entity, Topic topic) {
      if (entity.parentId == parentId) {
         // it's our child, so directly subscribe them
         topic.subscribe(entity.entityId);
      } else {
         // just subscribe their parent as proxy
         topic.subscribe(entity.parentId);
      }
   }

   public boolean unsubscribe(EntityInfo entity, Topic topic) {
      if (entity.parentId == parentId) {
         // unsubscribe them directly
         return topic.unsubscribe(entity.entityId, true);
      } else {
         // unsubscribe the parent subscription which will decrement their counter
         return topic.unsubscribe(entity.parentId, false);
         // FIXME: there's a minor bug here if they subscribed more than once
      }
   }

   //////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void genericMessage(Message message) {}

   @Override
   public void messageEntityRegistered(EntityRegisteredMessage m) {
      final EntityInfo e = new EntityInfo(m.entity);
      register(e);
   }

   @Override
   public void messageEntityUnregistered(EntityUnregisteredMessage m) {
      unregister(m.entityId);
   }

   @Override
   public void messageTopicPublished(TopicPublishedMessage m) {

   }

   @Override
   public void messageTopicSubscribed(TopicSubscribedMessage m) {

   }

   @Override
   public void messageTopicUnpublished(TopicUnpublishedMessage m) {

   }

   @Override
   public void messageTopicUnsubscribed(TopicUnsubscribedMessage m) {

   }

   //////////////////////////////////////////////////////////////////////////////////////////

   public void logStats() {
      logger.info("===================== TETRAPOD CLUSTER REGISTRY =======================");
      for (EntityInfo e : entities.values()) {
         logger.info(String.format("0x%08X %-15s status=%08X topics=%d subscriptions=%d", e.entityId, e.name, e.status, e.getNumTopics(),
               e.getNumSubscriptions()));
      }
      logger.info("=======================================================================\n");

   }

}
