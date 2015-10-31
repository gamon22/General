package com.home.custom.exceptions;

public class TablesNotFoundException extends Exception {
	//private int nTablesFound;
	
	private int internalnTablesf;
	private int externalnTablesf;
	
	

	public int getInternalnTablesf() {
		return internalnTablesf;
	}

	public void setInternalnTablesf(int internalnTablesf) {
		this.internalnTablesf = internalnTablesf;
	}

	public int getExternalnTablesf() {
		return externalnTablesf;
	}

	public void setExternalnTablesf(int externalnTablesf) {
		this.externalnTablesf = externalnTablesf;
	}

	
	
}
