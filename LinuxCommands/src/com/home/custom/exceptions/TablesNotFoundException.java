package com.home.custom.exceptions;

public class TablesNotFoundException extends Exception {
	private int nTablesFound;

	public int getnTablesFound() {
		return nTablesFound;
	}

	public void setnTablesFound(int nTablesFound) {
		this.nTablesFound = nTablesFound;
	}
	
}
