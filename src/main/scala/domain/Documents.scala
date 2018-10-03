package domain

import interfaces.Paper

object Documents {
  def all(paper: Paper): Seq[String] = {
    paper.list().body match {
      case Right(decoded) =>
        decoded match {
          case Right(response) =>
            val r = paper.listContinue(response.cursor.value)
            println(r)
          case Left(_) =>
        }
      case Left(_) =>
    }
    Seq()
  }
}
