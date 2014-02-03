Calliope
========
Calliope is a library providing an interface to consume data from Cassandra to spark and store RDDs from Spark to Cassandra.

In Greek mythology, Calliope (/kəˈlaɪ.əpiː/ kə-ly-ə-pee; Ancient Greek: Καλλιόπη Kalliopē "beautiful-voiced") was the muse of epic poetry,daughter of Zeus and Mnemosyne, and is believed to be Homer's muse, the inspiration for the Odyssey and the Iliad.

We hope Calliope will also be the muse for your epic data poetry!

For more information visit - http://tuplejump.github.com/calliope

## Download Binary

You can download the library from for Spark v0.8.1 [here](http://bit.ly/1dC3kbZ) and for Spark v0.9 [here](http://bit.ly/1j7CpbW).

## Add to Maven

Working with Spark 0.8.1 and Scala 2.9.x,

```xml
<dependency>
  <groupId>com.tuplejump</groupId>
  <artifactId>calliope_2.9.3</artifactId>
  <version>0.8.1-EA</version>
</dependency>
```


Working with Spark 0.9.0 and Scala 2.10.x,

```xml
<dependency>
  <groupId>com.tuplejump</groupId>
  <artifactId>calliope_2.10</artifactId>
  <version>0.9.0-EA</version>
</dependency>
```


## Add to SBT

Working with Spark 0.8.1 and Scala 2.9.x,

```scala
libraryDependencies += "com.tuplejump" %% "calliope" % "0.8.1-EA"
```


Working with Spark 0.9.0 and Scala 2.10.x,

```scala
libraryDependencies += "com.tuplejump" %% "calliope" % "0.9.0-EA"
```

**Note on Scala version**
Every version of Calliope is built with the same version as the corresponding release of Spark, i.e. Calliope 0.8.1 is built against Scala 2.9.3 and Calliope 0.9.0 against Scala 2.10.3.

