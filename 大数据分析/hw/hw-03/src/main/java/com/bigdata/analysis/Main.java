package com.bigdata.analysis;

/**
 * @author zhengyi
 */
public class Main {

	private static final String FILE_NAME = "titanic.arff";

	public static void main(String[] args) throws Exception {
		DimensionReduceService dimensionReduce = new PCADimensionReduce(FILE_NAME);
		dimensionReduce.reduceDimension();
	}
}
