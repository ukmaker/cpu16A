package uk.co.ukmaker.cpu;

public class AddressGenerator {

	private int b;
	private int opcode;
	private int addr;
	private boolean writeback;
	private int writebackAddr;
	private boolean load;
	private boolean immediate;

	private RegisterFile registerFile;

	public AddressGenerator(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public int getAddr() {
		return addr;
	}

	public void setB(int b) {
		this.b = b;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public boolean isWriteback() {
		return writeback;
	}

	public int getWritebackAddr() {
		return writebackAddr;
	}

	public boolean isLoad() {
		return load;
	}
	
	public boolean isImmediate() {
		return immediate;
	}

	public void execute() {
		
		immediate = false;
		
		switch (Opcode.forInt(opcode)) {

		case LDI:
			immediate = true;
			load = true;
			writeback = false;
			addr = b;
			break;

		case STI:
			immediate = true;
			load = false;
			writeback = false;
			addr = b;
			break;

		case LDX:
			load = true;
			writeback = false;
			addr = registerFile.read(b);
			break;

		case STX:
			load = false;
			writeback = false;
			addr = registerFile.read(b);
			break;

		case LDXP:
			load = true;
			writeback = true;
			addr = registerFile.read(b);
			writebackAddr = addr + 2;
			break;

		case STXP:
			load = false;
			writeback = true;
			addr = registerFile.read(b);
			writebackAddr = addr + 2;
			break;

		case LDXM:
			load = true;
			writeback = true;
			addr = registerFile.read(b) - 2;
			writebackAddr = addr;
			break;

		case STXM:
			load = false;
			writeback = true;
			addr = registerFile.read(b) - 2;
			writebackAddr = addr;
			break;

		case LDC:
			load = true;
			writeback = false;
			addr = registerFile.read(RegisterFile.R_CP) + (b << 1);
			break;

		case STC:
			load = false;
			writeback = false;
			addr = registerFile.read(RegisterFile.R_CP) + (b << 1);
			break;

		default:
			throw new RuntimeException("Illegal opcode for AddressGenerator: "
					+ opcode);
		}
	}

}
