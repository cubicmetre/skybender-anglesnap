package com.skybender.anglesnap.client;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.skybender.anglesnap.SkybendEncoder;
import com.skybender.anglesnap.SkybendSequence;
import com.skybender.anglesnap.SkybenderTiming;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public final class SkybendCommands {
	/** Long look-ray for targeting (vanilla crosshair is only interaction reach). */
	private static final double LOOK_TARGET_RANGE = 512.0;

	private SkybendCommands() {
	}

	public static void register() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			LiteralArgumentBuilder<FabricClientCommandSource> root = literal("skybend")
				.then(literal("set")
					.then(argument("n", IntegerArgumentType.integer(1, 15))
						.then(argument("ox", IntegerArgumentType.integer())
							.then(argument("oz", IntegerArgumentType.integer())
								.executes(SkybendCommands::set)))))
				.then(literal("time")
					.executes(ctx -> timeOrFire(ctx, false, true))
					.then(argument("tx", IntegerArgumentType.integer())
						.then(argument("tz", IntegerArgumentType.integer())
							.executes(ctx -> timeOrFire(ctx, false, false)))))
				.then(literal("fire")
					.executes(ctx -> timeOrFire(ctx, true, true))
					.then(argument("tx", IntegerArgumentType.integer())
						.then(argument("tz", IntegerArgumentType.integer())
							.executes(ctx -> timeOrFire(ctx, true, false)))));

			dispatcher.register(root);
		});
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	private static <T> RequiredArgumentBuilder<FabricClientCommandSource, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}

	private static int set(CommandContext<FabricClientCommandSource> ctx) {
		int n = IntegerArgumentType.getInteger(ctx, "n");
		int ox = IntegerArgumentType.getInteger(ctx, "ox");
		int oz = IntegerArgumentType.getInteger(ctx, "oz");
		SkybendSettings.set(n, ox, oz);
		ctx.getSource().sendFeedback(Component.literal(
			String.format("skybend set n=%d origin=(%d,%d)", n, ox, oz)
		));
		return 1;
	}

	/**
	 * @param fire       true = run sequence + countdown; false = time only
	 * @param useLookRay true = resolve target from look ray; false = use tx/tz args
	 */
	private static int timeOrFire(CommandContext<FabricClientCommandSource> ctx, boolean fire, boolean useLookRay) {
		if (!SkybendSettings.isSet()) {
			ctx.getSource().sendError(Component.literal("No settings — use /skybend set <n> <ox> <oz> first"));
			return 0;
		}

		int tx;
		int tz;
		if (useLookRay) {
			int[] look = resolveLookTarget(ctx);
			if (look == null) {
				return 0;
			}
			tx = look[0];
			tz = look[1];
		} else {
			tx = IntegerArgumentType.getInteger(ctx, "tx");
			tz = IntegerArgumentType.getInteger(ctx, "tz");
		}

		int n = SkybendSettings.nukeSize();
		int ox = SkybendSettings.originX();
		int oz = SkybendSettings.originZ();

		SkybendEncoder.EncodeResult encoded = SkybendEncoder.encode(n, ox, oz, tx, tz);
		int eta = SkybenderTiming.predictTime(n, encoded.machineX(), encoded.machineZ());

		ctx.getSource().sendFeedback(Component.literal(
			"Time Estimate: " + EtaCountdown.formatHms((eta + 19) / 20)
		));

		if (fire) {
			SequencePlayer.start(SkybendSequence.build(encoded.hexDigits()));
			EtaCountdown.start(eta);
		}
		return 1;
	}

	private static int[] resolveLookTarget(CommandContext<FabricClientCommandSource> ctx) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) {
			ctx.getSource().sendError(Component.literal("No local player"));
			return null;
		}

		HitResult hit = player.pick(LOOK_TARGET_RANGE, 1.0f, false);
		if (!(hit instanceof BlockHitResult blockHit) || hit.getType() != HitResult.Type.BLOCK) {
			ctx.getSource().sendError(Component.literal(
				"No block along look ray (within " + (int) LOOK_TARGET_RANGE + " blocks) — aim at terrain or pass tx tz"
			));
			return null;
		}

		BlockPos pos = blockHit.getBlockPos();
		return new int[] {pos.getX(), pos.getZ()};
	}
}
