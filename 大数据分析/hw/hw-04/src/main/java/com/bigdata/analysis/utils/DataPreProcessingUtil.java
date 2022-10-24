package com.bigdata.analysis.utils;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
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
	 * 这里包括：缺失值处理、标准化、离散化
	 *
	 * @param rawData 待处理的数据
	 * @return 处理过后的数据
	 */
	public static Instances preProcessingData(Instances rawData) throws Exception {
		Filter replaceMissingValues = new ReplaceMissingValues();
		replaceMissingValues.setDebug(false);
		replaceMissingValues.setDoNotCheckCapabilities(false);
		replaceMissingValues.setInputFormat(rawData);
		Instances instances = Filter.useFilter(rawData, replaceMissingValues);

		// 标准化处理
		Filter standardize = new Standardize();
		standardize.setDebug(false);
		standardize.setDoNotCheckCapabilities(false);
		standardize.setInputFormat(instances);
		instances = Filter.useFilter(instances, standardize);

		// 离散化处理
		Filter discretize = new Discretize();
		discretize.setDebug(false);
		discretize.setDoNotCheckCapabilities(false);
		discretize.setInputFormat(instances);

		return Filter.useFilter(instances, discretize);
	}
}
