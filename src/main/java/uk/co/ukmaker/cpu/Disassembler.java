package uk.co.ukmaker.cpu;

import static uk.co.ukmaker.cpu.OpcodeBits.*;

public class Disassembler {
	
	private Processor processor;
	
	public Disassembler(Processor p) {
		processor = p;
	}

	public String disassemble(int instruction) {
		
		int code = instruction & 0x001f;
		int r, a, b;
		
		Opcode opcode = Opcode.forInt(code);
		if(opcode == null && (instruction & 0x1fff) == 0x1fff) {
			opcode = Opcode.HALT;
		}
		
		String txt = opcode.getName();

		switch(opcode) {
		
		case JR:
		case JL:
			r = processor.decodeR8(instruction);
			txt = txt.replace("r", Integer.toString(r));
			break;
			
		case JRX:
		case JLX:
			r = processor.decodeR(instruction);
			txt = txt.replace("r", Integer.toString(r));
			break;
			
		default:
			a = processor.decodeA(instruction);
			b = processor.decodeB(instruction);
			txt = txt.replace("a", "*");
			txt = txt.replace("b", "^");
			txt = txt.replace("*", Integer.toHexString(a));
			txt = txt.replace("^", Integer.toHexString(b));
		}
		
		switch(instruction & CC_MASK) {
		case CC_NONE:
			txt = txt + "  CC_NONE";
			break;
		case CC_C:
			txt = txt + "  CC_C";
			break;
		case CC_NC:
			txt = txt + "  CC_NC";
			break;
		case CC_Z:
			txt = txt + "  CC_Z";
			break;
		case CC_NZ:
			txt = txt + "  CC_NZ";
			break;
		case CC_V:
			txt = txt + "  CC_V";
			break;
		case CC_NV:
			txt = txt + "  CC_NV";
			break;
		}

		return txt;
	}
}
