package com.zyinnju.bigdata

import org.apache.spark.{SparkConf, SparkContext}

object SparkDemo {

	def main(args: Array[String]): Unit = {
		val conf = new SparkConf()
			.setAppName("First Spark Demo")
			.setMaster("local[1]")

		new SparkContext(conf)
			.parallelize(List(1, 2, 3, 4, 5, 6))
			.map(x => x * x)
			.filter(_ >= 10)
			.collect()
			.foreach(println)
	}
}
