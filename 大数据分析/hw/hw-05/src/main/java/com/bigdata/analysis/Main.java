package com.bigdata.analysis;

import com.bigdata.analysis.service.ClusterService;
import com.bigdata.analysis.service.impl.ClusterServiceImpl;

/**
 * @author zhengyi
 */
public class Main {

	/**
	 * 文件名
	 */
	private static final String FILE_NAME = "iris.arff";

	public static void main(String[] args) throws Exception {
		ClusterService clusterService = new ClusterServiceImpl();
		clusterService.setTrainSetAndTestSet(FILE_NAME);
		clusterService.setNumClusters(3);
		clusterService.trainModel();
		clusterService.getTestResult();
	}
}
