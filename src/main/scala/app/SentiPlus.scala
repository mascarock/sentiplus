package app

import app.attivita.{SentiClassifier, SentiReader}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import attivita.Classifier

import java.io

/**
  * Classe SentiPlus
  * costituisce il Main del sistema
  * che va eseguito
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

    /*
    val file_input = sc.textFile("data/dataset.csv")
    val header = file_input.first()
    val totLines = file_input.count().toInt

    /* salta la prima linea che costituisce l'header */

    val tweets = file_input.take(10000).filter((row => row != header))
    SentiReader.leggiCSV(tweets) */


    /* classificazione
    * 9231L
    * 10621L
    * 95000
    * 1124152
    * 1152161
    * */

    val __SEED = 612142124900L
    val __SEEB = 111112489153L
    val __FEATURES = 95000

    val posTweets = sc.parallelize(SentiReader.getPosTweets)
    val negTweets = sc.parallelize(SentiReader.getNegTweets)

    SentiClassifier.classifica(posTweets, negTweets, __SEED, __SEEB, __FEATURES)
    SentiClassifier.risultato()


    //val classificatore = new Classifier()
    //classificatore.run(posTweets, negTweets, __SEED, __SEEB, __FEATURES)

    sc.stop()
  }

}
