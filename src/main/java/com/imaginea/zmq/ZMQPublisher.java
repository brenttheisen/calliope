package com.imaginea.zmq;


import com.imaginea.cassandra.triggers.ITrigger;
import org.apache.cassandra.db.ColumnFamily;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.io.Closeable;
import java.io.IOException;


/**
 * User: suresh
 * Date: 16/8/13
 * Time: 10:14 AM
 */

public class ZMQPublisher implements Closeable, ITrigger {
    private static Logger logger = LoggerFactory.getLogger(ZMQPublisher.class);
    private ZMQ.Socket pub;
    private ZMQ.Context context;


    public ZMQPublisher() {
        context = ZMQ.context(1);
        pub = context.socket(ZMQ.PUB);
        pub.bind("tcp://*:5555");
        // pub.bind("ipc://cassandra");

//        Thread drainOnShutdown = new Thread(new WrappedRunnable() {
//            @Override
//            protected void runMayThrow() throws Exception {
//                close();
//            }
//        }, "ZMQPublisherShutdownHook");
//        Runtime.getRuntime().addShutdownHook(drainOnShutdown);
    }

    public void close() throws IOException {
        pub.close();
        context.term();
    }

    public void process(ColumnFamily cf, String keyspace) {
        logger.debug("publishing to ZMQ");
        String message = "update for keyspace " + keyspace;
        pub.send(message.getBytes(), 0);
        message = "Column Family update " + cf.toString();
        pub.send(message.getBytes(), 0);
    }
}
