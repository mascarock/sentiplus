package app.attivita
import org.apache.spark.rdd.RDD

object SentiClassifier {

  import org.apache.spark.mllib.feature.HashingTF
  import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
  import org.apache.spark.mllib.regression.LabeledPoint

  /**
    * Classe SentiClassifier
    * Classifica il testo utilizzando una regressione logistica
    * sfruttando l'algoritmo LBFGS
    *
    */

  // parametri per valutare metriche

  var tp = 0 // true positive
  var tn = 0 // true negative
  var fp = 0 // false positive
  var fn = 0 // false negative
  var index = 0
  var riga = 0
  var azzeccati = 0


  /** Valuta per ogni array di stringhe positive e negative il sentimento espresso
    * @param posTweets un RDD di Stringhe positive
    * @param  negTweets  un RDD di stringhe negative
    * @param __FEATURES numero di features
    *
    */

  def classifica(posTweets: RDD[String], negTweets: RDD[String], __SEED:Long, __SEEB:Long, __FEATURES:Int) {

    val dataPos = posTweets.count().toDouble
    val dataNeg = negTweets.count().toDouble

    /** calcolo della percentuale
      * per avere un bilanciamento
      * tra dati positivi e dati negativi
      * nel caso di dataset non bilanciati
      */

    val negX = BigDecimal((dataPos / (dataNeg + dataPos))).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
    val posX = BigDecimal(1 - negX).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble

    // insieme di tweet positivi e negativi
    val posSplits = posTweets.randomSplit(Array(posX, negX), __SEED)
    val negSplits = negTweets.randomSplit(Array(negX, posX), __SEEB)

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

    /* Commentare le seguenti linee per ingrandire l'insieme di test
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


    /* valuta il modello sui tweet positivi

     */
    for (twt <- testerPositive) {

      val singleTweet = tf.transform(twt)
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

      riga += 1;
    }


    /* valuta il modello sui tweet negativi

     */


    for (twt <- testerNegative) {
      val singleTweet = tf.transform(twt)
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
  }

  /** Descrivi i risultati ottenuti
    * stampando su schermo
    * @return void
    */


  def risultato(): Unit = {

    val accuracy = (tp + tn) / riga.toDouble * 100
    val precision = tp / (tp + fp).toDouble * 100
    val recall = tp / (tp + fn).toDouble * 100

    println(${Console.RED} "++++++++ RISULTATI ++++++++++")
    println("Identificati: " + (tp + tn) + " su " + riga)
    println("Accuratezza: " + accuracy + "%")
    println("Precisione: " + precision)
    println("Richiamo: " + recall)

    println("\ngrazie per aver usato Sentiplus. ")


  }





}
