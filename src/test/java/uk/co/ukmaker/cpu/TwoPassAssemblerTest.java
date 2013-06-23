package uk.co.ukmaker.cpu;

import org.junit.Before;
import org.junit.Test;

import uk.co.ukmaker.cpu.assembler.Address;
import uk.co.ukmaker.cpu.assembler.Assembler;
import uk.co.ukmaker.cpu.assembler.Constant;
import uk.co.ukmaker.cpu.assembler.Constants;
import uk.co.ukmaker.cpu.assembler.TwoPassAssembler;
import static uk.co.ukmaker.cpu.Opcode.*;
import static uk.co.ukmaker.cpu.OpcodeBits.*;
import static uk.co.ukmaker.cpu.RegisterFile.*;

public class TwoPassAssemblerTest {
	
	private Processor processor;
	private TwoPassAssembler a;
	private Memory ram;
	private UART uart;
	private Disassembler disassembler;
	private Debugger dbg;
	
	@Before
	public void setup() {
		processor = new Processor();
		ram = new Memory(32);
		uart = new UART();		
		processor.getBus().registerDevice(ram, 0);
		processor.getBus().registerDevice(uart, ram.getEnd()+1);
		a = new TwoPassAssembler();
		a.setMemory(ram);
		disassembler = new Disassembler(processor);
	}
	
	@Test
	public void shouldEcho() throws BusException {
		
		Constants CONSTANTS = new Constants();
		
		Constant UART_DATA = new Constant((uart.getStart() + UART.REG_DATA) << 1);
		Constant UART_STATUS = new Constant((uart.getStart() + UART.REG_STATUS) << 1);
		Constant LETTER_X = new Constant('x');
		
		Address START = new Address();
		Address LOOP = new Address();
		
		// Set up the constant area
		// This is the generic way to to it
		a.op(JL, START, CC_NONE);
		
		CONSTANTS.$(a,
				UART_DATA,
				UART_STATUS,
				LETTER_X
		);
		
		// Target of JL. PC contains address of constant pool
		START.$(
				a.op(MOV, R_CP, R_LA, CC_NONE));
				a.op(LDI, R_0, UART.BIT_RCVD, CC_NONE);
				a.op(LDC, R_1, UART_DATA, CC_NONE); // address of UART data
				a.op(LDC, R_2, UART_STATUS, CC_NONE); // address of UART status
				a.op(LDC, 4, LETTER_X, CC_NONE); // character to halt

		// test the UART's status bit
		// when it is set, read the data and print it
		// loop
		LOOP.$(
				a.op(LDX, R_3, R_2, CC_NONE)); // read status
				a.op(AND, R_3, R_0, CC_NONE); // check bit
				a.op(JR, LOOP, CC_Z); // loop if no data
				a.op(LDX,  R_3, R_1, CC_NONE); // read data
				a.op(STX, R_3, R_1, CC_NONE); // echo data
				a.op(SUB, R_3, 4, CC_NONE);
				a.op(JR, LOOP, CC_NZ);
				a.op(HALT, CC_NONE);
		
		a.assemble();
		
		processor.reset();
		int i = 0;
		while(!processor.isHalted() && i < 100) {
			processor.fetch();
			//System.out.println(disassembler.disassemble(processor.getInstruction()));
			processor.execute();
			//i++;
		}
		
		System.out.println("Ticks="+processor.getTicks());
	}
}
