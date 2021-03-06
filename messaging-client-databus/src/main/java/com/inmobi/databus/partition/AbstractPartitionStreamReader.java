package com.inmobi.databus.partition;

import java.io.IOException;

import com.inmobi.databus.files.StreamFile;
import com.inmobi.databus.readers.StreamReader;
import com.inmobi.messaging.Message;

public abstract class AbstractPartitionStreamReader implements
     PartitionStreamReader {

  protected StreamReader reader;
  protected boolean closed = false;

  protected StreamReader getReader() {
    return this.reader;
  }

  public StreamFile getCurrentFile() {
    return reader.getCurrentStreamFile();
  }

  @Override
  public long getCurrentLineNum() {
    return reader.getCurrentLineNum();
  }

  @Override
  public boolean openStream() throws IOException {
    return reader.openStream();
  }

  @Override
  public void close() throws IOException {
    closed = true;
    if (reader != null) {
      reader.close();
    }
  }

  @Override
  public void closeStream() throws IOException {
    if (reader != null) {
      reader.closeStream();
    }
  }

  public Message readLine() throws IOException, InterruptedException {
    return reader.readLine();
  }

}
