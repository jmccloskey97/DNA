import java.util.LinkedList;
import javax.swing.text.html.HTMLDocument.Iterator;

public class LinkStrand implements IDnaStrand
{
	private class Node 
	{
		String info;
		Node next;
		public Node(String s) 
		{
			info = s;
			next = null;
		}
	}
	
	private Node myFirst,myLast;
	private long mySize;
	private int myAppends;
	
	private Node list;
	private int count;
	private int dex;
	
	public LinkStrand(String s)
	{
		initialize(s);
	}
	
	public LinkStrand()
	{
		this("");
	}
	
	@Override
	public void initialize(String source) 
	{
		Node fir = new Node(source);
		myFirst = fir;
		myLast = fir;
		mySize = 1;
		myAppends = 0;
		
		list = fir;
		count = 0;
		dex = 0;
	}
	
	@Override
	public IDnaStrand cutAndSplice(String enzyme, String splicee) 
	{
		int pos = 0;
		int start = 0;
		String in = "";
		
		Node firz = myFirst;
		while(firz != null)
		{
			in += firz.info;
			firz = firz.next;
		}		
		
		String search = in;
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
	

	@Override
	public long size() 
	{
		long mySize = 0;
		Node head = myFirst;
		while(head != null)
		{
			mySize += head.info.length();
			head = head.next;
		}		
		
		return mySize;
	}
	

	@Override
	public String getStats() 
	{
		return String.format("# appends = %d", myAppends);
	}



	@Override
	public IDnaStrand append(String dna) 
	{
		Node d = new Node(dna);		
		myLast.next = d;
		myLast = d;
		myAppends++;
		
		return this;	
	}

	@Override
	public IDnaStrand reverse() 
	{
		LinkStrand rev = new LinkStrand();
		rev = this;
	
		Node prev = null;
		Node current = rev.myFirst;
		Node next = null;
		while(current != null)
		{
			StringBuilder copy = new StringBuilder(current.info);
			String s = copy.reverse().toString();
			current.info = s;
			
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		
		return rev;
		

	}


	@Override
	public char charAt(int index) 
	{
		if (index > size())
		{
			throw new IndexOutOfBoundsException();
		}
		
		if(index < count)
		{
			count = 0;
			dex = 0;
			list = myFirst;
		}
		
		while (count != index) 
		{
			count++;
			dex++;
			
			if (dex >= list.info.length()) 
			{
				dex = 0;
				list = list.next;
			}
		}

		
        return list.info.charAt(dex);

	}
	
	public String toString()
	{
		StringBuilder a = new StringBuilder();
		Node first = myFirst;
		while(first != null)
		{
			a.append(first.info);
			first = first.next;
		}	
		
		return a.toString();	
	}

	@Override
	public IDnaStrand getInstance(String source) 
	{
		return new LinkStrand(source);
	}

}
