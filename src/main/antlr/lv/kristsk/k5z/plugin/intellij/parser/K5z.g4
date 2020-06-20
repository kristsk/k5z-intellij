/*
 * Copyright (c) 2014, Krists Krigers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of  nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific
 *    prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

grammar K5z;

k5zFile
	:	header importsSection includesSection functionDeclarations EOF
	;



header
	: (K_PROGRAM | K_LIBRARY) CAMEL_ID SEMI
	;



importsSection
	: importLine*
	;



importLine
	: K_IMPORT importItems (K_FROM importLinePath)? SEMI
	;



importLinePath
	: QUOTED_STRING
	;



importItems
	: importItem (COMMA importItem)*
	;



importItem
	: importItemAlias
	| (importItemLibrary K_AS importItemAlias)
	;



importItemAlias
	: CAMEL_ID
	;



importItemLibrary
	: CAMEL_ID
	;



includesSection
	: includeLine*
	;



includeLine
	: K_INCLUDE (phtmlIncludeLine | phpIncludeLine)
	;



phtmlIncludeLine
	: K_PHTML phtmlIncludeFilename K_AS functionHeader SEMI
	;



phtmlIncludeFilename
	: QUOTED_STRING
	;



phpIncludeLine
	: K_PHP phpIncludeFilename K_WITH LBRACE (functionHeader K_AS phpIncludePhpFunctionName SEMI)* RBRACE
	;



phpIncludeFilename
	: QUOTED_STRING
	;



phpIncludePhpFunctionName
	: QUOTED_STRING
	;



functionHeader
	: K_DIRTY? K_FUNCTION K_REF? functionHeaderName LPAREN parameterList RPAREN
	;



functionHeaderName
	: CAMEL_ID
	;



parameterList
	: (mandatoryParameters (COMMA optionalParameters)?)
	| optionalParameters?
	;



mandatoryParameters
	: valOrRefParameter (COMMA valOrRefParameter)*
	;



optionalParameters
	:  optParameter (COMMA optParameter)*
	;



valOrRefParameter
	: (K_VAL | K_REF) parameterName
	;



optParameter
	: K_OPT parameterName ASSIGN staticExpressionAtom
	;



parameterName
	: SNAKE_ID
	;



functionDeclarations
	: functionDeclaration*
	;



functionDeclaration
	: functionHeader statementBlock
	;



statement
	: returnStatement
	| ifStatement
	| foreachStatement
	| whileStatement
	| assignStatement
	| forStatement
	| statementBlock
	| voidCallStatement
	| voidClosureInvocationStatement
	| K_BREAK expression? SEMI
	| K_CONTINUE expression? SEMI
	;



voidCallStatement
	: callExpression SEMI
	;



voidClosureInvocationStatement
	: closureInvocation SEMI
	;



statementBlock
	: LBRACE statement* RBRACE
	;



returnStatement
	: K_RETURN expression SEMI
    ;



ifStatement
	: K_IF LPAREN expression RPAREN statement (K_ELSE statement)?
	;



foreachStatement
	: K_FOREACH LPAREN (variable|array|callExpression|closureInvocation) K_AS (variable ARROW)? ASSIGNREF? variable RPAREN statement
	;



forStatement
	: K_FOR LPAREN (assignExpression|statementBlock) SEMI expression SEMI (assignExpression|statementBlock) RPAREN statement
	;



whileStatement
	: K_WHILE LPAREN expression RPAREN statement
	;



assignStatement
	: assignExpression SEMI
	;



assignExpression
	: assignExpressionLeftSide ASSIGN ASSIGNREF? expression
	;



assignExpressionLeftSide
	: (variable|array) (LBRACK RBRACK)?
	;



variable
	: DOLLAR? SNAKE_ID
	;



array
	: variable arraySegment+
	;



arraySegment
	: (DOT SNAKE_ID)
	| (LBRACK expression RBRACK)
	;



arrayDeclaration
	:	LBRACK arrayElementDeclarationList? RBRACK
	;



arrayElementDeclarationList
	: arrayElementDeclaration (COMMA arrayElementDeclaration)*
	;



arrayElementKey
	: (DOT SNAKE_ID)
	| expression
	;



arrayKeylessElementDeclaration
	: ASSIGNREF? expression
	;



arrayKeyedElementDeclaration
	: arrayElementKey ARROW ASSIGNREF? expression
	;



arrayElementDeclaration
	: arrayKeyedElementDeclaration
	| arrayKeylessElementDeclaration
	;



staticExpressionAtom
	: QUOTED_STRING
	| INTEGER
	| FLOAT
	| K_TRUE
	| K_FALSE
	| (LBRACK RBRACK)
	;



expressionAtom
	: staticExpressionAtom
	| callExpression
	| arrayDeclaration
	| ((variable|array) (INC|DEC))
	| ((INC|DEC) (variable|array))
	| variable
	| array
	| closureInvocation
	| closureDeclaration
	| functionIdentifier
	;



expression
	: orExpression
	;



orExpression
	: nullCoalesceExpression ((AND|OR|K_AND|K_OR) nullCoalesceExpression)*
	;



nullCoalesceExpression
	: ternaryExpression (QMQM ternaryExpression)*
	;



ternaryExpression
	: equalityExpression ((QM equalityExpression COLON equalityExpression) | (QMC equalityExpression))?
	;



equalityExpression
	: notExpression ((EQ|NE|EQ2|NE2|LT|GT|LE|GE|PLUS|MINUS|TWODOTS|MUL|DIV|MOD) notExpression)*
	;



notExpression
	: (BANG|K_NOT|MINUS)? primary
	;



primary
	: expressionAtom
	| expressionAsClosure
	| LPAREN expression RPAREN
	;



functionIdentifier
	: (functionIdentifierAlias? DOUBLECOLON)? functionIdentifierName
	;



functionIdentifierName
	: CAMEL_ID
	;



functionIdentifierAlias
	: CAMEL_ID
	;


callExpression
	: functionIdentifier LPAREN argumentList RPAREN
	;



argumentList
	: namedArgumentList
	| orderedArgumentList
	;



orderedArgumentList
	: (expression (COMMA expression)*)?
	;



namedArgumentList
	: (namedArgument (COMMA namedArgument)*)?
	;



namedArgument
	: SNAKE_ID COLON expression
	;



closureInvocation
	: AT (variable|array) LPAREN argumentList RPAREN
	;



closureDeclaration
	: closureHeader statementBlock
	;



closureHeader
	: AT (LPAREN parameterList RPAREN)?
	;



expressionAsClosure
	: AT LPAREN expression RPAREN
	;


K_FUNCTION: 'function';
K_DIRTY: 'dirty';
K_PROGRAM: 'program';
K_LIBRARY: 'library';
K_IMPORT: 'import';
K_INCLUDE: 'include';
K_PHTML: 'phtml';
K_PHP: 'php';
K_AS: 'as';
K_FROM: 'from';
K_IF: 'if';
K_ELSE: 'else';
K_FOREACH: 'foreach';
K_FOR: 'for';
K_WHILE: 'while';
K_TRUE: 'TRUE';
K_FALSE: 'FALSE';
K_VAL: 'val';
K_OPT: 'opt';
K_REF: 'ref';
K_WITH: 'with';
K_RETURN: 'return';
K_AND: 'and';
K_OR: 'or';
K_NOT: 'not';
K_BREAK: 'break';
K_CONTINUE: 'continue';

SNAKE_ID
	:	[_a-z][a-z_0-9]*
	;

CAMEL_ID
	:	[A-Z][a-zA-Z0-9]*
	;

DOUBLECOLON: '::';
QM: '?' ~(':'|'?');
QMC: '?:';
QMQM: '??';

ARROW: '=>';

ASSIGN: '=';

BANG: '!';
COLON: ':';

NE: '!=';
EQ: '==';
NE2: '!==';
EQ2: '===';
LT: '<';
GT: '>';
LE: '>=';
GE: '<=';

AND: '&&';
OR: '||';

PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
MOD: '%';

INC: '++';
DEC: '--';

AT: '@';
DOLLAR: '$';

SEMI: ';';

LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LBRACK: '[';
RBRACK: ']';
COMMA: ',';
DOT: '.';
TWODOTS: '..';

ASSIGNREF: '&';

BACKSLASH: '\\';

QUOTED_STRING
	:	'"' (~["\\]| '\\' [ntr"\\])* '"'
	;

BLOCK_COMMENT
	:	'/*'  .*? '*/' -> channel(HIDDEN)
	;

LINE_COMMENT
	:	'//' ~[\r\n]*  -> channel(HIDDEN)
	;

INTEGER
	: '0'
	| ([1-9] [0-9]*)
	;


FLOAT
	: INTEGER DOT INTEGER
	;

WS
	:	[ \t\r\n\f]+ -> channel(HIDDEN)
	;

ERRCHAR
	:	.	-> channel(HIDDEN)
	;
