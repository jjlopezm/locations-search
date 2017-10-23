package org.geoblink.search.common

/**
 * Utils
 */
object Utils {

  /**
   * Get the sqrt of an integer
   * @param x The number
   * @return [[Int]]
   */
  def sqrt(x: Int): Int = x * x

  /**
   * Print a seq of points as they appears in csv file
   * @param points
   */
  def printResults(points: Seq[Point]): Unit = {
    if (points.length != 0) {
      points foreach {
        case point: SimplePoint =>
          val attrsSorted = point.attrs.toSeq.sortBy(_._1)
          println(s"${point.x},${point.y},${attrsSorted.map(_._2).mkString(",")}")
        case point: PointWithExtra =>
          val attrsSorted = point.attrs.toSeq.sortBy(_._1)
          println(s"${point.x},${point.y},${attrsSorted.map(_._2).mkString(",")},${point.extra}")
        case _ => println("Data Error!")
      }
    }else {
      println("There are no points within the area")
    }
  }
}
