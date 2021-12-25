package org.osm2world.core.target.common.material;

import static java.awt.Color.*;
import static java.util.Arrays.asList;
import static org.osm2world.core.target.common.material.TextureTestUtil.*;
import static org.osm2world.core.test.TestUtil.assertAlmostEquals;

import java.awt.Color;

import org.junit.Test;
import org.osm2world.core.util.color.LColor;

public class TextureDataTest {

	@Test
	public void testAverageColor_singleColor() {

		for (Color testColor : asList(BLACK, WHITE, GREEN)) {
			TextureData testTexture = drawSingleColorTexture(testColor);
			assertAlmostEquals(testColor, testTexture.getAverageColor().toAWT());
		}

	}

	@Test
	public void testAverageColor_twoColors() {

		TextureData testTexture = drawTestTexture((res, g2d) -> {
			int widthLeft = res.width / 2;
			g2d.setBackground(RED);
			g2d.clearRect(0, 0, widthLeft, res.height);
			g2d.setBackground(BLUE);
			g2d.clearRect(widthLeft, 0, res.width - widthLeft, res.height);
		});

		System.out.println(testTexture.getAverageColor());
		assertAlmostEquals(new LColor(0.5f, 0f, 0.5f).toAWT(), testTexture.getAverageColor().toAWT());

	}

}
