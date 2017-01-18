package it.bigdataitaly.spark.twitter

import java.io.File

import com.google.gson.Gson
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import twitter4j.Status

/**
 * Collect at least the specified number of tweets into json text files.
 */
object TweetCollector {
  private var numTweetsCollected = 0L
  private var partNum = 0
  private var gson = new Gson()
  
  val numTweetsToCollect=999999
  val intervalSecs=600
  val outputDirectory="output"
  val partitionsEachInterval=1
  
 
  def filterTweets(tweet: Status):Boolean = {
    val text= tweet.getText.toLowerCase()
    return text.contains("christmas") || text.contains("xmas") || text.contains("natale")
  }

  def main(args: Array[String]) {

    Utils.setupTwitter()
    Utils.setupLogging()
    
    println("Initializing Streaming Spark Context...")
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName)
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(intervalSecs))

    val tweetStream = TwitterUtils.createStream(ssc, None).filter { filterTweets }
      .map(gson.toJson(_))
      
    tweetStream.foreachRDD((rdd, time) => {
      val count = rdd.count()
      if (count > 0) {
        val outputRDD = rdd.repartition(partitionsEachInterval)
        outputRDD.saveAsTextFile(outputDirectory + "/tweets_" + time.milliseconds.toString)
        numTweetsCollected += count
        if (numTweetsCollected > numTweetsToCollect) {
          System.exit(0)
        }
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
