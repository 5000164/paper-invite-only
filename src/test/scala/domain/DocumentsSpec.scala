package domain

import domain.paper.List
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
  }
}
