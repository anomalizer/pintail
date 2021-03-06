package com.inmobi.messaging.consumer.databus;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.inmobi.databus.readers.CollectorStreamReader;
import com.inmobi.messaging.ClientConfig;
import com.inmobi.messaging.consumer.AbstractMessageConsumer;
import com.inmobi.messaging.consumer.MessageConsumerFactory;
import com.inmobi.messaging.consumer.util.ConsumerUtil;
import com.inmobi.messaging.consumer.util.TestUtil;

public class TestDatabusConsumer extends TestAbstractDatabusConsumer {

  ClientConfig loadConfig() {
    return ClientConfig.loadFromClasspath(
        MessageConsumerFactory.MESSAGE_CLIENT_CONF_FILE);
  }

  @BeforeTest
  public void setup() throws Exception {
    consumerName = "c1";
    collectors = new String[] {"collector1"};
    dataFiles = new String[] {TestUtil.files[0], TestUtil.files[1],
        TestUtil.files[2]};
    super.setup(1);
  }

  @Test
  public void testTimeoutStats() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck1);
    ConsumerUtil.testTimeoutStats(config, testStream, consumerName,
        CollectorStreamReader.getDateFromCollectorFile(dataFiles[0]), false, 300);
  }

  @Test
  public void testMarkAndReset() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck5);
    config.set(MessagingConsumerConfig.relativeStartTimeConfig,
        relativeStartTime);
    ConsumerUtil.testMarkAndReset(config, testStream, consumerName, false);
  }

  @Test
  public void testMarkAndResetWithStartTime() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck2);
    ConsumerUtil.testMarkAndResetWithStartTime(config, testStream, consumerName,
        CollectorStreamReader.getDateFromCollectorFile(dataFiles[1]), false);
  }

  @Test
  public void testMultipleClusters() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString() + "," + rootDirs[1].toUri().toString());
    config.set(DatabusConsumerConfig.checkpointDirConfig,
        ck3);
    config.set(MessagingConsumerConfig.relativeStartTimeConfig,
        relativeStartTime);
    assertMessages(config, 2, 1);
  }

  @Test
  public void testMultipleClusters2() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toString() + "," + rootDirs[1].toString() + ","
            + rootDirs[2].toString());
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck4);
    config.set(MessagingConsumerConfig.relativeStartTimeConfig,
        relativeStartTime);
    assertMessages(config, 3, 1);
  }

  @Test
  public void testConsumerStartUp() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck6);
    config.set(MessagingConsumerConfig.relativeStartTimeConfig,
        relativeStartTime);
    ConsumerUtil.testConsumerStartUp(config, testStream,
        consumerName, false,
        CollectorStreamReader.getDateFromCollectorFile(dataFiles[1]),
        rootDirs[0], chkpointPathPrefix);
  }

  @Test
  public void testConsumerWithConfiguredStartTime() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck7);
    Date absoluteStartTime = CollectorStreamReader.
        getDateFromCollectorFile(dataFiles[1]);
    config.set(MessageConsumerFactory.ABSOLUTE_START_TIME,
        AbstractMessageConsumer.minDirFormat.get().format(absoluteStartTime));
    ConsumerUtil.testConsumerWithConfiguredStartTime(config, false);
  }

  @Test
  public void testConsumerWithFutureStartTime() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    Date absoluteStartTime = CollectorStreamReader.
        getDateFromCollectorFile(dataFiles[1]);
    // created a future time stamp
    Calendar cal = new GregorianCalendar();
    cal.setTime(absoluteStartTime);
    cal.add(Calendar.HOUR, 2);

    config.set(MessageConsumerFactory.ABSOLUTE_START_TIME,
        AbstractMessageConsumer.minDirFormat.get().format(cal.getTime()));
    ConsumerUtil.testConsumerWithFutureStartTime(config);
  }

  @Test
  public void testConsumerWithoutConfiguredOptions() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck8);
    ConsumerUtil.testConsumerWithoutConfiguredOptions(config);
  }

  @Test
  public void testConsumerWithRetentionPeriod() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck9);
    config.set(MessagingConsumerConfig.retentionConfig, "1");
    ConsumerUtil.testConsumerWithRetentionPeriod(config, testStream,
        consumerName, false);
  }

  /*
   *  setting retention period as 0 hours and relative time is 30 minutes.
   *  Consumer should start consume the messages from 30 minutes beyond the
   *  current time
   */
  @Test
  public void testConsumerWithRelativeAndRetention() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck10);
    config.set(DatabusConsumerConfig.retentionConfig, "0");
    config.set(MessagingConsumerConfig.relativeStartTimeConfig,
        relativeStartTime);
    Date absoluteStartTime = CollectorStreamReader.
        getDateFromCollectorFile(dataFiles[1]);
    config.set(MessageConsumerFactory.ABSOLUTE_START_TIME,
        AbstractMessageConsumer.minDirFormat.get().format(absoluteStartTime));
    ConsumerUtil.testConsumerWithRelativeAndRetention(config, testStream,
        consumerName, absoluteStartTime, false);
  }

  @Test
  public void testConsumerWithAbsoluteStartTimeAndRetention()
      throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    config.set(DatabusConsumerConfig.retentionConfig, "1");
    Date absoluteStartTime = CollectorStreamReader.
        getDateFromCollectorFile(dataFiles[1]);
    config.set(MessageConsumerFactory.ABSOLUTE_START_TIME,
        AbstractMessageConsumer.minDirFormat.get().format(absoluteStartTime));
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck11);
    ConsumerUtil.testConsumerWithAbsoluteStartTimeAndRetention(config,
        testStream, consumerName, absoluteStartTime, false);
  }

  @Test
  public void testConsumerWithStopTimeBeyondCheckpoint() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    Date absoluteStartTime = CollectorStreamReader.
        getDateFromCollectorFile(dataFiles[0]);
    config.set(MessageConsumerFactory.ABSOLUTE_START_TIME,
        AbstractMessageConsumer.minDirFormat.get().format(absoluteStartTime));
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck12);
    Date stopDate = CollectorStreamReader.getDateFromCollectorFile(dataFiles[1]);
    Date stopDateForCheckpoint = CollectorStreamReader.
        getDateFromCollectorFile(dataFiles[0]);
    config.set(DatabusConsumerConfig.stopDateConfig,
        AbstractMessageConsumer.minDirFormat.get().format(stopDate));
    ConsumerUtil.testConsumerWithStopTimeBeyondCheckpoint(config,
        testStream, consumerName, absoluteStartTime, false, stopDateForCheckpoint);
  }

  @Test
  public void testConsumerWithStartOfStream() throws Exception {
    ClientConfig config = loadConfig();
    config.set(DatabusConsumerConfig.databusRootDirsConfig,
        rootDirs[0].toUri().toString());
    config.set(MessagingConsumerConfig.startOfStreamConfig, "true");
    config.set(DatabusConsumerConfig.checkpointDirConfig, ck13);
    ConsumerUtil.testConsumerWithStartOfStream(config, testStream, consumerName,
        false);
  }

  @AfterTest
  public void cleanup() throws IOException {
    super.cleanup();
  }
}
