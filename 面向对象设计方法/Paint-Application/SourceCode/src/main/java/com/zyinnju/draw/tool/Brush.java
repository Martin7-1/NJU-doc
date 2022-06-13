package com.zyinnju.draw.tool;

import java.awt.*;
import java.util.Random;

/**
 * 笔刷类
 *
 * @author Zyi
 */
public class Brush extends AbstractPaintTool {

	private final int[] fx = new int[100];
	private final int[] fy = new int[100];

	public Brush() {
		Random random = new Random();
		for (int i = 0; i < fx.length; i++) {
			fx[i] = random.nextInt(16) - 16;
			fy[i] = random.nextInt(16) - 16;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setPaint(color);
		g.setStroke(new BasicStroke(0, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		for (int i = 0; i < fx.length; i++) {
			double d = fx[i];
			double c = fy[i];
			g.drawLine((int) (startPoint.getX() + d * Math.sin(d)), (int) (startPoint.getY() + c * Math.sin(c)), (int) (endPoint.getX() + d * Math.sin(d)),
				(int) (startPoint.getY() + c * Math.sin(c)));
		}
	}
}
