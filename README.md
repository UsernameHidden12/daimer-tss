# Intervalle Verschmelzen

## Aufgabenbeschreibung

Implementieren Sie eine Funktion MERGE die eine Liste von Intervallen entgegennimmt und als Ergebnis wiederum eine Liste
von Intervallen zurückgibt. Im Ergebnis sollen alle sich überlappenden Intervalle gemerged sein. Alle nicht
überlappenden Intervalle bleiben unberührt.

### Beispiel:

```
Input: [25,30] [2,19] [14, 23] [4,8] 
Output: [2,23] [25,30]
```

### Fragen

#### Wie ist die Laufzeit Ihres Programms?

*Annahme: Parsen des Inputs läuft pro Element in O(1)*

1. Das Program erhält einen Input welchen es in eine Liste von Listen transformiert.
    1. Liste generieren: `n * O(1)`
2. Der darauf folgende Schritt gibt eine transformation von einer Liste von Listen in eine Liste von Intervallen.
    1. Transformation von Liste von Listenelement in Liste von Interval: `n * O(1)`
3. Die Intervalle werden iterativ in eine
   Collection [Java TreeMap](https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html) eingefügt.
    1. Einfügen kostet einmalig:
        1. Interval schneidet kein bisheriges: `O(log(n))`
        2. Interval schneidet bisheriges: `m * O(log(n))` wobei mit `m` die Anzahl der Überschneidungen gemeint ist. In
           diesem Fall kommt es neben `m` neuen Intervallen zu `m` Löschungen von Elementen in der Liste welche ebenso
           pro Operation `O(log(n))` dauern. Total also `m * O(log(n))`
    2. Einfügen der Liste von Intervallen: `n * m * O(log(n))`
4. Die fertige Collection wird nun durchiteriert und eine Liste von Listen über eine Liste von Intervallen wieder
   generiert.
    1. Iterieren: `k * O(1)`
    2. Liste aus Interval: `k * O(1)`
    3. Liste aus Liste von Intervallen: `k * O(1)`

Anmerkung: `n` >= `m` >= `k`

Gesamtkosten:

* Best-Case: `O(n)`
* Expected: `O(n * log(n))`
* Worst-Case: `O(n * m * log(n))`

#### Wie kann die Robustheit sichergestellt werden, vor allem auch mit Hinblick auf sehr große Eingaben?

* Annahme: Große Eingaben bedeutet große Datenmengen
    * Annahme: Eingabe kann komplett geparst werden von der Gegenstelle und kommt in einer Operation
        * Das Program ist darauf ausgelegt möglichst effizient mit dem Speicher umzugehen. Es werden nur Intervalle
          beibehalten, die im aktuellen Moment disjunkt sind. Sich überschneidende Intervalle werden sofort gemergt und
          damit Speicherverbrauch minimal gehalten.
    * Annahme: Eingabe nicht komplett lesbar in einem Stück
        * In mehreren Operationen in die Collection einfügen und erst nach komplettem Einfügen die gemergten Intervalle
          lesen.
* Annahme: Große Eingaben bedeutet große Zahlen
    * Typisierung auf `BigInteger` bzw `BigDecimal` setzen bei der Instanziierung.

#### Wie verhält sich der Speicherverbrauch ihres Programms?

*Annahme: Stack kostet keinen Speicher.*

Das Program ist dahingehend ausgelegt, dass überschneidende Intervalle sofort gemergt werden. Dies erlaubt es dem
Program sich speichereffizient zu verhalten. Der Speicher ist damit limitiert auf das Maximum an disjunkten Intervallen,
welche in der Einfügereihenfolge existieren.

* Best-Case: `O(1)` (das größte Interval welches alle überdeckt kommt als erstes)
* Worst-Case: `O(n)` (kein Interval überschneidet sich)

# Dokumentation

## Fachlich

Getroffene Annahmen:

* Input und Output erfüllen das Pattern: `[[x_0, y_0], [x_1, y_1]]` wobei `[x_0, y_0]` ein geschlossenes Interval
  von `x_0` bis `y_0` abdeckt.
* Intervalle sind nie negativ, sprich `x_0` <= `y_0`.
* Der Datentyp muss `int compareTo(T other)` erfüllen für eine Ordnung. Sonst ist er generisch.
* Die verschmolzenen Intervalle dürfen aufsteigend sortiert zurückgeben werden.
* Input erfolgt über STDIN, Output über STDOUT.
* Der gewünschte Input ist von Typ `BigDecimal` für das Testen der Applikation über STDIN/STDOUT.

Benötigte Arbeitszeit (geschätzt):

* Research: 2h
* Code + Refactorings: 8h
* Testen: 2h
* Build-Artefakte: 1h
* Dokumentation: 2h
* **Total: 15h**

## Technisch

### Build

Requirements:

* Java 11
* maven

JAR-Generierung zum Ausführen:

```
mvn clean verify
```

### Run

```
java -jar target/intervalMerger-1.0.0.jar
```

### Testabdeckung
Siehe JaCoCo Coverage Report unter `./target/site/jacoco/index.html`