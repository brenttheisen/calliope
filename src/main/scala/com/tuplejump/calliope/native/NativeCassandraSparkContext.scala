package com.tuplejump.calliope.native

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import com.datastax.driver.core.Row
import com.tuplejump.calliope.{NativeCasBuilder, CasBuilder}
import scala.annotation.implicitNotFound

class NativeCassandraSparkContext(self: SparkContext) {

  /**
   *
   * @param host
   * @param port
   * @param keyspace
   * @param columnFamily
   * @param unmarshaller
   * @param tm
   * @tparam T
   * @return
   */
  @implicitNotFound(
    "No transformer found for Row => ${T}. You must have an implicit method defined of type Row => ${T}"
  )
  def nativeCassandra[T](host: String, port: String, keyspace: String, columnFamily: String, partitionColumns: List[String])
                        (implicit unmarshaller: Row => T,
                         tm: Manifest[T]): RDD[T] = {
    val cas = CasBuilder.native.withColumnFamilyAndKeyColumns(keyspace, columnFamily).onHost(host).onPort(port)
    this.nativeCassandra[T](cas)
  }

  /**
   *
   * @param keyspace
   * @param columnFamily
   * @param unmarshaller
   * @param tm
   * @tparam T
   * @return
   */
  @implicitNotFound(
    "No transformer found for Row => ${T}. You must have an implicit method defined of type Row => ${T}"
  )
  def nativeCassandra[T](keyspace: String, columnFamily: String, partitionColumns: List[String])
                        (implicit unmarshaller: Row => T, tm: Manifest[T]): RDD[T] = {
    val cas = CasBuilder.native.withColumnFamilyAndKeyColumns(keyspace, columnFamily, partitionColumns: _*)
    nativeCassandra[T](cas)(unmarshaller, tm)
  }


  /**
   *
   * @param cas
   * @param unmarshaller
   * @param tm
   * @tparam T
   * @return
   */
  @implicitNotFound(
    "No transformer found for Row => ${T}. You must have an implicit method defined of type Row => ${T}"
  )
  def nativeCassandra[T](cas: NativeCasBuilder)(implicit unmarshaller: Row => T, tm: Manifest[T]): RDD[T] = {
    new NativeCassandraRDD[T](self, cas, unmarshaller)
  }

}
