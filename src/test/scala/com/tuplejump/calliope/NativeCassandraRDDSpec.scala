package com.tuplejump.calliope

import org.scalatest.{BeforeAndAfterAll, FunSpec}
import org.scalatest.matchers.{MustMatchers, ShouldMatchers}
import org.apache.spark.SparkContext

import com.tuplejump.calliope.Implicits._
import com.tuplejump.calliope.macros.NativeRowReader
import scala.language.implicitConversions

/**
 * To run this test you need a Cassandra cluster up and running
 * and run the cql3test.cql in it to create the data.
 *
 */
class NativeCassandraRDDSpec extends FunSpec with BeforeAndAfterAll with ShouldMatchers with MustMatchers {

  val CASSANDRA_NODE_COUNT = 3
  val CASSANDRA_NODE_LOCATIONS = List("127.0.0.1", "127.0.0.2", "127.0.0.3")
  val TEST_KEYSPACE = "casSparkTest"
  val TEST_INPUT_COLUMN_FAMILY = "Words"

  info("Describes the functionality provided by the Cassandra RDD")

  val sc = new SparkContext("local[1]", "nattest")

  describe("Native Cassandra RDD") {
    it("should be able to build and process RDD[U]") {
      val transformer = NativeRowReader.columnListMapper[NativeEmployee]("deptid", "empid", "first_name", "last_name")
      import transformer._

      val cas = CasBuilder.native.withColumnFamilyAndKeyColumns("cql3_test", "emp_read_test", "deptid").inputSplitSize(64 * 1024 * (256 / 4)).mergeRangesInMultiRangeSplit(256)


      val casrdd = sc.nativeCassandra[NativeEmployee](cas)



      /*val result = casrdd.collect().toList

      result must have length (5)
      result should contain(NativeEmployee(20, 105, "jack", "carpenter"))
      result should contain(NativeEmployee(20, 106, "john", "grumpy")) */

      casrdd.count() must equal(5)
    }


    /* it("should be able to query selected columns") {

      implicit val Row2NameTuple: Row => (String, String) = { row: Row => (row.getString("first_name"), row.getString("last_name"))}

      val columnFamily: String = "emp_read_test"

      val token = "token(deptid)"
      val inputCql: String = s"select deptid, first_name, last_name from $columnFamily where $token > ? and $token <? allow filtering"

      val cas = CasBuilder.native.withColumnFamilyAndQuery("cql3_test", columnFamily, inputCql) //.mergeRangesInMultiRangeSplit(256)

      val casrdd = sc.nativeCassandra[(String, String)](cas)

      val result = casrdd.collect().toList

      result must have length (5)
      result should contain(("jack", "carpenter"))
      result should contain(("john", "grumpy"))

    }

    it("should be able to use secodary indexes") {
      val transformer = NativeRowReader.columnListMapper[NativeEmployee]("deptid", "empid", "first_name", "last_name")

      import transformer._

      val columnFamily = "emp_read_test"

      val token = "token(deptid)"

      val query = s"select * from $columnFamily where $token > ? and $token <? and first_name = 'john' allow filtering"
      val cas = CasBuilder.native.withColumnFamilyAndQuery("cql3_test", "emp_read_test", query) //.mergeRangesInMultiRangeSplit(256)

      val casrdd = sc.nativeCassandra[NativeEmployee](cas)

      val result = casrdd.collect().toList

      result must have length 1


      result should contain(NativeEmployee(20, 106, "john", "grumpy"))
      result should not contain (NativeEmployee(20, 105, "jack", "carpenter"))
    } */


  }

  override def afterAll() {
    sc.stop()
  }
}

case class NativeEmployee(deptId: Int, empId: Int, firstName: String, lastName: String)