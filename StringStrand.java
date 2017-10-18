import java.util.Iterator;

/**
 * Simple but somewhat efficient implementation of IDnaStrand. \ This
 * implementation uses StringBuilders to represent genomic/DNA data.
 * 
 * @author ola
 * @date January 2008, modified and commented September 2008
 * @date October 2011, made myInfo a StringBuilder rather than a String
 * @date October 2011, modified to add new methods and remove old ones
 * @date October 2016, modified for new version of IDnaStrand
 */

public class StringStrand implements IDnaStrand {
	
	private String myInfo;
	private int myAppends;

	public StringStrand(){
		this("");
	}
	/**
	 * Create a strand representing s. No error checking is done to see if s
	 * represents valid genomic/DNA data.
	 * 
	 * @param s
	 *            is the source of cgat data for this strand
	 */
	public StringStrand(String s) {
		initialize(s);
	}

	@Override 
	public IDnaStrand cutAndSplice(String enzyme, String splicee) {
		int pos = 0;
		int start = 0;
		String search = myInfo;
		boolean first = true;
		IDnaStrand ret = null;

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
		myInfo = new String(source);
		myAppends = 0;
	}
	
	/**
	 * @return the number of base pairs in this strand
	 */
	
	@Override 
	public long size() {
		return myInfo.length();
	}

	@Override
	public String toString() {
		return myInfo;
	}

	/**
	 * Simply append a strand of dna data to this strand. No error checking is
	 * done. This method isn't efficient; it doesn't use a StringBuilder or a
	 * StringBuffer.
	 * 
	 * @param dna
	 *            is the String appended to this strand
	 */
	@Override 
	public IDnaStrand append(String dna) {
		myInfo = myInfo + dna;
		myAppends++;
		return this;
	}

	@Override 
	public IDnaStrand reverse() {
		StringBuilder copy = new StringBuilder(myInfo);
		copy.reverse();
		StringStrand ss = new StringStrand(copy.toString());
		return ss;
	}
	
	@Override 
	public IDnaStrand getInstance(String source) {
		return new StringStrand(source);
	}

	@Override 
	public String getStats() {
		return String.format("# appends = %d", myAppends);
	}
	
	@Override 
	public char charAt(int index){
		return myInfo.charAt(index);
	}
}
