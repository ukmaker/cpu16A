package uk.co.ukmaker.cpu;

public class Memory implements Device {
	
	private int start;
	private int[] ram;
	
	public Memory(int size) {
		ram = new int[size];
	}

	@Override
	public int getSize() {
		return ram.length;
	}

	@Override
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public int getStart() {
		return start;
	}
	
	@Override
	public int getEnd() {
		return start + ram.length-1;
	}

	@Override
	public int read(int addr) {
		return ram[ addr % ram.length];
	}

	@Override
	public void write(int addr, int data) {
		ram[addr % ram.length]= data; 
	}

}
