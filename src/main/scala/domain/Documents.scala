package domain

import interfaces.Paper

object Documents {
  def all(paper: Paper): Seq[String] = {
    paper.list() match {
      case Right(list) =>
        list.doc_ids
      case Left(_) =>
        Seq()
    }
  }
}
