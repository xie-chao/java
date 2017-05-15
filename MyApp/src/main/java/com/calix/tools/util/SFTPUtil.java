package com.calix.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPUtil {

    public static void main(String[] args) {
        SFTPUtil util = new SFTPUtil();
//		util.upload("/home/calix/Documents/tmp", "/tmp");
        util.download("/home/calix/Documents/sftpUploadTest", "/tmp/", "sftpUploadTest.txt");
        System.exit(0);
    }

    public void upload(String filePath, String remotePath) {
        ChannelSftp sftp = getChannel();
        InputStream is = null;
        try {
            is = new FileInputStream(new File(filePath));
            sftp.cd(remotePath);
            System.out.println("starting upload file...");
            sftp.put(is, "sftpUploadTest.txt");
            System.out.println("upload file finished");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                sftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void download(String filePath, String remotePath, String remoteFileName) {
        ChannelSftp sftp = getChannel();
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(filePath));
            sftp.cd(remotePath);
            System.out.println("starting download file...");
            sftp.get(remoteFileName, os);
            System.out.println("download file finished");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                sftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ChannelSftp getChannel() {
        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession("logview", "172.25.201.41");
            session.setPassword("viewlog");
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp connected...");
            return (ChannelSftp) channel;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return null;
    }

}
