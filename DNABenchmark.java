import java.io.File;
import java.util.*;
/**
 * Code for benchmarking the time taken to simulate cutting
 * and splicing strands of DNA. These benchmark methods are
 * intended to be used in reasoning about tradeoffs in using 
 * a linked list to represent a strand of DNA and to compare
 * this representation with a simple String representation.
 * @author Owen Astrachan
 * @date 2/11/2009
 * 
 * Update: System.nanoTime, Repeated and threaded tests.
 * @contributor Brian Lavallee
 * @date 10 March 2016
 */
import javax.swing.JFileChooser;
public class DNABenchmark {
	
	/*
	 * Change these to change the tests to
	 * use a different type
	 */
//	private static final String strandType = "StringStrand";
	private static final String strandType = "LinkStrand";
//	private static final String strandType = "StringBuilderStrand";
	
	private static final String ENZYME = "gat";
	private static final int TRIALS = 2;
	
	private static String mySource;
	/**
	 * Return a string representing the DNA read from the scanner, ignoring any
	 * characters can't be part of DNA and converting all characters to lower
	 * case.
	 * 
	 * @param s is the Scanner read from
	 * @return a string representing the DNA read, characters in the returned
	 *         string are restricted to 'c', 'g', 't', 'a'
	 */
	public static String dnaFromScanner(Scanner s) {
		StringBuilder buf = new StringBuilder();
		while (s.hasNextLine()) {
			String line = s.nextLine().toLowerCase();
			for (int k = 0; k < line.length(); k++) {
				char ch = line.charAt(k);
				if ("acgt".indexOf(ch) != -1) {
					buf.append(ch);
				}
			}
		}
		return buf.toString();
	}
 
	public static String strandSpliceBenchmark(String enzyme, String splicee, String className)
			throws Exception {
		String dna = mySource;
		IDnaStrand strand;
		try {
			strand = (IDnaStrand) Class.forName(className).newInstance();
			strand.initialize(dna);
			long length = strand.size();
			IDnaStrand recomb = strand.cutAndSplice(enzyme, splicee);
			long length2 = strand.size();
			if (length != length2) {
				System.err.printf("trouble splicing %d strand to %d\n", length, length2);
			}
			long recLength = recomb.size();
			double time = 0;
			for (int i = 0; i < TRIALS; i++) {
				Thread thread = new Thread(() -> {
					strand.cutAndSplice(enzyme, splicee);
				});
				double start = System.nanoTime();
				thread.run();
				thread.join();
				time += (System.nanoTime() - start) / TRIALS / 1e9;
			}
			String ret = String.format("%s:\t%,15d\t%,15d\t%1.3f\t%s", className, splicee.length(), recLength, time,
					recomb.getStats());
			return ret;
		} catch (ClassNotFoundException e) {
			return "could not create class " + className;
		}
	}
	public static void main(String[] args)
			throws Exception {
		File file = FileSelector.selectFile(); // ourChooser.getSelectedFile();
		mySource = dnaFromScanner(new Scanner(file));
		System.out.printf("dna length = %,d\n", mySource.length());
		System.out.println("cutting at enzyme " + ENZYME);
		System.out.println("-----");
		System.out.printf("Class\t%23s\t%12s\ttime\n", "splicee", "recomb");
		System.out.println("-----");
		for (int j = 8; j <= 32; j++) {
			StringBuilder b = new StringBuilder("");
			int spSize = (int) Math.pow(2, j);
			for (int k = 0; k < spSize; k++) {
				b.append("c");
			}
			String splicee = b.toString();
			String results = strandSpliceBenchmark(ENZYME, splicee, strandType);
			System.out.println(results);
		}
		System.exit(0);
	}
}
