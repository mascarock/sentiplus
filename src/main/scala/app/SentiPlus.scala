package app

import app.attivita.{SentiClassifier, SentiReader}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Classe SentiPlus
  * costituisce il Main del sistema
  *
  */
object SentiPlus {

  def main(args: Array[String]) {
    /* Leggi il file di testo e tokenizza */

    val sc = new SparkContext(new SparkConf().setMaster("local").setAppName("Sentiplus"))
    val file_input = sc.textFile("data/TestSet.txt")
    val totLines = file_input.count().toInt
    val tweets = file_input.take(totLines)

    SentiReader.leggi(tweets)
    var posTweets = sc.parallelize(SentiReader.getPosTweets)
    var negTweets = sc.parallelize(SentiReader.getNegTweets)

    /* classificazione */

    val __SEED = 9231L
    val __SEEB = 10621L
    val __FEATURES = 95000

    SentiClassifier.classifica(posTweets, negTweets, __SEED, __SEEB, __FEATURES)
    SentiClassifier.risultato()

    sc.stop()
  }

}
