package com.skybender.anglesnap;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the timed look schedule from Scarpet input_sequence_player.
 */
public final class SkybendSequence {
	public record AngleStep(int tick, float yaw, float pitch) {
	}

	private SkybendSequence() {
	}

	public static List<AngleStep> build(int[] hexDigits) {
		List<AngleStep> steps = new ArrayList<>();
		int i = 0;

		steps.add(new AngleStep(i, SkybendEncoder.ACTIVATE_ANGLES[0][0], SkybendEncoder.ACTIVATE_ANGLES[0][1]));
		i += 33;
		steps.add(new AngleStep(i, SkybendEncoder.ACTIVATE_ANGLES[1][0], SkybendEncoder.ACTIVATE_ANGLES[1][1]));
		i += 33;
		steps.add(new AngleStep(i, 0.0f, -90.0f));
		i += 93;

		for (int digit : hexDigits) {
			float[] angle = SkybendEncoder.ENCODER_ANGLES[digit];
			steps.add(new AngleStep(i, angle[0], angle[1]));
			i += 10;
			steps.add(new AngleStep(i, angle[0], -90.0f));
			i += 10;
		}

		return steps;
	}
}
