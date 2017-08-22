package app.attivita

import org.apache.spark.SparkContext
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.regression.LabeledPoint

/**
  * SentiClassifier
  * Classifica il testo utilizzando una regressione logistica
  * sfruttando l'algoritmo LBFGS
  *
  */

object SentiClassifier {

  // parametri per valutare metriche

  private var tp = 0 // true positive
  private var tn = 0 // true negative
  private var fp = 0 // false positive
  private var fn = 0 // false negative
  private var index = 0
  private var riga = 0
  private var azzeccati = 0

  // elementi positivi
  private var dataPos = 0
  private var dataNeg = 0

  // train set
  private var trainPos = 0.0
  private var trainNeg = 0.0

  // percentuale train set
  private var kpos = 0.0
  private var kneg = 0.0


  /** Valuta per ogni array di stringhe positive e negative il sentimento espresso
    *
    * @param posTweets un RDD di Stringhe positive
    * @param  negTweets  un RDD di stringhe negative
    * @param __FEATURES numero di features
    *
    */

  def classifica(posTweets: RDD[String], negTweets: RDD[String], __SEED:Long, __SEEB:Long, __FEATURES:Int) {

    dataPos = posTweets.count().toInt
    dataNeg = negTweets.count().toInt


    /** calcolo della percentuale
      * per avere un bilanciamento
      * tra dati positivi e dati negativi
      * nel caso di dataset non bilanciati
      *
      */
    bilancia(dataPos,dataNeg)

    val posSplits = posTweets.randomSplit(Array(kpos,1-kpos), __SEED)
    val negSplits = negTweets.randomSplit(Array(kneg,1-kneg), __SEEB)


    trainPos = posSplits(0).count()
    trainNeg = negSplits(0).count()


    val tf = new HashingTF(numFeatures = __FEATURES)

    // Ogni tweet è diviso in parole, e ogni parola è mappata ad una feature
    // le features vengono valutate per l'insieme di test, calcolato prendendo
    // la prima parte dei tweet negativi e la prima parte dei tweet positivi.

    val posFeatures = posSplits(0).map(text => tf.transform(text.split(" ")))
    val negFeatures = negSplits(0).map(text => tf.transform(text.split(" ")))

    // Crea un insieme "LabeledPoint" per i tweet positivi e negativi
    val positiveExamples = posFeatures.map(features => LabeledPoint(1, features))
    val negativeExamples = negFeatures.map(features => LabeledPoint(0, features))

    // allena il sistema: apprendimento
    val trainingData = positiveExamples ++ negativeExamples
    trainingData.cache()

    // Crea un Logistic Regression learner che utilizza l'algoritmo LBFGS
    val lrLearner = new LogisticRegressionWithLBFGS()

    // Usa l'algoritmo per addestrare il modello
    val model = lrLearner.run(trainingData)

    /* testa il sistema sulla seconda parte del dataset, non utilizzato in fase di apprendimento */

    var testerNegative = negSplits(1)
    var testerPositive = posSplits(1)

    /* DEcommentare le seguenti linee per ingrandire l'insieme di test
    *  con i tweet usati in fase di apprendimento
    */

    //testerPositive = posSplits(1) .union(posSplits(0))
    //testerNegative = negSplits(1) .union(negSplits(0))

    /* DECommentare le seguenti linee per testare il modello
    *  Su altri tweet già pre-etichettati
    */

    //testerPositive = sc.textFile("yourtweets/pos")
    //testerNegative = sc.textFile("yourtweets/neg")


    /* valuta il modello sui tweet positivi */
    for (twt <- testerPositive) {

      val singleTweet = tf.transform(twt.split(" "))


      println(" tweet in esame: " + twt)
      println(s"Previsione: ${model.predict(singleTweet).toFloat}")
      println("Etichetta: 1 ")


      if (model.predict(singleTweet).toInt == 1) {
        azzeccati += 1
        println("OK! :) ")
        tp += 1
      }

      else {
        println("NO :(")
        fn += 1
      }

      riga += 1
    }


    /* valuta il modello sui tweet negativi */


    for (twt <- testerNegative) {
      val singleTweet = tf.transform(twt.split(" "))


      println("\nTweet in esame: " + twt)
      println(s"> Previsione:  ${model.predict(singleTweet).toFloat}")
      println("> Etichetta: 0 ")


      if (model.predict(singleTweet).toInt == 0) {
        azzeccati += 1
        println("OK! :) ")
        tn += 1
      }

      else {
        println("NO :( ")
        fp += 1
      }
      riga += 1

    }


    /* calcola metriche per valutarle */

    val posTestFeatures = posSplits(1).map(text => tf.transform(text.split(" ")))
    val negTestFeatures = negSplits(1).map(text => tf.transform(text.split(" ")))

    val positiveTest = posTestFeatures.map(features => LabeledPoint(1, features))
    val negativeTest = negTestFeatures.map(features => LabeledPoint(0, features))

    val testerData = positiveTest ++ negativeTest
    testerData.cache()

    // Elimina la soglia così che il modello ritornerà le probabilità
    model.clearThreshold

    // Calcola i risultati grezzi sul test set
    val predictionAndLabels = testerData.map { case LabeledPoint(label, features) =>
      val prediction = model.predict(features)
      (prediction, label)
    }

    val metrics = new BinaryClassificationMetrics(predictionAndLabels)

    // Precision-Recall Curve
    val PRC = metrics.pr

    // AUPRC
    val auPRC = metrics.areaUnderPR


    // ROC Curve
    val roc = metrics.roc

    // AUROC
    val auROC = metrics.areaUnderROC


    println("\nArea sotto la curva P-R = " + arrotonda(auPRC*100) + "% ")
    println("Area sotto la curva ROC = " + arrotonda(auROC*100) + "%" )


  }


  /** Descrivi i risultati ottenuti
    * stampando su schermo
    */

  def risultato(): Unit = {

    println("\n++++++++ RISULTATI ++++++++++")

    println("Sono stati processati " + (dataNeg + dataPos) + " tweet, così divisi")
    println("> Valore positivo: " + dataPos)
    println("> Valore negativo: " + dataNeg)

    println("\nL'insieme train è costituito da " + (getTrainPos + getTrainNeg) + " tweet, così diviso: ")
    println("> Train Set Negativo: " + percTrainNeg+ "% totale: " + getTrainNeg + " su: " + dataNeg)
    println("> Train Set Positivo: " + percTrainPos+ "% totale: " + getTrainPos + " su: " + dataPos)

    println("\nL'insieme test è costituito da " + (getTestPos + getTestNeg) + " tweet così diviso: ")
    println("> Test Set Negativo: " + arrotonda(percTestNeg) + "% totale: " + getTestNeg + " su: " + dataNeg)
    println("> Test Set Positivo: " + arrotonda(percTestPos) + "% totale: " + getTestPos + " su: " + dataPos)

    println("\nIdentificati: " + (tp + tn) + " su " + riga)
    println("> Accuratezza: " + getAccuracy + "%")
    println("> Precisione: " + getPrecision )
    println("> Richiamo: " + getRecall)

    println("\nGrazie per aver usato Sentiplus. ")

  }


  /** Descrivi i risultati ottenuti
    * stampando su schermo
 *
    * @param pos un intero che rappresenta tweet positivi
    * @param neg un intero che rappresenta tweet negativi
    */

  def bilancia(pos: Integer, neg: Integer) = {

    /*

   Bilanciamento al 50%:
   negX = BigDecimal((dataPos / (dataNeg + dataPos))).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
   posX = BigDecimal(1 - negX).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble

   */

    var testerPos = 0.0
    var testerNeg = 0.0

    if (pos <= neg) {
      kpos = valutak(pos,neg)
      testerPos = kpos*pos
      kneg = testerPos / neg
      testerNeg = testerPos
    }

    if (pos > neg) {
      kneg = valutak(pos,neg)
      testerNeg = kneg*neg
      kpos = testerNeg / pos
      testerPos = testerNeg
    }

  }

  def valutak(pos: Integer, neg: Integer) : Double = {

    val tester = math.min(pos,neg)
    var k = 1.0
    var z = 0.99

    while (k > 0.85) {
      k = ( z * (pos + neg) ) / ( 2 * tester )
      z = z - 0.01
    }

    k

  }

  private def percTrainPos : Double = arrotonda(kpos*100)
  private def percTrainNeg: Double = arrotonda(kneg*100)
  private def percTestPos : Double = arrotonda(100-kpos*100)
  private def percTestNeg: Double = arrotonda(100-kneg*100)
  private def getTrainPos: Int = trainPos.toInt
  private def getTrainNeg: Int = trainNeg.toInt
  private def getTestPos: Int =  dataPos - getTrainPos
  private def getTestNeg: Int =  dataNeg - getTrainNeg
  private def getAccuracy: Double =  arrotonda((tp + tn) / riga.toDouble * 100)
  private def getPrecision: Double =  arrotonda(tp / (tp + fp).toDouble * 100)
  private def getRecall: Double =  arrotonda(tp / (tp + fn).toDouble * 100)
  // arrotonda  a 3 cifre decimali
  private def arrotonda(x: Double) : Double =  BigDecimal( x ).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble


}
