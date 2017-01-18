package it.bigdataitaly.spark.twitter

import scala.io.Source

object Utils {
  def setupTwitter() = {

    for (line <- Source.fromFile("../twitter.txt").getLines) {
      val fields = line.split(" ")
      if (fields.length == 2) {
        System.setProperty("twitter4j.oauth." + fields(0), fields(1))
      }
    }
  }

  def setupLogging() = {
    import org.apache.log4j.{ Level, Logger }
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)
  }
  
  def setupS3() : Map[String,String] = {
    var keyMap: Map[String,String] =Map()
   
    for (line <- Source.fromFile("../s3config.txt").getLines) {
      val fields = line.split("=")
      if (fields.length == 2) {
         keyMap += (fields(0) -> fields(1))
      }
    }
    return keyMap;
  }
}