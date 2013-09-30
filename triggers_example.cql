use triggers;

insert into Triggers (id, ks, cf, class)
values (1,'dev', 'emp', 'com.tuplejump.zmq.ZMQPublisher');

insert into Triggers (id, ks, cf, class)
values (2, 'dev', 'emp', 'com.tuplejump.File.FilePublisher');


select * from triggers;