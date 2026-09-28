package com.skybender.anglesnap.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

/** Minecraft 26.1+ action-bar API. */
final class OverlayMessage {
	private OverlayMessage() {
	}

	static void show(LocalPlayer player, Component message) {
		player.sendOverlayMessage(message);
	}
}
