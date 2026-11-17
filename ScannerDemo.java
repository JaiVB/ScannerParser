
/**
 * @author scharffc
 * 
 *         Prints every token of a JAY file in the required format:
 *         Token 1 - Type: Literal - Value: 31
 *
 *         Usage: java ScannerDemo [file.jay] (defaults to prog1.jay)
 */
public class ScannerDemo {

	// Default input file; a path can also be passed on the command line
	private static String file1 = "prog1.jay";
	private static int counter = 1;

	public static void main(String args[]) {
		if (args.length > 0)
			file1 = args[0];
		TokenStream ts = new TokenStream(file1);
		System.out.println(file1);
		while (!ts.isEndofFile()) {
			Token tk = ts.nextToken();
			// At the end of the file (e.g. trailing blank lines or a final
			// comment) the scanner hands back an empty token - don't print it
			if (tk.getValue().equals(""))
				continue;
			System.out.println("Token " + counter++ + " - Type: " + tk.getType() + " - Value: " + tk.getValue());
		}
	}
}
