package com.bigdata.analysis.service.impl;

import com.bigdata.analysis.service.ClusterService;
import com.bigdata.analysis.utils.DataPreProcessingUtil;
import com.bigdata.analysis.utils.DataSetUtil;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

/**
 * @author zhengyi
 */
public class ClusterServiceImpl implements ClusterService {

	private Instances data;
	private final SimpleKMeans simpleKMeans;
	private int clusterNum;

	public ClusterServiceImpl() {
		simpleKMeans = new SimpleKMeans();
	}

	@Override
	public void setTrainSetAndTestSet(String fileName) throws Exception {
		Instances instances = DataSetUtil.loadDataSet(fileName);
		data = DataPreProcessingUtil.preProcessingData(instances);
	}

	@Override
	public void setNumClusters(int num) throws Exception {
		clusterNum = num;
		simpleKMeans.setNumClusters(clusterNum);
	}

	@Override
	public void trainModel() throws Exception {
		// Build Classifier
		simpleKMeans.buildClusterer(data);
	}

	@Override
	public void getTestResult() {
		// 结果
		Instances centroids = simpleKMeans.getClusterCentroids();
		for (int i = 0; i < clusterNum; i++) {
			System.out.print("Cluster " + i + " size: " + simpleKMeans.getClusterSizes()[i]);
			System.out.println(" Centroid: " + centroids.instance(i));
		}
	}
}
