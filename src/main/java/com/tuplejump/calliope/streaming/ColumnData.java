package com.tuplejump.calliope.streaming;


import java.io.Serializable;

/**
 * Return name and value as byte array for a given column
 */
public class ColumnData implements Serializable {

    private final byte[] cName;
    private final byte[] value;

    public ColumnData(byte[] cName, byte[] value) {
        this.cName = cName;
        this.value = value;
    }

    /**
     *
     * @return column value
     */
    public byte[] getValue() {
        return value;
    }

    /**
     *
     * @return column name
     */
    public byte[] getcName() {
        return cName;
    }
}