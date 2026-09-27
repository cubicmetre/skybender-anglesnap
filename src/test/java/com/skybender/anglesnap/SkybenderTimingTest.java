package com.skybender.anglesnap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkybenderTimingTest {
	@Test
	void encodeXzSanity() {
		assertEquals(new SkybenderTiming.EncodedAxis(0, 255, 0), SkybenderTiming.encodeXz(0));
		assertEquals(new SkybenderTiming.EncodedAxis(0, 255, 0), SkybenderTiming.encodeXz(1));
		assertEquals(new SkybenderTiming.EncodedAxis(0, 0, 0), SkybenderTiming.encodeXz(256));
		assertEquals(new SkybenderTiming.EncodedAxis(0, 255, 1), SkybenderTiming.encodeXz(257));
	}

	@Test
	void durationSanity() {
		assertEquals(189, SkybenderTiming.payloadDur(1));
		assertEquals(511, SkybenderTiming.payloadDur(8));
		assertEquals(299, SkybenderTiming.trimDur(255));
		assertEquals(58, SkybenderTiming.trimDur(0));
		assertEquals(52, SkybenderTiming.trimDur(1));
		assertEquals(0, SkybenderTiming.accelDur(0));
		assertEquals(40, SkybenderTiming.accelDur(1));
		assertEquals(369, SkybenderTiming.inputSequenceDur());
	}

	@Test
	void smokePredictN1ZeroZero() {
		int machine = SkybenderTiming.cannonStop(1, 0, 0, 30);
		assertTrue(Math.abs(machine - 881) <= 14, "machine=" + machine);
		assertEquals(369 + machine, SkybenderTiming.predictTime(1, 0, 0));

		int[] range = SkybenderTiming.predictTimeRange(1, 0, 0);
		assertEquals(2, range.length);
		assertTrue(range[0] <= range[1]);
		assertTrue(range[0] <= SkybenderTiming.predictTime(1, 0, 0));
		assertTrue(SkybenderTiming.predictTime(1, 0, 0) <= range[1]);
	}
}
