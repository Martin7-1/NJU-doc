package com.bigdata.analysis.utils;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.Standardize;

/**
 * 数据预处理工具类
 *
 * @author zhengyi
 */
public class DataPreProcessingUtil {

	private DataPreProcessingUtil() {

	}

	/**
	 * 对数据进行预处理
	 * 这里包括：规范化
	 *
	 * @param rawData 待处理的数据
	 * @return 处理过后的数据
	 */
	public static Instances preProcessingData(Instances rawData) throws Exception {
		// 规范化
		Filter normalize = new Normalize();
		normalize.setDebug(false);
		normalize.setDoNotCheckCapabilities(false);
		normalize.setInputFormat(rawData);

		return Filter.useFilter(rawData, normalize);
	}
}
