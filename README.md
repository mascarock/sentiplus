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

Prima di eseguire il file controllare i requisiti di sistema nel file "REQUIREMENTS"

Se si vuole eseguire il codice già compilato, eseguire i seguenti comandi, all'interno della cartella "sentiplus" clonata dal repository.

```shell
$ cd target
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-4.0-SNAPSHOT.jar
```

Se si vuole compilare in autonomia dopo aver modificato o meno il sorgente, eseguire lo script  make
```shell
$ ./make
```

Questo è un estratto di risultato che si dovrebbe ottenere eseguendo il comando spark-submit
```shell

[...]

Tweet in esame: marino gli ha preparato un buon intrallazzo e lui e  immediatamente pa ito   si pa ito di testa
> Previsione:  0.0
> Etichetta: 0
OK! :)

Tweet in esame:  mafiacapitale  alemanno e marino conoscevano le irregolarit  negli appalti
> Previsione:  0.0
> Etichetta: 0
OK! :)

Tweet in esame:  chiocci    alemanno mi chiese di incontrare buzzi   mafiacapitale   si arricchisce ogni giorno di pi
> Previsione:  0.0
> Etichetta: 0
OK! :)


++++++++ RISULTATI ++++++++++
Identificati: 555 su 612
Accuratezza: 90.68627450980392%
Precisione: 9.090909090909092
Richiamo: 5.128205128205128

grazie per aver usato Sentiplus.


```

