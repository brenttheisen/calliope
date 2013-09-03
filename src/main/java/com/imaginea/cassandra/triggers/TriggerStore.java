package com.imaginea.cassandra.triggers;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;


/**
 * User: suresh
 * Date: 22/8/13
 * Time: 2:46 PM
 */
public class TriggerStore implements Closeable {

    public static TriggerStore instance = new TriggerStore();
    private static Logger logger = LoggerFactory.getLogger(TriggerStore.class);
    private Cluster cluster;
    private Session session;
    private String keySpace;
    private String columnFamily;
    private String clusterNode;
    private Map<String, List<ITrigger>> triggersMap = new HashMap<String, List<ITrigger>>();

    public TriggerStore() {
        Properties prop = new Properties();
        try {
            prop.load(TriggerStore.class.getClassLoader().getResourceAsStream("config.properties"));
            this.keySpace = prop.getProperty("Trigger.keyspace");
            this.columnFamily = prop.getProperty("Trigger.columnFamily");
            this.clusterNode = prop.getProperty("clusterNode");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file config.properties");
        }

    }

    public List<ITrigger> getTriggersForCF(String ks, String cf) {
        return triggersMap.get(ks + ":" + cf);
    }

    public void loadTriggers() {

        //initiate CQ3 client connection and reads triggers key space.
        connect(clusterNode);

        List<ITrigger> list;
        ResultSet results = session.execute("SELECT * FROM " + columnFamily);

        for (Row row : results) {
            String ks = row.getString("ks");
            String cf = row.getString("cf");
            String key = ks + ":" + cf;
            String _clazz = row.getString("class");

            try {
                @SuppressWarnings("unchecked")
                Class<ITrigger> clazz = (Class<ITrigger>) Class.forName(_clazz);
                ITrigger trigger = clazz.newInstance();
                if (!triggersMap.containsKey(key)) {
                    list = new ArrayList<ITrigger>();
                    triggersMap.put(key, list);
                }
                list = triggersMap.get(key);
                list.add(trigger);

            } catch (Exception e) {
                logger.info("failed to load class " + _clazz + " : possible reasons " + e.getMessage());
            }
        }
    }

    public void connect(String node) {
        cluster = Cluster.builder().addContactPoint(node).build();
        session = cluster.connect(keySpace);
    }

    public void close() {
        if (cluster != null)
            cluster.shutdown();
    }

}
