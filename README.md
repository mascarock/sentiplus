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

Il funzionamento del classificatore prevede l'utilizzo di un algoritmo di classificazione denominato L-BFGS

I dovuti approfondimenti saranno discussi nella tesi di laurea associata al progetto.

## Configurazione

Il file [config.sp](https://github.com/mascarock/sentiplus/blob/master/target/data/config.sp) contiene sei righe, che rappresentano

Riga| Significato
------------ | -------------
1 | Random Seed per mischiare i tweet positivi
2 | Random Seed per mischiare i tweet negativi
3 | Numero di Features
4 | Valore percentuale per il train dei tweet positivi
5 | Valore percentuale per il train dei tweet negativi
6 | Valore booleano per definire quantità tweet da testare (0: automatico, 1: tutti)

Il random Seed è qualsiasi numero intero a 32 bit, utilizzato per controllare la sequenza generata da un'esecuzione di un algoritmo di divisione randomica. Ciascun Seed corrisponde a una e una sola sequenza. La più piccola modifica di quel numero potrebbe generare in una squenza completamente diversa.

Il numero di features rappresenta il numero di parole per allenare il modello matematico.

Il valore percentuale (0.6 per indicare 60%) permette di definire quanti tweet utilizzare per allenare il modello, sia per i tweet positivi che negativi.

Il valore booleano permette di stabilire se utilizzare tutto l'insieme dei tweet (1) oppure l'insieme di test, definito come 1 - train ovvero se abbiamo usato il 60% dell'insieme train, allora il test sarà necessariamente il 40%.


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
e processa più di un milione di tweet

```shell
$ cd target
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-7.jar 2 1550000
```
ottenendo:

```
[...]

Tweet in esame: time to hit the gym see y all in a bit 
> Previsione:  1.0
> Etichetta: 0 
NO :( 

Tweet in esame: time to hit the sack i m feeling a bit sick hopefully cold is not getting near me 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Tweet in esame: time to lay down and unwind missing him like crazy 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Area sotto la curva PR = 79.83% 
Area sotto la curva ROC = 83.083%

++++++++ RISULTATI ++++++++++
Sono stati processati 1550000 tweet, così divisi
> Valore positivo: 778104
> Valore negativo: 771896

L'insieme train è costituito da 1302165 tweet, così diviso: 
> Train Set Negativo: 84.338% totale: 650986 su: 771896
> Train Set Positivo: 83.665% totale: 651179 su: 778104

L'insieme test è costituito da 247835 tweet così diviso: 
> Test Set Negativo: 15.662% totale: 120910 su: 771896
> Test Set Positivo: 16.335% totale: 126925 su: 778104

Identificati: 192095 su 247835
> Accuratezza: 77.509%
> Precisione: 78.131
> Richiamo: 77.885



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
