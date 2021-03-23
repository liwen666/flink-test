package jrx.data.hub.flink.scala

import java.text.SimpleDateFormat

import org.apache.flink.table.functions.ScalarFunction

class ReDate() extends ScalarFunction {
  def eval(str: String): String = {
    val newStr = new SimpleDateFormat("yyyyMMdd").format(new SimpleDateFormat("yyyy-MM-dd").parse(str))
    newStr
  }

}