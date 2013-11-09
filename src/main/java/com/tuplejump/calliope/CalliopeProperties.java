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

package com.tuplejump.calliope;

import java.io.IOException;
import java.util.Properties;

public class CalliopeProperties {

    private final String triggerStoreKS;
    private final String triggerStoreCF;
    private final String clusterNode;
    private final int clusterPort;
    private final int zmqPort;
    private final String filePath;


    public static final CalliopeProperties instance = new CalliopeProperties();

    private CalliopeProperties() {

        Properties prop = new Properties();
        try {
            prop.load(CalliopeProperties.class.getClassLoader().getResourceAsStream("calliope-config.properties"));
            this.triggerStoreKS = prop.getProperty("trigger.keyspace");
            this.triggerStoreCF = prop.getProperty("trigger.cf");
            this.clusterNode = prop.getProperty("cluster.node");
            this.clusterPort = Integer.parseInt(prop.getProperty("cluster.port"));
            this.zmqPort = Integer.parseInt(prop.getProperty("zmq.port"));
            this.filePath = prop.getProperty("file.path");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file calliope-config.properties");
        }

    }

    public String getTriggerStoreKS() {
        return triggerStoreKS;
    }

    public String getTriggerStoreCF() {
        return triggerStoreCF;
    }

    public String getClusterNode() {
        return clusterNode;
    }

    public int getClusterPort() {
        return clusterPort;
    }

    public int getZmqPort() {
        return zmqPort;
    }

    public String getFilePath() {
        return filePath;
    }
}
