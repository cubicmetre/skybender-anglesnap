package com.skybender.anglesnap.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

/**
 * Action-bar ETA countdown (hh:mm:ss), refreshed once per second.
 */
public final class EtaCountdown {
	private static boolean active;
	private static int startTick;
	private static int totalTicks;
	private static int lastDisplayedSeconds = -1;

	private EtaCountdown() {
	}

	public static void start(int totalTicks) {
		LocalPlayer player = Minecraft.getInstance().player;
		EtaCountdown.totalTicks = Math.max(0, totalTicks);
		startTick = player != null ? player.tickCount : 0;
		active = true;
		lastDisplayedSeconds = -1;
		onClientTick(Minecraft.getInstance());
	}

	public static void cancel() {
		if (active) {
			active = false;
			lastDisplayedSeconds = -1;
			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null) {
				OverlayMessage.show(player, Component.empty());
			}
		}
	}

	public static boolean isActive() {
		return active;
	}

	public static void onClientTick(Minecraft client) {
		if (!active) {
			return;
		}
		LocalPlayer player = client.player;
		if (player == null) {
			cancel();
			return;
		}

		int elapsed = player.tickCount - startTick;
		int remaining = totalTicks - elapsed;
		if (remaining <= 0) {
			if (lastDisplayedSeconds != 0) {
				OverlayMessage.show(player, Component.literal("ETA 00:00:00"));
				lastDisplayedSeconds = 0;
			}
			active = false;
			return;
		}

		int seconds = (remaining + 19) / 20; // ceil(remaining / 20)
		if (seconds != lastDisplayedSeconds) {
			lastDisplayedSeconds = seconds;
			OverlayMessage.show(player, Component.literal("ETA " + formatHms(seconds)));
		}
	}

	static String formatHms(int totalSeconds) {
		int h = totalSeconds / 3600;
		int m = (totalSeconds % 3600) / 60;
		int s = totalSeconds % 60;
		return String.format("%02d:%02d:%02d", h, m, s);
	}
}
