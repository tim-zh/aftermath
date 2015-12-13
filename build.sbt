name := "aftermath"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Sonatype OSS Snapshots" at
		"https://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
	"org.scalatest" %% "scalatest" % "2.2.4" % Test,
	"com.storm-enroute" %% "scalameter-core" % "0.7" % Test
)
