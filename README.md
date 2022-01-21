# Linguaggio MyFun

Specifica del linguaggio MyFun implementato all'interno del progetto:

## Regole di precedenze

Sono state utilizzate le seguenti regole di precedenza dalla più alta alla più bassa. Eventuali altre ambiguità sono state risolte tramite l'uso dell'associativià sinistra (se non indicata nella tabella):

| Operatore         | Precendenza |
| ----------------- | ----------- |
| ( )               |             |
| ^                 | destra      |
| \* / div          |
| \+ -              |             |
| &                 |             |
| = != <> < <= > >= | nessuna     |
| not               | destra      |
| and               |             |
| or                |             |

**N.B.:** Sono state codificate in javacup solo quelle necessarie all'eliminazione dei conflitti.

## Gestione degli errori

Sono stati generati i seguenti errori:

* **Stringa costante non completata:** nel caso in cui il programma in input presenti una stringa
costante aperta ma non chiusa (es. "questa è una stringa non chiusa ).
* **Commento non chiuso:** nel caso in cui il programma in input presenti un commento non chiuso
(es. /* questo è un commento non chiuso )

**N.B.:** in entrambi i casi si raggiunge l'EOF mentre si sta riconoscendo un commento
o una stringa. Se si usano gli stati jflex (ad es. COMMENT e STRING), questo si
traduce nell'incontrare un EOF mentre si è nel corrispondente stato.

## Specifica lessicale

### Commenti

All'interno del linguaggio i commenti iniziano con **#** oppure con **//**. Inoltre un blocco di commenti è delimitato da **#\*** e  **#**.

### Parole chiave

Nel linguaggio sono implementate le seguenti parole chiave:

| Identificativo | Valore                                          |
| -------------- | ----------------------------------------------- |
| MAIN           | main                                            |
| ID             | /[\$_A-Za-z][$_A-Za-z0-9]*/                     |
| INTEGER        | integer                                         |
| STRING         | string                                          |
| REAL           | real                                            |
| BOOL           | bool                                            |
| LPAR           | (                                               |
| RPAR           | )                                               |
| COLON          | :                                               |
| FUN            | fun                                             |
| END            | end                                             |
| IF             | if                                              |
| THEN           | then                                            |
| ELSE           | else                                            |
| WHILE          | while                                           |
| LOOP           | loop                                            |
| READ           | %                                               |
| WRITE          | ?                                               |
| WRITELN        | ?.                                              |
| WRITEB         | ?,                                              |
| WRITET         | ?:                                              |
| ASSIGN         | :=                                              |
| PLUS           | +                                               |
| MINUS          | -                                               |
| TIMES          | *                                               |
| DIVINT         | div                                             |
| DIV            | /                                               |
| POW            | ^                                               |
| STR_CONCAT     | &                                               |
| EQ             | =                                               |
| NE             | <> or !=                                        |
| LT             | <                                               |
| LE             | <=                                              |
| GT             | >                                               |
| GE             | >=                                              |
| AND            | and                                             |
| OR             | or                                              |
| NOT            | not                                             |
| NULL           | null                                            |
| TRUE           | true                                            |
| FALSE          | false                                           |
| INTEGER_CONST  | any integer number (sequence of decimal digits) |
| REAL_CONST     | any real number                                 |
| STRING_CONST   | any string between " or between                 |
| SEMI           | ;                                               |
| COMMA          | ,                                               |
| RETURN         | return                                          |
| OUTPAR         | @                                               |
| VAR            | var                                             |
| OUT            | out                                             |
| LOOP           | loop                                            |

## Specifiche Grammaticali

### Struttura di base

Un programma è strutturato come:

1. Una **lista di dichiarazione di variabili**.
2. Una **lista di funzioni**.
3. Un **Main**.

```c
Program ::= VarDeclList FunList Main
```

### Dichiarazioni di variabili

Una dichiarazione di una variabile è composta da:

* Un **tipo**.
* Una **lista di ID**.
* Un **punto e virgola**.

Oppure:

* La parola chiave **VAR**.
* Una **lista di inizizializzazioni**.
* Una **punto e virgola**.

```c
VarDecl ::= Type IdListInit SEMI
 | VAR IdListInitObbl SEMI
```

Una lista di dichiarazioni di variabili può essere **vuota** oppure composta da:

* Una **dichiarzione di variabile**.
* Una **lista di dichiarazione di variabili**.

```c
VarDeclList ::= ε 
 | VardDecl VarDeclList
```

#### Gestione dei Tipi

Un tipo è defito come segue:

```c
Type ::= INTEGER | BOOL | REAL | STRING  
```

#### Gestione degli ID

Una lista semplice di **ID** e composta da una **ID** oppure da:

* Una **lista di ID**.
* Un **virgola**.
* Un **ID**.

```c
IdList ::= ID 
 | IdList COMMA ID
```

Una lista di inizializazzioni di ID e composta da un **ID** oppure da:

* Una **lista di inizializzazioni**.
* Una **virgola**.
* Un **ID**.

Oppure:

* Un **ID**.
* Un **assegnazione**.
* Un **espressione**.

Oppure:

* Una **lista di Inizializzazioni**.
* Una **virgola**.
* Un **ID**.
* Un **assegnazione**.
* Un **espressione**.

```c
IdListInit ::= ID 
 | IdListInit COMMA ID
 | ID ASSIGN Expr
 | IdListInit COMMA ID ASSIGN Expr
```

Una lista di iniziallizazioni obbligatorie è definita come:

* Un **ID**.
* Un **assegnamento**.
* Una **costante**.

Oppure:

* Una **lista di assegnazioni obbligatorie**.
* Una **virgola**.
* Un **ID**.
* Un **assegnamento**.
* Una **costante**.

```c
IdListInitObbl ::= ID ASSIGN Const
 | IdListInitObbl COMMA ID ASSIGN Const
```

#### Gestione delle costanti

Una costante è definita nel seguente modo:

```c
Const ::= INTEGER_CONST | REAL_CONST | TRUE | FALSE | STRING_CONST
```

### Dichiarazioni di funzioni

Una funzione è definita come:

* Parola chiave **FUN**.
* Un **ID**.
* Una **parentesi aperta**.
* Una **lista di Parametri**.
* Una **parentesi chiusa**.
* **Due punti**.
* Un **tipo di ritorno**.
* Una **lista di dichiarazioni di variabili**.
* Una **lista di statement**.
* La parola chiave **END FUN**.
* Una **virgola**.

Oppure:

* Parola chiave **FUN**.
* Un **ID**.
* Una **parentesi aperta**.
* Una **lista di Parametri**.
* Una **parentesi chiusa**.
* Una **lista di dichiarazioni di variabili**.
* Una **lista di statement**.
* La parola chiave **END FUN**.
* Una **virgola**.

```c
Fun := FUN ID LPAR ParamDeclList RPAR COLON Type VarDeclList StatList END FUN SEMI 
 | FUN ID LPAR ParamDeclList RPAR VarDeclList StatList END FUN SEMI
```

Una lista di funzioni è definita come la **parola vuota** oppure:

* Una **funzione**.
* Una **lista di funzioni**.

```c
FunList ::= ε  
 | Fun FunList
```

#### Chiamate di funzioni

Una chiamata di funzione è definita come:

* Un **ID**.
* Una **paratesi aperta**.
* Una **lista di espressioni**.
* Una **parentesi chiusa**.

Oppure:

* Un **ID**.
* Una **parentesi aperta**.
* Una **parentesi chiusa**.

```c
CallFun ::= ID LPAR ExprList RPAR   
 | ID LPAR RPAR 
```

#### Gestione dei parametri

Un parametro è definito come:

* Un **tipo**.
* Un **ID**.
  
Oppure:

* La parola chiave **OUT**.
* Un **tipo**.
* Un **ID**.

```c
ParDecl ::= Type ID
 | OUT Type ID
```

Una lista di parametri è definita come la **parola vuota** oppure:

* Una **lista non vuota di parametri**.

```c
ParamDeclList ::= ε
 | NonEmptyParamDeclList
```

Una lista non vuota di paramentri è definita come una **dichiarazione di parametro** oppure:

* Una **lista non vuota di parametri**.
* Una **virgola**.
* Una **dichiarazione di parametro**.

```c
NonEmptyParamDeclList ::= ParDecl
 | NonEmptyParamDeclList COMMA ParDecl
```

### Corpo Main

Il corpo Main è definito come:

* La parola chiave **MAIN**
* Una **lista di dichiarazioni**.
* Una **lista di statement**.
* La parola chiave **END MAIN**.
* una **virgola**.
  
```c
Main ::= MAIN VarDeclList StatList END MAIN SEMI
```

#### Gestione degli statement

Uno statement è definito come uno dei seguenti:

```c
Stat ::= IfStat SEMI
 | WhileStat SEMI
 | ReadStat SEMI
 | WriteStat SEMI
 | AssignStat SEMI
 | CallFun SEMI
 | RETURN Expr SEMI
 | ε
```

Una lista di statement è definita come uno **statement** oppure:

* Uno **statement**.
* Una **lista di statement**.

```c
StatList ::= Stat 
  | Stat StatList
```

##### Statement di assegnamento

Uno statement di assegnamento è definito come:

* Un **ID**.
* Un **operazione di assegnamento**.
* Un **espressione**.

*

```c
AssignStat ::=  ID ASSIGN Expr
```

##### Statement di lettura

Uno statement di lettura è definito come:

* La parala chiave **READ**.
* Una **lista di ID**.
* Un **espressione**.

oppure:

* La parola chiave **READ**.
* Una **lista di ID**.
  
```c
ReadStat ::= READ IdList Expr // Expr deve essere di tipo stringa
 | READ IdList
```

##### Statement di Scrittura

Uno statement di scrittura è definit come:

* La parola chive **WRITE**.
* Un **espressione**.

Oppure:

* La parola chiave **WRITELN**.
* Un **espressione**.
  
Oppure:

* La parole chiave **WRITET**.
* Un **espressione**.
  
Oppure:

* La parole chiave **WRITEB**.
* Un **espressione**.

```c
WriteStat ::=  WRITE  Expr 
 | WRITELN  Expr 
 | WRITET  Expr
 | WRITEB  Expr 
```

##### Statement IF

Lo statement if è composta da:

* La parola chiave **IF**.
* Un **espressione**.
* La parola chiave **THEN**.
* Una **lista di dichiarazione di variabili**.
* Una **lista di statement**.
* Un **Else**.
* La parola chiave **END IF**.

```c
IfStat ::= IF Expr THEN VarDeclList StatList Else END IF
```

Lo statement Else è composto da la parola vuota oppure:

* La parola chiave **ELSE**.
* Una **lista di dichiarazioni di variabili**.
* Una **lista di statement**.

```c
Else ::= ε
 | ELSE VarDeclList  StatList
```

##### Statement WHILE

Lo statement while è composta da:

* La parola chiave **WHILE**.
* Un **espressione**.
* La parola chiave **LOOP**.
* Una **lista di dichiarazioni di variabili**.
* Una **lista di statement**.
* La parola chiave **END LOOP**.

```c
WhileStat ::= WHILE Expr LOOP VarDeclList  StatList END LOOP
```

#### Gestione delle espressioni

Una espressione è composta nel seguente modo:

```c
Expr ::= TRUE                            
 | FALSE                           
 | INTEGER_CONST                    
 | REAL_CONST
 | STRING_CONST
 | ID
 | CallFun
 | Expr  PLUS Expr
 | Expr  MINUS Expr
 | Expr  TIMES Expr
 | Expr  DIV Expr
 | Expr  DIVINT Expr
 | Expr  AND Expr
 | Expr POW Expr
 | Expr STR_CONCAT Expr
 | Expr  OR Expr
 | Expr  GT Expr
 | Expr  GE Expr
 | Expr  LT Expr
 | Expr  LE Expr
 | Expr  EQ Expr
 | Expr  NE Expr
 | MINUS Expr
 | NOT Expr
 | LPAR Expr RPAR
```

Una lista di espressioni è composta da un **espressione** oppure:

* Un **espressione**.
* Una **virgola**.
* Una **lista di espressioni**.

Oppure:

* Una **chiocchiola**.
* Un **ID**.
  
Oppure:

* Una **chiocchiola**.
* Un **ID**.
* Una **virgola**.
* Una **lista di espressioni**.

```c
ExprList ::= Expr 
 | Expr COMMA ExprList
 | OUTPAR ID
 | OUTPAR ID COMMA ExprList
```
