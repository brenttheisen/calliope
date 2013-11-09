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


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.tuplejump.calliope.CalliopeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Trigger store
 */
public class TriggerStore implements Closeable {

    public static final TriggerStore instance = new TriggerStore();
    private static Logger logger = LoggerFactory.getLogger(TriggerStore.class);
    private Cluster cluster;
    private Session session;
    private Map<String, Set<ITrigger>> triggersMap = new HashMap<String, Set<ITrigger>>();
    private volatile boolean clusterinit;

    public Set<ITrigger> getTriggersForCF(String ks, String cf) {
        return triggersMap.get(ks + ":" + cf);
    }

    public synchronized void loadTriggers() {

        //initiate CQ3 client connection and reads triggers key space.
        if (!clusterinit) {
            connect(CalliopeProperties.instance.getClusterNode(), CalliopeProperties.instance.getClusterPort());
            clusterinit = true;
        }

        ResultSet results = session.execute("SELECT * FROM " + CalliopeProperties.instance.getTriggerStoreCF());

        for (Row row : results) {
            String ks = row.getString("ks");
            String cf = row.getString("cf");
            String key = ks + ":" + cf;
            String _clazz = row.getString("class");

            try {
                @SuppressWarnings("unchecked")
                Class<ITrigger> clazz = (Class<ITrigger>) Class.forName(_clazz);
                ITrigger trigger = (ITrigger) clazz.getField("instance").get(clazz);
                logger.info("Adding trigger: [key = " + key + "],[class = " + trigger.getClass().getCanonicalName() + "]");
                Set<ITrigger> set;
                if (!triggersMap.containsKey(key)) {
                    set = new HashSet<ITrigger>();
                    triggersMap.put(key, set);
                }
                set = triggersMap.get(key);
                set.add(trigger);

            } catch (Exception e) {
                logger.error("Failed to load class " + _clazz, e);
                throw new RuntimeException(e);
            }
        }
    }

    private void connect(String node, int port) {
        try {
            logger.info("initiating client connection to cluster");
            cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
            session = cluster.connect(CalliopeProperties.instance.getTriggerStoreKS());
        } catch (Exception e) {
            logger.error("Failed to connect to cassandra ", e);
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (cluster != null)
            cluster.shutdown();
    }

}
