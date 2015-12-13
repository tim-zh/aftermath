package aftermath

import org.scalameter._
import org.scalatest._

class PerformanceSuite extends FunSuite with Matchers {
	test("Relative performance testing") {
		val strings = Seq("1234567", "1234567", "1234567", "1234567", "1234567", "123456", "1", "1", "1", "2", "1234", "4567")

		val time = config(
			Key.exec.benchRuns -> 10000,
			Key.exec.outliers.suspectPercent -> 20
		) withWarmer {
			new Warmer.Default
		} withMeasurer {
			new Measurer.IgnoringGC
		} measure {
			Main.findOutliers(strings, 2, 4)
		}
		println(s"Total time: $time")
	}
}
