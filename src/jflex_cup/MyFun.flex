/* ------------------------ User code ------------------------ */
package flex;
import java_cup.runtime.*;
import cup.sym;


/* ------------------------ Options and declarations ------------------------ */
%%

%class Lexer
%unicode
%cup
%line
%column
%public

%{
    StringBuffer string = new StringBuffer();
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

// Deliminatori
New_Line = \r|\n|\r\n
White_Space = {New_Line} | [ \t\f]

// Numeri
Number_From_Zero = [0-9]
Number_From_One = [1-9]
Letter = [A-Za-z]

// Costanti
ID = [$_A-Za-z][$_A-Za-z0-9]*
INTEGER_CONST = (({Number_From_One}{Number_From_Zero}*)|(0))
REAL_CONST = (({Number_From_One}{Number_From_Zero}*)|(0))\.(({Number_From_Zero}*{Number_From_One}+)|(0))

// Stringhe
String_Const_Start = \'|\"

// Commenti
Comment_Block_Start = "#*"
Comment_Line_Start_I = "#"
Comment_Line_Start_II = "//"

%state STRING
%state COMMENT_BLOCK
%state COMMENT_LINE

/* ------------------------ Lexical rules ------------------------ */
%%

<YYINITIAL> {
    "loop"          {return symbol(sym.LOOP);}
    "out"           {return symbol(sym.OUT);}
    "var"           {return symbol(sym.VAR);}
    "@"             {return symbol(sym.OUTPAR);}
    "return"        {return symbol(sym.RETURN);}
    ","             {return symbol(sym.COMMA);}
    ";"             {return symbol(sym.SEMI);}
    "null"          {return symbol(sym.NULL);}
    "not"           {return symbol(sym.NOT);}
    "or"            {return symbol(sym.OR);}
    "and"           {return symbol(sym.AND);}
    ">="            {return symbol(sym.GE);}
    ">"             {return symbol(sym.GT);}
    "<="            {return symbol(sym.LE);}
    "<"             {return symbol(sym.LT);}
    "<>"            {return symbol(sym.NE);}
    "!="            {return symbol(sym.NE);}
    "="             {return symbol(sym.EQ);}
    "&"             {return symbol(sym.STR_CONCAT);}
    "^"             {return symbol(sym.POW);}
    "/"             {return symbol(sym.DIV);}
    "*"             {return symbol(sym.TIMES);}
    "div"           {return symbol(sym.DIVINT);}
    "-"             {return symbol(sym.MINUS);}
    "+"             {return symbol(sym.PLUS);}
    ":="            {return symbol(sym.ASSIGN);}
    "?:"            {return symbol(sym.WRITET);}
    "?,"            {return symbol(sym.WRITEB);}
    "?."            {return symbol(sym.WRITELN);}
    "?"             {return symbol(sym.WRITE);}
    "%"             {return symbol(sym.READ);}
    "end"           {return symbol(sym.END);}
    "while"         {return symbol(sym.WHILE);}
    "else"          {return symbol(sym.ELSE);}
    "then"          {return symbol(sym.THEN);}
    "if"            {return symbol(sym.IF);}
    "end"           {return symbol(sym.END);}
    "fun"           {return symbol(sym.FUN);}
    ":"             {return symbol(sym.COLON);}
    ")"             {return symbol(sym.RPAR);}
    "("             {return symbol(sym.LPAR);}
    "bool"          {return symbol(sym.BOOL);}
    "real"          {return symbol(sym.REAL);}
    "string"        {return symbol(sym.STRING);}
    "integer"       {return symbol(sym.INTEGER);}
    "main"          {return symbol(sym.MAIN);}

    // Spazi
    {White_Space}   { }

    // Booleani
    "true" { 
        try{ 
            return new Symbol(sym.TRUE, Boolean.parseBoolean(yytext()));
        } catch(Exception e) { 
            return new Symbol(sym.error, "Boolean.parseBoolean() error!"); 
        }
    }
    "false" { 
        try{ 
            return new Symbol(sym.FALSE, Boolean.parseBoolean(yytext()));
        } catch(Exception e) {
            return new Symbol(sym.error, "Boolean.parseBoolean() error!"); 
        }
    }

    // Identificatori
    {ID}            {return symbol(sym.ID, yytext()); }

    // Costanti intere
    {INTEGER_CONST} { 
            try{ 
                return new Symbol(sym.INTEGER_CONST, Integer.parseInt(yytext()));
            } catch(Exception e) { 
                return new Symbol(sym.error, "Integer parse error!"); 
            }
        }

    // Costanti reali
    {REAL_CONST}    { 
            try{ 
                return new Symbol(sym.REAL_CONST, Double.parseDouble(yytext()));
            } catch(Exception e) { 
                return new Symbol(sym.error, "Real parse error!"); 
            }
        }

    // Inizio di stringa
    {String_Const_Start}    { 
            string.setLength(0); 
            yybegin(STRING); 
        }

    // Inizio di commento a blocco
    {Comment_Block_Start}   {
            string.setLength(0); 
            yybegin(COMMENT_BLOCK);
        }
    
    // Inizio di commento di riga tipo I
    {Comment_Line_Start_I}   {
            string.setLength(0); 
            yybegin(COMMENT_LINE);
        }

    // Inizio di commento di riga tipo II
    {Comment_Line_Start_II}   {
            string.setLength(0); 
            yybegin(COMMENT_LINE);
        }
}

<STRING> {
    // Stringa terminata
    ' | \"           { 
            yybegin(YYINITIAL); 
            return new Symbol(sym.STRING_CONST, string.toString()); 
        }


    // Finchè trovi caratteri non presenti nelle quadre
    [^\n\r\'\"\\]+    { string.append(yytext()); }

    // CONTROLLARE QUESTO /n CHE DEVE FARE
    "\n"            { string.append('\n');}
    "\r"            { string.append('\r'); }
    "\\'"           { string.append('\''); }
    "\\\""           { string.append("\\\""); }
    "\\"            { string.append('\\'); }

    <<EOF>>         { 
        yybegin(YYINITIAL);
        return new Symbol(sym.error, "String not closed"); 
    }
}

<COMMENT_BLOCK> {
    // Finchè trovi caratteri non presenti nelle quadre
    [^"#"]          { }
    {New_Line}      { }

    "#"             { yybegin(YYINITIAL); }

    <<EOF>>         { 
        yybegin(YYINITIAL);
        return new Symbol(sym.error, "String not closed"); 
    }
}

<COMMENT_LINE> {
    // Finchè trovi caratteri non presenti nelle quadre
    [^"\n"]     { }

    "\n"      { yybegin(YYINITIAL); }

    <<EOF>>         { yybegin(YYINITIAL); }
}

[^] { 
    return new Symbol(sym.error, "Illegal character \"" + yytext() + "\", line: " + (yyline + 1) + ", column: " + (yycolumn + 1)); 
}
