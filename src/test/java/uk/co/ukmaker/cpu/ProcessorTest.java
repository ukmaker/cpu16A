package uk.co.ukmaker.cpu;

import org.junit.Before;
import org.junit.Test;
import static uk.co.ukmaker.cpu.Opcode.*;
import static uk.co.ukmaker.cpu.OpcodeBits.*;
import static uk.co.ukmaker.cpu.RegisterFile.*;

public class ProcessorTest {
	
	private Processor processor;
	private Assembler a;
	private Memory ram;
	private UART uart;
	private Disassembler disassembler;
	private Debugger dbg;
	
	@Before
	public void setup() {
		processor = new Processor();
		ram = new Memory(32);
		uart = new UART();
		int[] data = new int[8];
		data[0]=(int)'a';
		data[1]=(int)'b';
		data[2]=(int)'c';
		data[3]=(int)'d';
		data[4]=(int)'e';
		data[5]=(int)'f';
		data[6]=(int)'g';
		data[7]=(int)'x';
		//uart.setTestData(data);
		
		processor.getBus().registerDevice(ram, 0);
		processor.getBus().registerDevice(uart, ram.getEnd()+1);
		a = new Assembler();
		a.setMemory(ram);
		disassembler = new Disassembler(processor);
		//dbg = new Debugger(a);
		//processor.attachDebugger(dbg);
	}
	
	@Test
	public void shouldBoot() throws BusException {
		// Set up the constant area
		// This is the generic way to to it
		a.op(JL, 1, CC_NONE);
		a.constant(uart.getStart() << 1);
		// Target of JL. PC contains address of constant pool
		a.op(MOV, R_CP, R_LA, CC_NONE);

		
		a.op(LDI, 0, 15, CC_NONE);
		a.op(LDI, 1, 1, CC_NONE);
		a.op(LDC, 2, 0, CC_NONE); // address of UART

		// Start of the loop. Print the current value of reg0
		a.op(STX, 0, 2, CC_NONE);
		// Decrement the count
		a.op(SUB, 0, 1, CC_NONE);
		// Jump if not zero
		a.op(JR, -3, CC_NZ);
		
		processor.reset();
		
		for(int i=0; i < 50; i++) {
			processor.fetch();
			System.out.println(disassembler.disassemble(processor.getInstruction()));
			processor.execute();
		}		
	}

	@Test
	public void shouldEcho() throws BusException {
		// Set up the constant area
		// This is the generic way to to it
		a.op(JL, 3, CC_NONE);
		a.constant((uart.getStart() + UART.REG_DATA) << 1);
		a.constant((uart.getStart() + UART.REG_STATUS) << 1);
		a.constant('x');
		// Target of JL. PC contains address of constant pool
		a.op(MOV, R_CP, R_LA, CC_NONE);
		// test the UART's status bit
		// when it is set, read the data and print it
		// loop
		a.op(LDI, R_0, UART.BIT_RCVD, CC_NONE);
		a.op(LDC, R_1, 0, CC_NONE); // address of UART data
		a.op(LDC, R_2, 1, CC_NONE); // address of UART status
		a.op(LDC, 4, 2, CC_NONE); // character to halt
		// start the loop
		//dbg.setBreakpoint();
		int LOOP = a.op(LDX, R_3, R_2, CC_NONE); // read status
		a.op(AND, R_3, R_0, CC_NONE); // check bit
		a.op(JR, a.rel(LOOP), CC_Z); // loop if no data
		a.op(LDX,  R_3, R_1, CC_NONE); // read data
		a.op(STX, R_3, R_1, CC_NONE); // echo data
		a.op(SUB, R_3, 4, CC_NONE);
		a.op(JR, a.rel(LOOP), CC_NZ);
		a.op(HALT, CC_NONE);
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
