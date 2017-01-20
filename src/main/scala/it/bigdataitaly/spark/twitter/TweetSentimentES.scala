package it.bigdataitaly.spark.twitter


import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.functions.explode
import org.apache.spark.sql.functions.udf
import org.elasticsearch.spark.sql._

import com.databricks.spark.corenlp.functions._

object TweetSentimentES {

  val fromDir="unifiedTweets"
  
  def removeUrl(commentstr:String) : String ={
    val urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)".r
    urlPattern.replaceAllIn(commentstr.toLowerCase(),"").trim()     
  }
    
  def main(args: Array[String]) {
    
    val spark = SparkSession
      .builder
      .appName(this.getClass.getSimpleName)
      .master("local[*]")
  //    .config("spark.sql.warehouse.dir", "file:///C:/temp") // Necessary to work around a Windows bug in Spark 2.0.0; omit if you're not on Windows.
      .getOrCreate()
      
    import spark.implicits._
   
    val removeUrlUDF= udf((s:String) => removeUrl(s))
    
    Utils.setupLogging()
   
    val tweets=spark.read.json(fromDir+"/*")
      .select("text","user","geolocation","place","lang").where("lang == 'en'").where("geolocation != null")
      .select(col("text"),col("user"),col("geolocation"),col("place"),explode(ssplit(removeUrlUDF(col("text")))) as "sentences",col("lang"))
      .select(col("text"),col("user"),col("geolocation"),col("place"),col("sentences"),col("lang"),sentiment(col("sentences")) as "sentiment",tokenize(col("sentences")) as "words",pos(col("sentences")) as "pos",lemma(col("sentences")) as "lemmas",ner(col("sentences")) as "nerTags")
      
    
    tweets.saveToEs("twitter-geo-sentiment/tweet")
    }
  }