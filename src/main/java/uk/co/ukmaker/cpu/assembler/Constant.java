package uk.co.ukmaker.cpu.assembler;

import uk.co.ukmaker.cpu.Opcode;

public class Constant implements Instruction {
	
	private int value;
	private int offset;
	
	public Constant(int value) {
		this.value = value;
	}
	
	public Constant(int offset, int value) {
		this.offset = offset;
		this.value = value;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int assemble() {
		return value;
	}
	
	public int getOffset() {
		return offset;
	}

}
