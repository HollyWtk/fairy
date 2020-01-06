package com.fairy.utils.type;

public enum ViewFldFiterType {
	FILTER_COMMON(1), 
	FILTER_DATE(2), 
	FILTER_TIME(3),
	FILTER_DATETIME(4),
	FILTER_SELECT(5), 
	FILTER_ENUM(6),
	FILTER_RANGE(7),
	FILTER_LINKSELECT(8),
	FILTER_DATERANGETIME(9),
	FILTER_DATEMONTH(10);

	private int _type = 1;

	private ViewFldFiterType(int type) {
		_type = type;
	}
	    
	public int getType() {
		return _type;
	}

}