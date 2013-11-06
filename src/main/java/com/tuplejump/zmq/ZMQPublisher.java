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


import com.tuplejump.calliope.streaming.CasMutation;
import com.tuplejump.calliope.streaming.ColumnData;
import com.tuplejump.calliope.streaming.ITrigger;
import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.db.IColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * ZMQ publisher, publish stream to 127.0.0.1:1237
 */
public class ZMQPublisher implements Closeable, ITrigger {

    private static Logger logger = LoggerFactory.getLogger(ZMQPublisher.class);
    private ZMQ.Socket pub;
    private ZContext context;


    public ZMQPublisher() {
        context = new ZContext();
        pub = context.createSocket(ZMQ.PUB);
        pub.bind("tcp://127.0.0.1:1237");
    }

    public void close() throws IOException {
        pub.close();
        context.destroy();
    }


    public void process(CasMutation mutation) {

        try {
            logger.debug("publishing to ZMQ");

            pub.send("A".getBytes(), ZMQ.SNDMORE); //SendMore

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(mutation);
            pub.send(baos.toByteArray(), ZMQ.NOBLOCK); //NOBLOCK
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish to ZMQ ", e);
        }
    }
}
