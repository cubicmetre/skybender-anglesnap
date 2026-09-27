package com.skybender.anglesnap;

/**
 * Ports Scarpet skybender_test.sc encode/pack/hex pipeline (with x_enc sign fix).
 */
public final class SkybendEncoder {
	public static final double IMPULSE = 0.997666666666 * 0.9;
	public static final int TRIMMER_BASE = 256;

	public static final float[][] ACTIVATE_ANGLES = {
		{90.0f, 32.5f},
		{90.0f, 90.0f}
	};

	/** Index 0–15 → [yaw, pitch] matching Scarpet global_encoder_angles. */
	public static final float[][] ENCODER_ANGLES = {
		{-170.8031f, 7.7902412f},
		{-149.36864f, 8.521353f},
		{-121.29873f, 8.160474f},
		{-99.73335f, 7.841793f},
		{-80.732994f, 7.852333f},
		{-59.123596f, 8.196302f},
		{-30.883183f, 8.196543f},
		{-9.272438f, 7.8528614f},
		{9.729492f, 7.8425293f},
		{31.297073f, 8.161423f},
		{59.370346f, 8.138026f},
		{80.80696f, 7.790963f},
		{99.65579f, 7.78067f},
		{121.05005f, 8.102962f},
		{148.95667f, 8.102729f},
		{170.34962f, 7.780156f}
	};

	private SkybendEncoder() {
	}

	public record EncodedCoord(int sign, int mid, int high) {
	}

	public record EncodeResult(int machineX, int machineZ, int[] hexDigits, String hexString) {
	}

	public static EncodedCoord encodeXz(int v) {
		int a = Math.abs(v);
		int sign = v >= 0 ? 0 : 1;
		if (a == 0) {
			return new EncodedCoord(sign, 255, 0);
		}
		int high = high256(a - 1);
		int mid = encodeMid(a, high);
		return new EncodedCoord(sign, mid, high);
	}

	public static int packCoord(int sign, int mid, int high) {
		return sign | (mid << 1) | (high << 9);
	}

	public static int[] toHexDigits(int value, int count) {
		int[] digits = new int[count];
		for (int i = 0; i < count; i++) {
			digits[i] = (value >> (i * 4)) & 15;
		}
		return digits;
	}

	public static EncodeResult encode(int nukeSize, int originX, int originZ, int targetX, int targetZ) {
		int dx = targetX - originX;
		int dz = targetZ - originZ;
		int machineX = (int) Math.round(dx / IMPULSE);
		int machineZ = (int) Math.round(dz / IMPULSE);

		EncodedCoord xEnc = encodeXz(machineX);
		int xBin = packCoord(xEnc.sign(), xEnc.mid(), xEnc.high());
		int[] xDigits = toHexDigits(xBin, 5);

		EncodedCoord zEnc = encodeXz(machineZ);
		int zBin = packCoord(zEnc.sign(), zEnc.mid(), zEnc.high());
		int[] zDigits = toHexDigits(zBin, 5);

		int[] hex = new int[11];
		hex[0] = nukeSize;
		System.arraycopy(xDigits, 0, hex, 1, 5);
		System.arraycopy(zDigits, 0, hex, 6, 5);

		return new EncodeResult(machineX, machineZ, hex, toHexString(hex));
	}

	public static String toHexString(int[] digits) {
		StringBuilder out = new StringBuilder(digits.length);
		for (int digit : digits) {
			out.append("0123456789ABCDEF".charAt(digit));
		}
		return out.toString();
	}

	private static int high256(int v) {
		return (v / TRIMMER_BASE) & 4095;
	}

	private static int encodeMid(int a, int high) {
		return (high * TRIMMER_BASE - a) & (TRIMMER_BASE - 1);
	}
}
