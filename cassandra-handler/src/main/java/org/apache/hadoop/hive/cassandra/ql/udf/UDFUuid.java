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

package org.apache.hadoop.hive.cassandra.ql.udf;

import java.util.UUID;

import org.apache.cassandra.utils.UUIDGen;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

@UDFType(deterministic = true)
@Description(name = "to_uuid",
    value = "_FUNC_([string]) - Returns a UUID parsed from an input stringa string of 32 hexidecimal characters",
    extended = "Takes a String of 32 hexidecimal characters, \n" +
                "split up using dashes in the standard UUID format:\n" +
                " XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX \n" +
                " and passes it through UUIDGen.decompose(UUID.fromString(str))")

public class UDFUuid extends UDF {

  public BytesWritable evaluate(Text text){
    return new BytesWritable(UUIDGen.decompose(UUID.fromString(new String(text.getBytes()))));
  }

}
