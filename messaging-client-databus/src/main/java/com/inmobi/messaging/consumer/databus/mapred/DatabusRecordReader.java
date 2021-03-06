package com.inmobi.messaging.consumer.databus.mapred;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.LineRecordReader;

import com.inmobi.messaging.Message;
import com.inmobi.messaging.consumer.util.DatabusUtil;

public class DatabusRecordReader implements RecordReader<LongWritable, Message> {

  private LineRecordReader lineReader;
  private Text textValue;

  public DatabusRecordReader(JobConf job, InputSplit split) throws IOException {
    lineReader = new LineRecordReader(job, (FileSplit) split);
  }

  public LongWritable createKey() {
    return lineReader.createKey();
  }

  public Message createValue() {
    textValue = lineReader.createValue();
    return new Message(new byte[0]);
  }

  public long getPos() throws IOException {
    return lineReader.getPos();
  }

  public boolean next(LongWritable key, Message value) throws IOException {
    textValue.clear();
    boolean ret = lineReader.next(key, this.textValue);
    if (ret) {
      // get the byte array corresponding to the value read
      int length = textValue.getLength();
      byte[] msg = new byte[length];
      System.arraycopy(textValue.getBytes(), 0, msg, 0, length);
      //decode Base 64
      DatabusUtil.decodeMessage(msg, value);
    }
    return ret;
  }

  public void close() throws IOException {
    lineReader.close();
  }

  public float getProgress() throws IOException {
    return lineReader.getProgress();
  }
}
