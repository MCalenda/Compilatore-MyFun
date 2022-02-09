# Linguaggio MyFun

Il seguente documento contiene la specifica del linguaggio MyFun implementato all'interno del progetto.

### Configurazione IntelliJ IDEA:

- Project SDK: **17**
- Project Language Level: **SDK Default**
- Maven: **Utilizzato (pom.xml versione MacOS su Gitlab)**

**GENERAZIONE PARSER,LEXER E JAR:** Si consiglia di utilizzare le configurazioni fornite in IntelliJ.

**ISTRUZIONI ESECUZIONE:** Si consiglia di utilizzare per il lancio del progetto le configurazioni create in IntelliJ (eventualmente modificando i path per aggiungere file esterni), inoltre è stato fornito il test file 8, vuoto per l'eventuale aggiunta di codice di test. 

**ATTENZIONE:** Il file valid2.txt contiene alcuni errori sintattici che sono stati corretti, rispetto alla versione originiale (' all'interno di stringhe, e l'utilzzo della variabile continue, keyword di C).

Indice dei contenuti:

- [Analisi Lessicale](#analisi-lessicale)
- [Analisi Sintattica](#analisi-sintattica)
- [Analisi Semantica e Generazione del Codice Intermedio](#analisi-semantica-e-generazione-del-codice-intermedio)
- [Bug Noti e Scelte Progettuali Fatte](#bug-noti-e-scelte-progettuali-fatte)

## Analisi Lessicale

Per l'analisi lessicale si è utilizzato **JFLEX**, per la generazione automatica del **LEXER**, le specifiche necessarie sono state inserite all'interno del file **MyFun.flex**.

### Gestione degli errori

Sono stati generati i seguenti errori:

- **Stringa costante non completata:** nel caso in cui il programma in input presenti una stringa
  costante aperta ma non chiusa (es. "questa è una stringa non chiusa ).
- **Commento non chiuso:** nel caso in cui il programma in input presenti un commento non chiuso
  (es. /\* questo è un commento non chiuso )

**N.B.:** in entrambi i casi si raggiunge l'EOF mentre si sta riconoscendo un commento
o una stringa. Se si usano gli stati jflex (ad es. COMMENT e STRING), questo si
traduce nell'incontrare un EOF mentre si è nel corrispondente stato.

### Commenti

All'interno del linguaggio i commenti iniziano con **#** oppure con **//**. Inoltre un blocco di commenti è delimitato da **#\*** e **#**.

### Parole chiave

Nel linguaggio sono implementate le seguenti parole chiave:

| Identificativo | Valore                                         |
| -------------- | ---------------------------------------------- |
| MAIN           | main                                           |
| ID             | /[\$\_A-Za-z][$_a-za-z0-9]\*/                  |
| INTEGER        | integer                                        |
| STRING         | string                                         |
| REAL           | real                                           |
| BOOL           | bool                                           |
| LPAR           | (                                              |
| RPAR           | )                                              |
| COLON          | :                                              |
| FUN            | fun                                            |
| END            | end                                            |
| IF             | if                                             |
| THEN           | then                                           |
| ELSE           | else                                           |
| WHILE          | while                                          |
| LOOP           | loop                                           |
| READ           | %                                              |
| WRITE          | ?                                              |
| WRITELN        | ?.                                             |
| WRITEB         | ?,                                             |
| WRITET         | ?:                                             |
| ASSIGN         | :=                                             |
| PLUS           | +                                              |
| MINUS          | -                                              |
| TIMES          | \*                                             |
| DIVINT         | div                                            |
| DIV            | /                                              |
| POW            | ^                                              |
| STR_CONCAT     | &                                              |
| EQ             | =                                              |
| NE             | <> or !=                                       |
| LT             | <                                              |
| LE             | <=                                             |
| GT             | >                                              |
| GE             | >=                                             |
| AND            | and                                            |
| OR             | or                                             |
| NOT            | not                                            |
| NULL           | null                                           |
| TRUE           | true                                           |
| FALSE          | false                                          |
| INTEGER_CONST  | (([1-9][0-9]\*)\|(0))                          |
| REAL_CONST     | (([1-9][0-9]\*)\|(0))\.(([0-9]\*[1-9]\+)\|(0)) |
| STRING_CONST   | any string between " or between '              |
| SEMI           | ;                                              |
| COMMA          | ,                                              |
| RETURN         | return                                         |
| OUTPAR         | @                                              |
| VAR            | var                                            |
| OUT            | out                                            |
| LOOP           | loop                                           |

## Analisi Sintattica

Per l'analisi lessicale si è utilizzato il **CUP**, per la generaazione automatica di un **PARSER** di tipo **Top Down**, le specifiche necessarie sono state inserite all'interno del file **MyFun.cup**.

### Regole di precedenze

Sono state utilizzate le seguenti regole di precedenza dalla più alta alla più bassa. Eventuali altre ambiguità sono state risolte tramite l'uso dell'associativià sinistra (se non indicata nella tabella):

| Operatore         | Precendenza |
| ----------------- | ----------- |
| uminus            |             |
| ( )               |             |
| ^                 | destra      |
| \* / div / divint |
| \+ -              |             |
| &                 |             |
| = != <> < <= > >= | nessuna     |
| not               | destra      |
| and               |             |
| or                |             |

**N.B.:** Sono state codificate in javacup solo quelle necessarie all'eliminazione dei conflitti.

### Grammatica

#### Struttura di base

Un programma è composta da:

```c
Program ::= VarDeclList FunList Main
```

#### Main

Un Main è composto da:

```c
Main ::= MAIN VarDeclList StatList END MAIN SEMI
```

#### Gestione delle variabili

##### Dichiarazione di variabile

Una dichiarazione di una variabile è composta da:

```c
VarDecl ::= Type IdListInit SEMI
 | VAR IdListInitObbl SEMI
```

Una lista di dichiarazioni di variabili può essere **vuota** oppure composta da:

```c
VarDeclList ::= ε
 | VardDecl VarDeclList
```

##### Gestione dei Tipi

Un tipo è defito come segue:

```c
Type ::= INTEGER | BOOL | REAL | STRING
```

##### Gestione degli ID

Una lista semplice di **ID** e composta da una **ID** oppure da:

```c
IdList ::= ID
 | IdList COMMA ID
```

Una lista di inizializazzioni di ID e composta da un **ID** oppure da:

```c
IdListInit ::= ID
 | IdListInit COMMA ID
 | ID ASSIGN Expr
 | IdListInit COMMA ID ASSIGN Expr
```

Una lista di inizializzazioni obbligatorie è definita come:

```c
IdListInitObbl ::= ID ASSIGN Const
 | IdListInitObbl COMMA ID ASSIGN Const
```

##### Gestione delle costanti

Una costante è definita come segue:

```c
Const ::= INTEGER_CONST | REAL_CONST | TRUE | FALSE | STRING_CONST
```

#### Gestione delle funzioni

##### Dichiarazione di funzione

Una dichiarazione di funzione è composta da:

```c
Fun := FUN ID LPAR ParamDeclList RPAR COLON Type VarDeclList StatList END FUN SEMI
 | FUN ID LPAR ParamDeclList RPAR VarDeclList StatList END FUN SEMI
```

Una lista di funzioni puo essere **vuota** o composta da:

```c
FunList ::= ε
 | Fun FunList
```

##### Chiamate di funzioni

Una chiamata di funzione è composta da:

```c
CallFun ::= ID LPAR ExprList RPAR
 | ID LPAR RPAR
```

##### Gestione dei parametri

Un parametro è composto da:

```c
ParDecl ::= Type ID
 | OUT Type ID
```

Una lista di parametri puo essere **vuota** o composta da:

```c
ParamDeclList ::= ε
 | NonEmptyParamDeclList
```

Una lista non vuota di paramentri è composta da:

```c
NonEmptyParamDeclList ::= ParDecl
 | NonEmptyParamDeclList COMMA ParDecl
```

#### Gestione degli statement

##### Statement generico

Uno statement è definito in uno dei seguenti modi:

```c
Stat ::= IfStat SEMI
 | WhileStat SEMI
 | ReadStat SEMI
 | WriteStat SEMI
 | AssignStat SEMI
 | CallFun SEMI
 | RETURN Expr SEMI
```

Una lista di statement puo essere **vuota** o composta da:

```c
StatList ::= ε
  | StatList Stat
```

##### Statement di assegnamento

Uno statement di assegnamento è composto da:

```c
AssignStat ::=  ID ASSIGN Expr
```

##### Statement di lettura

Uno statement di lettura è composto da:

```c
ReadStat ::= READ IdList Expr
 | READ IdList
```

##### Statement di Scrittura

Uno statement di scrittura è definito in uno dei seguenti modi:

```c
WriteStat ::=  WRITE  Expr
 | WRITELN  Expr
 | WRITET  Expr
 | WRITEB  Expr
```

##### Statement IF

Uno statement if è composto da:

```c
IfStat ::= IF Expr THEN VarDeclList StatList Else END IF
```

##### Statement Else

Lo statement Else è composto da la parola vuota oppure:

```c
Else ::= ε
 | ELSE VarDeclList  StatList
```

##### Statement WHILE

Lo statement while è composta da:

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

```c
ExprList ::= Expr
 | Expr COMMA ExprList
 | OUTPAR ID
 | OUTPAR ID COMMA ExprList
```

## Analisi Semantica

Per l'analisi sematica è stato utilizzato il patter **VISITOR** implementato nel file **Semantic_Visitor.java**: per l'analisi semantica;

### Regole di type-checking

Di seguito, le regole di type checking utilizzate all'interno del visitor per l'analisi semantica.

**Costanti**

* <img src="https://latex.codecogs.com/png.image?\bg_white&space;\Gamma&space;\vdash&space;INTEGER\_CONST&space;:&space;integer"/><br>
* <img src="https://latex.codecogs.com/png.image?\bg_white&space;\Gamma&space;\vdash&space;REAL\_CONST&space;:&space;real"/><br>
* <img src="https://latex.codecogs.com/png.image?\bg_white&space;\Gamma&space;\vdash&space;STRING\_CONST&space;:&space;string"/><br>
* <img src="https://latex.codecogs.com/png.image?\bg_white&space;\Gamma&space;\vdash&space;TRUE&space;:&space;boolean"/><br>
* <img src="https://latex.codecogs.com/png.image?\bg_white&space;\Gamma&space;\vdash&space;FALSE&space;:&space;boolean"/><br>
  
**ID**

  <img src="https://latex.codecogs.com/png.image?\bg_white&space;\frac{\Gamma&space;\&space;(id)&space;\&space;=&space;\&space;\tau}{\Gamma&space;\&space;\vdash&space;\&space;id&space;\&space;:&space;\&space;\tau}"/><br>

**Operatori unari**

<img src="https://latex.codecogs.com/png.image?\bg_white&space;\frac{\Gamma&space;\&space;\vdash&space;\&space;e&space;\&space;:&space;\&space;\tau_1&space;\&space;\&space;\&space;optype1(op_1,&space;\tau_1)&space;\&space;=&space;\&space;\tau}{\Gamma&space;\&space;\vdash&space;\&space;op_1&space;\&space;e&space;\&space;:&space;\&space;\tau}"/><br>

Tabella per optype1(op, t)

| op1   | operando | risultato |
| ----- | -------- | --------- |
| MINUS | integer  | integer   |
| MINUS | real     | real      |
| NOT   | bool     | bool      |

**Operatori binari**

<img src="https://latex.codecogs.com/png.image?\bg_white&space;\frac{\Gamma&space;\&space;\vdash&space;\&space;e&space;\&space;:&space;\&space;\tau_1&space;\&space;\&space;\&space;\Gamma&space;\&space;\vdash&space;\&space;e_2&space;\&space;:&space;\&space;\tau_2&space;\&space;\&space;\&space;optype2(op_2,&space;\tau_1,&space;\tau_2)&space;\&space;=&space;\&space;\tau}{\Gamma&space;\&space;\vdash&space;\&space;e_1&space;\&space;op_2&space;\&space;e_2&space;\&space;:&space;\&space;\tau}"/><br>

Tabella per optype2(op, $t_1$, $t_2$)

| op1                     | operando | operando2 | risultato |
| ----------------------- | -------- | --------- | --------- |
| PLUS, MINUS, TIMES, DIV | integer  | integer   | integer   |
| PLUS, MINUS, TIMES, DIV | integer  | real      | real      |
| PLUS, MINUS, TIMES, DIV | real     | integer   | real      |
| PLUS, MINUS, TIMES, DIV | real     | real      | real      |
| DIVINT                  | integer  | integer   | integer   |
| DIVINT                  | real     | integer   | integer   |
| STR_CONCAT              | string   | string    | string    |
| STR_CONCAT              | string   | integer   | string    |
| STR_CONCAT              | string   | real      | string    |
| STR_CONCAT              | string   | bool      | string    |
| AND                     | bool     | bool      | bool      |
| OR                      | bool     | bool      | bool      |
| GT, GE, LT, LE          | integer  | integer   | bool      |
| GT, GE, LT, LE          | integer  | real      | bool      |
| GT, GE, LT, LE          | real     | integer   | bool      |
| GT, GE, LT, LE          | real     | real      | bool      |
| EQ, NE                  | integer  | integer   | bool      |
| EQ, NE                  | real     | real      | bool      |
| EQ, NE                  | integer  | real      | bool      |
| EQ, NE                  | real     | integer   | bool      |
| EQ, NE                  | string   | string    | bool      |
| EQ, NE                  | bool     | bool      | bool      |

**Lista di istruzioni**

<img src="https://latex.codecogs.com/png.image?\bg_white&space;\frac{\Gamma&space;\&space;\vdash&space;\&space;stmt_1&space;\&space;:&space;\&space;notype&space;\&space;\&space;\&space;\Gamma&space;\&space;\vdash&space;\&space;stmt_2&space;\&space;:&space;\&space;notype}{\Gamma&space;\&space;\vdash&space;\&space;stmt_1&space;\&space;;&space;\&space;stmt_2&space;\&space;:&space;\&space;notype}"/><br>

**Assegnazione**

<img src="https://latex.codecogs.com/png.image?\bg_white&space;\frac{\Gamma&space;\&space;(id)&space;\&space;=&space;\&space;\tau&space;\&space;\&space;\&space;\Gamma&space;\&space;\vdash&space;\&space;e&space;\&space;=&space;\&space;\tau}{\Gamma&space;\&space;\vdash&space;\&space;id&space;\&space;:=&space;\&space;e&space;\&space;:&space;\&space;notype}"/><br>

**Chiamata a funzione con o senza tipo di ritorno**

<img src="https://latex.codecogs.com/png.image?\bg_white&space;\frac{\Gamma&space;\&space;\vdash&space;\&space;f&space;\&space;:&space;\&space;\tau_1&space;\&space;\times&space;\&space;...&space;\&space;\times&space;\&space;\tau_n&space;\&space;\rightarrow&space;&space;\&space;\tau&space;\&space;\&space;\&space;\Gamma&space;\&space;\vdash&space;\&space;e_i&space;\&space;:&space;\&space;\tau_i^{i&space;\in&space;1...n}}{\Gamma&space;\&space;\vdash&space;\&space;f(e_1,...,e_n)&space;\&space;:&space;\&space;\tau}"/><br>

<img src="https://latex.codecogs.com/png.image?\bg_white&space;\frac{\Gamma&space;\&space;\vdash&space;\&space;f&space;\&space;:&space;\&space;\tau_1&space;\&space;\times&space;\&space;...&space;\&space;\times&space;\&space;\tau_n&space;\&space;\rightarrow&space;\&space;notype&space;\&space;\&space;\&space;\Gamma&space;\&space;\vdash&space;\&space;e_i&space;\&space;:&space;\&space;\tau_i^{i&space;\in&space;1...n}}{\Gamma&space;\&space;\vdash&space;\&space;f(e_1,...,e_n)&space;\&space;:&space;\&space;notype}"/><br>

**Istruzione while**

<img src="https://latex.codecogs.com/png.image?\dpi{110}&space;\bg_white&space;\frac{\Gamma&space;\&space;\vdash&space;\&space;e&space;\&space;:&space;\&space;boolean&space;\&space;\&space;\&space;\Gamma&space;\&space;\vdash&space;\&space;block&space;\&space;:&space;\&space;notype}{\Gamma&space;\&space;\vdash&space;\&space;\mathbf{while}&space;\&space;e&space;\&space;\mathbf{loop}&space;\&space;block&space;\&space;\mathbf{end&space;\&space;loop}&space;\&space;:&space;\&space;notype}"/><br>

**Istruzione if then else**

<img src="https://latex.codecogs.com/png.image?\dpi{110}&space;\bg_white&space;\frac{\Gamma&space;\&space;\vdash&space;\&space;e&space;\&space;:&space;\&space;boolean&space;\&space;\&space;\&space;\Gamma&space;\&space;\vdash&space;\&space;block&space;\&space;:&space;\&space;notype}{\Gamma&space;\&space;\vdash&space;\&space;\mathbf{if}&space;\&space;e&space;\&space;\mathbf{then}&space;\&space;block&space;\&space;\mathbf{else}&space;\&space;block&space;\&space;\mathbf{end&space;\&space;if}&space;\&space;:&space;\&space;notype}"/><br>

**Istruzione read**

<img src="https://latex.codecogs.com/png.image?\dpi{110}&space;\bg_white&space;\frac{\Gamma&space;\&space;(&space;\&space;id_1,...,id_n&space;\&space;)&space;=&space;\tau_1,...,\tau_n\&space;\&space;\&space;\Gamma&space;\&space;\vdash&space;e&space;:&space;string}{\Gamma&space;\&space;\vdash&space;\&space;\mathbf{READ}&space;\&space;id_1,...,id_n\&space;e&space;\&space;:&space;\&space;notype}"/><br>

**Istruzione write**

<img src="https://latex.codecogs.com/png.image?\dpi{110}&space;\bg_white&space;\frac{\Gamma&space;\&space;\vdash&space;\&space;e&space;\&space;:&space;\&space;\tau}{\Gamma&space;\&space;\vdash&space;\&space;\mathbf{WRITE}&space;\&space;e&space;\&space;:&space;\&space;notype}"/><br>

**Istruzione return**

<img src="https://latex.codecogs.com/png.image?\dpi{110}&space;\bg_white&space;\frac{\Gamma&space;\&space;\vdash&space;e&space;:&space;\tau&space;\&space;\&space;\&space;\Gamma&space;\&space;\vdash&space;f&space;\&space;\rightarrow&space;\tau}{\Gamma&space;\&space;\vdash&space;\&space;\mathbf{return}&space;\&space;e&space;\&space;:&space;\&space;notype}"/><br>

## Generazione del Codice Intermedio

Per la generazione del codice è stato utilizza il pattern **VISITOR** implementato nel file **CodeGen_Visitor.java**: per la generazione del codice intermedio (C).

* Sono state introdotte le librerie **stdio.h**, **stdlib.h**, **stdbool.h** e **string.h** al fine di effettuare le operazioni standard di input/output, effettuare operazioni sulle stringhe (es. strcmp, strcpy) e poter gestire i tipi booleani.

* Per la gestione della concatenazione delle stringhe e le conversione implicite richieste sono state create quattro funzioni all'interno del codice C:
  * ``` char *concatInt(char *string, int toConcat) ```
  * ``` char *concatReal(char *string, float toConcat) ```
  * ``` char *concatBool(char *string, int toConcat) ```
  * ``` char *concatString(char *string, char *toConcat) ```

## Bug Noti e Scelte Progettuali Fatte

Nell'implementazione del progetto, con lo scopo di rendere il codice più pulito e mantenibile possibile si è scelto di non supportare alcuni casi estremamente particolari e di difficile riproduzione.

Casi particolari non supportati noti:
* Gestione di concatenazioni di stringha al seguito della dichiarazione di una variabile globale (Sia tramite IdInitNode che IdInitObblNode).