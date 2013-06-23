package uk.co.ukmaker.cpu;

public class TestUART implements Device {
	
	private int[] data;
	private int ptr = 0;
	private int start;
	
	public void setTestData(int[]  data) {
		this.data = data;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void setStart(int start) {
		// TODO Auto-generated method stub
		this.start = start;
	}

	@Override
	public int getStart() {
		// TODO Auto-generated method stub
		return start;
	}

	@Override
	public int getEnd() {
		// TODO Auto-generated method stub
		return start+1;
	}

	@Override
	public int read(int addr) {
		// TODO Auto-generated method stub
		int d;
		if(addr == 0) {
			d=data[ptr];
			ptr++;
			return d;
		}
		return (ptr < data.length) ? 1 : 0;
	}

	@Override
	public void write(int addr, int data) {
		// TODO Auto-generated method stub
		System.out.print((char)data);
		
	}

}
