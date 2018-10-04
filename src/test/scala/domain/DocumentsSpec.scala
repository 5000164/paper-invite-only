package domain

import domain.paper.List
import interfaces.Paper
import org.scalamock.scalatest.MockFactory
import org.scalatest.FeatureSpec

class DocumentsSpec extends FeatureSpec with MockFactory {
  feature("ドキュメントの ID 一覧が取得できる") {
    scenario("成功した場合") {
      val paper = stub[Paper]
      (paper.list _)
        .when(None, None, None, None)
        .returns(Right(List.Response(doc_ids = Seq(), cursor = List.Cursor("", ""), has_more = false)))
      Documents.all(paper)
    }
  }
}
