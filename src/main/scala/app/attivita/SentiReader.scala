package app.attivita
import scala.collection.mutable.ArrayBuffer

/**
  * Classe "SentiReader"
  * legge un array di stringhe contente i tweet li pulisce e li separa
  *
  */

object SentiReader {

  val __SENTINEG = 0
  val __SENTIPOS = 1

  // pattern per pulire il tweet
  val cleaner = "&(.+;)|http|\\\"|\\,|\\+|\\'|…|\\/|\\>|\\?|\\[|\\]|\\-|\\(|\\)|\\W+…|&gt;|RT|\\.|!|“|”|->|\\#|:|@\\w+\\S+|\\\\\"|\\\\\" ".r
  val url = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"

  private var posTweets = new ArrayBuffer[String]()
  private var negTweets = new ArrayBuffer[String]()

  /** Legge una stringa contenente le etichette e i tweet riga per riga
    * Pulisce le stringhe in modo da ridurre il rumore
    * Salva i tweet identificati come positivi e negativi nei corrispondenti array di stringhe
    * @param tweets un array di stringhe del tipo " 0.0|Questo è un tweet negativo " senza apici
    * @return  void
    *
    */

  def leggi(tweets: Array[String]): Unit = {

    // sentiPattern indica l'etichetta del tweet
    val sentiPattern = "[0|1].0".r

    // tweetPattern identifica il tweet
    val tweetPattern = "\\|(.+)".r

    // lettura e pulizia
    for (tweet <- tweets) {

      // separazione etichetta-tweet
      val currRes = sentiPattern.findFirstIn(tweet).get.toDouble.toInt
      val currTweet = tweetPattern.findFirstIn(tweet).get.drop(1)

      // pulizia tweet
      val fixed = currTweet.replaceAll(url,"")
      val fixedTweet = cleaner.replaceAllIn(fixed," ").replaceAll("\\s+", " ").toLowerCase

      // DEBUG println("S: " + currRes + " T: " + currTweet)

      if (currRes == __SENTINEG) {
        negTweets += fixedTweet
      }

      if (currRes == __SENTIPOS) {
        posTweets += fixedTweet
      }
    }
  }

  /* legge il file csv */

  def leggiCSV(tweets: Array[String]): Unit = {
    val pattern = "([0-9]+),(.),([a-zA-Z0-9]+),(.+)".r
    var grezzo = ""
    var sentiment = 0
    var id = ""
    var code = 1

    for (tweet <- tweets) {

       tweet match {
       case pattern(a, b, c, d) => (id = a, sentiment = b.toInt, c , grezzo = d )
      }

      /* DEBUG

      println("ID: " + id)
      println("Sentiment: " + sentiment)
      println("Tweet: " + grezzo)

      */

      val fixed = grezzo.replaceAll(url,"")
      val fixedTweet = cleaner.replaceAllIn(fixed," ").replaceAll("\\s+", " ").toLowerCase

      // println("Pulito: " + fixedTweet)

      if (sentiment == __SENTINEG) {
        negTweets += fixedTweet

      }

      if (sentiment == __SENTIPOS) {
        posTweets += fixedTweet
      }
    }

  }


    /** ottiene l'insieme di tweet identificati come positivi
    * @return posTweets, un array di stringhe di tweet puliti
    *
    *
    */

  def getPosTweets : ArrayBuffer[String] = posTweets

  /** ottiene l'insieme di tweet identificati come negativi
    * @return negTweets, un array di stringhe di tweet puliti
    *
    *
    */

  def getNegTweets : ArrayBuffer[String] = negTweets



}
