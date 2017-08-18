package app.attivita
import scala.collection.mutable.ArrayBuffer

/**
  * Classe "SentiReader"
  * legge un array di stringhe contente i tweet li pulisce e li separa
  *
  */

object SentiReader {

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

    // pattern per pulire il tweet
    val cleaner = "http|\\\"|\\,|\\+|\\'|…|\\/|\\>|\\?|\\[|\\]|\\-|\\(|\\)|\\W+…|&gt;|RT|\\.|!|“|”|->|\\#|:|@\\w+\\S+|\\\\\"|\\\\\" ".r
    val url = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"

    // lettura e pulizia
    for (tweet <- tweets) {

      // separazione etichetta-tweet
      val currRes = sentiPattern.findFirstIn(tweet).get.toDouble.toInt
      val currTweet = tweetPattern.findFirstIn(tweet).get.drop(1)

      // pulizia tweet
      val fixed = currTweet.replaceAll(url,"")
      val fixedTweet = cleaner.replaceAllIn(fixed," ").toLowerCase

      // DEBUG println("S: " + currRes + " T: " + currTweet)

      if (currRes == 0) {
        negTweets += fixedTweet
      }

      if (currRes == 1) {
        posTweets += fixedTweet
      }

    }

  }

  /** ottiene l'insieme di tweet identificati come positivi
    * @return posTweets, un array di stringhe di tweet puliti
    *
    *
    */

  def getPosTweets : ArrayBuffer[String] = {
    return posTweets
  }

  /** ottiene l'insieme di tweet identificati come negativi
    * @return negTweets, un array di stringhe di tweet puliti
    *
    *
    */

  def getNegTweets : ArrayBuffer[String] = {
    return negTweets
  }




}
