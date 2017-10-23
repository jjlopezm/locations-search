package org.geoblink.search.common

import org.scalatest.{FlatSpecLike, Matchers}

class pointTest extends Matchers with FlatSpecLike {

  trait WithPoint {

    val simpleAttrs = Map(1 -> 123, 2 -> 234, 3 -> 345)
    val simpleAttrsIncr = Map(1 -> 123, 2 -> 244, 3 -> 345)
    val point = new SimplePoint(1, 2, simpleAttrs)
    val pointWithExtra = new PointWithExtra(1, 2, simpleAttrs, 0)
    val pointWithExtraAvg = new PointWithExtra(1, 2, simpleAttrs, 234)
    val pointWithExtraMed = new PointWithExtra(1, 2, simpleAttrs, 234)
    val pointWithExtraAdd = new PointWithExtra(1, 2, simpleAttrs, 702)
  }

  "Point" should "contains a coordinates and attributes" in new WithPoint {
    point.x should be(1)
    point.y should be(2)
    point.attrs.size should be(3)
  }

  it should "have an attribute with an amount increased" in new WithPoint {
    point.sumAmountToAttr(10, 2).attrs should be(simpleAttrsIncr)
  }

  it should "have an extra attribute with median" in new WithPoint {
    pointWithExtra.median.extra should be(pointWithExtraMed.extra)
  }

  it should "have an extra attribute with average" in new WithPoint {
    pointWithExtra.average.extra should be(pointWithExtraMed.extra)
  }

  it should "have an extra attribute with the addition of all attributes" in new WithPoint {
    pointWithExtra.add.extra should be(pointWithExtraAdd.extra)
  }
}
