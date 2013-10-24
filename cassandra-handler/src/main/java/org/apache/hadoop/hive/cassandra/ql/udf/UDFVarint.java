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

import java.math.BigInteger;

import org.apache.cassandra.db.marshal.IntegerType;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

@UDFType(deterministic = true)
@Description(name = "to_varint",
    value = "_FUNC_([arg]) - Returns a binary representation of a Java BigInteger",
    extended = "For use with Cassandra varint values. Takes a string or numeric \n" +
                 "argument and returns the binary object")

public class UDFVarint extends UDF {

  public BytesWritable evaluate(Text text){
    return new BytesWritable(
                  IntegerType.instance.decompose(
                      new BigInteger(new String(text.getBytes()))).array());
  }

  public BytesWritable evaluate(IntWritable iw){
    return new BytesWritable(
                  IntegerType.instance.decompose(
                      BigInteger.valueOf(iw.get())).array());
  }

  public BytesWritable evaluate(LongWritable lw){
    return new BytesWritable(
                  IntegerType.instance.decompose(
                      BigInteger.valueOf(lw.get())).array());
  }

}
