# Sentiplus
Sentiplus - Un classificatore per individuare il sentimento dei tweet

L'obiettivo di Sentiplus è individuare il sentimento di un dataset di tweets già etichettato strutturato in modo che sia facilmente estraibile il valore numerico che identifica il sentimento e la stringa che rappresenta il tweet da analizzare. 

Ad esempio:

> 0.0|#Marino si e' dimessoooo??? Non ci credo!!!   #MAFIACAPITALE  #Romaliberata

L'elemento numerico che precede il carattere \"|\" costituisce il valore che indica il sentimento, codificato come segue

Valore numerico | Sentimento
------------ | -------------
0.0 | Negativo
1.0 | Positivo

La stringa che segue il carattere \"|\" rappresenta il tweet da analizzare.



## Funzionamento

Il funzionamento del classificatore prevede l'utilizzo di un algoritmo di classificazione denominato L-BFG

I dovuti approfondimenti saranno discussi nella tesi di laurea associata al progetto.

## Configurazione

Il file [config.sp](https://github.com/mascarock/sentiplus/blob/master/target/data/config.sp) contiene tre righe, che rappresentano

Riga| Significato
------------ | -------------
1 | Random Seed per mischiare i tweet positivi
2 | Random Seed per mischiare i tweet negativi
3 | Numero di Features

Il random Seed è qualsiasi numero intero a 32 bit, utilizzato per controllare la sequenza generata da un'esecuzione di un algoritmo di divisione randomica. Ciascun Seed corrisponde a una e una sola sequenza. La più piccola modifica di quel numero potrebbe generare in una squenza completamente diversa.

Il numero di features rappresenta il numero di parole per allenare il modello matematico.

## Esecuzione

Prima di eseguire il file controllare i requisiti di sistema nel file [REQUIREMENTS](https://github.com/mascarock/sentiplus/blob/master/REQUIREMENTS)

Se si vuole eseguire Sentiplus compilato, digitare i seguenti comandi, all'interno della cartella "sentiplus" clonata dal repository.

```shell
$ cd target
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-numVersione.jar 1 

```

Se si vuole compilare ed eseguire in autonomia dopo aver modificato o meno il sorgente tramite un IDE che supporta Maven, eseguire lo script make dalla cartella sentiplus.

```shell
$ ./make
```

#### Dataset "Politica 2015"

Questo comando utilizza il dataset "1" riguardante il topic "politica2015"

```shell
$ cd target
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-7.jar 1 
```
ottenendo:

```
[...]

Tweet in esame:  pansa vi spiego xch renzi salva marino e xch la pagher cara sfacelo & drammadi mafiacapitale e dintorni 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Area sotto la curva PR = 18.482% 
Area sotto la curva ROC = 78.409%

++++++++ RISULTATI ++++++++++
Sono stati processati 890 tweet, così divisi
> Valore positivo: 180
> Valore negativo: 710

L'insieme train è costituito da 296 tweet, così diviso: 
> Train Set Negativo: 21.31% totale: 150 su: 710
> Train Set Positivo: 84.056% totale: 146 su: 180

L'insieme test è costituito da 594 tweet così diviso: 
> Test Set Negativo: 78.69% totale: 560 su: 710
> Test Set Positivo: 15.944% totale: 34 su: 180

Identificati: 440 su 594
> Accuratezza: 74.074%
> Precisione: 14.286
> Richiamo: 70.588



```
#### Dataset "Twitter Sentiment Analysis Corpus"

Questo comando utilizza una versione ridotta del dadaset "2" [Twitter Sentiment Analysis Corpus](http://thinknook.com/twitter-sentiment-analysis-training-corpus-dataset-2012-09-22/)
e processa 120 milioni di tweet.

```shell
$ cd target
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-7.jar 2 12000000
```
ottenendo:

```
[...]

Tweet in esame:  slumdog millionaire is a decent flick hadn t seen it till tonight i m just home and prob wont sleep tiil the early hours boo 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Tweet in esame: smart bro is breaking my heart 
> Previsione:  1.0
> Etichetta: 0 
NO :( 

Tweet in esame:  smh im sick with this bug that s going around almost everyone i know is sick including me 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Area sotto la curva PR = 83.321% 
Area sotto la curva ROC = 81.625%

++++++++ RISULTATI ++++++++++
Sono stati processati 1200000 tweet, così divisi
> Valore positivo: 622841
> Valore negativo: 577159

L'insieme train è costituito da 972350 tweet, così diviso: 
> Train Set Negativo: 84.206% totale: 486154 su: 577159
> Train Set Positivo: 78.03% totale: 486196 su: 622841

L'insieme test è costituito da 227650 tweet così diviso: 
> Test Set Negativo: 15.794% totale: 91005 su: 577159
> Test Set Positivo: 21.97% totale: 136645 su: 622841

Identificati: 174625 su 227650
> Accuratezza: 76.708%
> Precisione: 82.902
> Richiamo: 77.095


```

Per analizzare più tweet, se la macchina possiede sufficiente memoria, è possibile scaricare il dataset completo

```shell
$ wget http://thinknook.com/wp-content/uploads/2012/09/Sentiment-Analysis-Dataset.zip
```

scompattarlo nella cartella /data/ dandogli il nome "dataset.csv"


---

a Cura di Niccolò Mascaro

Realizzato nell'ambito del tirocinio interno presso l'Università di Roma "Sapienza"



Sentiplus - a Classifier for twitter sentiment analysis.

Note: this software is documented and written in italian.

If you want a translation or explanation please MP me
