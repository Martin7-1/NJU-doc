package com.zyinnju.utils.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Zyi
 */
public class BMPFilter extends FileFilter {

	private static final String POSTFIX = ".bmp";

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		return f.getName().endsWith(POSTFIX);
	}

	@Override
	public String getDescription() {
		return POSTFIX;
	}
}
