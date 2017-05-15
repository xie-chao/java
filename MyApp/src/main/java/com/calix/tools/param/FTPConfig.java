package com.calix.tools.param;

public class FTPConfig {

    boolean storeFile = false;
    boolean binaryTransfer = false;
    boolean error = false;
    boolean listFiles = false;
    boolean listNames = false;
    boolean hidden = false;
    boolean localActive = false;
    boolean useEpsvWithIPv4 = false;
    boolean feat = false;
    boolean printHash = false;
    boolean mlst = false;
    boolean mlsd = false;
    boolean mdtm = false;
    boolean saveUnparseable = false;
    boolean lenient = false;
    long keepAliveTimeout = -1;
    int controlKeepAliveReplyTimeout = -1;
    int minParams = 5; // listings require 3 params
    String protocol = null; // SSL protocol
    String doCommand = null;
    String trustmgr = null;
    String proxyHost = null;
    int proxyPort = 80;
    String proxyUser = null;
    String proxyPassword = null;
    String username = null;
    String password = null;
    String encoding = null;
    String serverTimeZoneId = null;
    String displayTimeZoneId = null;
    String serverType = null;
    String defaultDateFormat = null;
    String recentDateFormat = null;

    public boolean isStoreFile() {
        return storeFile;
    }

    public void setStoreFile(boolean storeFile) {
        this.storeFile = storeFile;
    }

    public boolean isBinaryTransfer() {
        return binaryTransfer;
    }

    public void setBinaryTransfer(boolean binaryTransfer) {
        this.binaryTransfer = binaryTransfer;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isListFiles() {
        return listFiles;
    }

    public void setListFiles(boolean listFiles) {
        this.listFiles = listFiles;
    }

    public boolean isListNames() {
        return listNames;
    }

    public void setListNames(boolean listNames) {
        this.listNames = listNames;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isLocalActive() {
        return localActive;
    }

    public void setLocalActive(boolean localActive) {
        this.localActive = localActive;
    }

    public boolean isUseEpsvWithIPv4() {
        return useEpsvWithIPv4;
    }

    public void setUseEpsvWithIPv4(boolean useEpsvWithIPv4) {
        this.useEpsvWithIPv4 = useEpsvWithIPv4;
    }

    public boolean isFeat() {
        return feat;
    }

    public void setFeat(boolean feat) {
        this.feat = feat;
    }

    public boolean isPrintHash() {
        return printHash;
    }

    public void setPrintHash(boolean printHash) {
        this.printHash = printHash;
    }

    public boolean isMlst() {
        return mlst;
    }

    public void setMlst(boolean mlst) {
        this.mlst = mlst;
    }

    public boolean isMlsd() {
        return mlsd;
    }

    public void setMlsd(boolean mlsd) {
        this.mlsd = mlsd;
    }

    public boolean isMdtm() {
        return mdtm;
    }

    public void setMdtm(boolean mdtm) {
        this.mdtm = mdtm;
    }

    public boolean isSaveUnparseable() {
        return saveUnparseable;
    }

    public void setSaveUnparseable(boolean saveUnparseable) {
        this.saveUnparseable = saveUnparseable;
    }

    public boolean isLenient() {
        return lenient;
    }

    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public long getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public void setKeepAliveTimeout(long keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public int getControlKeepAliveReplyTimeout() {
        return controlKeepAliveReplyTimeout;
    }

    public void setControlKeepAliveReplyTimeout(int controlKeepAliveReplyTimeout) {
        this.controlKeepAliveReplyTimeout = controlKeepAliveReplyTimeout;
    }

    public int getMinParams() {
        return minParams;
    }

    public void setMinParams(int minParams) {
        this.minParams = minParams;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDoCommand() {
        return doCommand;
    }

    public void setDoCommand(String doCommand) {
        this.doCommand = doCommand;
    }

    public String getTrustmgr() {
        return trustmgr;
    }

    public void setTrustmgr(String trustmgr) {
        this.trustmgr = trustmgr;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getServerTimeZoneId() {
        return serverTimeZoneId;
    }

    public void setServerTimeZoneId(String serverTimeZoneId) {
        this.serverTimeZoneId = serverTimeZoneId;
    }

    public String getDisplayTimeZoneId() {
        return displayTimeZoneId;
    }

    public void setDisplayTimeZoneId(String displayTimeZoneId) {
        this.displayTimeZoneId = displayTimeZoneId;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getDefaultDateFormat() {
        return defaultDateFormat;
    }

    public void setDefaultDateFormat(String defaultDateFormat) {
        this.defaultDateFormat = defaultDateFormat;
    }

    public String getRecentDateFormat() {
        return recentDateFormat;
    }

    public void setRecentDateFormat(String recentDateFormat) {
        this.recentDateFormat = recentDateFormat;
    }

}
