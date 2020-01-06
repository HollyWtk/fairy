package com.fairy.utils.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSet {

    private List<Map<String, Object>> dataSource;

    public DataSet() {
        dataSource = new ArrayList<>();
    }

    public void copyDataSet(DataSet ds) {
        dataSource.addAll(ds.getDataSource());
    }

    public DataSet(List<Map<String, Object>> resultSet) {
        this.dataSource = resultSet;

    }

    public List<Map<String, Object>> getDataSource() {
        return dataSource;
    }

    public List<Map<String, Object>> setDataSource(List<Map<String, Object>> dataSource) {
        return this.dataSource = dataSource;
    }

    public int getRowCount() {
        return dataSource.size();
    }

    public Object[] asArray(String columnName) {

        Object[] result = new Object[dataSource.size()];

        int index = 0;
        for (Map<String, Object> map : dataSource) {
            result[index] = map.get(columnName);
            index++;
        }

        return result;

    }

    public DataSet union(DataSet other, String queryFld, String unionFld, String relFld) {

        this.dataSource.forEach(s -> s.put(unionFld, null));
        System.out.println(dataSource.toString());
        this.dataSource.forEach(s -> {
            String value = s.get(relFld).toString();
            other.dataSource.stream().filter(o -> o.get(relFld).toString().equals(value))
                    .forEach(r -> s.put(unionFld, r.get(queryFld)));

        });

        return this;

    }

    public Object getValueByColumnAtRow(String columnName, int row) {

        if (row < dataSource.size()) {
            Map<String, Object> rowData = dataSource.get(row);
            if (rowData != null) {
                return rowData.get(columnName);
            }
            return null;
        }
        return null;
    }

}
