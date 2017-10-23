package org.geoblink.search.inCircle

import org.apache.spark.sql.Row

/**
 * DSL to give more verbosity to attributes
 */
package object spark {

  implicit class RowFunctions(val row: Row) {
    def toMapAttributes: Map[Int, Int] = {
      Map(1->row.getInt(2),
        2->row.getInt(3),
        3->row.getInt(4),
        4->row.getInt(5),
        5->row.getInt(6),
        6->row.getInt(7),
        7->row.getInt(8),
        8->row.getInt(9),
        9->row.getInt(10),
        10->row.getInt(11),
        11->row.getInt(12),
        12->row.getInt(13),
        13->row.getInt(14),
        14->row.getInt(15),
        15->row.getInt(16)
      )
    }
  }
}
