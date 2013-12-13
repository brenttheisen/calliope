package com.tuplejump.calliope.streaming.examples

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.StreamingContext._
import akka.zeromq.Subscribe
import com.tuplejump.calliope.streaming.CasMutation
import java.io.{ObjectInputStream, ByteArrayInputStream}

/**
 * A sample wordcount with ZeroMQStream stream to process mutations received via cassandra.
 *
 * To work with zeroMQ, some native libraries have to be installed.
 * Install zeroMQ (release 2.1) core libraries. [ZeroMQ Install guide](http://www.zeromq.org/intro:get-the-software)
 * Please refer to castrigger official documentation page for more information.
 */
object ZeroMQCastriggerWordCount {

  /** Deserialize an object using Java serialization */
  def deserialize[T](bytes: Array[Byte]): T = {
    val bis = new ByteArrayInputStream(bytes)
    val ois = new ObjectInputStream(bis)
    ois.readObject.asInstanceOf[T]
  }

  def main(args: Array[String]) {
    if (args.length < 3) {

      System.err.println(
        "Usage: ZeroMQCastriggerWordCount <master> <zeroMQurl> <topic>" +
          "In local mode, <master> should be 'local[n]' with n > 1")
      System.exit(1)
    }
    val Seq(master, url, topic) = args.toSeq

    // Create the context and set the batch size
    val ssc = new StreamingContext(master, "ZeroMQCastriggerWordCount", Seconds(5))

    def bytesToStringIterator(x: Seq[Seq[Byte]]) = {
      x.map {
        x1 =>
          deserialize[CasMutation](x1.toArray)
      }
    }.iterator
    //For this stream, a zeroMQ publisher should be running.
    val lines = ssc.zeroMQStream(url, Subscribe(topic), bytesToStringIterator)
    val words = lines.map(x => x.toString)
    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
    wordCounts.print()
    ssc.start()
  }

}