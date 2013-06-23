package uk.co.ukmaker.cpu.assembler;

public class Address extends Argument {

	public void $(int addr) {
		this.value = addr;
	}
}
