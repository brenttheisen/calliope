package com.tuplejump.File;

import com.tuplejump.calliope.streaming.ITrigger;
import org.apache.cassandra.db.ColumnFamily;

import java.io.*;

/**
 * User: suresh
 * Date: 23/8/13
 * Time: 1:40 PM
 */
public class FilePublisher implements ITrigger, Closeable {


    private static PrintWriter pw;

    static{
        try {
            pw = new PrintWriter("out.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void process(ColumnFamily cf, String keyspace) {
        pw.write("update for keyspace "+keyspace);
        pw.write("Column Family update "+cf.toString());
        pw.flush();
    }

    public void close() throws IOException {
        pw.close();
    }
}
