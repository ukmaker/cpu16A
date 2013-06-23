package uk.co.ukmaker.cpu.assembler;

import uk.co.ukmaker.cpu.Opcode;
import uk.co.ukmaker.cpu.OpcodeBits;

public class OneArg implements Instruction {
	
	private Opcode opcode;
	private int cc;
	private Argument arg;
	
	public OneArg(Opcode opcode, Argument arg, int cc) {
		this.opcode = opcode;
		this.arg = arg;
		this.cc = cc;
	}
	
	public int assemble() {
		
		int op = opcode.getCode() | ((arg.getValue() & 0b011111111) << OpcodeBits.OPB_SHIFT) | cc;

		return op;
	}

}
