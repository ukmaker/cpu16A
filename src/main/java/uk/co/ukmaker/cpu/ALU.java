package uk.co.ukmaker.cpu;

public class ALU {
	
	public static final int BIT_CARRY = 0b00000001;
	public static final int BIT_ZERO =  0b00000010;
	public static final int BIT_OVFL =  0b00000100;
	
	private int a;
	private int b;
	
	private boolean cin;
	private boolean zin;
	private boolean vin;
	
	
	private int result;
	
	private boolean cout;
	private boolean zero;
	private boolean overflow;
	
	private int opcode;
	
	public ALU() { }
	
	public void setA(int a) {
		this.a = a;
	}
	
	public void setB(int b) {
		this.b = b;
	}
	
	public void setCin(boolean cin) {
		this.cin = cin;
	}
	
	public void setVin(boolean vin) {
		this.vin = vin;
	}
	
	public void setZin(boolean zin) {
		this.zin = zin;
	}
	
	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}
	
	public int getResult() {
		return result & 0xffff;
	}
	
	public boolean getCout() {
		return cout;
	}
	
	public boolean getZero() {
		return zero;
	}
	
	public boolean getOverflow() {
		return overflow;
	}
	
	public void execute() {
		
		int subtrahend; 
		
		switch(Opcode.forInt(opcode)) {
		
		case ADD:
			result = a + b + (cin ? 1 : 0);
			zero = (result & 0xffff) == 0;
			cout = (result & 0x010000) != 0;
			overflow = ((a & 0x8000) == (b & 0x8000)) && ((result & 0x8000) != (a & 0x8000));
			break;
		
		
		case ADD_B:
			result = (a & 0xff) + (b & 0xff) + (cin ? 1 : 0);
			zero = (result & 0xff) == 0;
			cout = (result & 0x0100) != 0;
			overflow = ((a & 0x80) == (b & 0x80)) && ((result & 0x80) != (a & 0x80));
			result = (a & 0xff00) | (result & 0x00ff);
			break;
		
			// SUB uses the carry bit as a borrow, so it should be cleared manually
			// before the first operation
			// sub 0,0 [---] -> 0 [---]
			// -256 - 255 = -511 (511 = 01ff, -511 = fe01)
			// ff80 - 007f = fe01 
			// ff80 + ff80 + 1 (= !cin)
			//=ff 101 => generated a borrow
			// high bytes
			// ff+ff = 1fe => generated a borrow
		case SUB:
			subtrahend = ~b & 0xffff;
			result = (a & 0xffff) + subtrahend + (cin ? 0 : 1);
			zero = (result & 0xffff) == 0;
			cout = (result & 0x010000) == 0;
			overflow = ((a & 0x8000) == (subtrahend & 0x8000)) && ((result & 0x8000) != (a & 0x8000));
			break;
			
		case SUB_B:
			subtrahend = ~b & 0xff;
			result = (a & 0xff) + subtrahend + (cin ? 0 : 1);
			zero = (result & 0xff) == 0;
			cout = (result & 0x0100) == 0;
			overflow = ((a & 0x80) == (subtrahend & 0x80)) && ((result & 0x80) != (a & 0x80));
			result = (a & 0xff00) | (result & 0x00ff);
			break;
			
		case MOV:
			result = b;
			zero = zin;
			overflow = vin;
			cout = cin;
			break;
			
		case MOV_B:
			result = (a & 0xff00) | (b & 0x00ff);
			zero = zin;
			overflow = vin;
			cout = cin;
			break;
			
		case AND:
			result = a & b;
			zero = result == 0;
			cout = false;
			overflow = vin;
			break;
			
		case AND_B:
			result = (a & 0xff00) | (a & b & 0x00ff);
			zero = (result & 0x00ff) == 0;
			cout = false;
			overflow = vin;
			break;
			
		case OR:
			result = a | b;
			zero = result == 0;
			cout = false;
			overflow = vin;
			break;
			
		case OR_B:
			result = (a & 0xff00) | ((a | b) & 0x00ff);
			zero = (result & 0x00ff) == 0;
			cout = false;
			overflow = vin;
			break;
			
		case XOR:
			result = (a & ~b) | (b & ~a);
			zero = result == 0;
			cout = false;
			overflow = vin;
			break;
			
		case XOR_B:
			result = (a & 0xff00) | (((a & ~b) | (b & ~a)) & 0x00ff);
			zero = (result & 0x00ff) == 0;
			cout = false;
			overflow = vin;
			break;
			
		case SLC:
			result = (a << 1) | (cin ? 1 : 0);
			cout = (result & 0x10000) != 0;
			zero = result == 0;
			overflow = vin;
			break;
			
		case SLC_B:
			result = (a << 1) | (cin ? 1 : 0);
			cout = (result & 0x100) != 0;
			result = (result & 0x00ff) | (a & 0xff00);
			zero = (result & 0x00ff) == 0;
			overflow = vin;
			break;
			
		case SRC:
			result = (a >> 1) | (a & 0x8000);
			cout = (a & 0x0001) == 1;
			zero = result == 0;
			overflow = vin;
			break;
			
		case SRC_B:
			result = (a & 0xff80) | ((a & 0x00ff) >> 1);
			cout = (a & 0x0001) == 1;
			zero = (result & 0x00ff) == 0;
			overflow = vin;
		}
			
	}

}
