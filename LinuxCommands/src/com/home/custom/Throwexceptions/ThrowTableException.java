package com.home.custom.Throwexceptions;

import com.home.custom.exceptions.TablesNotFoundException;

public class ThrowTableException {
	private int nTablesFound;
	
	public ThrowTableException() {
		this.nTablesFound=nTablesFound;
	}
	public void checkTables(int nTablesFound) throws TablesNotFoundException{
		if(nTablesFound>0){
			this.nTablesFound=nTablesFound;
		}
		
		else{
			throw new TablesNotFoundException();
		}
	}
}
