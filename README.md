# JAY Scanner

Scanner (`TokenStream`) for JAY. The parser (`ConcreteSyntax`, `ParserDemo`) is still the starter code.

## Run

```
javac -d build src/*.java
java -cp build ScannerDemo prog1.jay     # list of tokens
./run_tests.sh                           # all scanner tests
```

In Eclipse, put the `.jay` path in Run Configurations > Arguments (ScannerDemo defaults to `prog1.jay`).

## Decisions taken from the JAY spec

- Keywords: `boolean else if int main void while` (case-sensitive, so `While`, `True`, `bool` are identifiers).
- Separators: `( ) { } ; ,` (`:` `.` `[` `]` are lexical errors, type `Other`).
- Operators: `= + - * / < <= > >= == != && || !`; a lone `&` or `|` is `Other`.
- Longest match: `!==` is `!=` then `=`; `<>` is `<` then `>`.
- A bad token is read up to the next space, operator or separator: `3aaa2!=4` gives `Other 3aaa2`, `Operator !=`, `Literal 4`.
- `// ...` comments run to the end of the line (or end of file). `/* */` is not a comment in JAY.

## Tests

Tests are in `tests/scanner/`: each `.expected` lists `Type Value`, one line per token.

| Test | What it checks |
|---|---|
| instructor_tests | the full list from "Testing of the Scanner of JAY" (output reviewed line by line against the comments in that document) |
| own_comments | consecutive and indented comments, comment at end of file with no newline, `/` as division, `/ /` |
| own_operators | operators with no spaces (`a<=b>=c==d!=e`), `<>`, `===`, `&&&`, `\|\|\|`, `!!x`, `=<`, `>==` |
| own_nospaces_crlf | real code with no spaces, Windows `\r\n` line endings, tab and form feed |
| own_errors | `a#b`, `9z`, `$`, `_x`, `x_y`, `007`, `12.0`, `4a`, `x@1` |
| own_keywords_case | `Int`, `VOID`, `whilex`, `if1`, `bool`, `true1` are identifiers; `boolean` is a keyword |
| own_only_comments, own_empty | no tokens at all, and no empty token printed at the end |

