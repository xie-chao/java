package com.calix.tools.param;

/**
 * Created by calix on 16-11-7
 */
public class LoginInfo {

    private String dbName;
    private String host;
    private int port;
    private boolean SNameconnect;
    private String connect;
    private String userName;
    private String password;
    private boolean newConnection;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean getSNameconnect() {
        return SNameconnect;
    }

    public void setSNameconnect(boolean SNameconnect) {
        this.SNameconnect = SNameconnect;
    }

    public String getConnect() {
        return connect;
    }

    public void setConnect(String connect) {
        this.connect = connect;
    }

    public String getDBUrl() {
        return "jdbc:oracle:thin:@/" + (SNameconnect ? '/' + host : host) + ":" + port + '/' + connect;
    }

    public boolean isNewConnection() {
        return newConnection;
    }

    public void setNewConnection(boolean newConnection) {
        this.newConnection = newConnection;
    }

    @Override
    public String toString() {
        return dbName;
    }
}
