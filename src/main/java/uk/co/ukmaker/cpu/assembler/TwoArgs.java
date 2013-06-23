package uk.co.ukmaker.cpu.assembler;

import uk.co.ukmaker.cpu.Opcode;
import uk.co.ukmaker.cpu.OpcodeBits;

public class TwoArgs implements Instruction {
	
	private Opcode opcode;
	private int cc;
	private Argument arg1;
	private Argument arg2;
	
	public TwoArgs(Opcode opcode, Argument arg1, Argument arg2, int cc) {
		this.opcode = opcode;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.cc = cc;
	}
	
	public int assemble() {
		
		int op = opcode.getCode() | ((arg1.getValue() & 0b01111) << OpcodeBits.OPA_SHIFT) | ((arg2.getValue() & 0b01111) << OpcodeBits.OPB_SHIFT) | cc;

		return op;
	}

}
