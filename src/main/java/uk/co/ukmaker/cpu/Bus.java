package uk.co.ukmaker.cpu;

import java.util.ArrayList;
import java.util.List;

public class Bus {
	
	public List<Device> devices = new ArrayList<Device>();
	
	
	// Devices must be registered in order of priority
	// E.g a UART will make a hole in memory if it is registered first
	public void registerDevice(Device d, int start) {
		d.setStart(start);
		devices.add(d);
	}
	
	public int read(int addr) throws BusException {
		// convert to word address
		addr = addr >> 1;
		for(Device d : devices) {
			if(d.getStart() <= addr && (d.getEnd() >= addr)) {
				return d.read(addr - d.getStart());
			}
		}
		
		throw new BusException("No device at address "+addr);
	}

	public void write(int addr, int data) throws BusException {
		addr = addr >> 1;
		for(Device d : devices) {
			if(d.getStart() <= addr && (d.getEnd() > addr)) {
				d.write(addr - d.getStart(), data);
				return;
			}
		}
		
		throw new BusException("No device at address "+addr);
	}
}
