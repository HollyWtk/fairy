package com.fairy.utils.type;


public enum ViewFldType {
	TYPE_COMMON(1),  
	TYPE_QUERYONE(2), 
	TYPE_QUERYLIST(3),
	TYPE_READ(4),
    TYPE_ENUM(5, ViewFldFiterType.FILTER_ENUM),
	TYPE_REMARK(6),
	TYPE_DATE(7, ViewFldFiterType.FILTER_DATE),
	TYPE_TIME(8, ViewFldFiterType.FILTER_TIME),
	TYPE_DATETIME(9, ViewFldFiterType.FILTER_DATETIME),
	TYPE_VIRTUAL(10),
	TYPE_SINGLEIMAGE(11),
	TYPE_MUTILIMAGE(12),
	TYPE_TREE(13),
	TYPE_PASSWORD(14),
    TYPE_LINKSELECT(15, ViewFldFiterType.FILTER_LINKSELECT),
    TYPE_DATERANGETIME(16, ViewFldFiterType.FILTER_DATERANGETIME),
    TYPE_SORT(17),
    TYPE_TEXTAREA(18),
    TYPE_DATEMONTH(19,ViewFldFiterType.FILTER_DATEMONTH);

	private int _type = 1;

    private ViewFldFiterType defaultFldFilterType = ViewFldFiterType.FILTER_COMMON;

	private ViewFldType(int type) {
		_type = type;
	}

    private ViewFldType(int type, ViewFldFiterType defaultFldFilterType) {
        _type = type;
        this.defaultFldFilterType = defaultFldFilterType;
    }

	public int getType() {
		return _type;
	}

    public ViewFldFiterType getDefaultFldFilterType() {
        return this.defaultFldFilterType;
    }

}
