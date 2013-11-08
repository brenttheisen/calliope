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


import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.db.IColumn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * encapsulates Keyspace, coulfamily name and column data for streaming
 */
public class CasMutation implements Serializable {


    private final String ks;
    private final String cf;
    private final List<ColumnData> columnData;
    private final String coulmndataString;

    public CasMutation(String ks, ColumnFamily columnFamily) {
        this.ks = ks;
        this.cf = columnFamily.metadata().cfName;

        List<ColumnData> list = new ArrayList<ColumnData>();
        for (IColumn columnExternal : columnFamily.getSortedColumns()) {
            list.add(new ColumnData(columnExternal.name().array(), columnExternal.value().array()));
        }
        this.columnData = list;
        this.coulmndataString = columnFamily.toString();
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
        sb.append(coulmndataString);
        return sb.toString();
    }
}