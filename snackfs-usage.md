---
layout: default
title: SnackFS Usage - Calliope
---

# I can't wait to get my hands dirty! How do I start using SnackFS?

All you need to start using SnackFS is a Cassandra cluster (or just one instance) and SnackFS distribution.

## How do I install SnackFS?

You can download the binary distribution of SnackFS for your version of Scala,
* [Scala 2.10.x](http://bit.ly/1jI7vVw)
* [Scala 2.9.x](http://bit.ly/1eKV1ae)

and extract it. That's all there is! For your convenience you may set the SNACKFS_HOME environment variable to point to the extracted directory.

## How do I add dependencies to my SBT/Maven Project?

For SBT
```scala
"com.tuplejump" %% "snackfs" % "0.6.1-EA"
```

For Maven in a project with Scala 2.9.3,
```xml
<dependency>
  <groupId>com.tuplejump</groupId>
  <artifactId>snackfs_2.9.3</artifactId>
  <version>0.6.1-EA</version>
</dependency>
```

And with Scala 2.10.3,
```xml
<dependency>
  <groupId>com.tuplejump</groupId>
  <artifactId>snackfs_2.10</artifactId>
  <version>0.6.1-EA</version>
</dependency>
```


## How do I upload a file to SnackFS?

You can use either the -put command or the -copyFromLocal command to move
a local file or directory into SnackFS. For example, if you plan to move the
local directory /var/tuplejump/app/logs into the distributed file system
TargetDir directory,
using the put command:

`snackfs -put /var/tuplejump/app/logs snackfs:///TargetDir/`

Using the copyFromLocal command:

`snackfs -copyFromLocal /var/tuplejump/app/logs snackfs:///TargetDir/`

## How do I download a file?
You can use the -get command or the -copyToLocal command to copy a file from
SnackFS to the local system. For example, if you plan to copy /app/logs into
the local /var/tuplejump/app/TargetDir/ directory,
using the get command:

`snackfs -get snackfs:///app/logs /var/tuplejump/app/TargetDir/`

using copyToLocal command:

`snackfs -copyToLocal snackfs:///app/logs /var/tuplejump/app/TargetDir/`

## How do I list files in a directory?
You can use the -ls command to list the contents of a directory. For example,
if you want to view the list for /app directory,

`snackfs -ls snackfs:///app`

And to view a list of contents recursively, you can use the -lsr command.

## What are the SnackFS commands?
The following Hadoop Shell commands are supported in SnackFS

1. ls, lsr
2. du, dus, stat
3. cp
4. mv
5. mkdir
6. rm, rmr
7. cat
8. tail
9. put
10. copyFromLocal
11. getmerge
12. get
13. copyToLocal
14. touchz
15. test
16. count
17. text
18. help

The help command can be used to see the usage of all or individual commands. For example,

`snackfs -help`

will display help for all the supported commands, and to see help for mkdir,

`snackfs -help mkdir`



