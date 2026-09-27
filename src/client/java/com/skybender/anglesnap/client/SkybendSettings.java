package com.skybender.anglesnap.client;

/**
 * Passiveively stored payload settings from /skybend set.
 */
public final class SkybendSettings {
	private static boolean set;
	private static int nukeSize;
	private static int originX;
	private static int originZ;

	private SkybendSettings() {
	}

	public static void set(int nukeSize, int originX, int originZ) {
		SkybendSettings.nukeSize = nukeSize;
		SkybendSettings.originX = originX;
		SkybendSettings.originZ = originZ;
		set = true;
	}

	public static boolean isSet() {
		return set;
	}

	public static int nukeSize() {
		return nukeSize;
	}

	public static int originX() {
		return originX;
	}

	public static int originZ() {
		return originZ;
	}
}
