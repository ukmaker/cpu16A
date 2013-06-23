package uk.co.ukmaker.cpu;

/**
 * A normal register
 * 
 * @author duncan
 *
 */
public class Register {
	
	protected int value;
	protected int dIn;
	protected boolean load;
	
	
	public int read() {
		return value;
	}
	
	public void clock() {
		if(load) {
			value = dIn;
		}
	}
	
	public void setLoad(boolean load) {
		this.load = load;
	}
	
	public void setDin(int dIn) {
		this.dIn = dIn;
	}

}
