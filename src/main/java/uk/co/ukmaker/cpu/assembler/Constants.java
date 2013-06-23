package uk.co.ukmaker.cpu.assembler;

import java.util.List;

/**
 * Used to mark the start of a constant pool so that later references are generated
 * relative to it.
 * 
 * Constants PORTS = new Constants();
 * Constant ADC = new Constant(1234);
 * Constant DAC = new Constant(456);
 * 
 * PORTS.$(a,
 * 	ADC,
 *  DAC
 * );
 * 
 * a.op(LDC R_0, ADC, CC_NONE);
 * 
 * @author duncan
 *
 */
public class Constants {
	
	private List<Constant> constants;
	
	public void $(TwoPassAssembler a, Constant... constants) {
		
		int offset = 0;
		
		for(Constant c : constants) {
			c.setOffset(offset++);
			a.constant(c);
		}
	}

}
