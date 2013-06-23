package uk.co.ukmaker.cpu;

public interface Device {
	
	int getSize();
	void setStart(int start);
	int getStart();
	int getEnd();
	
	int read(int addr);
	void write(int addr, int data);

}
