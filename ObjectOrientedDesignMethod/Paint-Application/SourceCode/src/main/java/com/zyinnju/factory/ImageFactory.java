package com.zyinnju.factory;

import com.zyinnju.draw.Images;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * @author Zyi
 */
public class ImageFactory extends ContentFactory {

	/**
	 * 图像
	 */
	private final BufferedImage image;
	/**
	 * 画布
	 */
	private final JPanel board;

	public ImageFactory(BufferedImage image, JPanel board) {
		this.image = image;
		this.board = board;
	}

	public Images createImage() {
		return new Images(image, board);
	}
}
