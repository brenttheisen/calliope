use triggers;
insert into Triggers (id, ks, cf, class) values (1,'dev', 'emp', 'com.tuplejump.zmq.ZMQPublisher');
insert into Triggers (id, ks, cf, class) values (2, 'dev', 'emp', 'com.tuplejump.file.FilePublisher');


create keyspace dev with replication = {'class':'SimpleStrategy','replication_factor':1};
use dev;
create table emp (empid int primary key, emp_first varchar, emp_last varchar, emp_dept varchar);
insert into emp (empid, emp_first, emp_last, emp_dept) values (1,'fred','smith','eng');