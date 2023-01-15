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

program:  TAG statementList TAG  {printf("Program -> # StatamentList #");}
statementList: statement {printf("StatamentList -> statement");}
                | statement statementList {printf("StatamentList -> statement statementList");};
statement: simpleStmt {printf("statement -> simpleStmt");}
            | structStmt {printf("statement -> structStmt");};
simpleStmt: declaration {printf("simpleStmt -> declaration");}
            | ioStmt    {printf("simpleStmt -> ioStmt");}
            | assignStmt    {printf("simpleStmt -> assignStmt");};

declaration : VAR ID DECL type  {printf("declaration -> var id :: type");}
            | VAR ID DECL type INITIALIZE CONST {printf("var id :: type = constant");}

type : simpleType {printf("type -> simpleType");}
        | arrayType {printf("type -> arrayType");}

simpleType : CHAR {printf("simpleType -> char");}
            | INT {printf("simpleType -> int");}
            | STR {printf("simpleType -> str");}

arrayType : ARR OPEN_SQUARE simpleType INTEGER CLOSED_SQUARE {printf("arrayType -> arr[simpleType integer]");}

ioStmt : READ OPEN ID CLOSE {printf("ioStmt -> read(id)");}
        | PRINT OPEN identifierList CLOSE {printf("ioStmt -> print(identifierList)");}
        | PRINT OPEN CONST CLOSE {printf("iostmt -> print(const)");}

identifierList : ID {printf("identifierList -> id");}
                | ID COMMA identifierList {printf("identifierList -> id,identifierList");}


specialAssignOp : ASSIGN_PLUS {printf("specialAssignOp -> +:=");}
                | ASSIGN_MINUS {printf("specialAssignOp -> -:=");}

simpleAssign : ID ASSIGN expression {printf("simpleAssign -> id := expression");}

specialAssign : ID specialAssignOp CONST {printf("id specialAssignOp const");}

assignStmt : simpleAssign {printf("assignStmt -> simpleAssign");}
            | specialAssign {printf("assignStmt -> specialAssign");}

expression : expression secOrdOp term {printf("expression -> expression secOrdOp term");}
            | term {printf("");}

secOrdOp : PLUS {printf("secOrdOp -> +");}
            | MINUS {printf("secOrdOp -> -");}

term : term firstOrdOp factor {printf("term -> term firstOrdOp factor");}
        | factor {printf("term -> factor");}

firstOrdOp : TIMES {printf("firstOrdOp -> *");}
            | DIV {printf("firstOrdOp -> /");}

factor : OPEN expression CLOSE {printf("factor -> (expression)");}
        | ID {printf("factor -> id");}
        | arrAccess {printf("factor -> arrAccess");}
        | CONST {printf("factor -> const");}

arrAccess : ID OPEN_SQUARE ID CLOSED_SQUARE {printf("arrAccess -> id[id]");}
            | ID OPEN_SQUARE INTEGER CLOSED_SQUARE {printf("arrAccess -> id[integer]");}

structStmt : compoundStmt {printf("structStmt -> compoundCond");}
            | ifStmt {printf("structStmt -> ifStmt");}
            | whileStmt {printf("structStmt -> whileStmt");}
            | forStmt {printf("structStmt -> forStmt");}

compoundStmt : OPEN_BRACK statementList CLOSE_BRACK {printf("compoundStmt -> {statementList}");}

ifStmt : IF compoundCond statement ELSE statement {printf("ifStmt -> if compoundCond statement else statement");}


whileStmt : ASLA compoundCond statement {printf("whileStmt -> asla compoundCond statement");}

compoundCond : condition {printf("compoundCond -> condition");}
            | condition logicOp compoundCond {printf("compoundCond -> condition logicOp compoundCond");}

logicOp : AND {printf("logicOp -> &");}
        | OR {printf("logicOp -> |");}

condition : expression relation expression {printf("condition -> expression relation expression");}

relation : LEQ {printf("relation -> <=");}
            | L {printf("relation -> <");}
            | EQ {printf("relation -> ==");}
            | DIFF {printf("relation -> !=");}
            | GEQ {printf("relation -> >=");}
            | G {printf("relation -> >");}

forStmt : FOR OPEN_SQUARE initStmt COMMA compoundCond COMMA operationAssignStmt CLOSED_SQUARE statement {printf("forStmt -> for[initStmt, compoundCond, operationAssignStmt] statement");}

initStmt : ID INITIALIZE CONST {printf("initStmt -> id = const");}

operationAssignStmt : ID specialAssignOp CONST {printf("operationAssignStmt -> id specialAssignOp const");}

%%


extern FILE *yyin;

int main(int argc, char **argv)
{
  if(argc>1) yyin = fopen(argv[1], "r");
  if((argc>2)&&(!strcmp(argv[2],"-d"))) yydebug = 1;
  if(!yyparse()) fprintf(stderr,"\tOK\n");
}
