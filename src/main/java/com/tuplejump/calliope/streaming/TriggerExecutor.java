/*
 * Licensed to Tuplejump Software Pvt. Ltd. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Tuplejump Software Pvt. Ltd. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * Trigger executor
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
                    logger.debug("Processing row mutation from trigger execution " + mutation.toString(false));
                    for (ColumnFamily cf : ((RowMutation) mutation).getColumnFamilies()) {
                        logger.debug("processing mutation for cf " + cf.toString());
                        CasMutation casM = prepareCasMutation(mutation.getTable(), cf);
                        execute(casM);
                    }
                }
            }

        }
    }

    private CasMutation prepareCasMutation(String keyspace, ColumnFamily cf) {
        return new CasMutation(keyspace, cf);
    }

    private void execute(CasMutation casM) {

        //If it is an update to trigger column family, reload triggers
        if(casM.getKeySpace().equals(TriggerStore.instance.getTriggerKeySpace())
                && casM.getCfName().equals(TriggerStore.instance.getTriggerColumnFamily())){
            TriggerStore.instance.loadTriggers();
        }

        try {
            List<ITrigger> triggers = TriggerStore.instance.getTriggersForCF(casM.getKeySpace(), casM.getCfName());
            if (triggers != null) {
                for (ITrigger trigger : triggers) {
                    logger.info("calling Trigger " + trigger.getClass().getName() + " for " + casM.getKeySpace() + ":" + casM.getCfName());
                    trigger.process(casM);
                }
            } else {
                logger.info("No triggers are called for " + casM.getKeySpace() + ":" + casM.getCfName());
            }
        } catch (Exception e) {
            logger.error("Error while executing the trigger for " + casM.getKeySpace() + ":" + casM.getCfName(), e);
        }
    }
}
