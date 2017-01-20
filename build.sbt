name := "XMasTweetAnalysis"

version := "1.0"

organization := "it.bigdataitaly"

scalaVersion := "2.11.8"

resolvers +="Spark Packages" at "https://dl.bintray.com/spark-packages/maven/"

libraryDependencies ++= Seq(
"org.apache.spark" %% "spark-core" % "2.0.2" % "provided",
"org.apache.spark" %% "spark-streaming" % "2.0.2" % "provided",
"org.apache.spark" %% "spark-sql" % "2.0.2" % "provided",
"org.twitter4j" % "twitter4j-core" % "4.0.4",
"org.twitter4j" % "twitter4j-stream" % "4.0.4",
"org.apache.bahir" %% "spark-streaming-twitter" % "2.0.1",
"com.google.code.gson" % "gson" % "2.8.0",
"databricks" % "spark-corenlp" % "0.2.0-s_2.11",
"edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models",
"com.amazonaws" % "aws-java-sdk" % "1.7.4",
"org.apache.hadoop" % "hadoop-aws" % "2.7.3"  exclude("com.amazonaws", "aws-java-sdk-s3"),
("org.elasticsearch" % "elasticsearch-spark-20_2.11" % "5.1.2") 
	 .exclude("com.google.guava", "guava")
     .exclude("org.apache.hadoop", "hadoop-yarn-api")
     .exclude("org.eclipse.jetty.orbit", "javax.mail.glassfish")
     .exclude("org.eclipse.jetty.orbit", "javax.servlet")
	 .exclude("org.slf4j", "slf4j-api")
)

assemblyMergeStrategy in assembly := {
  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("javax", "ws", xs @ _*) => MergeStrategy.last
  case PathList("com", "sun", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}