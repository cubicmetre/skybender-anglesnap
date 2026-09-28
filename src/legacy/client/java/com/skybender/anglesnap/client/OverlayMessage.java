package com.skybender.anglesnap.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

/** Minecraft 1.21.11 action-bar API. */
final class OverlayMessage {
	private OverlayMessage() {
	}

	static void show(LocalPlayer player, Component message) {
		player.displayClientMessage(message, true);
	}
}
