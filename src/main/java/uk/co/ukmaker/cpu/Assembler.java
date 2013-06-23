package uk.co.ukmaker.cpu;

public class Assembler {

	private Memory memory;
	private int addr = 0;
	
	public void setMemory(Memory m) {
		memory = m;
	}
	
	public int getAddress() {
		return addr;
	}
	
	public void constant(int c) {
		memory.write(addr,  c);
		addr++;
	}
	
	public int op(Opcode opcode, int ccBits) {
		int op = opcode.getCode() | ccBits;
		memory.write(addr, op);
		return addr++;
	}
	
	public int op(Opcode opcode, int a, int b, int ccBits) {
		int op = opcode.getCode() | ((a & 0b01111) << OpcodeBits.OPA_SHIFT) | ((b & 0b01111) << OpcodeBits.OPB_SHIFT) | ccBits;
		memory.write(addr, op);
		return addr++;
	}
	
	public int op(Opcode opcode, int r, int ccBits) {
		int op = opcode.getCode() | ((r & 0b011111111) << OpcodeBits.OPB_SHIFT) | ccBits;
		memory.write(addr, op);
		return addr++;
	}
	
	public int rel(int targetAddr) {
		int rel = targetAddr - addr -1;
		return rel;
	}
}
