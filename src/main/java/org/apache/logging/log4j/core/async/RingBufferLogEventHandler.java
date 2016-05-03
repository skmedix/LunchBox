package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;

public class RingBufferLogEventHandler implements SequenceReportingEventHandler {

    private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
    private Sequence sequenceCallback;
    private int counter;

    public void setSequenceCallback(Sequence sequence) {
        this.sequenceCallback = sequence;
    }

    public void onEvent(RingBufferLogEvent ringbufferlogevent, long i, boolean flag) throws Exception {
        ringbufferlogevent.execute(flag);
        ringbufferlogevent.clear();
        if (++this.counter > 50) {
            this.sequenceCallback.set(i);
            this.counter = 0;
        }

    }
}
