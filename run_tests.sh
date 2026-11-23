#!/usr/bin/env bash
# Compiles the project and runs every test in tests/.
#   tests/scanner/X.jay + X.expected  -> one "Type Value" line per token
#   tests/parser/X.jay  + X.expected  -> exact ParserDemo output (the tree, or
#                                        the "Syntax error" line for a bad program)
# Usage: ./run_tests.sh
cd "$(dirname "$0")"
unset JAVA_TOOL_OPTIONS
rm -rf build && mkdir -p build
javac -nowarn -d build src/*.java || { echo "COMPILE FAILED"; exit 1; }

pass=0; fail=0
check() { # name, actual-file, expected-file
  if diff -u "$3" "$2" > build/diff.txt; then pass=$((pass+1)); echo "PASS  $1"
  else fail=$((fail+1)); echo "FAIL  $1"; sed 's/^/      /' build/diff.txt; fi
}

for f in tests/scanner/*.jay; do
  exp="${f%.jay}.expected"; [ -f "$exp" ] || continue
  java -cp build ScannerDemo "$f" | tail -n +2 \
    | sed -E 's/^Token [0-9]+ - Type: ([A-Za-z]+) - Value: (.*)$/\1 \2/' > build/actual.txt
  check "$f" build/actual.txt "$exp"
done

for f in tests/parser/*.jay; do
  exp="${f%.jay}.expected"; [ -f "$exp" ] || continue
  # stdout only: for a syntax error that is the "Syntax error - ..." line
  # (the Java stack trace goes to stderr and is not compared)
  java -cp build ParserDemo "$f" > build/actual.txt 2>/dev/null
  check "$f" build/actual.txt "$exp"
done

echo "----"; echo "$pass passed, $fail failed"
[ "$fail" -eq 0 ]
