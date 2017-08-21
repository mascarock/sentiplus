package app.attivita
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.regression.LabeledPoint


/**
  * TO DEBUG
  */
class Classifier()  {

  // coefficiente di bilanciamento
  private val _CFBILANC = 0.85

  private var negX = 0.0
  private var posX = 0.0

  private var dataPos = 0.0
  private var dataNeg = 0.0

  private var testPos = 0.0
  private var testNeg = 0.0

  private var kpos = 0.0
  private var kneg = 0.0

  // parametri per valutare metriche


  def run(posTweets: RDD[String], negTweets: RDD[String], __SEED:Long, __SEEB:Long, __FEATURES:Int): Unit = {

    this.dataPos = posTweets.count().toInt
    this.dataNeg = negTweets.count().toInt

    this.bilanciaDataset()
    //this.apprendi(posTweets,negTweets,__SEED,__SEEB,__FEATURES)
    //this.stampaRisultati()

    var tp = 0 // true positive
    var tn = 0 // true negative
    var fp = 0 // false positive
    var fn = 0 // false negative

    // indici per stampare risultati

    var index = 0
    var riga = 0
    var azzeccati = 0

    var accuracy = 0.0
    var precision = 0.0
    var recall = 0.0

    dataPos = posTweets.count().toDouble
    dataNeg = negTweets.count().toDouble

    /*
    OLD: queste due righe servono per bilanciare un dataset 50 train e 50 test
    negX = BigDecimal((dataPos / (dataNeg + dataPos))).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
    posX = BigDecimal(1 - negX).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
    */

    // ottimali per CSV
    // 80 20
    // 60 40
    // insieme di tweet positivi e negativi posX, negX

    bilancia(dataPos.toInt,dataNeg.toInt,_CFBILANC)

    val posSplits = posTweets.randomSplit(Array(kpos,1-kpos), __SEED)
    val negSplits = negTweets.randomSplit(Array(kneg,1-kneg), __SEEB)

    negX = negSplits(0).count() / dataNeg
    posX = posSplits(0).count() / dataPos

    testPos = posSplits(0).count()
    testNeg = negSplits(0).count()


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

    // Effetta un test per un esempio positivo e un esempio negativo
    // utilizzando la stessa trasformazione in feature HashingTF usata sul dataset di test
    val posTestExample = tf.transform("sostiene marino abbiamo tolto il male da roma dimarted".split(" "))
    val negTestExample = tf.transform("mafiacapitale  ormai siamo alla frutta".split(" "))

    // Ora utilizza il modello allenato su nuovi esempi sconosciuti al modello stesso
    println(s"Previsione per un esempio POSITIVO: ${model.predict(posTestExample)}")
    println(s"Previsione per un esempio NEGATIVO: ${model.predict(negTestExample)}")

    /* testa il sistema sulla seconda parte del dataset, non utilizzato in fase di apprendimento */

    var testerNegative = negSplits(1)
    var testerPositive = posSplits(1)

    /* DEcommentare le seguenti linee per ingrandire l'insieme di test
    *  con i tweet usati in fase di apprendimento
    *
    */

    //testerPositive = posSplits(1) .union(posSplits(0))
    //testerNegative = negSplits(1) .union(negSplits(0))

    /* Commentare le seguenti linee per testare il modello
    *  su altri tweet già pre-etichettati
    */

    //testerPositive = sc.textFile("yourtweets/pos")
    //testerNegative = sc.textFile("yourtweets/neg")


    /* valuta il modello sui tweet positivi */

    var ciccio = 0.0
    for (twt <- testerPositive) {

      val singleTweet = tf.transform(twt)

      println(" tweet in esame: " + twt)
      println(s"Previsione: ${model.predict(singleTweet).toFloat}")
      println("Etichetta: 1 ")


      if (model.predict(singleTweet).toInt == 1) {
        azzeccati += 1
        println(azzeccati)
        println("OK! :) ")
        tp += 1
        println(tp)
      }

      else {
        println("NO :(")
        fn += 1
      }

      riga += 1;

    }


    /* valuta il modello sui tweet negativi */


    for (twt <- testerNegative) {
      val singleTweet = tf.transform(twt)


      println("\nTweet in esame: " + twt)
      println(s"> Previsione:  ${model.predict(singleTweet).toFloat}")
      println("> Etichetta: 0 ")


      if (model.predict(singleTweet).toInt == 0) {
        azzeccati += 1
        println(azzeccati)
        println("OK! :) ")
        tn += 1
        println(tn)
      }

      else {
        println("NO :( ")
        fp += 1
      }
      riga += 1

    }

    accuracy = (tp + tn) / riga.toDouble * 100
    precision = tp / (tp + fp).toDouble * 100
    recall = tp / (tp + fn).toDouble * 100

    println("++++++++ RISULTATI ++++++++++")

    println("Identificati: " + (tp + tn) + " su " + riga)
    println("> test Set Negativo: " + getNegX()*100 + "% valore: " + getTestNeg() + " su: " + this.dataNeg)
    println("> test Set Positivo: " + getPosX()*100 + "% valore: " + getTestPos() + " su: " + this.dataPos)

    println("Accuratezza: " + accuracy + "%")
    println("Precisione: " + precision )
    println("Richiamo: " + recall )

    println("\ngrazie per aver usato Sentiplus. ")


  }

  private def apprendi(posTweets: RDD[String], negTweets: RDD[String], __SEED:Long, __SEEB:Long, __FEATURES:Int): Unit = {


  }


  private def bilanciaDataset(): Unit = {

    bilancia(dataPos.toInt,dataNeg.toInt,_CFBILANC)

    /* Se si desidera avere un dataset bilanciato al 50% , decommentare queste righe */

    /*
    queste due righe servono per bilanciare un dataset 50 train e 50 test
    negX = BigDecimal((dataPos / (dataNeg + dataPos))).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
    posX = BigDecimal(1 - negX).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
    */


  }

  private def bilancia(pos: Integer, neg: Integer, coeff: Double) = {

    var testerPos = 0.0
    var testerNeg = 0.0

    if (pos <= neg) {
      this.kpos = valutak(pos,neg,coeff)
      testerPos = kpos*pos
      this.kneg = (testerPos / neg)
      testerNeg = testerPos
    }

    if (pos > neg) {
      this.kneg = valutak(pos,neg,coeff)
      testerNeg = kneg*neg
      this.kpos = (testerNeg / pos)
      testerPos = testerNeg
    }

  }

  private def valutak(pos: Integer, neg: Integer, coeff: Double) : Double = {

    val tester = math.min(pos,neg)
    var k = 1.0
    var z = 0.99

    while (k > coeff ) {

      k = ( z * (pos + neg) ) / ( 2 * tester )
      z = z - 0.01

    }

    return k

  }





  private def stampaRisultati(): Unit = {


  }

  def getNegX() : Double = {
    return  negX
  }

  def getPosX(): Double = {
    return posX
  }

  def getTestPos() : Double = {
    return testPos
  }


  def getTestNeg() : Double = {
    return testNeg
  }

  /*

  def getAccuracy(): Double = {
    return accuracy
  }

  def getPrecision(): Double = {
    return precision
  }

  def getRecall(): Double = {
    return recall
  }

  */




}
