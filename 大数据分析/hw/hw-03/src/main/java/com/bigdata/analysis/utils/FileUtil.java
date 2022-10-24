package com.bigdata.analysis.utils;

import weka.core.Instances;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author zhengyi
 */
public class FileUtil {

	public static final String OUTPUT_FILE_NAME = "result.arff";

	private FileUtil() {

	}

	/**
	 * 将结果写入文件
	 *
	 * @param instances 结果数据
	 * @throws Exception 抛出的异常
	 */
	public static void inputToFile(Instances instances) {
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME))) {
			bufferedWriter.write(instances.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
