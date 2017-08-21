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
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-#VERSION#-SNAPSHOT.jar 1 

```

Se si vuole compilare ed eseguire in autonomia dopo aver modificato o meno il sorgente tramite un IDE che supporta Maven, eseguire lo script make dalla cartella sentiplus.

```shell
$ ./make
```

#### Esempi di esecuzione 

Questo comando utilizza il dataset "1" riguardante il topic "politica2015"
```shell
$ cd target
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-6.5-SNAPSHOT.jar 1 
```
ottenendo:

```

[...]

++++++++ RISULTATI ++++++++++
Identificati: 555 su 612
Accuratezza: 90.68%
Precisione: 9.09
Richiamo: 5.12

grazie per aver usato Sentiplus.


```

Questo comando utilizza il dataset "2" [Twitter Sentiment Analysis Corpus](http://thinknook.com/twitter-sentiment-analysis-training-corpus-dataset-2012-09-22/)

```shell
$ cd target
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-6.5-SNAPSHOT.jar 2 9600
```
ottenendo:

```
[...]

Identificati: 2269 su 3077
> Train Set Negativo: 57.18531468531468% totale: 3271.0 su: 5720.0
> Train Set Positivo: 83.81026037638567% valore: 3251.0 su: 3879.0
Accuratezza: 73.741%
Precisione: 23.214
Richiamo: 12.42


grazie per aver usato Sentiplus.
```

---

a Cura di Niccolò Mascaro

Realizzato nell'ambito del tirocinio interno presso l'Università di Roma "Sapienza"



Sentiplus - a Classifier for twitter sentiment analysis.

Note: this software is documented and written in italian.

If you want a translation or explanation please MP me
