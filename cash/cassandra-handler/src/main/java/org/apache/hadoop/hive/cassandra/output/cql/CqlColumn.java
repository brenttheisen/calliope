/**
 * Licensed to Tuplejump Software Pvt. Ltd. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Tuplejump Software Pvt. Ltd. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.cassandra.output.cql;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * This represents a cassandra column.
 */
public class CqlColumn implements Writable {

  private String columnFamily;
  private long timeStamp;
  private byte[] column;
  private byte[] value;

  @Override
  public void readFields(DataInput din) throws IOException {
    columnFamily = din.readUTF();
    timeStamp = din.readLong();
    int clength = din.readInt();
    column = new byte[clength];
    din.readFully(column, 0, clength);
    int vlength = din.readInt();
    value = new byte[vlength];
    din.readFully(value, 0, vlength);
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeUTF(columnFamily);
    out.writeLong(timeStamp);
    out.writeInt(column.length);
    out.write(column);
    out.writeInt(value.length);
    out.write(value);
  }

  public String getColumnFamily() {
    return columnFamily;
  }

  public void setColumnFamily(String columnFamily) {
    this.columnFamily = columnFamily;
  }

  public byte[] getColumn() {
    return column;
  }

  public void setColumn(byte[] column) {
    this.column = column;
  }

  public byte[] getValue() {
    return value;
  }

  public void setValue(byte[] value) {
    this.value = value;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("cf:" + this.columnFamily);
    sb.append("column:" + new String(this.column));
    sb.append("value:" + new String(this.value));
    return sb.toString();
  }
}