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