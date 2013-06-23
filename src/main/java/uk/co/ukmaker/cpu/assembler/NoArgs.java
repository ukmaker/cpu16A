package uk.co.ukmaker.cpu.assembler;

import uk.co.ukmaker.cpu.Opcode;

public class NoArgs implements Instruction {
	
	private Opcode opcode;
	private int cc;
	
	public NoArgs(Opcode opcode, int cc) {
		this.opcode = opcode;
		this.cc = cc;
	}
	
	public int assemble() {
		int op = opcode.getCode() | cc;
		return op;
	}

}
