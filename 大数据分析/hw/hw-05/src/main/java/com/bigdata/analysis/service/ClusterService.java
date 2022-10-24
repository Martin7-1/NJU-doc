package com.bigdata.analysis.service;

/**
 * @author zhengyi
 */
public interface ClusterService {

	/**
	 * 设置训练集和测试集
	 *
	 * @param fileName 数据集文件名字
	 * @throws Exception any failed, throw exception
	 */
	void setTrainSetAndTestSet(String fileName) throws Exception;

	/**
	 * 聚类的类数
	 *
	 * @param num 聚类的类数
	 * @throws Exception any failed, throw exception
	 */
	void setNumClusters(int num) throws Exception;

	/**
	 * 训练模型
	 *
	 * @throws Exception any failed, throw exception
	 */
	void trainModel() throws Exception;

	/**
	 * 获得测试集的准确率
	 */
	void getTestResult();
}
