package com.calix.tools.util;

import com.calix.tools.param.FTPConfig;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.*;

public class FTPUtil {

    private final FTPConfig config = initConfig();

    private static FTPConfig initConfig() {
        FTPConfig config = new FTPConfig();

        return config;
    }

    public static void main(String[] args) {
        FTPUtil util = new FTPUtil();
        util.upload("/home/calix/Documents/tmp", "/tmp");
    }

    public void upload(String filePath, String remotePath) {
        final FTPClient ftp = getClient();
        InputStream is = null;
        try {
            ftp.connect("172.25.201.41");
            ftp.login("logview", "viewlog");

            File file = new File(filePath);
            is = new FileInputStream(file);
            ftp.changeWorkingDirectory(remotePath);
            ftp.storeFile(remotePath, is);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void download(String filePath, String remotePath) {
        final FTPClient ftp = getClient();
        OutputStream os = null;
        try {
            ftp.connect("172.25.201.41");
            ftp.login("logview", "viewlog");
            os = new FileOutputStream(new File(filePath));
            ftp.retrieveFile(remotePath, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private FTPClient getClient() {
        FTPClient ftp;
        String protocol = config.getProtocol();
        if (protocol == null) {
            if (config.getProxyHost() != null) {
                System.out.println("Using HTTP proxy server: " + config.getProxyHost());
                ftp = new FTPHTTPClient(config.getProxyHost(), config.getProxyPort(), config.getProxyUser(), config.getProxyPassword());
            } else {
                ftp = new FTPClient();
            }
        } else {
            FTPSClient ftps;
            if (protocol.equals("true")) {
                ftps = new FTPSClient(true);
            } else if (protocol.equals("false")) {
                ftps = new FTPSClient(false);
            } else {
                String prot[] = protocol.split(",");
                if (prot.length == 1) { // Just protocol
                    ftps = new FTPSClient(protocol);
                } else { // protocol,true|false
                    ftps = new FTPSClient(prot[0], Boolean.parseBoolean(prot[1]));
                }
            }
            ftp = ftps;
            ftp.setControlEncoding("UTF-8");
            ftp.setBufferSize(1024);
            try {
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ftp;
    }

}
