create keyspace triggers
with replication = {'class':'SimpleStrategy','replication_factor' : 1};

use triggers;

create table Triggers (
id int PRIMARY KEY,
ks text,
cf text,
class text
 )
WITH compaction = {'class' : 'LeveledCompactionStrategy'};

insert into Triggers (id, ks, cf, class)
values (1,'dev', 'emp', 'com.imaginea.zmq.ZMQPublisher');

insert into Triggers (id, ks, cf, class)
values (2, 'dev', 'emp', 'com.imaginea.File.FilePublisher');


select * from triggers