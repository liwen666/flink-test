package jrx.data.hub.flink.scala

import java.text.SimpleDateFormat

import org.apache.flink.table.functions.ScalarFunction

class ReformatDateADD() extends ScalarFunction {
  def eval(dateStr: String, add: Int): String = {
    val newDateStr = new SimpleDateFormat("yyyyMMdd").format(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr).getTime + add * 3600 * 1000 * 24)
    newDateStr
  }

}