package uk.co.ukmaker.cpu;

public interface OpcodeBits {
	
	// ALU Operation
	//
	// fedcba9876543210
	// cccaaaabbbb0oooo

	public static final int ALU_OP_BITS = 0b0001111;
	public static final int ALU_OP_MASK = 0b0010000;
	public static final int ALU_OP_TEST = 0b0000000;
	
	public static final int OPA_MASK = 0b0001111000000000;
	public static final int OPA_SHIFT = 9;
	
	public static final int OPB_MASK = 0b0000000111100000;
	public static final int OPB_SHIFT = 5;
	
	public static final int OPR_SHIFT = 5;
	
	public static final int LDST_OP_BITS = 0b11111;
	// ld/st operations
	// cccaaaabbbb10ooo
	public static final int LDST_MASK = 0b11000;
	public static final int LDST_TEST = 0b10000;
	// LDI a,#b
	// LD a,[b]
	// LD a,[b++]
	// LD a,[--b]
	
	// STI [b],#a
	// LD  [b],a
	// LD  [b++],a
	// LD  [--b],a
	
	// extended load/store
	// cccaaaabbbb1100o
	public static final int LDSTE_MASK = 0b11110;
	public static final int LDSTE_TEST = 0b11000;
	// LDE a,[cp+#b]
	// STE [cp+#b],a
	
	public static final int JUMP_MASK = 0b011111;
	public static final int JR_OFFSET_MASK = 0b0001111111100000;
	public static final int JR_OFFSET_SHIFT = 5;
	// jump
	// cccrrrrrrrr11010
	// JR #r
	public static final int OP_JR = 0b011010;
	
	// cccaaaabbbb11011
	// JR a+#b
	public static final int OP_JRX = 0b011011;
	
	// cccrrrrrrrr11100
	// JL #r
	public static final int OP_JL = 0b011100;
	
	// cccaaaabbbb11101
	// JL a+#b
	public static final int OP_JLX = 0b011101;
	
	// Status register - condition code, interrupts
	// 15 - 8 : interrupt masks
	// 7      : interrupt enable
	//
	// 2 - 0  : condition codes
	//
	// ccc0000bbbb11110
	// pushs [b++]
	// ccc0001bbbb11110
	// pops [--b]
	
	// ccc0010bbbb11110
	// set #b
	// ccc0011bbbb11110
	// cl #b
	// ccc0100bbbb11110
	// test #b

		
	public static final int CC_MASK  = 0b1110000000000000;
	public static final int CC_NONE  = 0b0000000000000000;
	public static final int CC_Z     = 0b0010000000000000;
	public static final int CC_NZ    = 0b0100000000000000;
	public static final int CC_C     = 0b0110000000000000;
	public static final int CC_NC    = 0b1000000000000000;
	public static final int CC_V     = 0b1010000000000000;
	public static final int CC_NV    = 0b1100000000000000;
	
	public static final int CC_Z_BIT = 13;
	public static final int CC_C_BIT = 14;
	public static final int CC_V_BIT = 15;
}
