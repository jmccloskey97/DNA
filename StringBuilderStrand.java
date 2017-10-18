import java.util.Iterator;


/**
 * Simple but somewhat efficient implementation of IDnaStrand. \ This
 * implementation uses StringBuilders to represent genomic/DNA data.
 * 
 * @author ola
 * @date January 2008, modified and commented September 2008
 * @date October 2011, made myInfo a StringBuilder rather than a String
 * @date October 2011, modified to add new methods and remove old ones
 * @date October 2016, updated to implement new interface
 */

public class StringBuilderStrand implements IDnaStrand {
	
	private StringBuilder myInfo;
	private int myAppends;

	public StringBuilderStrand(){
		this("");
	}
	/**
	 * Create a strand representing s. No error checking is done to see if s
	 * represents valid genomic/DNA data.
	 * 
	 * @param s
	 *            is the source of cgat data for this strand
	 */

	public StringBuilderStrand(String s) {
		initialize(s);
	}

	@Override 
	public IDnaStrand cutAndSplice(String enzyme, String splicee) {
		int pos = 0;
		int start = 0;
		StringBuilder search = myInfo;
		boolean first = true;
		IDnaStrand ret = null;

		// code identical to StringStrand, both String and StringBuilder
		// support .substring and .indexOf
		
		while ((pos = search.indexOf(enzyme, pos)) >= 0) {
			if (first) {
				ret = getInstance(search.substring(start, pos));
				first = false;
			} else {
				ret.append(search.substring(start, pos));

			}
			start = pos + enzyme.length();
			ret.append(splicee);
			pos++;
		}

		if (start < search.length()) {
			// NOTE: This is an important special case! If the enzyme
			// is never found, return an empty String.
			if (ret == null) {
				ret = getInstance("");
			} else {
				ret.append(search.substring(start));
			}
		}
		return ret;
	}

	/**
	 * Initialize this strand so that it represents the value of source. No
	 * error checking is performed.
	 * 
	 * @param source
	 *            is the source of this enzyme
	 */
	@Override 
	public void initialize(String source) {
		myInfo = new StringBuilder(source);
		myAppends = 0;
	}

	/**
	 * @return number of base-pairs in this strand
	 */
	@Override
	public long size() {
		return myInfo.length();
	}

	@Override
	public String toString() {
		return myInfo.toString();
	}

	/**
	 * Simply append a strand of dna data to this strand. No error checking is
	 * done. This method isn't efficient; it doesn't use a StringBuilder or a
	 * StringBuffer.
	 * 
	 * @param dna
	 *            is the String appended to this strand
	 */
	public IDnaStrand append(String dna) {
		myInfo.append(dna);
		myAppends++;
		return this;
	}

	public IDnaStrand reverse() {
		StringBuilder copy = new StringBuilder(myInfo);
		StringBuilderStrand ss = new StringBuilderStrand("replace");
		copy.reverse();
		ss.myInfo = copy;
		return ss;
	}

	@Override
	public String getStats() {
		return String.format("# appends = %d", myAppends);
	}
 
	public char charAt(int index) {
		return myInfo.charAt(index);
	}
	
	@Override
	public IDnaStrand getInstance(String source) {
		return new StringBuilderStrand(source);
	}
}
