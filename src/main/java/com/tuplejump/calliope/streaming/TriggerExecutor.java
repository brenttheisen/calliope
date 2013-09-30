package com.tuplejump.calliope.streaming;


import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.db.IMutation;
import org.apache.cassandra.db.RowMutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * User: suresh
 * Date: 22/8/13
 * Time: 1:41 PM
 */
public class TriggerExecutor {
    public static final TriggerExecutor instance = new TriggerExecutor();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private static Logger logger = LoggerFactory.getLogger(TriggerExecutor.class);

    private TriggerExecutor() {
        TriggerStore.instance.loadTriggers();
    }

    public void execute(List<IMutation> mutations) {
        executorService.submit(new TriggerTask(mutations));
    }

    private class TriggerTask implements Runnable {

        private List<IMutation> mutations;

        public TriggerTask(List<IMutation> mutations) {
            this.mutations = mutations;
        }

        public void run() {

            for (IMutation mutation : mutations) {
                if (mutation instanceof RowMutation) {
                    logger.info("Processing row mutation from trigger execution " + mutation.toString(false));
                    for (ColumnFamily cf : ((RowMutation) mutation).getColumnFamilies()) {
                        execute(mutation.getTable(), cf);
                    }
                }
            }

        }
    }

    private void execute(String keyspace, ColumnFamily cf) {
        try {
            logger.info("cf to string.......  " + cf.toString());
            List<ITrigger> triggers = TriggerStore.instance.getTriggersForCF(keyspace, cf.metadata().cfName);
            if (triggers != null) {
                for (ITrigger trigger : triggers) {
                    logger.info("calling Trigger "+trigger.getClass().getName()+" for keyspace "+ keyspace+ " for column family "+cf.metadata().cfName);
                    trigger.process(cf, keyspace);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
