package aftermath

import org.scalatest.{Matchers, FlatSpec}

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
		result2.size should be(2)
		result2.head should be("bbb")
		result2.tail.head should be("bbb")
	}
}
