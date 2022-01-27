/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


//USERCODE
import java_cup.runtime.*;

//: USERCODE

%%

//OPTIONS AND DECLARATIONS

%class Lexer
%unicode
%cup
%line
%column

%{
    StringBuffer string = new StringBuffer();
%}

//Regex

/* Delimitatori */
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

ZeroNumber = [0-9]
NoZeroNumber = [1-9]

ID = [a-zA-Z_][a-zA-Z_0-9]*
FLOAT_CONST = ({NoZeroNumber}{ZeroNumber}*|0)(\.({ZeroNumber}*{NoZeroNumber}+|0))
INT_CONST = ({NoZeroNumber}{ZeroNumber}*|0)

StringInit = "\""

CommentInit = "\/\*"

%state STRING
%state COMMENT

//: OPTIONS AND DECLARATIONS
%%
//LEXICAL RULES

<YYINITIAL> ";"             {return new Symbol(sym.SEMI);}
<YYINITIAL> ","             {return new Symbol(sym.COMMA);}
<YYINITIAL> "int"           {return new Symbol(sym.INT);}
<YYINITIAL> "string"        {return new Symbol(sym.STRING);}
<YYINITIAL> "float"         {return new Symbol(sym.FLOAT);}
<YYINITIAL> "bool"          {return new Symbol(sym.BOOL);}
<YYINITIAL> "proc"          {return new Symbol(sym.PROC);}
<YYINITIAL> "("             {return new Symbol(sym.LPAR);}
<YYINITIAL> ")"             {return new Symbol(sym.RPAR);}
<YYINITIAL> ":"             {return new Symbol(sym.COLON);}
<YYINITIAL> "corp"          {return new Symbol(sym.CORP);}
<YYINITIAL> "void"          {return new Symbol(sym.VOID);}
<YYINITIAL> "if"            {return new Symbol(sym.IF);}
<YYINITIAL> "then"          {return new Symbol(sym.THEN);}
<YYINITIAL> "elif"          {return new Symbol(sym.ELIF);}
<YYINITIAL> "fi"            {return new Symbol(sym.FI);}
<YYINITIAL> "else"          {return new Symbol(sym.ELSE);}
<YYINITIAL> "while"         {return new Symbol(sym.WHILE);}
<YYINITIAL> "do"            {return new Symbol(sym.DO);}
<YYINITIAL> "od"            {return new Symbol(sym.OD);}
<YYINITIAL> "readln"        {return new Symbol(sym.READ);}
<YYINITIAL> "write"         {return new Symbol(sym.WRITE);}
<YYINITIAL> ":="            {return new Symbol(sym.ASSIGN);}
<YYINITIAL> "+"             {return new Symbol(sym.PLUS);}
<YYINITIAL> "-"             {return new Symbol(sym.MINUS);}
<YYINITIAL> "*"             {return new Symbol(sym.TIMES);}
<YYINITIAL> "/"             {return new Symbol(sym.DIV);}
<YYINITIAL> "="             {return new Symbol(sym.EQ);}
<YYINITIAL> "<>"            {return new Symbol(sym.NE);}
<YYINITIAL> "<"             {return new Symbol(sym.LT);}
<YYINITIAL> "<="            {return new Symbol(sym.LE);}
<YYINITIAL> ">"             {return new Symbol(sym.GT);}
<YYINITIAL> ">="            {return new Symbol(sym.GE);}
<YYINITIAL> "&&"            {return new Symbol(sym.AND);}
<YYINITIAL> "||"            {return new Symbol(sym.OR);}
<YYINITIAL> "!"             {return new Symbol(sym.NOT);}
<YYINITIAL> "null"          {return new Symbol(sym.NULL);}
<YYINITIAL> "->"            {return new Symbol(sym.RETURN);}


<YYINITIAL> {
    /* Delimitatori */
    {WhiteSpace}                    { /*do nothing*/ }

    /* Bool */
    "true"                          { try{ return new Symbol(sym.TRUE, Boolean.parseBoolean(yytext()));} catch(Exception e) { return new Symbol(sym.error, "Boolean.parseBoolean() error!"); }}
    "false"                         { try{ return new Symbol(sym.FALSE, Boolean.parseBoolean(yytext()));} catch(Exception e) { return new Symbol(sym.error, "Boolean.parseBoolean() error!"); }}

    /* Id */
    {ID}                            { return new Symbol(sym.ID, yytext());}

    /* Float const */
    {FLOAT_CONST}                   { try{ return new Symbol(sym.FLOAT_CONST, Float.parseFloat(yytext()));} catch(Exception e) { return new Symbol(sym.error, "Float.parseFloat() error!"); }}

    /* Int const */
    {INT_CONST}                     { try{ return new Symbol(sym.INT_CONST, Integer.parseInt(yytext()));} catch(Exception e) { return new Symbol(sym.error, "Integer.parseInt() error!"); }}

    /* Inizio String State */
    {StringInit}                    { string.setLength(0); yybegin(STRING); }

    /* Inizio Comment State */
    {CommentInit}                   { string.setLength(0); yybegin(COMMENT); }
}

<STRING> {

    \"                              { yybegin(YYINITIAL); return new Symbol(sym.STRING_CONST, string.toString()); }

    [^\Z]                           { string.append( yytext() ); }

    <<EOF>>                         { return new Symbol(sym.error, "Stringa costante non completata"); }
}

<COMMENT> {
    "*"                             { }
    [^"*/"]                         { }
    "*/"                            { yybegin(YYINITIAL); }

    <<EOF>>                         { return new Symbol(sym.error, "Commento non chiuso"); }
}

[^]                                 { return new Symbol(sym.error, "Illegal character <" + yytext() + ">, line: " + (yyline + 1) + ", column: " + (yycolumn + 1)); }
//: LEXICAL RULES