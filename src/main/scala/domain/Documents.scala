package domain

import interfaces.Paper

object Documents {
  def all(paper: Paper): Seq[String] = {
    paper.list() match {
      case Right(list) =>
        val r = paper.listContinue(list.cursor.value)
        println(r)
      case Left(_) =>
    }
    Seq()
  }
}
