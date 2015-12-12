package aftermath

import scala.io.Source

object Main extends App {
	def getStrings(filename: String) = Source.fromFile(filename).getLines().toSeq

	def levenshteinDistanceBetween(s1: String, s2: String): Int = {
		val matrix = new Array[Array[Int]](s1.length + 1)
		for (i <- 0 to s1.length) {
			matrix(i) = new Array[Int](s2.length + 1)
			matrix(i)(0) = i
		}
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

	def findOutliers(strings: Iterable[String], neighbourDistanceThreshold: Int, densityThreshold: Int) =
		strings.filter { str =>
			strings.count {
				levenshteinDistanceBetween(_, str) <= neighbourDistanceThreshold
			} < densityThreshold
		}

	findOutliers(getStrings(args(0)), Integer.valueOf(args(1)), Integer.valueOf(args(2)))
}
