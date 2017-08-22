package app

import app.attivita.{DebugSentiClassifier, SentiReader}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * SentiPlus
  * costituisce il Main del sistema
  * che va eseguito da riga di comando
  *
  */

object DebugSentiPlus {

  def main(args: Array[String]) {
    /* Leggi la configurazione */

    val sc = new SparkContext(new SparkConf().setMaster("local").setAppName("Sentiplus"))
    val file_input = sc.textFile("data/dataset1m.csv")
    val totLines = file_input.count().toInt
    val header = file_input.first()
    val tweets = file_input.take(totLines+1).filter(row => row != header)
    SentiReader.leggiCSV(tweets)



  }
}

