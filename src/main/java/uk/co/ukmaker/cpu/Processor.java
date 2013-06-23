package uk.co.ukmaker.cpu;


public class Processor {
	
	private Bus bus;
	private ALU alu;
	private AddressGenerator addressGenerator;
	private RegisterFile registerFile;
	private Debugger debugger;
	
	private int instruction;
	
	private int statusRegister;
	
	public static final int BUS_ERR_VECTOR = 0;
	
	public static final int PC = 15;
	public static final int CC = 14;
	
	public static final int CC_C_BIT = 0b0000000000000001;
	public static final int CC_Z_BIT = 0b0000000000000010;
	public static final int CC_V_BIT = 0b0000000000000100;
	
	private boolean halted;
	private long ticks;
	
	public Processor() {
		bus = new Bus();
		alu = new ALU();
		registerFile = new RegisterFile();
		addressGenerator = new AddressGenerator(registerFile);
		
	}
	
	public void attachDebugger(Debugger d) {
		debugger = d;
	}
	
	public void detachDebugger() {
		debugger = null;
	}
	
	public Bus getBus() {
		return bus;
	}
	
	public void log(String message) {
		System.out.println(message);
	}
	
	public void reset() {
		registerFile.write(RegisterFile.R_PC, 0);
		halted = false;
		ticks = 0;
	}
	
	public boolean isHalted() {
		return halted;
	}
	
	public void fetch() {
		
		int addr;
		ticks++;
		
		try {
			addr = registerFile.read(RegisterFile.R_PC);
			if(debugger != null) {
				debugger.debug(this, addr);
			}
			instruction = bus.read(addr);
			registerFile.write(RegisterFile.R_PC, addr+2);
			
		} catch (BusException e) {
			
			ticks++;
			
			log("Caught a BusException, trying to call handler");
			e.printStackTrace(System.out);
			
			registerFile.write(RegisterFile.R_PC, BUS_ERR_VECTOR);
			
			try {
				
				addr = registerFile.read(RegisterFile.R_PC);
				if(debugger != null) {
					debugger.debug(this, addr);
				}
				instruction = bus.read(addr);
				registerFile.write(RegisterFile.R_PC, addr+2);

			} catch (BusException e1) {
				
				log("Caught a BusException trying to handle a BusException - halting");
				e1.printStackTrace(System.out);
				instruction = Opcode.HALT.getCode();
			}
		}
	}
	
	public int getInstruction() {
		return instruction;
	}
	
	public long getTicks() {
		return ticks;
	}
	
	public void execute() throws BusException {
		
		// only execute if the cc bits allow it
		switch(instruction & OpcodeBits.CC_MASK) {
	
		case OpcodeBits.CC_NONE:
			break;
			
		case OpcodeBits.CC_C:
			if((statusRegister & CC_C_BIT) == 0) {
				return;
			}
			break;

		case OpcodeBits.CC_NC:
			if((statusRegister & CC_C_BIT) != 0) {
				return;
			}
			break;

		case OpcodeBits.CC_Z:
			if((statusRegister & CC_Z_BIT) == 0) {
				return;
			}
			break;

		case OpcodeBits.CC_NZ:
			if((statusRegister & CC_Z_BIT) != 0) {
				return;
			}
			break;

		case OpcodeBits.CC_V:
			if((statusRegister & CC_V_BIT) == 0) {
				return;
			}
			break;

		case OpcodeBits.CC_NV:
			if((statusRegister & CC_V_BIT) != 0) {
				return;
			}
			break;
		}
		
		if((instruction & ~OpcodeBits.CC_MASK) == Opcode.HALT.getCode()) {
			halted = true;
			return;
		}
		
		if((instruction & OpcodeBits.ALU_OP_MASK) == OpcodeBits.ALU_OP_TEST) {
			executeAlu();
			return;
		}
		
		if((instruction & OpcodeBits.LDST_MASK) == OpcodeBits.LDST_TEST) {
			executeLdSt();
			return;
		}
		
		if((instruction & OpcodeBits.LDSTE_MASK) == OpcodeBits.LDSTE_TEST) {
			executeLdSt();
			return;
		}
		
		int jp = instruction & OpcodeBits.JUMP_MASK;
		if(jp == OpcodeBits.OP_JR) {
			executeJR();
			return;
		}
		if(jp == OpcodeBits.OP_JRX) {
			executeJRX();
			return;
		}
		if(jp == OpcodeBits.OP_JL) {
			executeJL();
			return;
		}
		if(jp == OpcodeBits.OP_JLX) {
			executeJLX();
			return;
		}
	}
	
	public void executeAlu() {
		alu.setA(registerFile.read(decodeA(instruction)));
		alu.setB(registerFile.read(decodeB(instruction)));
		alu.setOpcode(instruction & OpcodeBits.ALU_OP_BITS);
		alu.setCin((statusRegister & CC_C_BIT) != 0);
		alu.setVin((statusRegister & CC_V_BIT) != 0);
		alu.setZin((statusRegister & CC_Z_BIT) != 0);
		alu.execute();
		registerFile.write(decodeA(instruction), alu.getResult());
		statusRegister = setBit(statusRegister, CC_Z_BIT, alu.getZero());
		statusRegister = setBit(statusRegister, CC_C_BIT, alu.getCout());
		statusRegister = setBit(statusRegister, CC_V_BIT, alu.getOverflow());
	}
	
	public void executeLdSt() throws BusException {
		addressGenerator.setB(decodeB(instruction));
		addressGenerator.setOpcode(instruction & OpcodeBits.LDST_OP_BITS);
		addressGenerator.execute();
		if(addressGenerator.isWriteback()) {
			registerFile.write(decodeB(instruction), addressGenerator.getWritebackAddr());
		}
		
		if(addressGenerator.isLoad()) {
			if(addressGenerator.isImmediate()) {
				// LDI a,#b
				registerFile.write(decodeA(instruction), addressGenerator.getAddr());
			} else {
				registerFile.write(decodeA(instruction), bus.read(addressGenerator.getAddr()));
			}
		} else {
			if(addressGenerator.isImmediate()) {
				// STI [a],#b
				bus.write(registerFile.read(decodeA(instruction)), addressGenerator.getAddr());
			} else {
				// ST [b], a
				bus.write(addressGenerator.getAddr(), registerFile.read(decodeA(instruction)));
			}
		}
	}
	
	public void executeJR() {
		int offset = decodeR8(instruction);
		registerFile.write(RegisterFile.R_PC, 
				registerFile.read(RegisterFile.R_PC)+ offset
		);
	}
	
	public void executeJRX() {
		int a = decodeA(instruction);
		int r = decodeR(instruction);
		int addr = registerFile.read(a) + (r << 1);
		registerFile.write(RegisterFile.R_PC, addr);
	}
	
	public void executeJL() {
		int offset = decodeR8(instruction);
		registerFile.write(RegisterFile.R_LA, 
				registerFile.read(RegisterFile.R_PC)
		);
		registerFile.write(RegisterFile.R_PC, 
				registerFile.read(RegisterFile.R_PC)+ offset
		);
	}
	
	public void executeJLX() {
		int a = decodeA(instruction);
		int r = decodeR(instruction);

		int addr = registerFile.read(a) + (r << 1);
		registerFile.write(RegisterFile.R_LA, 
				registerFile.read(RegisterFile.R_PC)
		);
		registerFile.write(RegisterFile.R_PC, addr);
	}
	
	public int decodeA(int instruction) {
		return (instruction & OpcodeBits.OPA_MASK) >> OpcodeBits.OPA_SHIFT;
	}

	public int decodeB(int instruction) {
		return (instruction & OpcodeBits.OPB_MASK) >> OpcodeBits.OPB_SHIFT;
	}
	
	public int decodeR(int instruction) {
		int a = decodeA(instruction);
		int b = decodeB(instruction);
		if((b & 0x0008) != 0) {
			b |= 0xfffffff0;
		}
		
		return b;
	}
	
	public int decodeR8(int instruction) {
		// offset is signed 8-bit
		int offset = ((instruction & OpcodeBits.JR_OFFSET_MASK) >> (OpcodeBits.JR_OFFSET_SHIFT -1));
		// sign-extend from the 8th bit
		if((offset & 0x0080) != 0) {
			offset |= 0xffffff00;
		}
		return offset;
	}
	
	public int setBit(int value, int mask, boolean set) {
		if(set) {
			return value | mask;
		}
		
		return value & ~mask;
	}

}
