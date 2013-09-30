package com.tuplejump.calliope.streaming;


import org.apache.cassandra.db.ColumnFamily;


/**
 * User: suresh
 * Date: 22/8/13
 * Time: 5:36 PM
 */
public interface ITrigger {
    void process(ColumnFamily cf, String keyspace);
}
