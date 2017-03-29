package com.soundcloud.spark.pagerank

import org.scalatest.{ FunSuite, Matchers }

class PageRankAppTest
  extends FunSuite
  with Matchers
  with GraphTesting
  with SparkTesting {

  // TODO(jd): design a better integration test as this just runs the app without assertions
  test("integration test") {
    val options = new PageRankApp.Options()
    options.output = "target/test/PageRankAppTest"

    val numVertices = 5
    val prior = 1.0 / numVertices
    val stats = Seq(s"numVertices,$numVertices")

    val edges = sc.parallelize(Seq[OutEdgePair](
      // node 1 is dangling
      (2, OutEdge(1, 1.0)),
      (3, OutEdge(1, 1.0)),
      (4, OutEdge(2, 0.5)),
      (4, OutEdge(3, 0.5)),
      (5, OutEdge(3, 0.5)),
      (5, OutEdge(4, 0.5))
    ))
    val vertices = sc.parallelize(Seq[RichVertexPair](
      (1, VertexMetadata(prior, true)),
      (2, VertexMetadata(prior, false)),
      (3, VertexMetadata(prior, false)),
      (4, VertexMetadata(prior, false)),
      (5, VertexMetadata(prior, false))
    ))

    PageRankApp.runFromInputs(
      options,
      sc,
      stats,
      edges,
      vertices,
      priorsOpt = None
    )
  }
}
