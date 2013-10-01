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

package org.apache.hadoop.hive.serde2.lazy;

import org.apache.cassandra.db.marshal.AbstractType;
import org.apache.cassandra.db.marshal.BooleanType;
import org.apache.cassandra.db.marshal.BytesType;
import org.apache.cassandra.db.marshal.DateType;
import org.apache.cassandra.db.marshal.DoubleType;
import org.apache.cassandra.db.marshal.FloatType;
import org.apache.cassandra.db.marshal.Int32Type;
import org.apache.cassandra.db.marshal.LongType;
import org.apache.cassandra.db.marshal.UTF8Type;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;

public class LazyCassandraUtils {
  public static AbstractType getCassandraType(PrimitiveObjectInspector oi) {
    switch (oi.getPrimitiveCategory()) {
    case BOOLEAN:
      return BooleanType.instance;
    case INT:
      return Int32Type.instance;
    case LONG:
      return LongType.instance;
    case FLOAT:
      return FloatType.instance;
    case DOUBLE:
      return DoubleType.instance;
    case STRING:
      return UTF8Type.instance;
    case BYTE:
    case SHORT:
    case BINARY:
      return BytesType.instance;
    case TIMESTAMP:
      return DateType.instance;
    default:
      throw new RuntimeException("Hive internal error.");

    }
  }
}
