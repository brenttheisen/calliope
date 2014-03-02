---
layout: default
title: Coming soon... - Calliope
---

#What's on the Calliope roadmap

Calliope originally was intended to be a library to make it easy to use Spark with Cassandra. It is growing to be more than just that. Adding a file system, shark integration, streaming from cassandra and more . . .

We have these components ready and being tested in our labs and will be released shortly in the next E.A. release of Calliope.


##C\*Stream

C\*Stream provides a way to easily and reliable stream mutations from C\* to Spark Streaming. On C\* 2.0 it uses native triggers and even works on C\* 1.2.x using our AOP based dropin extension to emulate triggers.

##C*Hive

The C\*Hive hive handler for Cassandra provides a way to query Cassandra from Hive and hence Shark. It supports both the old thrift/compact storage tables as well as the new CQL3 tables. We are also working on an hive metastore implementation that can rely on CQL3 ColumnFamily Meta Information, so that C\* is a first class citizen in Hive. i.e. we would be able to seamlessly query and use C\* CF from Hive.


