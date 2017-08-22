package app

import app.attivita.{SentiClassifier, SentiReader}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * SentiPlus
  * costituisce il Main del sistema
  * che va eseguito da riga di comando
  *
  */

object SentiPlus {
  private val _MAXINPUT = 1578628

  def main(args: Array[String]) {
    /* Leggi la configurazione */

    val sc = new SparkContext(new SparkConf().setMaster("local").setAppName("Sentiplus"))

    // mostra solo i log in caso di errore
    sc.setLogLevel("ERROR")

    val file_input = sc.textFile("data/config.sp")
    val totLines = file_input.count().toInt
    val lines = file_input.take(totLines)

    val __SEED = lines(0).toLong
    val __SEEB = lines(1).toLong
    val __FEATURES = lines(2).toInt


    if (args.length == 0 || args.length > 2) {
      println("l'app Sentiplus funziona su due dataset: ITA (1) e ENG (2)\n ")
      println("Utilizzare $SentiPlus 1 per il primo dataset, $ SentiPlus 2 altrimenti")
      sc.stop()
    }


    else {

      if (args(0).toInt == 1) {

        val file_input = sc.textFile("data/TestSet.txt")
        val totLines = file_input.count().toInt
        val tweets = file_input.take(totLines)
        SentiReader.leggi(tweets)

      }

      else if (args.length > 1 && args(0).toInt == 2) {

        val file_input = sc.textFile("data/dataset.csv")
        val header = file_input.first()

        if (args(1).toInt > _MAXINPUT) {
          println("Superato il limite di tweet.")
          sc.stop()

        } else {

          val totLines = args(1).toInt
          /* salta la prima linea che costituisce l'header */
          val tweets = file_input.take(totLines+1).filter(row => row != header)
          SentiReader.leggiCSV(tweets)
        }

      }

      val posTweets = sc.parallelize(SentiReader.getPosTweets)
      val negTweets = sc.parallelize(SentiReader.getNegTweets)
      SentiClassifier.classifica(posTweets, negTweets, __SEED, __SEEB, __FEATURES)
      SentiClassifier.risultato()

      sc.stop()
    }


  }
}

    /* classificazione
    * 9231L
    * 10621L
    * 95000
    * 1124152
    * 1152161
    * */

    //val classificatore = new Classifier()
    //classificatore.run(posTweets, negTweets, __SEED, __SEEB, __FEATURES) */

