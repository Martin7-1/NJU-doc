package com.bigdata.analysis;

import com.bigdata.analysis.utils.DataSetUtil;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;

/**
 * PCA 降维
 *
 * @author zhengyi
 */
public abstract class DimensionReduceService {

	protected final String fileName;
	protected final Instances instances;

	protected DimensionReduceService(String fileName) throws Exception {
		this.fileName = fileName;
		instances = DataSetUtil.loadDataSet(fileName);
	}

	/**
	 * 降维
	 *
	 * @throws Exception 异常
	 */
	public abstract void reduceDimension() throws Exception;

	/**
	 * 数据预处理，这里只采用标准化处理
	 *
	 * @param instances 要处理的数据
	 * @return 处理后的数据
	 * @throws Exception 抛出的异常
	 */
	protected Instances preProcessing(Instances instances) throws Exception {
		// 标准化处理
		Standardize standardize = new Standardize();
		standardize.setDebug(false);
		standardize.setIgnoreClass(false);
		standardize.setDoNotCheckCapabilities(false);
		standardize.setInputFormat(instances);

		return Filter.useFilter(instances, standardize);
	}
}
