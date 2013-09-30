How to install
==============

After extracting castriggers package, execute the following scripts

export CASSANDRA_HOME=<<PATH to cassandra home>>
cd bin
./install.sh
./setup.sh


How to setup triggers
=====================

There is a sample triggers_example script provided in the bin directory. Modify the scripy and run as follows to setup triggers

Make sure cassandra is running before running this script

$CASSANDRA_HOME/bin/cqlsh < triggers_example.cql
