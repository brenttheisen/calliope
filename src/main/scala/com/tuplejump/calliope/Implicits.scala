/*
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
 *
 */

package com.tuplejump.calliope

import com.tuplejump.calliope.thrift.ThriftCassandraSparkContext
import com.tuplejump.calliope.cql3.Cql3CassandraSparkContext
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import java.nio.ByteBuffer

import scala.language.implicitConversions
import com.tuplejump.calliope.macros.{Mappable, MapperMacro}

trait Generator {
  def generate[T]: Mappable[T]
}


/* object Codec {

  import scala.language.experimental.macros

  def useLowerCaseMapper = {
    def toLowerCase(word: String) = word.toLowerCase

    def generate[T] = macro MapperMacro._generateMapper[T]
  }

  object useSnakeCaseMapper {
    private def toSnakeCase(word: String) = word.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z\\d])([A-Z])", "$1_$2").toLowerCase

  }

  private def toSnakeCase(word: String) = word.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z\\d])([A-Z])", "$1_$2").toLowerCase

} */

object Implicits {

  implicit def RddToCassandraRDDFunctions[U](rdd: RDD[U]) =
    new CassandraRDDFunctions[U](rdd)

  implicit def SparkContext2ThriftCasSparkContext(sc: SparkContext) = new ThriftCassandraSparkContext(sc)

  implicit def SparkContext2Cql3CasSparkContext(sc: SparkContext) = new Cql3CassandraSparkContext(sc)
}
