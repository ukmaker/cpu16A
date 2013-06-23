package uk.co.ukmaker.cpu;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Opcode {

	ADD(  0b00000000, "ADD a,b"),
	ADD_B(0b00000001, "ADD.B a,b"),
	
	SUB(  0b00000010, "SUB a,b"),
	SUB_B(0b00000011, "SUB.B a,b"),
	
	AND(  0b00000100, "AND a,b"),
	AND_B(0b00000101, "AND.B a,b"),

	OR(   0b00000110, "OR a,b"),
	OR_B( 0b00000111, "OR.B a,b"),

	XOR(   0b00001000, "XOR a,b"),
	XOR_B( 0b00001001, "XOR.B a,b"),

	MOV(   0b00001010, "MOV a,b"),
	MOV_B( 0b00001011, "MOV.B a,b"),
	
	SLC(   0b00001100, "SLC a,b"),
	SLC_B( 0b00001101, "SLC.B a,b"),
	
	SRC(   0b00001110, "SRC a,b"),
	SRC_B( 0b00001111, "SRC.B a,b"),
	

	LDI(  0b00010000, "LDI a,#b"),
	STI(  0b00010001, "STI [a],#b"),
	
	LDX(  0b00010010, "LD a,[b]"),
	STX(  0b00010011, "ST [b],a"),
	
	LDXP( 0b00010100, "LD a,[b++]"),
	STXP( 0b00010101, "ST [b++],a"),

	LDXM( 0b00010110, "LD a,[--b]"),
	STXM( 0b00010111, "ST [--b],a"),

	LDC(  0b00011000, "LDC a,[cp+#b]"),
	STC(  0b00011001, "STC [cp+#b],a"),
	
	JR(   0b00011010, "JR #r"),
	JRX(  0b00011011, "JRX a+#b"),
	JL(   0b00011100, "JL #r"),
	JLX(  0b00011101, "JLX a+#b"),
	
	HALT( 0x1fff, "HALT")
;
	
	private int code;
	private String name;
	
	private static final Map<Integer, Opcode> map = new HashMap<>();
	
	static {
		for(Opcode op : EnumSet.allOf(Opcode.class)) {
			map.put(op.getCode(), op);
		}
	}
	
	
	private Opcode(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public static Opcode forInt(int code) {
		return map.get(code);
	}
}