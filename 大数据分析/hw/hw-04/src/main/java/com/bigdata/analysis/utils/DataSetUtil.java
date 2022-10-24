package com.bigdata.analysis.utils;

import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;
import java.io.IOException;

/**
 * @author zhengyi
 */
public class DataSetUtil {

	private static final ArffLoader LOADER = new ArffLoader();

	private DataSetUtil() {

	}

	/**
	 * 根据文件名来加载数据集
	 *
	 * @param fileName 文件名
	 * @return 该数据集对应的 instances 对象
	 */
	public static Instances loadDataSet(String fileName) throws IOException {
		LOADER.setSource(new File(fileName));
		return LOADER.getDataSet();
	}
}
