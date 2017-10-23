package org.geoblink.search.inCircle

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.scalatest.{FlatSpecLike, Matchers}

class SearchLocationTest extends Matchers with FlatSpecLike {

  trait WithArea {
    val sparkConf = new SparkConf()
      .setAppName("GeoblinkTest")
      .setMaster("local[1]")

    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    val locations = sparkSession.sqlContext.emptyDataFrame
    val searchInCircle = new SearchInCircle(123456, 234432, 5500, locations)
  }

  "SearchLocation" should "reject outer point of circle" in new WithArea {
    searchInCircle.in_circle(18412, 1805) should be(false)
  }

  it should "valid valid point into circle" in new WithArea {
    searchInCircle.in_circle(123456, 234341) should be(true)
  }
}
