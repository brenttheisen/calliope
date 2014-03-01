---
layout: default
title: SnackFS with Spark - Calliope
---

# Upload/Downloading is fine, I am here for using it with Spark!

SnackFS was written for use with Spark. Hance using it doesn't require any special API. Once you add the necessary dependencies, the **textFile** method in SparkContext and any other method that works with a HDFS File works as is with SnackFS Files too.

## How do I add SnackFS dependencies to Spark?

Assuming you have extracted the SnackFS distribution to $SNACKFS_HOME, you need to take the following steps to get started.

* Copy $SNACKFS_HOME/conf/core-site.xml to $SPARK_HOME/conf
* When you are using just the Spark Shell, in the $SPARK_HOME/spark-env.sh file, Add the following -

`export SPARK_CLASSPATH=$SNACKFS_HOME/snack_spark/*`

When using a Spark Cluster, add the jars in $SNACKFS_HOME/snack_spark using ADD_JARS command to the cluster, or add them to the workers classpath on the cluster nodes.

## How do I read SnackFS files in Spark?

As mentioned **sc.textFile** and all other APIs in Spark that work with HDFS work with SnackFS too, just use snackfs:// as the file URI protocol. So to read a file, just use the textFile method,

```scala
val textFile = sc.textFile("snackfs:///path/to/file")
```


## How do I use output data from Spark to SnackFS?

Similar to read, the write methods in Spark work with SnackFS too,

```scala
rdd.saveAsTextFile("snackfs://path/to/output")
```


## You say it is HDFS compatible, does thaat mean I can use this to checkpoint RDDs too?

Yes, surely you can. Just setup the snackfs directory you want to use as the checkpointing directory on the Spark Context and you are goo to go!

```scala
sc.setCheckpointDir("snackfs://path/to/checkpoint/dir")
...
rdd.checkpoint()
```

## Does that apply to DStream checkpoint?

Yes, it will work. Just set your checkpoint dir as the checkpoint location for for your Spark Streaming Context (ssc),

```scala
ssc.checkpoint("snackfs://path/to/checkpoint/dir")
```

## If it would work wih Shark, I could have moved my warehouse to SnackFS! Can I?

Again a Yes! You can use SnackFS for your Shark Warehouse too! You will need to set the warehouse directory in your Shark's hive configuration to use snackfs.

```xml
<property>
    <name>hive.metastore.warehouse.dir</name>
    <value>snackfs:///user/warehouse</value>
    <description>location of default database for the warehouse</description>
</property>
```



