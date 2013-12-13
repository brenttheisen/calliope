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


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * encapsulates Keyspace, columnfamily name and column data for streaming
 */
public class CasMutation implements Serializable {


    private String ks;
    private String cf;
    private List<ColumnData> columnData;
    private String columndataString;

    public CasMutation(String ks, String cf, List<ColumnData> columnData, String coulmndataString) {
        this.ks = ks;
        this.cf = cf;
        this.columnData = columnData;
        this.columndataString = coulmndataString;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(ks);
        out.writeObject(cf);
        out.writeObject(columnData);
        out.writeObject(columndataString);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        this.ks = (String) in.readObject();
        this.cf = (String) in.readObject();
        this.columnData = (List<ColumnData>) in.readObject();
        this.columndataString = (String) in.readObject();
    }

    public List<ColumnData> getColumnData() {
        return columnData;
    }

    public String getCfName() {
        return cf;
    }

    public String getKeySpace() {
        return ks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("keyspace = ").append(ks).append(", ");
        sb.append(columndataString);
        return sb.toString();
    }
}