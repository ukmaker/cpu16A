package uk.co.ukmaker.cpu;

import org.junit.Test;
import static org.junit.Assert.*;

import static uk.co.ukmaker.cpu.Opcode.*;

public class ALUTest {

	private ALU alu = new ALU();


	@Test
	public void shouldADD() {
	
		test(ADD, 0x0000, 0x0000, "---", 0x0000, "-Z-");
		test(ADD, 0x0000, 0x0000, "--C", 0x0001, "---");

		test(ADD, 0x0001, 0x0000, "---", 0x0001, "---");
		test(ADD, 0x0001, 0x0000, "--C", 0x0002, "---");
		
		test(ADD, 0x2222, 0x4444, "---", 0x6666, "---");

		// overflow maxint
		test(ADD, 0x7fff, 0x0001, "---", 0x8000, "V--");
		test(ADD, 0x7fff, 0x0000, "--C", 0x8000, "V--");

		// overflow -maxint
		test(ADD, 0x8000, 0xffff, "---", 0x7fff, "V-C");
		// no overflow due to carryin
		test(ADD, 0x8000, 0xffff, "--C", 0x8000, "--C");
	}
	
	@Test
	public void shouldADD_B() {
	
		test(ADD_B, 0x0000, 0x0000, "---", 0x0000, "-Z-");
		test(ADD_B, 0x0000, 0x0000, "--C", 0x0001, "---");
		// should leave high byte of a untouched
		test(ADD_B, 0x5500, 0x0000, "---", 0x5500, "-Z-");

		test(ADD_B, 0x0001, 0x0000, "---", 0x0001, "---");
		test(ADD_B, 0x0001, 0x0000, "--C", 0x0002, "---");
		
		test(ADD_B, 0x2222, 0x4444, "---", 0x2266, "---");

		// overflow maxint
		test(ADD_B, 0x007f, 0x0001, "---", 0x0080, "V--");
		test(ADD_B, 0x007f, 0x0000, "--C", 0x0080, "V--");

		// overflow -maxint
		test(ADD_B, 0x0080, 0x00ff, "---", 0x007f, "V-C");
		// no overflow due to carryin
		test(ADD_B, 0x0080, 0x00ff, "--C", 0x0080, "--C");
	}
	
	@Test
	public void shouldSUB() {
		test(SUB,  0x0000, 0x0000, "---", 0x0000, "-Z-");
		test(SUB,  0x0000, 0x0000, "--C", 0xffff, "--C");
		
		// sub two big numbers and get no carry and overflow
		// (-256) - 255 -1 = -512
		test(SUB,  0x8000, 0x7fff, "--C", 0x0000, "VZ-");
	}
	
	@Test
	public void shouldSUB_B() {
		test(SUB_B,  0x0000, 0x0000, "---", 0x0000, "-Z-");
		test(SUB_B,  0x0000, 0x0000, "--C", 0x00ff, "--C");
		
		// sub two big numbers and get no carry and overflow
		// (-256) - 255 -1 = -512
		test(SUB_B,  0x8000, 0x7fff, "--C", 0x8000, "-ZC");
		test(SUB_B,  0x0080, 0x007f, "--C", 0x0000, "VZ-");
	}
	
	@Test
	public void shouldMOV() {
		test(MOV,  0x0000, 0x0000, "---", 0x0000, "---");
		test(MOV,  0x0000, 0xffff, "VZC", 0xffff, "VZC");
	}
	
	@Test
	public void shouldMOV_B() {
		test(MOV_B,  0x0000, 0x0000, "---", 0x0000, "---");
		test(MOV_B,  0xaa00, 0xffff, "VZC", 0xaaff, "VZC");
	}
	
	@Test
	public void shouldAND() {
		test(AND,  0x0000, 0x0000, "---", 0x0000, "-Z-");
		test(AND,  0x0000, 0xffff, "---", 0x0000, "-Z-");
		test(AND,  0xffff, 0x0000, "---", 0x0000, "-Z-");
		test(AND,  0xffff, 0xffff, "---", 0xffff, "---");
		test(AND,  0x0000, 0x0000, "V--", 0x0000, "VZ-");
	}
	
	@Test
	public void shouldAND_B() {
		test(AND_B,  0x0000, 0x0000, "---", 0x0000, "-Z-");
		test(AND_B,  0x0000, 0xffff, "---", 0x0000, "-Z-");
		test(AND_B,  0xffff, 0x0000, "---", 0xff00, "-Z-");
		test(AND_B,  0xffff, 0xffff, "---", 0xffff, "---");
		test(AND_B,  0x0000, 0x0000, "V--", 0x0000, "VZ-");
	}
	
	@Test
	public void shouldOR() {
		test(OR,  0x0000, 0x0000, "---", 0x0000, "-Z-");
		test(OR,  0x0000, 0xffff, "---", 0xffff, "---");
		test(OR,  0xffff, 0x0000, "---", 0xffff, "---");
		test(OR,  0xffff, 0xffff, "---", 0xffff, "---");
		test(OR,  0x0000, 0x0000, "V--", 0x0000, "VZ-");
	}
	
	@Test
	public void shouldOR_B() {
		test(OR_B,  0x0000, 0x0000, "---", 0x0000, "-Z-");
		test(OR_B,  0x0000, 0xffff, "---", 0x00ff, "---");
		test(OR_B,  0xffff, 0x0000, "---", 0xffff, "---");
		test(OR_B,  0xffff, 0xffff, "---", 0xffff, "---");
		test(OR_B,  0x0000, 0x0000, "V--", 0x0000, "VZ-");
	}
	
	@Test
	public void shouldXOR() {
		test(XOR,  0x0000, 0x0000, "---", 0x0000, "-Z-");
		test(XOR,  0x0000, 0xffff, "---", 0xffff, "---");
		test(XOR,  0xffff, 0x0000, "---", 0xffff, "---");
		test(XOR,  0xffff, 0xffff, "---", 0x0000, "-Z-");
		test(XOR,  0x0000, 0x0000, "V--", 0x0000, "VZ-");
	}
	
	@Test
	public void shouldXOR_B() {
		test(XOR_B,  0x0000, 0x0000, "---", 0x0000, "-Z-");
		test(XOR_B,  0x0000, 0xffff, "---", 0x00ff, "---");
		test(XOR_B,  0xffff, 0x0000, "---", 0xffff, "---");
		test(XOR_B,  0xffff, 0xffff, "---", 0xff00, "-Z-");
		test(XOR_B,  0x0000, 0x0000, "V--", 0x0000, "VZ-");
	}
	
	@Test
	public void shouldSLC() {
		test(SLC,  0x0000, 0x5555, "---", 0x0000, "-Z-");
		test(SLC,  0x0000, 0x5555, "--C", 0x0001, "---");
		test(SLC,  0x0020, 0x5555, "--C", 0x0041, "---");
		test(SLC,  0x8020, 0x5555, "--C", 0x0041, "--C");
	}
	
	@Test
	public void shouldSLC_B() {
		test(SLC_B,  0x0000, 0x5555, "---", 0x0000, "-Z-");
		test(SLC_B,  0x0000, 0x5555, "--C", 0x0001, "---");
		test(SLC_B,  0x0020, 0x5555, "--C", 0x0041, "---");
		test(SLC_B,  0x0080, 0x5555, "--C", 0x0001, "--C");
	}
	
	@Test
	public void shouldSRC() {
		test(SRC,  0x0000, 0x5555, "---", 0x0000, "-Z-");
		test(SRC,  0x0001, 0x5555, "---", 0x0000, "-ZC");
		test(SRC,  0x0040, 0x5555, "---", 0x0020, "---");
		test(SRC,  0x8040, 0x5555, "---", 0xc020, "---");
	}
	
	@Test
	public void shouldSRC_B() {
		test(SRC_B,  0x0000, 0x5555, "---", 0x0000, "-Z-");
		test(SRC_B,  0x0001, 0x5555, "---", 0x0000, "-ZC");
		test(SRC_B,  0x0040, 0x5555, "---", 0x0020, "---");
		test(SRC_B,  0x80c0, 0x5555, "---", 0x80e0, "---");
	}
	
	
	public void test(Opcode opcode, int a, int b, String bits, int expectedResult, String expectedBits) {
	
		alu.setA(a);
		alu.setB(b);
		alu.setCin(bits.substring(2,3).equals("C"));
		alu.setZin(bits.substring(1,2).equals("Z"));
		alu.setVin(bits.substring(0,1).equals("V"));
		alu.setOpcode(opcode.getCode());
		
		alu.execute();
		
		String input = String.format("%6s",opcode.getName()) + " "+hex(a)+","+hex(b)+" ["+bits+"]";
		String expected = hex(expectedResult) + " ["+expectedBits+"]";
		String actual = hex(alu.getResult()) + " ["+codes(alu)+"]";
		
		String message = "\n" + input + " = " + expected +"\n";
		message = message + "                          -> " + actual +"\n";
		System.out.println(message);
		assertTrue(message, expected.equals(actual));
	}
	
	public String hex(int val) {
		return String.format("0x%04x", val);
	}
	
	public String bin(int val) {
		return String.format("0b%5s", Integer.toBinaryString(val)).replace(" ", "0");
	}
	
	public String codes(ALU alu) {
		StringBuffer b = new StringBuffer();
		b.append(alu.getOverflow()  ? 'V' : '-');
		b.append(alu.getZero()  ? 'Z' : '-');
		b.append(alu.getCout() ? 'C' : '-');
		
		return b.toString();
	}
}