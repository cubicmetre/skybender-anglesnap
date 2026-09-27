package com.skybender.anglesnap;

/**
 * Fitted concurrent cannon timing model (Scarpet predict_time / predict_time_range).
 * Times are game ticks from input-sequence start to cannon_stop.
 */
public final class SkybenderTiming {
	private static final int T_PAYLOAD_START = 44;
	private static final int D_TRIM_AFTER_PAYLOAD_START = 26;
	private static final int D_PA_NOM = 30;
	private static final int D_PA_LO = 21;
	private static final int D_PA_HI = 48;
	private static final int D_TRIM_Z_AFTER_X = 124;
	private static final int D_ACCEL_Z_AFTER_X = 184;
	private static final int TAIL_TRIM_WINS = 95;
	private static final int TAIL_ACCEL_WINS = 101;
	private static final int TAIL_ACCEL_WINS_ZERO = 127;

	private static final int INPUT_ACTIVATE_STEP = 33;
	private static final int INPUT_SETTLE = 93;
	private static final int INPUT_DIGIT_STEP = 20;
	private static final int INPUT_DIGIT_COUNT = 11;

	private SkybenderTiming() {
	}

	public record EncodedAxis(int sign, int mid, int high) {
	}

	public static EncodedAxis encodeXz(int v) {
		SkybendEncoder.EncodedCoord c = SkybendEncoder.encodeXz(v);
		return new EncodedAxis(c.sign(), c.mid(), c.high());
	}

	/** Nominal ticks from input-sequence start to cannon_stop. */
	public static int predictTime(int n, int xImpulse, int zImpulse) {
		return inputSequenceDur() + cannonStop(n, xImpulse, zImpulse, D_PA_NOM);
	}

	/** [lo, hi] with dPa = 21 and dPa = 48 (chunkloader phase band). */
	public static int[] predictTimeRange(int n, int xImpulse, int zImpulse) {
		int input = inputSequenceDur();
		return new int[] {
			input + cannonStop(n, xImpulse, zImpulse, D_PA_LO),
			input + cannonStop(n, xImpulse, zImpulse, D_PA_HI)
		};
	}

	static int inputSequenceDur() {
		return 2 * INPUT_ACTIVATE_STEP
			+ INPUT_SETTLE
			+ (INPUT_DIGIT_COUNT - 1) * INPUT_DIGIT_STEP
			+ 10;
	}

	static int payloadDur(int n) {
		return 6 * n * (n + 1) / 2 + 16 * n + 167;
	}

	static int trimDur(int mid) {
		int floor4 = (mid / 4) * 4;
		int fine = mid & 3;
		return floor4 + 6 * (3 - fine) + (floor4 == 0 ? 40 : 47);
	}

	static int accelDur(int high) {
		return high == 0 ? 0 : 32 * high + 8;
	}

	static int cannonStop(int n, int x, int z, int dPa) {
		EncodedAxis X = encodeXz(x);
		EncodedAxis Z = encodeXz(z);

		int payloadStart = T_PAYLOAD_START;
		int payloadStop = payloadStart + payloadDur(n);
		int trimXStop = payloadStart + D_TRIM_AFTER_PAYLOAD_START + trimDur(X.mid());
		int accelXStop = payloadStop + dPa + accelDur(X.high());
		int xDone = Math.max(trimXStop, accelXStop);

		int trimZStop = xDone + D_TRIM_Z_AFTER_X + trimDur(Z.mid());
		int accelZStop = xDone + D_ACCEL_Z_AFTER_X + accelDur(Z.high());
		int zDone = Math.max(trimZStop, accelZStop);

		int tail;
		if (trimZStop >= accelZStop) {
			tail = TAIL_TRIM_WINS;
		} else if (Z.high() == 0) {
			tail = TAIL_ACCEL_WINS_ZERO;
		} else {
			tail = TAIL_ACCEL_WINS;
		}
		return zDone + tail;
	}
}
