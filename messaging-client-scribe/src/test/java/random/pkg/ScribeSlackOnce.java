package random.pkg;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.thrift.TException;

import com.facebook.fb303.fb_status;

import scribe.thrift.LogEntry;
import scribe.thrift.ResultCode;
import scribe.thrift.scribe.Iface;

public class ScribeSlackOnce implements Iface {

  private Log LOG;

  public ScribeSlackOnce() {
    LOG = LogFactory.getLog("scribeserver");
  }

  @Override
  public String getName() throws TException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getVersion() throws TException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public fb_status getStatus() throws TException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getStatusDetails() throws TException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, Long> getCounters() throws TException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long getCounter(String key) throws TException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setOption(String key, String value) throws TException {
    // TODO Auto-generated method stub

  }

  @Override
  public String getOption(String key) throws TException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, String> getOptions() throws TException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getCpuProfile(int profileDurationInSec) throws TException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long aliveSince() throws TException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void reinitialize() throws TException {
    // TODO Auto-generated method stub

  }

  @Override
  public void shutdown() throws TException {
    // TODO Auto-generated method stub

  }

  boolean slackonce = true;
  @Override
  public ResultCode Log(List<LogEntry> messages) throws TException {
    if (LOG.isInfoEnabled()) {
      for (LogEntry m : messages) {
        LOG.info(m.getCategory() + ":" + m.getMessage());
      }
    }
    if (slackonce) {
      slackonce = false;
      try {
        System.err.println("server is slacking");
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new TException(e);
      }
    }
    return ResultCode.OK;
  }
}
