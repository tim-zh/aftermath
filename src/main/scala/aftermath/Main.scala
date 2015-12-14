package aftermath

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source

object Main extends App {
	def getStrings(filename: String) = Source.fromFile(filename).getLines().toSeq

	def weakDistanceBetween(s1: String, s2: String): Int = math.abs(s1.length - s2.length)

	def strongDistanceBetween(s1: String, s2: String): Int = {
		if (s1 == s2)
			return 0
		//Levenshtein
		val matrix = Array.ofDim[Int](s1.length + 1, s2.length + 1)
		for (i <- 0 to s1.length)
			matrix(i)(0) = i
		for (i <- 0 to s2.length)
			matrix(0)(i) = i

		for (i <- 1 to s1.length)
			for (j <- 1 to s2.length)
				matrix(i)(j) =
						if (s1.charAt(i - 1) == s2.charAt(j - 1))
							matrix(i - 1)(j - 1)
						else
							1 + math.min(math.min(matrix(i)(j - 1), matrix(i - 1)(j)), matrix(i - 1)(j - 1))
		matrix(s1.length)(s2.length)
	}

	def findOutliers(strings: Seq[String], neighborDistanceThreshold: Int, densityThreshold: Int): Seq[String] = {
		val neighborCounters = Array.fill(strings.size)(1)

		var outliers = Seq[String]()
		for (i <- strings.indices) {
			for (j <- (i + 1) until strings.size)
				if (weakDistanceBetween(strings(i), strings(j)) <= neighborDistanceThreshold &&
						strongDistanceBetween(strings(i), strings(j)) <= neighborDistanceThreshold) {
					neighborCounters(i) += 1
					neighborCounters(j) += 1
				}
			if (neighborCounters(i) < densityThreshold)
				outliers :+= strings(i)
		}
		outliers.distinct
	}

	def parallelFindOutliers(strings: Seq[String], filter: Seq[String] => Seq[String], parallelBlockSize: Int): Future[Seq[String]] = {
		Future.traverse(strings.grouped(parallelBlockSize)) { block =>
			Future(filter(block))
		}.map(_.reduce(_ ++ _).distinct)
	}

	Await.result(
		parallelFindOutliers(
			getStrings(args(0)),
			findOutliers(_, Integer.valueOf(args(1)), Integer.valueOf(args(2))),
			Integer.valueOf(args(3))
		),
		Duration.Inf
	) foreach println
}
