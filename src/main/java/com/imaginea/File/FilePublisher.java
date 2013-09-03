package com.imaginea.File;

import com.imaginea.cassandra.triggers.ITrigger;
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

    public FilePublisher(){
//        Thread drainOnShutdown = new Thread(new WrappedRunnable() {
//            @Override
//            protected void runMayThrow() throws Exception {
//                close();
//            }
//        }, "FilePublisherShutdownHook");
//        Runtime.getRuntime().addShutdownHook(drainOnShutdown);
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
