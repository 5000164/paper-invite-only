package domain

import interfaces.Paper

object Documents {

  /**
    * すべてのドキュメントを取得する
    *
    * @param paper Dropbox Paper へアクセスするためのクライアント
    * @return すべてのドキュメントの ID 一覧
    */
  def all(paper: Paper): Seq[String] = {

    /**
      * ドキュメントの続きがなくなるまで取得する
      *
      * @param now 現在取得済みの ID 一覧の内容
      * @param value どこから続きを取得するかを表す cursor の値
      * @return 続きがなくなるまで取得した結果の ID の一覧
      */
    def continue(now: Seq[String], value: String): Seq[String] = {
      paper.listContinue(value) match {
        case Right(list) =>
          if (list.has_more) continue(now ++ list.doc_ids, list.cursor.value)
          else now ++ list.doc_ids
        case Left(_) =>
          Seq()
      }
    }

    paper.list() match {
      case Right(list) =>
        if (list.has_more) continue(list.doc_ids, list.cursor.value)
        else list.doc_ids
      case Left(_) =>
        Seq()
    }
  }

  def inviteOnly(paper: Paper, idList: Seq[String]): Unit = {
    val result = paper.toPrivateSharingPolicy(idList.head)
    println(result)
  }
}
