package scanner;

/**
 * This file defines a simple lexer for the compilers course 
 * which uses regEx for lexical token specifications
 */

import java.io.*;

%%
/* lexical functions */
/* 
 * This specifies that the class will be called Scanner and the function to get the next
 * token is called nextToken.  
 * Turn on line number and column number counting
 */

%class Scanner
%unicode
%line 
%column

%public
%function nextToken
/*  return String objects - the actual lexemes */
/*  returns the String "END: at end of file */
%type String
%eofval{
return "END";
%eofval}
/**
 * Pattern definitions
 */
Letter = [a-zA-Z]
Digit = [0-9]
Identifier = [a-zA-Z][0-9a-zA-Z]*
Id = {Letter}({Letter}|\d)*
Url = ({Letter}|{Digit})({Letter}|{Digit})+(\.{Letter}({Letter})+)+
Tel = \d\d\d\-\d\d\d\-\d\d\d\d
%%
/**
 * lexical rules
 */
"abb"			{return "Found " + yytext() + " at line " + yyline + " and column " + yycolumn; } 
"if"			{return "KEYWORD: IF";}
{Url}			{return "Website : <" + yytext() + ">";}
{Id}			{return "<ID " + yytext() + ">";}
{Tel}			{return "TelNo <" + yytext() + ">";}
[0-9]+			{return "<Number:" + yytext()+">";}
","             	{ return "COMMA"; }
"("             	{ return "LPAR"; }
"\""             	{ return "RPAR"; }
"="             	{ return "EQ"; }
"-"             	{ return "MINUS"; }
"+"             	{ return "PLUS"; }
"*"             	{ return "TIMES"; }
"/"             	{ return "DIV"; }
"<"             	{ return "LE"; }
"<="            	{ return "LEQ"; }
[\ \t\b\f\r\n]+ 	{ /* eat whitespace */ }
"//"[^\n]*      	{ /* one-line comment */ }
.			{}

/* 
* Extra definitions for testing
* {Identifier}		{return "<Identifier:" + yytext()+">";}
*/