package com.zyinnju.utils.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Zyi
 */
public class GIFFilter extends FileFilter {

	private static final String POSTFIX = ".gif";

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
