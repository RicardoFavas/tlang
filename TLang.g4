grammar TLang;

@header {
	package tlang.parser;
	import org.antlr.v4.misc.CharSupport;
	import com.yuvalshavit.antlr4.DenterHelper.DenterHelper;
	import com.yuvalshavit.antlr4.DenterHelper.DenterOptions;
}


tokens { INDENT, DEDENT }
@lexer::members {
	private final DenterHelper denter = DenterHelper.builder()
    	.nl(NL)
  	  	.indent(TLangParser.INDENT)
    	.dedent(TLangParser.DEDENT)
    	.pullToken(TLangLexer.super::nextToken);
  	@Override
  	public Token nextToken() {
    	return denter.nextToken();
  	}
}
NL: ('\r'? '\n' ' '*);
WS: ' '+ -> skip;

/*

WS: (' '|'\t'|'\r'|'\n')+ -> skip;
*/
DECIMAL_LITERAL: Integer ('_'? '.' '_'? Integer)?  ([eE] '-'? Integer+)?;
fragment Integer: Digit (Digit|'_')*;
fragment Digit: [0-9];

ID : [a-zA-Z$_][a-zA-Z0-9$_]* ;             // match lower-case identifiers

REGULAR_STRING:
	('"'  (~'"' | '\\"')* '"')
|	('\''  (~'\'' | '\\\'')* '\'')
;

VERBATIUM_STRING:
	('@"' (~'"' | '""')* '"')
|	('@\'' (~'\'' | '\'\'')* '\'')
;

fragment CommonCharacter: 
	SimpleEscapeSequence
| 	HexEscapeSequence
| 	UnicodeEscapeSequence
;
fragment SimpleEscapeSequence: 
	'\\\''
| 	'\\"'
| 	'\\\\'
| 	'\\0'
| 	'\\a'
| 	'\\b'
| 	'\\f'
| 	'\\n'
| 	'\\r'
| 	'\\t'
| 	'\\v'
;
fragment HexEscapeSequence: 
	'\\x' HexDigit
|	'\\x' HexDigit HexDigit
|	'\\x' HexDigit HexDigit HexDigit
|	'\\x' HexDigit HexDigit HexDigit HexDigit
	;
fragment HexDigit : [0-9] | [A-F] | [a-f];
fragment UnicodeEscapeSequence: 
	'\\u' HexDigit HexDigit HexDigit HexDigit
|	'\\U' HexDigit HexDigit HexDigit HexDigit HexDigit HexDigit HexDigit HexDigit
;


COMMENT: '#' ~[\r\n\u2028\u2029]* -> skip;

//COMMENT: '//' ~[\r\n\u2028\u2029]* -> skip;
//MULTILINE_COMMENT: '/*' .*? '*/' -> skip;

program:
//	INDENT* exp (NL* INDENT* exp (NL DEDENT)*)* (NL? DEDENT)* INDENT* NL*
	(NL|INDENT|DEDENT)* exp ((NL|INDENT|DEDENT)* exp (NL|INDENT|DEDENT)*)* (NL|INDENT|DEDENT)*
;

closedBlock:
	(NL|INDENT|DEDENT)* exp ((NL|INDENT|DEDENT)* exp (NL|INDENT|DEDENT)*)* (NL|INDENT|DEDENT)*
;

block:
	INDENT exp (NL? exp)* NL DEDENT
|	exp	(NL? exp)* INDENT exp (NL? exp)* NL DEDENT
|	exp NL
;

exp:
	cconst='const' ID '=' exp
|	cvar='var' ID ('=' exp)?
|	cmerge='...' exp


| 	creturn='return' exp
|	cnext='next'
|	cbreak='break' 
|	cthis='this'
|	cframe='frame'
|	cnop='nop'
|	cthrow='throw' exp?


|	ID cassign='=' exp NL?
|	literal cassignThis='=' exp NL?

|	cassert='assert' exp
|	casync='async' exp
|	cawait='await' '(' exp (',' exp)* ')'

|	cjava= 'java' '(' exp ')'
|	cprint='print' '(' exp ')'

| 	exp cpow='^' exp
|	cpriority='(' exp ')'

| 	exp cdivide='/' exp
| 	exp cmultiply='*' exp
|	exp cmod='%' exp

| 	exp csub='-' exp
| 	exp cadd='+' exp

| 	exp clesserThan='<' exp
| 	exp clesserThan='<=' exp
| 	exp cgreaterThan='>' exp
| 	exp cgreaterThan='>=' exp

|	exp cequals='==' exp
|	exp cnequals='!=' exp
|	exp crefequals='===' exp
|	exp crefnequals='!==' exp

|	exp cin='in' exp
|	exp ccontains='contains' exp
|	exp cimplements='implements' exp


|	exp cmatch='match' exp
|	exp clike='clike' exp
|	exp cis='is' exp

|	exp safenav='?''.'ID (':' exp)?
|	exp '.' ID elvis='?' (':' exp)?
	

|	exp cget='.' ID	
|	exp cset='.' ID '=' exp

|	'(' (param (',' param)*)? ')' clambda='->' block
|	'(' (param (',' param)*)? ')' clambda='->' closedBlock ';'

|	ID cinvoke2='(' (exp (',' exp)*)? ')' 
|	exp cinvoke='(' (exp (',' exp)*)? ')' 


|	cif='if' ifExp=exp 'do' closedBlock (celif='elif' exp 'do' closedBlock)* (celse='else' closedBlock)? ';'

|	cif='if' ifExp=exp 'do' closedBlock (celif='elif' exp 'do' closedBlock)* 'else' block
|	cif='if' ifExp=exp 'do' closedBlock 'else' block
|	cif='if' ifExp=exp 'do' closedBlock celif='elif' exp 'do' block
|	cif='if' ifExp=exp 'do' block

|	cwhile='while' whileExp=exp 'do' block	
|	cwhile='while' whileExp=exp 'do' closedBlock ';'

|	cdowhile='do' closedBlock 'while' whileExp=exp 

|	cfor='for' ID 'in' exp 'do' block
|	cfor='for' ID 'in' exp 'do' closedBlock ';'

|	ctry='try' closedBlock ccatch='catch' ID? closedBlock cfinallyn='finally' block
|	ctry='try' closedBlock cfinallyn='finally' block
|	ctry='try' closedBlock ccatch='catch' ID? block
|	ctry='try' block 

|	ctry='try' closedBlock (ccatch='catch' ID? closedBlock)? (cfinally='finally' closedBlock)? ';'

| 	cimport='import' '(' exp  ')'

|	literal

|	'`' block ',' block ',' block '`'
|	ID ID 

;

param:
	ID ('=' exp )?
|	ID
;

literal: 

  cnull='null'
| ctrue='true'
| cfalse='false'

| cobject='{' closedBlock '}'

| clist='[' (exp (',' exp)*)? ']'

| cid=ID

| cdecimal=DECIMAL_LITERAL 
| cnegate='-' exp

| cregular_string=REGULAR_STRING
| cverbatium_string=VERBATIUM_STRING



;


