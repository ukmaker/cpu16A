package uk.co.ukmaker.cpu;

/**
 * This register may be loaded directly from the PC
 * 
 * @author duncan
 *
 */
public class LoopAddressRegister extends Register {
	
	private int pcIn;
	
	private boolean ldPc;
	
	public void clock() {
		if(ldPc) {
			value = pcIn;
		} else {
			super.clock();
		}
	}

}
