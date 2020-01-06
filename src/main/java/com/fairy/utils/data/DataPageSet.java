package com.fairy.utils.data;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataPageSet extends DataSet {

	private Long totalCount;

	private int page;

	public DataPageSet(List<Map<String, Object>> resultSet) {
		super(resultSet);
		totalCount = 0L;

	}

}
