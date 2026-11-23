/**
 * Parses a JAY file and prints its abstract syntax tree.
 *
 * Usage: java ParserDemo [file.jay]   (defaults to prog1.jay)
 *
 * If the program has a syntax error, ConcreteSyntax prints
 * "Syntax error - Expecting: ... But saw: ..." and throws a
 * RuntimeException, which stops the program with the stack trace.
 */
public class ParserDemo {

	public static void main(String[] args) {

		// Default input file; a path can also be passed on the command line
		String file = "prog1.jay";
		if (args.length > 0)
			file = args[0];
		TokenStream tStream = new TokenStream(file);

		ConcreteSyntax cSyntax = new ConcreteSyntax(tStream);
		Program p = cSyntax.program();
		// display() starts with a blank line; trim it so the output begins
		// with "Abstract syntax of the JAY Program:" like the required output
		System.out.println(p.display().trim());

	}

}
