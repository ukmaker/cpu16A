package uk.co.ukmaker.cpu;

import java.io.DataInputStream;
import java.io.IOException;

public class UART implements Device {
	
	public static final int REG_DATA = 0;
	public static final int REG_STATUS = 1;
	
	public static final int BIT_RCVD = 0b00000001;
	
	
	private int start;
	
	private char rxd;
	private Boolean rcvd = false;
	
	public class Poller implements Runnable {
		
		private UART uart;
		DataInputStream stream;
				
		public Poller(UART uart) {
			this.uart= uart;
			stream = new DataInputStream(System.in);
		}

		@Override
		public void run() {
			while(true) {
				try {
					int c = stream.readByte();
					if(c == -1) {
						throw new RuntimeException();
					}
					
					// tell the UART we have a byte
					// no overrun protection just yet
					while(uart.read(1) == 1) {
						//
						Thread.sleep(10);
					}
					uart.received(c);
					
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		
	}
	
	public UART() {
		Poller p = new Poller(this);
		(new Thread(p)).start();
	}
	
	public void received(int c) {
		synchronized(rcvd) {
			rcvd = true;
			rxd = (char)c;
		}
	}

	@Override
	public int getSize() {
		return 2;
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
		return start + 1;
	}
	
	public boolean hasData() {
		return rcvd;
	}

	@Override
	public int read(int addr) {
		synchronized(rcvd) {
			if(addr == REG_DATA) {
				rcvd = false;
				return rxd;
			}
			if(rcvd) {
				return BIT_RCVD;
			}
			return 0;
		}
	}

	@Override
	public void write(int addr, int data) {
		System.out.print((char)data);
	}

}
