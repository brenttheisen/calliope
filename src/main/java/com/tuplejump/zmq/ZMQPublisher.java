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

package com.tuplejump.zmq;


import com.tuplejump.calliope.streaming.ITrigger;
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
    }

    public void close() throws IOException {
        pub.close();
        context.term();
    }

    public void process(ColumnFamily cf, String keyspace) {
        logger.debug("publishing to ZMQ");
        String message = "update for keyspace " + keyspace;
        pub.send(message.getBytes(), 0);
        pub.send(cf.toString().getBytes(), 0);
    }
}
