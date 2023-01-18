%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define YYDEBUG 1
int yylex();
void yyerror(char *s)
{
  printf("%s\n", s);
}
%}

%token INT;
%token CHAR;
%token INTEGER;
%token ARR;
%token INR;
%token STR;
%token PRINT;
%token READ;
%token IF;
%token ELSE;
%token ASLA;
%token VAR;
%token FOR;
%token CONST;
%token ID;
%token PLUS;
%token MINUS;
%token DIV;
%token TIMES;
%token MOD;
%token L;
%token LEQ;
%token G;
%token GEQ;
%token EQ;
%token INITIALIZE;
%token DECL;
%token OR;
%token AND;
%token ASSIGN;
%token ASSIGN_PLUS;
%token ASSIGN_MINUS;
%token DIFF;
%token OPEN_SQUARE;
%token CLOSED_SQUARE;
%token OPEN;
%token CLOSE;
%token OPEN_BRACK;
%token CLOSE_BRACK;
%token COLON;
%token SEMICOLON;
%token COMMA;
%token TAG;

%left PLUS MINUS
%left DIV MOD TIMES
%left OR
%left AND
%left NOT

%start program

%%

program:  TAG statementList TAG  {printf("Program -> # StatamentList #\n");}
statementList: statement {printf("StatamentList -> statement\n");}
                | statement statementList {printf("StatamentList -> statement statementList\n");};
statement: simpleStmt {printf("statement -> simpleStmt\n");}
            | structStmt {printf("statement -> structStmt\n");};
simpleStmt: declaration {printf("simpleStmt -> declaration\n");}
            | ioStmt    {printf("simpleStmt -> ioStmt\n");}
            | assignStmt    {printf("simpleStmt -> assignStmt\n");};

declaration : VAR ID DECL type  {printf("declaration -> var id :: type\n");}
            | VAR ID DECL type INITIALIZE CONST {printf("var id :: type = constant\n");}

type : simpleType {printf("type -> simpleType\n");}
        | arrayType {printf("type -> arrayType\n");}

simpleType : CHAR {printf("simpleType -> char\n");}
            | INT {printf("simpleType -> int\n");}
            | STR {printf("simpleType -> str\n");}

arrayType : ARR OPEN_SQUARE simpleType INTEGER CLOSED_SQUARE {printf("arrayType -> arr[simpleType integer]\n");}

ioStmt : READ OPEN ID CLOSE {printf("ioStmt -> read(id)]\n");}
        | PRINT OPEN identifierList CLOSE {printf("ioStmt -> print(identifierList)\n");}
        | PRINT OPEN CONST CLOSE {printf("iostmt -> print(const)\n");}

identifierList : ID {printf("identifierList -> id\n");}
                | ID COMMA identifierList {printf("identifierList -> id,identifierList\n");}


specialAssignOp : ASSIGN_PLUS {printf("specialAssignOp -> +:=\n");}
                | ASSIGN_MINUS {printf("specialAssignOp -> -:=\n");}

simpleAssign : ID ASSIGN expression {printf("simpleAssign -> id := expression\n");}

specialAssign : ID specialAssignOp CONST {printf("id specialAssignOp const\n");}

assignStmt : simpleAssign {printf("assignStmt -> simpleAssign\n");}
            | specialAssign {printf("assignStmt -> specialAssign\n");}

expression : expression secOrdOp term {printf("expression -> expression secOrdOp term\n");}
            | term {printf("");}

secOrdOp : PLUS {printf("secOrdOp -> +\n");}
            | MINUS {printf("secOrdOp -> -\n");}

term : term firstOrdOp factor {printf("term -> term firstOrdOp factor\n");}
        | factor {printf("term -> factor\n");}

firstOrdOp : TIMES {printf("firstOrdOp -> *\n");}
            | DIV {printf("firstOrdOp -> /\n");}

factor : OPEN expression CLOSE {printf("factor -> (expression)\n");}
        | ID {printf("factor -> id\n");}
        | arrAccess {printf("factor -> arrAccess\n");}
        | CONST {printf("factor -> const\n");}

arrAccess : ID OPEN_SQUARE ID CLOSED_SQUARE {printf("arrAccess -> id[id]\n");}
            | ID OPEN_SQUARE INTEGER CLOSED_SQUARE {printf("arrAccess -> id[integer]\n");}

structStmt : compoundStmt {printf("structStmt -> compoundCond\n");}
            | ifStmt {printf("structStmt -> ifStmt\n");}
            | whileStmt {printf("structStmt -> whileStmt\n");}
            | forStmt {printf("structStmt -> forStmt\n");}

compoundStmt : OPEN_BRACK statementList CLOSE_BRACK {printf("compoundStmt -> {statementList}\n");}

ifStmt : IF compoundCond statement ELSE statement {printf("ifStmt -> if compoundCond statement else statement\n");}


whileStmt : ASLA compoundCond statement {printf("whileStmt -> asla compoundCond statement\n");}

compoundCond : condition {printf("compoundCond -> condition\n");}
            | condition logicOp compoundCond {printf("compoundCond -> condition logicOp compoundCond\n");}

logicOp : AND {printf("logicOp -> &\n");}
        | OR {printf("logicOp -> |\n");}

condition : expression relation expression {printf("condition -> expression relation expression\n");}

relation : LEQ {printf("relation -> <=\n");}
            | L {printf("relation -> <\n");}
            | EQ {printf("relation -> ==\n");}
            | DIFF {printf("relation -> !=\n");}
            | GEQ {printf("relation -> >=\n");}
            | G {printf("relation -> >\n");}

forStmt : FOR OPEN_SQUARE initStmt COMMA compoundCond COMMA operationAssignStmt CLOSED_SQUARE statement {printf("forStmt -> for[initStmt, compoundCond, operationAssignStmt] statement\n");}

initStmt : ID INITIALIZE CONST {printf("initStmt -> id = const\n");}

operationAssignStmt : ID specialAssignOp CONST {printf("operationAssignStmt -> id specialAssignOp const\n");}

%%


extern FILE *yyin;

int main(int argc, char **argv)
{
  if(argc>1) yyin = fopen(argv[1], "r");
  if((argc>2)&&(!strcmp(argv[2],"-d"))) yydebug = 1;
  if(!yyparse()) fprintf(stderr,"\tSequence is accepted\n");
}
