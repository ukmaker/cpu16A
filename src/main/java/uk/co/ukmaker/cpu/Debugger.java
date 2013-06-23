package uk.co.ukmaker.cpu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.ukmaker.cpu.assembler.Assembler;

public class Debugger {
	
	private Set<Integer> breakpoints = new HashSet<Integer>();
	private Assembler assembler;
	
	public Debugger() {
		
	}
	
	public Debugger(Assembler a) {
		assembler = a;
	}
		
	public void setBreakpoint(int addr) {
		breakpoints.add(addr);
	}
	
	public void setBreakpoint() {
		breakpoints.add(assembler.getAddress()<<1);
	}
	
	public boolean atBreakpoint(int addr) {
		return breakpoints.contains(addr);
	}
	
	public void debug(Processor p, int addr) {
		if(atBreakpoint(addr)) {
			System.out.println("Stopped for breakpoint");
		}
	}

}
