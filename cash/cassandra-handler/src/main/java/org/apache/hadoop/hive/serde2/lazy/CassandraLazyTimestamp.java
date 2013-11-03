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

import java.nio.ByteBuffer;
import java.sql.Timestamp;

import org.apache.hadoop.hive.serde2.lazy.objectinspector.primitive.LazyTimestampObjectInspector;

/**
 * CassandraLazyTimestamp parses the object into TimestampWritable value.
 *
 */
public class CassandraLazyTimestamp extends LazyTimestamp
{
  public CassandraLazyTimestamp(LazyTimestampObjectInspector oi) {
    super(oi);
  }

  @Override
  public void init(ByteArrayRef bytes, int start, int length) {

    if ( length == 8 ) {
      try {
        ByteBuffer buf = ByteBuffer.wrap(bytes.getData(), start, length);
        data.set(new Timestamp(buf.getLong(buf.position())));
        isNull = false;
        return;
      } catch (Throwable ie) {
        //we are unable to parse the data, try to parse it in the hive lazy way.
      }
    }

    super.init(bytes, start, length);
  }
}

