package com.calix.tools.param;

import java.util.Vector;

/**
 * Created by calix on 16-11-4.
 *
 * 查询结果集
 */
public class QueryResult {

    private int startIndex = 0;
    private int pageSize = 20;
    private int totalSize = 0;

    private String sql;

    private int modify;

    private Vector<String> columns = new Vector<>(0);

    private Vector<Vector<String>> datas = new Vector<>(0);

    private String errorMsg;

    public Vector<String> getColumns() {
        return columns;
    }

    public void setColumns(Vector<String> columns) {
        this.columns = columns;
    }

    public Vector<Vector<String>> getDatas() {
        return datas;
    }

    public void setDatas(Vector<Vector<String>> datas) {
        this.datas = datas;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getModify() {
        return modify;
    }

    public void setModify(int modify) {
        this.modify = modify;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}
