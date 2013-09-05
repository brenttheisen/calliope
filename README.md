Installation instructions
========================

##software requirements

Cassandra 1.2.x
ZMQ 2.2
Scala binding for ZMQ

##Compile

From project root directory execute mvn install to generate jar file with aspect and other compile classes

##Library

Copy the following jar files to lib directory of cassandra : $CASSANDRA_HOME/lib

aspectjweaver-1.6.11.jar
cassandra-driver-core-1.0.2.jar
zeromq-scala-binding_2.10-0.0.7.jar
triggers-1.0.jar (Generated jar file)

##Configuration

Add aspect weaver as javaagent to $CASSANDRA_HOME/conf/cassandra-env.sh file

JVM_OPTS="$JVM_OPTS -javaagent:$CASSANDRA_HOME/lib/aspectjweaver-1.6.11.jar -Djava.library.path=/usr/local/lib"

##Running

Run cassandra from Cassandra script.

##Setting trigger database for the first time

Run cql queries from this script CQL script

##Setup Trigger for a columnFamily

Add an entry to Trigger CF keyspace, columnFamily, Trigger class

As of now, reloading Trigger classes is not supported. It will be supported soon.
