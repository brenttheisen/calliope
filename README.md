Installation instructions
========================

##software requirements


+ [Cassandra 1.2.x] (http://cassandra.apache.org/download/)
+ [ZMQ 2.2] ( http://download.zeromq.org/zeromq-2.2.0.tar.gz)
+ [Scala binding for ZMQ] (http://zeromq.org/bindings:scala-binding)
+ [AspectJ 1.6.11] (http://www.eclipse.org/aspectj/)
+ [Cassandra driver for Java] (https://github.com/datastax/java-driver)

pom.xml file is included with the dependencies as stated above.
There is maven assembly script provided to make zip/tar.gz file of the compiled jar, it’s dependencies and installation scripts.

##Compile and build

From project root directory Execute **mvn assembly:assembly** to compile and package generated jar and it’s dependencies as zip, tar.gz

##Instructions for installation castriggers

For installing cassandra triggers, following library is to be included into the  $CASSANDRA_HOME/lib

+ aspectjweaver-1.6.11.jar
+ cassandra-driver-core-1.0.2.jar
+ zeromq-scala-binding_2.10-0.0.7.jar
+ triggers-1.0.jar (Generated jar file)

###Configuration

Add aspect weaver as javaagent to $CASSANDRA_HOME/conf/cassandra-env.sh file

`JVM_OPTS="$JVM_OPTS -javaagent:$CASSANDRA_HOME/lib/aspectjweaver-1.6.11.jar -Djava.library.path=/usr/local/lib" `

From the distribution package there is a install.sh file that would take care of copying and modifying the configuration file.

After extracting castriggers package, execute the following scripts

+ `export CASSANDRA_HOME=<<PATH to cassandra home>> `
+ `cd bin`
+ `./install.sh`
+ `./setup.sh`


##Setup Trigger for a columnFamily

There is a sample triggers_example script provided in the bin directory. Modify the scripy and run as follows to setup triggers
Make sure cassandra is running before running this script

`$CASSANDRA_HOME/bin/cqlsh < triggers_example.cql`

## Future releases
As of now, reloading Trigger classes is not supported. It will be supported soon.
