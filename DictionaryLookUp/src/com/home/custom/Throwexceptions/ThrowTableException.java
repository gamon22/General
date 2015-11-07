package com.home.custom.Throwexceptions;

import com.home.custom.exceptions.TablesNotFoundException;

public class ThrowTableException {
	private int nTablesFound;
	
	public ThrowTableException() {
		this.nTablesFound=nTablesFound;
	}
	public void checkTables(int externalnTablesf, int internalnTablesf) throws TablesNotFoundException{
		if(externalnTablesf>internalnTablesf || externalnTablesf == internalnTablesf){
			this.nTablesFound=nTablesFound;
		}
		
		else{
			throw new TablesNotFoundException();
		}
	}
}
