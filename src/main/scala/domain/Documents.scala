package domain

import interfaces.Paper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Documents {

  /**
    * すべてのドキュメントを取得する
    *
    * @param paper Dropbox Paper へアクセスするためのクライアント
    * @return すべてのドキュメントの ID 一覧
    */
  def all(implicit paper: Paper): Seq[String] = {

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
        case Left(e) =>
          println(e)
          Seq()
      }
    }

    paper.list() match {
      case Right(list) =>
        if (list.has_more) continue(list.doc_ids, list.cursor.value)
        else list.doc_ids
      case Left(e) =>
        println(e)
        Seq()
    }
  }

  /**
    * invite only にする処理のラッパー
    *
    * @param idList invite only にする対象のドキュメント
    */
  implicit class InviteOnlyWrapper(val idList: Seq[String]) {

    /**
      * 指定されたドキュメントを invite only にする
      *
      * @param paper Dropbox Paper へアクセスするためのクライアント
      */
    def inviteOnly(implicit paper: Paper): Unit = {
      for {
        groupedIdList <- idList.grouped(4)
      } Await.ready(
        Future.sequence(for {
          id <- groupedIdList
        } yield {
          val f = Future {
            paper.inviteOnlySharingPolicy(id) match {
              case Right(_) =>
              case Left(message: String) => throw new Exception(message)
            }
          }
          f.failed.foreach(e => println(e))
          f
        }),
        Duration.Inf
      )
    }
  }
}
