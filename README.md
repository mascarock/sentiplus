# Sentiplus
Un classificatore per individuare il sentimento dei tweet

L'obiettivo di Sentiplus è indidivudare il sentimento di un dataset di tweets già etichettato strutturato in modo che sia facilmente estraibile il valore numerico che identifica il sentimento e la stringa che rappresenta il tweet da analizzare. 

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

Tweet in esame:  chiocci alemanno mi chiese di incontrare buzzi mafiacapitale si arricchisce ogni giorno di pi 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Tweet in esame:  piazzapulita i candidati alla successione di marino stanno gi pensando alle strategie alternative per rifare un altra mafiacapitale
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Tweet in esame:  pansa vi spiego xch renzi salva marino e xch la pagher cara sfacelo & drammadi mafiacapitale e dintorni 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 


++++++++ RISULTATI ++++++++++
Sono stati processati 890 tweet, così divisi
> Valore positivo: 180
> Valore negativo: 710

L'insieme train è costituito da: 315 tweet, così diviso: 
> Train Set Negativo: 21.31% totale: 151 su: 710
> Train Set Positivo: 84.056% totale: 164 su: 180

L'insieme test è costituito da: 575 così diviso: 
> Test Set Negativo: 78.69% totale: 559 su: 710
> Test Set Positivo: 15.944% totale: 16 su: 180

Identificati: 489 su 575
> Accuratezza: 85.043%
> Precisione: 1.389
> Richiamo: 6.25
.


```
#### Dataset "Twitter Sentiment Analysis Corpus"

Questo comando utilizza il dataset "2" [Twitter Sentiment Analysis Corpus](http://thinknook.com/twitter-sentiment-analysis-training-corpus-dataset-2012-09-22/)

```shell
$ cd target
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-7.jar 2 8500
```
ottenendo:

```
[...]

Tweet in esame:  yancey replaced me p s i m going all in now it s now or never 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Tweet in esame:  fwah big d s was closed when we went there just now pftttttt ima go to bed now gonna wake up in 6hrs time 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Tweet in esame:  i drank milk so now im so sick seriously 
> Previsione:  1.0
> Etichetta: 0 
NO :( 

++++++++ RISULTATI ++++++++++
Sono stati processati 8500 tweet, così divisi
> Valore positivo: 3260
> Valore negativo: 5240

L'insieme train è costituito da: 5545 tweet, così diviso: 
> Train Set Negativo: 52.719% totale: 2796 su: 5240
> Train Set Positivo: 84.739% totale: 2749 su: 3260

L'insieme test è costituito da: 2955 così diviso: 
> Test Set Negativo: 47.281% totale: 2444 su: 5240
> Test Set Positivo: 15.261% totale: 511 su: 3260

Identificati: 2299 su 2955
> Accuratezza: 77.8%
> Precisione: 19.665
> Richiamo: 9.198

```

---

a Cura di Niccolò Mascaro

Realizzato nell'ambito del tirocinio interno presso l'Università di Roma "Sapienza"



Sentiplus - a Classifier for twitter sentiment analysis.

Note: this software is documented and written in italian.

If you want a translation or explanation please MP me
