package com.atg.openssp.common.buffer;

import java.util.concurrent.CountDownLatch;

import util.SimpleRingBuffer;

/**
 * @author André Schmer
 *
 */
class LongTypedBuffer {

	private final SimpleRingBuffer<Long> rb;
	protected CountDownLatch latch;

	LongTypedBuffer(final int capacity) {
		rb = new SimpleRingBuffer<>(capacity, Long.class);
		latch = new CountDownLatch(1);
	}

	public void bufferValue(final long item) {
		rb.add(item);
	}

	public Long[] getBufferedData() {
		return rb.getAll();
	}

	public void shutDown() {
		latch.countDown();
	}

}
