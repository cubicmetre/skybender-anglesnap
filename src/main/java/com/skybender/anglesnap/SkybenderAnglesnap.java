package com.skybender.anglesnap;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkybenderAnglesnap implements ModInitializer {
	public static final String MOD_ID = "skybender-anglesnap";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Skybender AngleSnap initialized");
	}
}
