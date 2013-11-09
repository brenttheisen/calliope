create keyspace triggers
with replication = {'class':'SimpleStrategy','replication_factor' : 1};

use triggers;

create table Triggers (
id int PRIMARY KEY,
ks text,
cf text,
class text
 );

