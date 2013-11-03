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

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class MapWritableComparable extends MapWritable implements WritableComparable<MapWritableComparable> {

    @Override
    public int compareTo(MapWritableComparable that) {
        if (this == that) {
            return 0;
        }
        if (this.keySet().size() == that.keySet().size()) {
            for (Entry<Writable, Writable> thisEntry : this.entrySet()) {
                Writable thatValue = that.get(thisEntry.getKey());
                if (!thisEntry.getValue().equals(thatValue)) {
                    return -1;
                }
            }
            //implies all key-value pairs are equal.
            return 0;
        }
        return -1;
    }
}
