package jrx.data.hub.flink.scala

import java.text.SimpleDateFormat

import org.apache.flink.api.common.typeinfo.{TypeInformation, Types}
import org.apache.flink.table.functions.TableFunction

class HangToLie extends TableFunction[String] {
  @scala.annotation.varargs
  def eval(fields: String*): Unit = {
    var i: Int = 0
    val date = new SimpleDateFormat("HH:mm:ss").parse("00:00:00")
    val dateTime = date.getTime
    for (field <- fields) {
      val dateStr = new SimpleDateFormat("HH:mm:ss").format(dateTime + 60 * 1000 * i)
      collect(dateStr + "#" + field + "|P" + i / 15)
      i = i + 15
    }
  }

  override def getResultType: TypeInformation[String] = {
    Types.STRING
  }
}