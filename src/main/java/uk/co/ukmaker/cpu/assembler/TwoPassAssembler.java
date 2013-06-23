package uk.co.ukmaker.cpu.assembler;

import java.util.ArrayList;
import java.util.List;

import uk.co.ukmaker.cpu.Memory;
import uk.co.ukmaker.cpu.Opcode;
import uk.co.ukmaker.cpu.OpcodeBits;

/**
 * 
 * Address LOOP = new Address();
 * 
 * a.op(JR, LOOP, CC_NONE);
 * ..
 * ..
 * LOOP.$(a.op(ADD R_0, R_1, CC_NONE));
 * 
 * 
 * @author duncan
 *
 */

public class TwoPassAssembler {

	private Memory memory;
	private int addr = 0;
	
	private List<Instruction> instructions = new ArrayList<Instruction>();
	
	
	public void setMemory(Memory m) {
		memory = m;
	}
	
	public int getAddress() {
		return addr;
	}
	
	public void assemble() {
		addr = 0;
		for(Instruction i : instructions) {
			memory.write(addr, i.assemble());
			addr++;
		}
	}
	
	public int constant(Constant c) {
		instructions.add(c);
		return addr++;
	}
	
	public int op(Opcode opcode, int ccBits) {
		instructions.add(new NoArgs(opcode, ccBits));
		return addr++;
	}
	
	public int op(Opcode opcode, int r, int ccBits) {
		instructions.add(new OneArg(opcode, new Argument(r), ccBits));
		return addr++;
	}
	
	public int op(Opcode opcode, Argument r, int ccBits) {
		instructions.add(new OneArg(opcode, r, ccBits));
		return addr++;
	}
	
	public int op(Opcode opcode, Address r, int ccBits) {
		instructions.add(new OneArg(opcode, new RelativeAddress(addr, r), ccBits));
		return addr++;
	}
	
	public int op(Opcode opcode, int a, int b, int ccBits) {
		instructions.add(new TwoArgs(opcode, new Argument(a), new Argument(b), ccBits));
		return addr++;
	}
	
	public int op(Opcode opcode, int a, Argument b, int ccBits) {
		instructions.add(new TwoArgs(opcode, new Argument(a), b, ccBits));
		return addr++;
	}
	
	public int op(Opcode opcode, int a, Address b, int ccBits) {
		instructions.add(new TwoArgs(opcode, new Argument(a), new RelativeAddress(addr, b), ccBits));
		return addr++;
	}
	
	public int op(Opcode opcode, Argument a, int b, int ccBits) {
		instructions.add(new TwoArgs(opcode, a, new Argument(b), ccBits));
		return addr++;
	}
	
	public int op(Opcode opcode, Address a, int b, int ccBits) {
		instructions.add(new TwoArgs(opcode, new RelativeAddress(addr, a), new Argument(b), ccBits));
		return addr++;
	}
	
	public int op(Opcode opcode, Argument a, Argument b, int ccBits) {
		instructions.add(new TwoArgs(opcode, a, b, ccBits));
		return addr++;
	}
	
	public int op(Opcode opcode, Address a, Address b, int ccBits) {
		instructions.add(new TwoArgs(opcode, new RelativeAddress(addr, a), new RelativeAddress(addr, b), ccBits));
		return addr++;
	}

	public int op(Opcode opcode, int a, Constant b, int ccBits) {
		instructions.add(new TwoArgs(opcode, new Argument(a), new Argument(b.getOffset()), ccBits));
		return addr++;
	}
	
}
