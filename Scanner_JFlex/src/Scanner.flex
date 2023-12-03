package scanner;

/**
 * This file defines a simple lexer for the Compilers course
 * which uses regEx for lexical token specifications.
 * @author William Zhang
 * @author Nelson Gou
 * @version 9/11/23
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

/*  return String objects - the actual lexemes */
%public
%function nextToken
%type String

/*  returns the String "EOF" at end of file */
%eofval{
return "EOF";
%eofval}

/**
 * Pattern definitions
 */

validTags = (rss|channel|title|link|description|language|copyright|managingEditor|webMaster|pubDate|lastBuildDate|category|generator|docs|cloud|ttl|image|url|width|height|rating|textInput|name|skipDays|skipHours|item|author|comments|enclosure|guid|source)
specialTags = (atom:.*|media:.*|content:.*|dc:.*)
startTag = <({validTags}|{specialTags}).*>
endTag = <\/({validTags}|{specialTags})>
selfClosingTag = <({validTags}|{specialTags}).*\/>
tag = <({validTags}|{specialTags})>.*<\/\1>

%%
/**
 * lexical rules
 */

{selfClosingTag}    { return "SELF\t\t"  + (yyline+1) + " " + (yycolumn+1) + " \t\t" + yytext(); }
{tag}               { return "TAG\t\t\t" + (yyline+1) + " " + (yycolumn+1) + " \t\t" + yytext(); }
{endTag}            { return "END\t\t\t" + (yyline+1) + " " + (yycolumn+1) + " \t\t" + yytext(); }
{startTag}          { return "START\t\t" + (yyline+1) + " " + (yycolumn+1) + " \t\t" + yytext(); }
\s+ 	            { /* eat whitespace and do nothing */ }
.                   { return "UNKNOWN\t\t" + (yyline+1) + " " + (yycolumn+1) + " \t\t" + yytext(); }
