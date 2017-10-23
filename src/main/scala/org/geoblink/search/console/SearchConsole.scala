package org.geoblink.search.console

import scala.util.control.NonFatal

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row}
import org.geoblink.search.common.Utils
import org.geoblink.search.inCircle.SearchInCircle

/**
 * Console to interact with the software
 */
object SearchConsole {

  /**
   * Control Argument
   */
  val BAD_ARGUMENT = -1

  /**
   * The console
   * @param locations All points
   */
  def startConsole(implicit locations: DataFrame) {

    while (true) {
      val (searchInCircle: SearchInCircle, pointsInAreaRDD: RDD[Row]) = getPointsInArea(locations)
      getOperation(searchInCircle, pointsInAreaRDD)
    }
  }

  private def getPointsInArea(locations: DataFrame): (SearchInCircle, RDD[Row]) = {
    val (x, y, radius) = getPoints()
    val searchInCircle =
      new SearchInCircle(x.toInt, y.toInt, radius.toInt, locations)
    val pointsInAreaRDD = searchInCircle.getPointsInArea()
    (searchInCircle, pointsInAreaRDD)
  }

  private def getAttributeNumber(): Int = {
    try {
      println("Insert the attribute number to increase (1-15)")
      print("> ")
      val attributeNumber = scala.io.StdIn.readLine().toInt
      if (attributeNumber < 1 || attributeNumber > 15) {
        println("Attribute number must be an integer between 1 and 15")
        BAD_ARGUMENT
      } else {
        attributeNumber
      }
    } catch {
      case NonFatal(e) =>
        println("Attribute number must be an integer between 1 and 15")
        BAD_ARGUMENT
    }
  }

  private def getAmount(): Int = {
    println("Insert the amount")
    print("> ")
    try {
      scala.io.StdIn.readLine().toInt
    } catch {
      case NonFatal(e) =>
        println("amount must be an integer number")
        BAD_ARGUMENT
    }
  }

  private def getPoints(): (Int, Int, Int) = {
    var badPoint = 0
    var (x, y, radius) = (0, 0, 0)
    do {
      try {
        println("Please insert x coordinate")
        print("> ")
        x = scala.io.StdIn.readLine().toInt
        println("Please insert y coordinate")
        print("> ")
        y = scala.io.StdIn.readLine().toInt
        println("Please insert radius:")
        print("> ")
        radius = scala.io.StdIn.readLine().toInt
        badPoint = 0

      } catch {
        case NonFatal(e) =>
          println("Bad Point. x, y and radius must be an integer number")
          badPoint = 1
      }
    } while (badPoint != 0)
    (x, y, radius)
  }

  private def getOperation(searchInCircle: SearchInCircle, pointsInAreaRDD: RDD[Row])
                          (implicit locations: DataFrame): Unit = {

    println("""
     Which operation do you want to execute:
     ICR -> Increase an attribute, ADD --> Add all attributes, AVG -> Average of attributes,
     MED --> median of all attributes, POINT --> to reinsert the point and radius of reference
     (EXIT to finish)""")

    print("> ")
    val operation = scala.io.StdIn.readLine().toLowerCase()

    operation match {
      case "icr" =>
        var attributeNumber = getAttributeNumber()
        while (attributeNumber == BAD_ARGUMENT) {
          attributeNumber = getAttributeNumber()
        }

        var amount = getAmount()
        while (amount == BAD_ARGUMENT) {
          amount = getAmount()
        }

        val increasedPoints =
          searchInCircle.addAmountInRadius(pointsInAreaRDD, attributeNumber, amount)
        Utils.printResults(increasedPoints)
        getOperation(searchInCircle, pointsInAreaRDD)
      case "add" =>
        val addAttributes = searchInCircle.getAddOfAttributes(pointsInAreaRDD)
        Utils.printResults(addAttributes)
        getOperation(searchInCircle, pointsInAreaRDD)
      case "avg" =>
        val avgAttributes = searchInCircle.getAverageOfAttributes(pointsInAreaRDD)
        Utils.printResults(avgAttributes)
        getOperation(searchInCircle, pointsInAreaRDD)
      case "med" =>
        val medianAttribytes = searchInCircle.getMedian(pointsInAreaRDD)
        Utils.printResults(medianAttribytes)
        getOperation(searchInCircle, pointsInAreaRDD)
      case "point" =>
      case "exit" =>
        println("Bye!")
        locations.sparkSession.close()
        System.exit(0)
      case _ =>
        println("Operation not allowed")
        getOperation(searchInCircle, pointsInAreaRDD)
    }
  }
}
