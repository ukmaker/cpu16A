package uk.co.ukmaker.cpu;

import org.junit.Test;

public class UARTTest {
	
	@Test
	public void shouldPoll() throws InterruptedException {
		UART uart = new UART();
		
		while(true) {
			if(uart.read(1) == 1) {
				char c = (char)uart.read(0);
				System.out.print(c);
			}
		//	Thread.sleep(10);
		}
	}

}
