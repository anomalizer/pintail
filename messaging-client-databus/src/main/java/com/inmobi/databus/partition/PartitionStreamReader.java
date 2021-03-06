package com.inmobi.databus.partition;

import java.io.IOException;

import com.inmobi.databus.files.StreamFile;
import com.inmobi.messaging.Message;
import com.inmobi.messaging.consumer.databus.MessageCheckpoint;

public interface PartitionStreamReader {

  void initializeCurrentFile() throws IOException, InterruptedException;

  StreamFile getCurrentFile();

  long getCurrentLineNum();

  boolean openStream() throws IOException;

  Message readLine() throws IOException, InterruptedException;

  void closeStream() throws IOException;

  void close() throws IOException;

  MessageCheckpoint getMessageCheckpoint();

  MessageCheckpoint buildStartPartitionCheckpoints();
}
