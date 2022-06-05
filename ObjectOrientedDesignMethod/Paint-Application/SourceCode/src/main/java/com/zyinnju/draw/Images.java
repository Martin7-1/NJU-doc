package com.zyinnju.draw;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Zyi
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@SuperBuilder
public class Images extends AbstractContent {

	/**
	 * 绘制内容所保存 or 打开的图片
	 */
	protected BufferedImage image;
	/**
	 * 绘画的面板
	 */
	protected JPanel board;

	public Images(BufferedImage image, JPanel board) {
		this.image = image;
		this.board = board;
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(image, 0, 0, board);
	}
}
