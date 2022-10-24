package com.zyinnju.bigdata

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {

	def main(args: Array[String]): Unit = {
		val conf = new SparkConf()
			.setAppName("Word Count in Big Data Analysis")
			.setMaster("local[1]")

		val context = new SparkContext(conf)
		// 读取数据文件
		context.textFile("./test.txt")
			// 扁平化分词
			.flatMap(_.split(" "))
			// 排除空字符串和空括号
			.filter(x => x != "" && x != "()")
			// 不区分大小写
			.map(x => x.toLowerCase)
			// 去掉结尾句号，逗号，括号
			.map(x => removeStartAndEnd(x))
			// 标注单词次数为 1
			.map((_, 1))
			// 对相同的单词进行次数相加
			// 计算出现频率
			.reduceByKey(_ + _)
			// 排序
			.sortBy(_._2)
			.collect()
			// 输出
			.foreach(x => println(x))

		context.stop()
	}

	def removeStartAndEnd(str: String): String = {
		var result: String = ""
		if (str.endsWith(",") || str.endsWith(".") || str.endsWith(")") || str.endsWith("-")) {
			result = str.substring(0, str.length - 1)
		}

		if (result.startsWith("(")) {
			result = result.substring(1, result.length)
		}

		if (result == "") str else result
	}

}
