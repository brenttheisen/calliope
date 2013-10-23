package com.tuplejump.calliope.streaming;


import java.io.Serializable;

public class ColumnData implements Serializable {

    private final byte[] cName;
    private final byte[] value;

    public ColumnData(byte[] cName, byte[] value) {
        this.cName = cName;
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    public byte[] getcName() {
        return cName;
    }
}