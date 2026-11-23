# JAY Scanner and Parser

- Scanner: `TokenStream` (lexical analysis), shown by `ScannerDemo`.
- Parser: `ConcreteSyntax` (recursive descent, one method per grammar rule), shown by `ParserDemo`, which prints the abstract syntax tree built from the classes in `Program.java`.

## Run

```
javac -d build src/*.java
java -cp build ScannerDemo prog1.jay     # list of tokens
java -cp build ParserDemo prog1.jay      # abstract syntax tree
./run_tests.sh                           # all scanner and parser tests
```

In Eclipse, put the `.jay` path in Run Configurations > Arguments (both demos default to `prog1.jay`).

## Scanner decisions taken from the JAY spec

- Keywords: `boolean else if int main void while` (case-sensitive, so `While`, `True`, `bool` are identifiers).
- Separators: `( ) { } ; ,` (`:` `.` `[` `]` are lexical errors, type `Other`).
- Operators: `= + - * / < <= > >= == != && || !`; a lone `&` or `|` is `Other`.
- Longest match: `!==` is `!=` then `=`; `<>` is `<` then `>`.
- A bad token is read up to the next space, operator or separator: `3aaa2!=4` gives `Other 3aaa2`, `Operator !=`, `Literal 4`.
- `// ...` comments run to the end of the line (or end of file). `/* */` is not a comment in JAY.

## Parser decisions

- Program header: the grammar says `void main ( ) {` but the homework example is `main {`. Both are accepted; anything else (e.g. `void main {`) is a syntax error.
- Types are `int` and `boolean` (`bool` is an identifier in JAY). `int` is displayed as `integer`, as in the required output `<i, integer>`.
- Assignment uses `=`; relations are `< <= > >= == !=` (the starter code had `<>` and was missing `>`).
- Precedence, lowest to highest: `||`, `&&`, relations, `+ -`, `* /`, `!`. Operators of the same level group left to right (`10 - 4 - 3` is `(10 - 4) - 3`).
- An `else` belongs to the closest `if`.
- `!` may appear once before a factor (`{ ! }opt Factor`), so `!!c` is an error; `!(!c)` works.
- All declarations come before the statements; nothing may follow the program's closing `}`.
- An integer literal larger than 2147483647 does not fit in a `Value` (Java `int`) and gives a syntax error instead of a crash.
- On a syntax error the parser prints `Syntax error - Expecting: X But saw: Type = value` and throws a `RuntimeException` (stack trace on stderr).

## Tests

Scanner tests are in `tests/scanner/`: each `.expected` lists `Type Value`, one line per token.

| Test | What it checks |
|---|---|
| instructor_tests | the full list from "Testing of the Scanner of JAY" (output reviewed line by line against the comments in that document) |
| own_comments | consecutive and indented comments, comment at end of file with no newline, `/` as division, `/ /` |
| own_operators | operators with no spaces (`a<=b>=c==d!=e`), `<>`, `===`, `&&&`, `\|\|\|`, `!!x`, `=<`, `>==` |
| own_nospaces_crlf | real code with no spaces, Windows `\r\n` line endings, tab and form feed |
| own_errors | `a#b`, `9z`, `$`, `_x`, `x_y`, `007`, `12.0`, `4a`, `x@1` |
| own_keywords_case | `Int`, `VOID`, `whilex`, `if1`, `bool`, `true1` are identifiers; `boolean` is a keyword |
| own_only_comments, own_empty | no tokens at all, and no empty token printed at the end |

Parser tests are in `tests/parser/`: each `.expected` is the exact `ParserDemo` output (stdout).

| Test | What it checks |
|---|---|
| homework_example | the program from the homework; output identical to the required output |
| prog1_void_main | the same program with the full `void main ( )` header |
| declarations, empty_program | comma lists, both types, no statements / no declarations |
| statements | skip `;`, while, if/else, if without else, dangling else, nested blocks |
| precedence | `* /` before `+ -`, parentheses, left-to-right grouping, `!`, `&&` before `\|\|` |
| nospaces_crlf | a whole program with no spaces, comments and Windows line endings |
| err_* (17 tests) | syntax errors: wrong header, missing `;` `}` `(` `)`, `:=`, `bool`, `int 5;`, `int i, ;`, `else` without `if`, `3 + ;`, `!!c`, `2.5`, huge integer, declaration after a statement, code after the program |

