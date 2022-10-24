package com.bigdata.analysis.service;

import java.io.IOException;

/**
 * 分类
 *
 * @author zhengyi
 */
public interface ClassificationService {

	/**
	 * 设置训练集和测试集
	 *
	 * @param fileName 数据集文件名字
	 * @throws Exception any failed, throw exception
	 */
	void setTrainSetAndTestSet(String fileName) throws Exception;

	/**
	 * 熟练模型
	 *
	 * @throws Exception any failed, throw exception
	 */
	void trainModel() throws Exception;

	/**
	 * 获得测试集的准确率
	 *
	 * @return 测试集准确率
	 * @throws Exception any failed, throw exception
	 */
	String getTestResult() throws Exception;
}
