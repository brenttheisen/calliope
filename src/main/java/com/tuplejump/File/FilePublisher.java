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

package com.tuplejump.File;

import com.tuplejump.calliope.streaming.ITrigger;
import org.apache.cassandra.db.ColumnFamily;

import java.io.*;

/**
 * User: suresh
 * Date: 23/8/13
 * Time: 1:40 PM
 */
public class FilePublisher implements ITrigger, Closeable {


    private static PrintWriter pw;

    static{
        try {
            pw = new PrintWriter("out.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void process(ColumnFamily cf, String keyspace) {
        pw.write("update for keyspace "+keyspace);
        pw.write("Column Family update "+cf.toString());
        pw.flush();
    }

    public void close() throws IOException {
        pw.close();
    }
}
