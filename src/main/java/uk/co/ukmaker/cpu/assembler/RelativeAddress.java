package uk.co.ukmaker.cpu.assembler;

public class RelativeAddress extends Argument {
	
	protected Address targetAddress;
	
	public RelativeAddress(Integer baseAddress, Address targetAddress) {
		super(baseAddress);
		this.targetAddress = targetAddress;
	}
	
	public Integer getValue() {
		Integer base = super.getValue();
		return targetAddress.getValue() - base - 1;
	}

}
