/* A Bison parser, made by GNU Bison 2.3.  */

/* Skeleton interface for Bison's Yacc-like parsers in C

   Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA 02110-1301, USA.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     INT = 258,
     CHAR = 259,
     INTEGER = 260,
     ARR = 261,
     INR = 262,
     STR = 263,
     PRINT = 264,
     READ = 265,
     IF = 266,
     ELSE = 267,
     ASLA = 268,
     VAR = 269,
     FOR = 270,
     CONST = 271,
     ID = 272,
     PLUS = 273,
     MINUS = 274,
     DIV = 275,
     TIMES = 276,
     MOD = 277,
     L = 278,
     LEQ = 279,
     G = 280,
     GEQ = 281,
     EQ = 282,
     INITIALIZE = 283,
     DECL = 284,
     OR = 285,
     AND = 286,
     ASSIGN = 287,
     ASSIGN_PLUS = 288,
     ASSIGN_MINUS = 289,
     DIFF = 290,
     OPEN_SQUARE = 291,
     CLOSED_SQUARE = 292,
     OPEN = 293,
     CLOSE = 294,
     OPEN_BRACK = 295,
     CLOSE_BRACK = 296,
     COLON = 297,
     SEMICOLON = 298,
     COMMA = 299,
     TAG = 300,
     NOT = 301
   };
#endif
/* Tokens.  */
#define INT 258
#define CHAR 259
#define INTEGER 260
#define ARR 261
#define INR 262
#define STR 263
#define PRINT 264
#define READ 265
#define IF 266
#define ELSE 267
#define ASLA 268
#define VAR 269
#define FOR 270
#define CONST 271
#define ID 272
#define PLUS 273
#define MINUS 274
#define DIV 275
#define TIMES 276
#define MOD 277
#define L 278
#define LEQ 279
#define G 280
#define GEQ 281
#define EQ 282
#define INITIALIZE 283
#define DECL 284
#define OR 285
#define AND 286
#define ASSIGN 287
#define ASSIGN_PLUS 288
#define ASSIGN_MINUS 289
#define DIFF 290
#define OPEN_SQUARE 291
#define CLOSED_SQUARE 292
#define OPEN 293
#define CLOSE 294
#define OPEN_BRACK 295
#define CLOSE_BRACK 296
#define COLON 297
#define SEMICOLON 298
#define COMMA 299
#define TAG 300
#define NOT 301




#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
# define YYSTYPE_IS_TRIVIAL 1
#endif

extern YYSTYPE yylval;

