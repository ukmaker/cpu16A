package uk.co.ukmaker.cpu;

/**
 * 16 registers
 * 0 - r0
 * 1 - r1
 * 2 - r2
 * 3 - r3
 * 4 - IX
 * 5 - IY
 * 6 - IZ
 * 7 - DSP
 * 8 - RSP
 * 9 - FP
 * a - LA
 * b - IA
 * c - LPA
 * d - LPC
 * e - CP
 * f - PC
 * 
 * @author duncan
 *
 */
public class RegisterFile {
	
	public static final int R_PC = 15;
	public static final int R_CP = 14;
	public static final int R_LPC = 13;
	public static final int R_LPA = 12;
	public static final int R_IA = 11;
	public static final int R_LA = 10;
	public static final int R_FP = 9;
	public static final int R_RSP = 8;
	public static final int R_DSP = 7;
	public static final int R_IZ = 6;
	public static final int R_IY = 5;
	public static final int R_IX = 4;
	public static final int R_3 = 3;
	public static final int R_2 = 2;
	public static final int R_1 = 1;
	public static final int R_0 = 0;
	
	private int[] register = new int[16];
	
	public int read(int reg) {
		return register[reg];
	}
	
	public void write(int reg, int value) {
		register[reg] = value;
	}
}
