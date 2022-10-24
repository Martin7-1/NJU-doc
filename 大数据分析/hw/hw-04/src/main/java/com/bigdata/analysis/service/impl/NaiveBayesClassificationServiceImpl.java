package com.bigdata.analysis.service.impl;

import com.bigdata.analysis.service.ClassificationService;
import com.bigdata.analysis.utils.DataPreProcessingUtil;
import com.bigdata.analysis.utils.DataSetUtil;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

import java.util.Random;

/**
 * @author zhengyi
 */
public class NaiveBayesClassificationServiceImpl implements ClassificationService {

	/**
	 * 训练数据集
	 */
	private Instances trainInstances;
	/**
	 * 测试数据集
	 */
	private Instances testInstances;
	/**
	 * 朴素贝叶斯分类
	 */
	private final NaiveBayes naiveBayes;

	public NaiveBayesClassificationServiceImpl() {
		naiveBayes = new NaiveBayes();
	}

	@Override
	public void setTrainSetAndTestSet(String fileName) throws Exception {
		Instances instances = DataSetUtil.loadDataSet(fileName);
		instances = DataPreProcessingUtil.preProcessingData(instances);
		// 将数据进行一个随机化处理
		instances.randomize(new Random(0));
		// 80% 训练集，20% 测试集
		int trainingDataSize = (int) Math.round(instances.numInstances() * 0.8);
		int testDataSize = instances.numInstances() - trainingDataSize;

		// Create training data
		trainInstances = new Instances(instances, 0, trainingDataSize);
		// Create test data
		testInstances = new Instances(instances, trainingDataSize, testDataSize);

		// 设置类所在的 index，最后一个 attribute >= 50k 或者 < 50k 是类别
		trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
		testInstances.setClassIndex(testInstances.numAttributes() - 1);
	}

	@Override
	public void trainModel() throws Exception {
		// Build Classifier
		naiveBayes.buildClassifier(trainInstances);
	}

	@Override
	public String getTestResult() throws Exception {
		// Evaluation
		Evaluation evaluation = new Evaluation(trainInstances);
		evaluation.evaluateModel(naiveBayes, testInstances);
		return evaluation.toSummaryString("\nResults", false);
	}
}
