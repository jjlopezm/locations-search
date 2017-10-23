package org.geoblink.search

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.geoblink.search.console.SearchConsole

object Main {

  def main(args: Array[String]): Unit = {

    println("Hello Geoblink")

    val sparkConf = new SparkConf()
      .setAppName("GeoblinkTest")
      .setMaster("local[2]")


    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    sparkSession.sparkContext.setLogLevel("ERROR")

    implicit val locations = sparkSession.sqlContext
      .read
      .format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load("src/main/resources/bigcsv.csv")

    SearchConsole.startConsole
  }
}
