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

package org.apache.hadoop.hive.cassandra.input.cql;

import org.apache.cassandra.hadoop.ColumnFamilyRecordReader;
import org.apache.hadoop.hive.serde2.lazy.LazyMap;
import org.apache.hadoop.hive.serde2.lazy.LazyObject;
import org.apache.hadoop.hive.serde2.lazy.LazyPrimitive;
import org.apache.hadoop.hive.serde2.lazy.objectinspector.LazyMapObjectInspector;

import java.util.LinkedHashMap;
import java.util.Map;

public class LazyCqlCellMap extends LazyMap {

  private ColumnFamilyRecordReader rowResult;
  private String cassandraColumnFamily;

  protected LazyCqlCellMap(LazyMapObjectInspector oi) {
    super(oi);
  }

  public void init(ColumnFamilyRecordReader rr, String columnFamily) {
    rowResult = rr;
    cassandraColumnFamily = columnFamily;
    setParsed(false);
  }

  private void parse() {
    if (cachedMap == null) {
      cachedMap = new LinkedHashMap<Object, Object>();
    } else {
      cachedMap.clear();
    }
  }

  /**
   * Get the value in the map for the given key.
   *
   * @param key
   * @return
   */

  @Override
  public Object getMapValueElement(Object key) {
    if (!getParsed()) {
      parse();
    }

    for (Map.Entry<Object, Object> entry : cachedMap.entrySet()) {
      LazyPrimitive<?, ?> lazyKeyI = (LazyPrimitive<?, ?>) entry.getKey();
      // getWritableObject() will convert LazyPrimitive to actual primitive
      // writable objects.
      Object keyI = lazyKeyI.getWritableObject();
      if (keyI != null) {
        if (keyI.equals(key)) {
          // Got a match, return the value
          LazyObject v = (LazyObject) entry.getValue();
          return v == null ? v : v.getObject();
        }
      }
    }

    return null;
  }

  @Override
  public Map<Object, Object> getMap() {
    if (!getParsed()) {
      parse();
    }
    return cachedMap;
  }

  @Override
  public int getMapSize() {
    if (!getParsed()) {
      parse();
    }
    return cachedMap.size();
  }

}
