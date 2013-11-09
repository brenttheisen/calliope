#!/bin/sh


#Check for cassandra home
if [ -z "$CASSANDRA_HOME" ]; then
    echo "You must set the CASSANDRA_HOME var" >&2
    exit 1
fi

BASEDIR=$(dirname $0)

#copy jar depenceies
for f in $BASEDIR/../lib/*.jar
do 
   cp -v $f $CASSANDRA_HOME/lib
done

cp -v $BASEDIR/../conf/calliope-config.properties $CASSANDRA_HOME/conf/

# append javagent to conf script
cp $CASSANDRA_HOME/conf/cassandra-env.sh $CASSANDRA_HOME/conf/cassandra-env_backup.sh
echo "JVM_OPTS=\"\$JVM_OPTS -javaagent:\$CASSANDRA_HOME/lib/aspectjweaver-1.6.11.jar -Djava.library.path=/usr/local/lib\"" >> $CASSANDRA_HOME/conf/cassandra-env.sh
