package domain

import domain.paper.{List, ListContinue}
import interfaces.Paper
import org.scalamock.scalatest.MockFactory
import org.scalatest.FeatureSpec

class DocumentsSpec extends FeatureSpec with MockFactory {
  feature("ドキュメントの ID 一覧が取得できる") {
    scenario("続きがない") {
      val paper = stub[Paper]
      (paper.list _)
        .when(None, None, None, None)
        .returns(Right(List.Response(doc_ids = Seq("A", "B", "C"), cursor = List.Cursor("value", "expiration"), has_more = false)))
      assert(Documents.all(paper) === Seq("A", "B", "C"))
    }

    scenario("続きが 1 回ある") {
      val paper = stub[Paper]
      (paper.list _)
        .when(None, None, None, None)
        .returns(Right(List.Response(doc_ids = Seq("A", "B", "C"), cursor = List.Cursor("1", "expiration"), has_more = true)))
      (paper.listContinue _)
        .when("1")
        .returns(Right(ListContinue.Response(doc_ids = Seq("D", "E"), cursor = ListContinue.Cursor("value", "expiration"), has_more = false)))
      assert(Documents.all(paper) === Seq("A", "B", "C", "D", "E"))
    }

    scenario("続きが 2 回ある") {
      val paper = stub[Paper]
      (paper.list _)
        .when(None, None, None, None)
        .returns(Right(List.Response(doc_ids = Seq("A", "B", "C"), cursor = List.Cursor("1", "expiration"), has_more = true)))
      (paper.listContinue _)
        .when("1")
        .returns(Right(ListContinue.Response(doc_ids = Seq("D", "E"), cursor = ListContinue.Cursor("2", "expiration"), has_more = true)))
      (paper.listContinue _)
        .when("2")
        .returns(Right(ListContinue.Response(doc_ids = Seq("F"), cursor = ListContinue.Cursor("value", "expiration"), has_more = false)))
      assert(Documents.all(paper) === Seq("A", "B", "C", "D", "E", "F"))
    }
  }
}
