package org.geoblink.search.common

/**
 * Class Point
 */
sealed class Point

/**
 * Point as it appears in csv
 * @param x Coordinate x
 * @param y Coordinate y
 * @param attrs Attributes
 */
case class SimplePoint(x: Int, y: Int, attrs: Map[Int, Int]) extends Point {
  def sumAmountToAttr(amount: Int, attrNumber: Int): SimplePoint = {
    val attrsModified = attrs map { attr =>
      if (attr._1 == attrNumber) {
        (attr._1, attr._2 + amount)
      } else {
        attr
      }
    }
    SimplePoint(x, y, attrsModified)
  }
}

/**
 * Point with an extra attribute that can be avg, add, or median
 * @param x Coordinate x
 * @param y Coordinate y
 * @param attrs Attributes
 * @param extra extra attribute
 */
case class PointWithExtra(x: Int, y: Int, attrs: Map[Int, Int], extra:Int) extends Point{

  /**
   * Add all attribute values and it be placed in a new attribute
   * @return [[PointWithExtra]]
   */
  def add():PointWithExtra = {
    val added =attrs.foldLeft(0)(_+_._2)
    PointWithExtra(x,y,attrs,added)
  }

  /**
   * Average of attribute values and it be placed in a new attribute
   * @return [[PointWithExtra]]
   */
  def average():PointWithExtra = {
    val added = attrs.foldLeft(0)(_+_._2)
    val avg  = added/attrs.size
    PointWithExtra(x,y,attrs,avg)
  }

  /**
   * Median of attribute values and it be placed in a new attribute
   * @return [[PointWithExtra]]
   */
  def median():PointWithExtra = {
    val attrValues = attrs.values.toList.sorted
    val median = if (attrValues.length % 2 == 0 ) {
      val med1 = attrValues((attrValues.length/2) - 1)
      val med2 = attrValues(attrValues.length/2)
      (med1 + med2)/2
    } else {
      attrValues((attrValues.length/2))
    }
    PointWithExtra(x, y, attrs, median)
  }
}

