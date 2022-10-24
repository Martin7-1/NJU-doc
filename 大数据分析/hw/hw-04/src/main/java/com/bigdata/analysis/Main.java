package com.bigdata.analysis;

import com.bigdata.analysis.service.ClassificationService;
import com.bigdata.analysis.service.impl.NaiveBayesClassificationServiceImpl;

/**
 * 程序入口
 *
 * @author zhengyi
 */
public class Main {

	public static final String FILE_NAME = "adult_income_uk.arff";

	public static void main(String[] args) throws Exception {
		ClassificationService classification = new NaiveBayesClassificationServiceImpl();
		classification.setTrainSetAndTestSet(FILE_NAME);
		classification.trainModel();

		System.out.println(classification.getTestResult());
	}
}
