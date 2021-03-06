package com.inmobi.databus.partition;

import java.util.Map;
import java.util.TreeMap;

import com.inmobi.messaging.consumer.databus.MessageCheckpoint;

/**
 * Checkpoint for the segments of databus stream consumer.
 * this class is used to construct the checkpoint list for partition. Partition
 * checkpoint list contains the segment ids and respective partition checkpoints.
 */
public class PartitionCheckpointList implements MessageCheckpoint {

  // map of static id to its checkpoint
  private final Map<Integer, PartitionCheckpoint> pChkpoints =
      new TreeMap<Integer, PartitionCheckpoint>();

  public PartitionCheckpointList(Map<Integer, PartitionCheckpoint> chkpoints) {
    this.pChkpoints.putAll(chkpoints);
  }

  public PartitionCheckpointList() {
  }

  public Map<Integer, PartitionCheckpoint> getCheckpoints() {
    return pChkpoints;
  }

  public void set(int segmentId, PartitionCheckpoint pck) {
    pChkpoints.put(segmentId, pck);
  }

  /**
   * Checks the partition checkpoint list is empty or not..
   * @return false if the partition checkpoint list contains at least one entry.
   */
  @Override
  public boolean isNULL() {
    if (!pChkpoints.isEmpty()) {
      for (Map.Entry<Integer, PartitionCheckpoint> entry
          : pChkpoints.entrySet()) {
        if (entry.getValue() != null) {
          return false;
        }
      }
    }
    return true;
  }

  public String toString() {
    StringBuffer buf = new StringBuffer();
    for (Map.Entry<Integer, PartitionCheckpoint> entry : pChkpoints
        .entrySet()) {
      buf.append(entry.getKey().toString())
      .append(":");
      if (entry.getValue() != null) {
        buf.append(entry.getValue().toString());
      } else {
        buf.append("null");
      }
      buf.append(", ");
    }
    return buf.toString();
  }
}
