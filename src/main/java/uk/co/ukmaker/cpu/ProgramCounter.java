package uk.co.ukmaker.cpu;

/**
 * Normal flow:
 * 
 * Fetch: addr <- pc; alu = add pc, #2, addrD=pc
 * 
 * Jump: ld pc,pc+#offset
 * 
 * JL: ld pc, pc+#offset; la <- pc
 * 
 * RETI: ld pc, ia
 * 
 * 
 * @author duncan
 *
 */

public class ProgramCounter extends Register {
	
}
