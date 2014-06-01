package com.tuplejump.calliope.hadoop;


import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultiRangeSplit extends InputSplit implements Writable, org.apache.hadoop.mapred.InputSplit, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(MultiRangeSplit.class);

    private LongWritable length;
    private ArrayWritable tokenRanges;
    private String[] dataNodes;


    public MultiRangeSplit(TokenRange[] tokenRanges, long length, String[] dataNodes) {
        this.tokenRanges = new ArrayWritable(TokenRange.class, tokenRanges);
        this.length = new LongWritable(length);
        this.dataNodes = dataNodes;
    }

    public List<TokenRange> getTokenRanges() {
        Writable[] w = this.tokenRanges.get();
        List<TokenRange> tokenRangeList = new ArrayList<>();
        for (Writable tr : w) {
            tokenRangeList.add((TokenRange) tr);
        }
        return tokenRangeList;
    }

    @Override
    public long getLength() {
        return length.get();
    }

    @Override
    public String[] getLocations() {
        return dataNodes;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        try {
            length.write(out);
            tokenRanges.write(out);
            out.writeInt(dataNodes.length);
            for (String node : dataNodes) {
                out.writeUTF(node);
            }
        } catch (IOException ex) {
            logger.error("Error in writing out split");
            throw ex;
        }
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        try {
            length.readFields(in);
            tokenRanges.readFields(in);
            int numOfEndpoints = in.readInt();
            dataNodes = new String[numOfEndpoints];
            for (int i = 0; i < numOfEndpoints; i++) {
                dataNodes[i] = in.readUTF();
            }
        } catch (IOException ex) {
            logger.error("Error in reading out split");
            throw ex;
        }

    }

    public static class TokenRange implements Writable, Serializable {
        private String startToken;
        private String endToken;
        private long length;

        public TokenRange(String startToken, String endToken, long length) {
            this.startToken = startToken;
            this.endToken = endToken;
            this.length = length;
        }

        public String getStartToken() {
            return startToken;
        }

        public void setStartToken(String startToken) {
            this.startToken = startToken;
        }

        public String getEndToken() {
            return endToken;
        }

        public void setEndToken(String endToken) {
            this.endToken = endToken;
        }

        public long getLength() {
            return length;
        }

        public void setLength(long length) {
            this.length = length;
        }

        @Override
        public void write(DataOutput dataOutput) throws IOException {
            try {
                dataOutput.writeUTF(startToken);
                dataOutput.writeUTF(endToken);
                dataOutput.writeLong(length);
            } catch (IOException ex) {
                logger.error("Error in writing out split's token range");
                throw ex;
            }


        }

        @Override
        public void readFields(DataInput dataInput) throws IOException {
            try {
                this.startToken = dataInput.readUTF();
                this.endToken = dataInput.readUTF();
                this.length = dataInput.readLong();
            } catch (IOException ex) {
                logger.error("Error in reading out split's token range");
                throw ex;
            }

        }
    }
}
