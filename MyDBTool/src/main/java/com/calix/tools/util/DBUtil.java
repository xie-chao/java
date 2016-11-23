package com.calix.tools.util;

import com.calix.tools.param.QueryResult;
import com.calix.tools.widget.LogPanel;
import com.calix.tools.widget.MyDBTool;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Created by calix on 16-11-4
 * 数据库操作类
 */
public class DBUtil {

    private static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
    private static Connection connection;

    private static DBUtil dbUtil;

    private int startIndex = 0;
    private int pageSize = 20;

    private static int connection_timeout = 1000 * 60 * 5;

    private static Timer timeoutTimer = new Timer();

    private DBUtil() {
        try {
            Class.forName(DRIVER_NAME).newInstance();//加载驱动程序
        } catch (Exception e) {
            LogPanel.writeLog(e.getLocalizedMessage());
        }
    }

    public static DBUtil getDBUtil() {
        if (dbUtil == null) {
            dbUtil = new DBUtil();
        }
        return dbUtil;
    }

    public QueryResult query(String sql) throws Exception {
        return query(sql, startIndex, pageSize);
    }

    public QueryResult query(String sql, int startIndex, int pageSize) throws Exception {
        QueryResult result = new QueryResult();
        result.setStartIndex(startIndex);
        result.setPageSize(pageSize);
        if (connection == null || connection.isClosed()) {
            result = new QueryResult();
            result.setErrorMsg("数据库未连接！");
            return result;
        }
        Statement state = connection.createStatement();
        ResultSet countResult = state.executeQuery("select count(1) " + sql.substring(sql.indexOf("from")));
        int totalSize = 0;
        if (countResult.next()) {
            totalSize = countResult.getBigDecimal(1).intValue();
        }
        result.setTotalSize(totalSize);
        if (totalSize == 0) {
            return result;
        }
        PreparedStatement pstate = connection.prepareCall("select rs.* from (select rownum no, data.* from (" + sql + ") data) rs where rs.no > ? and rs.no <= ?");
        pstate.setInt(1, startIndex);
        pstate.setInt(2, startIndex + pageSize);
        ResultSet resultSet = pstate.executeQuery();

        return packageValues(resultSet, result);
    }

    public QueryResult execute(String sql) throws SQLException {
        QueryResult result = new QueryResult();
        if (connection == null || connection.isClosed()) {
            result.setErrorMsg("数据库未连接！");
            return result;
        }
        Statement state = connection.createStatement();
        result.setModify(state.executeUpdate(sql));
        return result;
    }

    public void initConnection(String url, String userName, String password) throws Exception {
        if (connection != null && !connection.isClosed()) {
            releaseAfter(0);
        }
        LogPanel.writeLog("获取数据库连接 : " + url);
        connection = DriverManager.getConnection(url, userName, password);
        releaseAfter(connection_timeout);
    }

    private QueryResult packageValues(ResultSet resultSet, QueryResult result) throws SQLException {
        Vector<Vector<String>> values = new Vector<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        Vector<String> columns = new Vector<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            columns.add(metaData.getColumnName(i + 1));
        }
        while (resultSet.next()) {
            Vector<String> data = new Vector<>();
            for (int i = 0; i < columnCount; i++) {
                data.add(resultSet.getString(i + 1));
            }
            values.add(data);
        }
        result.setColumns(columns);
        result.setDatas(values);
        return result;
    }

    public static void releaseAfter(int timeout) {
        if (timeout <= 0) {
            timeoutTimer.cancel();
            closeConnection();
        } else {
            timeoutTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    closeConnection();
                    MyDBTool.getLoginCombo().setSelectedIndex(-1);
                }
            }, timeout);
        }
    }

    private static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.rollback();
                    connection.close();
                }
            } catch (SQLException e) {
                LogPanel.writeLog(e.getStackTrace());
            }
            LogPanel.writeLog("数据库连接已关闭！");
        }
    }
}
