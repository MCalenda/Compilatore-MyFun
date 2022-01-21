// Circuit.flex
//
// CS2A Language Processing
//
// Description of lexer for circuit description language.
//
// Ian Stark
import java_cup.runtime.Symbol; //This is how we pass tokens to the parser
%%
// Declarations for JFlex
%unicode // We wish to read text files

%cup // Declare that we expect to use Java CUP
// Abbreviations for regular expressions
whitespace = [ \r\n\t\f]
digit = [0-9]
number = {digit}+
value1 = {number}("."{number})?
%%
// Now for the actual tokens and assocated actions
"seq" { return new Symbol(sym.SEQ); }
"par" { return new Symbol(sym.PAR); }
"end" { return new Symbol(sym.END); }
{value1} { return new Symbol(sym.RESISTOR,yytext()); }
{whitespace} { /* ignore */ }
[^]           { throw new Error("\n\nIllegal character < "+ yytext()+" >\n"); }
// End of file