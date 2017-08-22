# Sentiplus
Un classificatore per individuare il sentimento dei tweet

L'obiettivo di Sentiplus è indidivudare il sentimento di un dataset di tweet strutturato come segue:

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


## Esecuzione

Prima di eseguire il file controllare i requisiti di sistema nel file [REQUIREMENTS](https://github.com/mascarock/sentiplus/blob/master/REQUIREMENTS)

Se si vuole lanciare Sentiplus compilato, eseguire i seguenti comandi, all'interno della cartella "sentiplus" clonata dal repository.

```shell
$ cd target
$ spark-submit --class app.SentiPlus --master local[2] Sentiplus-#VERSION-SNAPSHOT.jar 1 

```


Se si vuole compilare ed eseguire in autonomia dopo aver modificato o meno il sorgente, eseguire lo script make dalla cartella sentiplus

```shell
$ ./make
```

Questo è un estratto di risultato che si dovrebbe ottenere eseguendo il comando di cui sopra
```

[...]

Tweet in esame:  mafiacapitale 96 giornalisti denunciati esposto imbarazzante 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Tweet in esame:  chiocci alemanno mi chiese di incontrare buzzi mafiacapitale si arricchisce ogni giorno di pi 
> Previsione:  0.0
> Etichetta: 0 
OK! :) 

Tweet in esame:  piazzapulita i candidati alla successione di marino stanno gi pensando alle strategie alternative per rifare un altra mafiacapitale
> Previsione:  0.0
> Etichetta: 0 
OK! :) 



++++++++ RISULTATI ++++++++++
Sono stati processati 890 tweet, così divisi
> Valore positivo: 180
> Valore negativo: 710

L'insieme train è costituito da: 292 tweet, così diviso: 
> Train Set Negativo: 21.31% totale: 146 su: 710
> Train Set Positivo: 84.056% totale: 146 su: 180

L'insieme test è costituito da: 598 così diviso: 
> Test Set Negativo: 78.69% totale: 564 su: 710
> Test Set Positivo: 15.944% totale: 34 su: 180

Identificati: 480 su 598
> Accuratezza: 80.268%
> Precisione: 3.333
> Richiamo: 8.824

Grazie per aver usato Sentiplus. 


```




---

a Cura di Niccolò Mascaro

Realizzato nell'ambito del tirocinio interno presso l'Università di Roma "Sapienza"



Sentiplus - a Classifier for twitter sentiment analysis.

Note: this software is documented and written in italian.

If you want a translation or explanation please MP me
