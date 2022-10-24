package com.bigdata.analysis;

import com.bigdata.analysis.utils.FileUtil;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Instances;

import java.util.Arrays;

/**
 * @author zhengyi
 */
public class PCADimensionReduce extends DimensionReduceService {

	public PCADimensionReduce(String fileName) throws Exception {
		super(fileName);
	}

	@Override
	public void reduceDimension() throws Exception {
		// 预处理数据
		instances.setClassIndex(instances.numAttributes() - 1);
		Instances postProcessingInstances = preProcessing(instances);
		PrincipalComponents components = new PrincipalComponents();
		components.buildEvaluator(postProcessingInstances);
		System.out.println(components);

		AttributeSelection attributeSelection = new AttributeSelection();
		Ranker ranker = new Ranker();
		ranker.setNumToSelect(2);
		attributeSelection.setEvaluator(components);
		attributeSelection.setSearch(ranker);
		attributeSelection.SelectAttributes(postProcessingInstances);
		attributeSelection.setRanking(true);

		Instances result = attributeSelection.reduceDimensionality(postProcessingInstances);
		System.out.println("====================== ranked attributes ====================");
		double[][] rankedAttributes = attributeSelection.rankedAttributes();
		for (double[] attribute : rankedAttributes) {
			System.out.println(Arrays.toString(attribute));
		}
		System.out.println("====================== select attribute  ====================");
		System.out.println(Arrays.toString(attributeSelection.selectedAttributes()));
		System.out.println("======================       result      ====================");
		FileUtil.inputToFile(result);
	}
}
