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

package com.tuplejump.file;

import com.tuplejump.calliope.CalliopeProperties;
import com.tuplejump.calliope.streaming.CasMutation;
import com.tuplejump.calliope.streaming.ITrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;

/**
 * File publisher. writes stream to a file.
 * file path is mentioned as file.path in  calliope-config.properties
 */
public class FilePublisher implements ITrigger, Closeable {

    public static final FilePublisher instance = new FilePublisher();
    private static Logger logger = LoggerFactory.getLogger(FilePublisher.class);
    private BufferedWriter bw;


    private FilePublisher() {
        try {
            bw = new BufferedWriter(new FileWriter(CalliopeProperties.instance.getFilePath()));
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized void process(CasMutation mutation) {
        try {
            logger.debug("publishing to File");
            bw.write("update for column family:" + "\r\n");
            bw.write(mutation.toString() + "\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        bw.close();
    }
}
