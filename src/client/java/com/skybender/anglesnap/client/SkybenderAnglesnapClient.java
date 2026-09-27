package com.skybender.anglesnap.client;

import com.skybender.anglesnap.SkybenderAnglesnap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class SkybenderAnglesnapClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SkybendCommands.register();
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			SequencePlayer.onClientTick(client);
			EtaCountdown.onClientTick(client);
		});
		SkybenderAnglesnap.LOGGER.info("Skybender AngleSnap client ready (/skybend set|time|fire)");
	}
}
