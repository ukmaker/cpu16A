package uk.co.ukmaker.cpu.assembler;

public class Argument {
	
	protected Integer value;
	
	public Argument() {
		
	}
	
	public Argument(int value) {
		this.value = value;
	}
	
	public Integer getValue() {
		if(value == null) {
			throw new RuntimeException("Unresolved argument");
		}
		return value;
	}
}
