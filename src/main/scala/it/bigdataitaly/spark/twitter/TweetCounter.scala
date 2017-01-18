package it.bigdataitaly.spark.twitter

import org.apache.spark.sql.SparkSession

object TweetCounter {
  
  val fromDir="output"
  val toDir="unifiedTweets"
    
  def main(args: Array[String]) {
    
    val spark = SparkSession
      .builder
      .appName(this.getClass.getSimpleName)
      .getOrCreate()
    
    Utils.setupLogging()
    
    val tweets = spark.read.json(fromDir+"/*")
    
    tweets.write.json(toDir)
    
    println("Number of tweets: "+tweets.count())
  }
}