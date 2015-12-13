package aftermath

import org.scalatest.{Matchers, FlatSpec}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MainSpec extends FlatSpec with Matchers {
	"Main" should "calculate precise distance between strings" in {
		Main.strongDistanceBetween("123abc", "123abc") should be(0)
		Main.strongDistanceBetween("321abc", "123abc") should be(2)
		Main.strongDistanceBetween("123", "123abc") should be(3)
	}

	it should "find outlying strings" in {
		val strings = Seq("aaa", "aaa", "aab", "bbb", "bbb")

		val result1 = Main.findOutliers(strings, 0, 2)
		result1.size should be(1)
		result1.head should be("aab")

		val result2 = Main.findOutliers(strings, 1, 3)
		result2.size should be(1)
		result2.head should be("bbb")
	}

	it should "find outlying strings in parallel" in {
		val strings = Seq("aaa", "aaa", "aab", "bbb", "bbb", "aaa", "aaa", "aab", "bbb", "bbb")

		val result = Await.result(Main.parallelFindOutliers(strings, Main.findOutliers(_, 0, 2), 5), Duration.Inf)
		result.size should be(1)
		result.head should be("aab")
	}
}
