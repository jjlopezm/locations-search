package org.geoblink.search.inCircle

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row}
import org.geoblink.search.common.{PointWithExtra, SimplePoint, Utils}

/**
 *
 * @param x Coordinate x
 * @param y Coordinate y
 * @param radius Radius
 * @param locations All points stored
 */
case class SearchInCircle(x: Int, y: Int, radius: Int, locations: DataFrame) {

  /**
   * Check if a point is in a circle radius
   * @param locX Coordinate x of a queried point
   * @param locY Coordinate y of a queried point
   * @return [[Boolean]]
   */
  def in_circle(locX: Int, locY: Int): Boolean = {
    if (x - locX >= 0 && y - locY > 0) {
      val dist = math.sqrt(Utils.sqrt(x - locX) + Utils.sqrt(y - locY))
      dist <= radius
    } else {
      false
    }
  }

  /**
   * Search in a distributed way using spark the points within the area
   * @return [[RDD[Row]]
   */
  def getPointsInArea(): RDD[Row] = {
    locations.rdd mapPartitions { location =>
      location filter { loc =>
        in_circle(loc.getInt(0), loc.getInt(1))
      }
    }
  }

  /**
   * Add an amount to every point inside an area
   * @param pointsInArea Points in the area
   * @param attributeNumber Number of attribute to increment
   * @param amount The amount
   * @return [[SimplePoint]]
   */
  def addAmountInRadius(pointsInArea: RDD[Row], attributeNumber:Int, amount: Int): Seq[SimplePoint] = {

    import org.geoblink.search.inCircle.spark.RowFunctions
    val pointsIncremented = pointsInArea mapPartitions { points =>
      points map { point =>
        val validPoint = SimplePoint(point.getInt(0), point.getInt(1), point.toMapAttributes)
        validPoint.sumAmountToAttr(amount, attributeNumber)
      }
    }
    pointsIncremented.collect().toSeq
  }

  /**
   * Calculates the addition of all attributes
   * @param pointsInArea Points within the area
   * @return [[PointWithExtra]]
   */
  def getAddOfAttributes(pointsInArea: RDD[Row]): Seq[PointWithExtra] = {

    import org.geoblink.search.inCircle.spark.RowFunctions
    val pointsWithAdd = pointsInArea mapPartitions { points =>
      points map { point =>
        val validPoint = PointWithExtra(point.getInt(0), point.getInt(1), point.toMapAttributes, 0)
        validPoint.add
      }
    }
    pointsWithAdd.collect().toSeq
  }

  /**
   * Calculates the average of all attributes
   * @param pointsInArea Points within the area
   * @return [[PointWithExtra]]
   */
  def getAverageOfAttributes(pointsInArea: RDD[Row]): Seq[PointWithExtra] = {

    import org.geoblink.search.inCircle.spark.RowFunctions
    val pointsWithAverage = pointsInArea mapPartitions { points =>
      points map { point =>
        val validPoint = PointWithExtra(point.getInt(0), point.getInt(1), point.toMapAttributes, 0)
        validPoint.average
      }
    }
    pointsWithAverage.collect().toSeq
  }

  /**
   * Calculates the median of all attributes
   * @param pointsInArea Points within the area
   * @return [[PointWithExtra]]
   */
  def getMedian(pointsInArea: RDD[Row]): Seq[PointWithExtra] = {
    import org.geoblink.search.inCircle.spark.RowFunctions
    val pointsWithMedian = pointsInArea mapPartitions { points =>
      points map { point =>
        val validPoint = PointWithExtra(point.getInt(0), point.getInt(1), point.toMapAttributes, 0)
        validPoint.median
      }
    }
    pointsWithMedian.collect().toSeq
  }
}
