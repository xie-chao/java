package com.calix.tools.param;

import com.calix.tools.widget.LogPanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Created by calix on 16-10-31
 */
public class GlobalConfig {

    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;

    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;

    public static final String KEY__SAVE_PASSWORD = "savePassword";

    private static GlobalConfig globalConfig;

    private final Properties properties;

    private static final String DB_KEY_PREFIX = "DB_";

    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
        SCREEN_WIDTH = screenSize.width;
        SCREEN_HEIGHT = screenSize.height;
    }

    private GlobalConfig() throws IOException {
        properties = new Properties();
        File file = getConfigFile();
        if (file != null) {
            properties.load(new FileReader(file));
        }
    }

    public static GlobalConfig getGlobalConfig() {
        if (globalConfig == null) {
            try {
                globalConfig = new GlobalConfig();
            } catch (IOException e) {
                LogPanel.writeLog(e.getLocalizedMessage());
            }
        }
        return globalConfig;
    }

    public void updateConfig(String key, String value) {
        properties.setProperty(key, value);
    }

    public LoginInfo[] readLoginConfig() {
        Set<String> keys = properties.stringPropertyNames();
        java.util.List<LoginInfo> loginInfos = new ArrayList<>();
        for (String key : keys) {
            if (!key.startsWith(DB_KEY_PREFIX)) {
                continue;
            }
            LoginInfo info = new LoginInfo();
            String[] values = properties.get(key).toString().split(",");
            info.setDbName(key.substring(DB_KEY_PREFIX.length()));
            info.setHost(values[0]);
            info.setPort(Integer.parseInt(values[1]));
            info.setSNameconnect(Boolean.valueOf(values[2]));
            info.setConnect(values[3]);
            info.setUserName(values[4]);
            info.setPassword(values[5]);
            loginInfos.add(info);
        }
        return loginInfos.toArray(new LoginInfo[loginInfos.size()]);
    }

    public void saveLoginInfo(LoginInfo info) {
        try {
            File config = getConfigFile();
            if (config == null) {
                return;
            }
            boolean savePassword = Boolean.TRUE.toString().equals(properties.getProperty(KEY__SAVE_PASSWORD));
            String dbValue = new StringBuilder().append(info.getHost()).append(",").append(info.getPort()).append(",").append(info.getSNameconnect()).append(",").append(info.getConnect()).append(",").append(info.getUserName()).append(",").append(savePassword ? info.getPassword() : "").toString();
            properties.setProperty(DB_KEY_PREFIX + info.getDbName(), dbValue);
            FileWriter writer = new FileWriter(config);
            properties.store(writer, null);
        } catch (Exception e) {
            LogPanel.writeLog(e.getLocalizedMessage());
        }
    }

    public static ImageIcon getImageIcon(String imageUrl) {
        return new ImageIcon(GlobalConfig.class.getResource("/icons/" + imageUrl));
    }

    private File getConfigFile() throws IOException {
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(path);
        String configPath = file.getParent() + "/loginConfig.properties";
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            LogPanel.writeLog("配置文件丢失");
            if (!configFile.createNewFile()) {
                LogPanel.writeLog("创建配置文件失败。。。");
                return null;
            }
        }
        return configFile;
    }
}
