# Sentiplus 
Un classificatore per individuare il sentimento dei tweet 


Questo progetto serve ad indidivudare il sentimento dato un file di testo strutturato come segue:

> 0.0|#Marino si e' dimessoooo??? Non ci credo!!!   #MAFIACAPITALE  #Romaliberata

L'elemento numerico che precede il carattere "|" costituisce il valore che indica il sentimento, codificato come segue

Valore numerico | Sentimento
------------ | -------------
0.0 | Negativo
1.0 | Positivo

La stringa che segue il carattere "|" rappresenta il tweet da analizzare.

---
Sentiplus - a Classifier for twitter sentiment analysis.

Note: this software is documentend and written in italian. 

If you want a translation or explanation please MP me

## Esecuzione

Se si vuole eseguire il codice già compilato, eseguire i seguenti comandi, all'interno della cartella "sentiplus" clonata dal repository.

```shell
$ cd target
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-4.0-SNAPSHOT.jar
```

Se si vuole compilare in autonomia dopo aver modificato o meno il sorgente, eseguire lo script  make
```shell
$ ./make
```


