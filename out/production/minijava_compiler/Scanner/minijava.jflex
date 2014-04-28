/*
 * JFlex specification for the lexical analyzer for a simple demo language.
 * Change this into the scanner for your implementation of MiniJava.
 * CSE 401/P501 Au11
 */


package Scanner;

import java_cup.runtime.*;
import Parser.sym;

%%

%public
%final
%class scanner
%unicode
%cup
%line
%column

/* Code copied into the generated scanner class.  */
/* Can be referenced in scanner action code. */
%{
  // Return new symbol objects with line and column numbers in the symbol 
  // left and right fields. This abuses the original idea of having left 
  // and right be character positions, but is   // is more useful and 
  // follows an example in the JFlex documentation.
  private Symbol symbol(int type) {
    return new Symbol(type, yyline+1, yycolumn+1);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline+1, yycolumn+1, value);
  }

  // Return a readable representation of symbol s (aka token)
  public String symbolToString(Symbol s) {
    String rep;
    switch (s.sym) {
      case sym.BECOMES: return "BECOMES";
      case sym.SEMICOLON: return "SEMICOLON";
      case sym.MULTIPLY: return "MULTIPLY";
      case sym.PLUS: return "PLUS";
      case sym.MINUS: return "MINUS";
      case sym.LESSTHAN: return "LESSTHAN";
      case sym.LOGICAL_AND: return "LOGICAL_AND";
      case sym.LOGICAL_NOT: return "LOGICAL_NOT";
      case sym.LPAREN: return "LPAREN";
      case sym.RPAREN: return "RPAREN";
      case sym.DISPLAY: return "DISPLAY";
      case sym.CLASS: return "CLASS";
      case sym.PUBLIC: return "PUBLIC";
      case sym.STATIC: return "STATIC";
      case sym.VOID: return "VOID";
      case sym.NEW: return "NEW";
      case sym.RETURN: return "RETURN";
      case sym.LBRACKET: return "LBRACKET";
      case sym.RBRACKET: return "RBRACKET";
      case sym.DOT: return "DOT";
      case sym.LENGTH: return "LENGTH";
      case sym.COMMA: return "COMMA";
      case sym.THIS: return "THIS";
      case sym.TRUE: return "TRUE";
      case sym.FALSE: return "FALSE";
      case sym.IF: return "IF";
      case sym.ELSE: return "ELSE";
      case sym.WHILE: return "WHILE";
      case sym.EXTENDS: return "EXTENDS";
      case sym.MAIN: return "MAIN";
      case sym.LCURLY: return "LCURLY";
      case sym.RCURLY: return "RCURLY";
      case sym.INTEGER_TYPE: return "INTEGER_TYPE";
      case sym.BOOLEAN_TYPE: return "BOOLEAN_TYPE";
      case sym.STRING_TYPE: return "STRING_TYPE";
      case sym.IDENTIFIER: return "IDENTIFIER(" + (String)s.value + ")";
      case sym.INTEGER_LITERAL: return "INTEGER_LITERAL(" + (String)s.value + ")";
      case sym.EOF: return "<EOF>";
      case sym.error: return "<ERROR>";
      default: return "<UNEXPECTED TOKEN " + s.toString() + ">";
    }
  }
%}

/* Helper definitions */
letter = [a-zA-Z]
digit = [0-9]
eol = [\r\n]
white = {eol}|[ \t]
display = System\.out\.println

/* comments */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*




%%

/* Token definitions */

/* reserved words */
/* (put here so that reserved words take precedence over identifiers) */
{display} 	{ return symbol(sym.DISPLAY); }
"class"		{ return symbol(sym.CLASS); }
"public"	{ return symbol(sym.PUBLIC); }
"static"	{ return symbol(sym.STATIC); }
"void"		{ return symbol(sym.VOID); }
"new"		{ return symbol(sym.NEW); }
"return"	{ return symbol(sym.RETURN); }
"this"		{ return symbol(sym.THIS); }
"if"		{ return symbol(sym.IF); }
"else"		{ return symbol(sym.ELSE); }
"true"		{ return symbol(sym.TRUE); }
"false"		{ return symbol(sym.FALSE); }
"while"		{ return symbol(sym.WHILE); }
"extends"	{ return symbol(sym.EXTENDS); }
"main"		{ return symbol(sym.MAIN); }
"length"	{ return symbol(sym.LENGTH); }

/* basic types */
"int"		{ return symbol(sym.INTEGER_TYPE); }
"boolean"	{ return symbol(sym.BOOLEAN_TYPE); }
"String"	{ return symbol(sym.STRING_TYPE); }

/* operators */
"*" 		{ return symbol(sym.MULTIPLY); }
"+" 		{ return symbol(sym.PLUS); }
"-" 		{ return symbol(sym.MINUS); }
"<" 		{ return symbol(sym.LESSTHAN); }
"&&" 		{ return symbol(sym.LOGICAL_AND); }
"!" 		{ return symbol(sym.LOGICAL_NOT); }
"=" 		{ return symbol(sym.BECOMES); }

/* delimiters */
"(" 		{ return symbol(sym.LPAREN); }
")" 		{ return symbol(sym.RPAREN); }
"[" 		{ return symbol(sym.LBRACKET); }
"]" 		{ return symbol(sym.RBRACKET); }
"{" 		{ return symbol(sym.LCURLY); }
"}" 		{ return symbol(sym.RCURLY); }
";" 		{ return symbol(sym.SEMICOLON); }
"."			{ return symbol(sym.DOT); }
","			{ return symbol(sym.COMMA); }

/* identifiers */
{letter} ({letter}|{digit}|_)*	{ return symbol(sym.IDENTIFIER, yytext()); }

/* constants */
{digit}  ({digit})* 			{ return symbol(sym.INTEGER_LITERAL, yytext()); }

/* comments */
{Comment}	{ /* ignore comments */ }

/* whitespace */
{white}+ 	{ /* ignore whitespace */ }

/* lexical errors (put last so other matches take precedence) */
. { System.err.println(
	"\nunexpected character in input: '" + yytext() + "' at line " +
	(yyline+1) + " column " + (yycolumn+1));
  }
