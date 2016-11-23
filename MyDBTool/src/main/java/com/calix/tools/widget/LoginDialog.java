package com.calix.tools.widget;

import com.calix.tools.param.GlobalConfig;
import com.calix.tools.param.LoginInfo;
import com.calix.tools.util.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;

/**
 * Created by calix on 16-11-1
 *
 */
class LoginDialog extends JDialog {

    private static LoginDialog loginDialog;

    private static JTextField dbInput;
    private static JTextField urlInput;
    private static JTextField portInput;
    private static JTextField connectInput;
    private static JTextField userNameInput;
    private static JRadioButton serverName;
    private static JPasswordField passwordInput;

    private static JFrame globalFrame;

    private static final int labelWidth = 70;
    private static final int inputWidth = 220;
    private static final int paddingLeft = 10;

    static void showLoginDialog(JFrame jFrame, LoginInfo loginInfo) {
        if (loginDialog == null) {
            loginDialog = new LoginDialog(globalFrame, "登录");
        }
        urlInput.setText(loginInfo.getHost());
        userNameInput.setText(loginInfo.getUserName());
        passwordInput.setText("");
        loginDialog.setVisible(true);
        globalFrame = jFrame;
    }

    private LoginDialog(JFrame jFrame, String title) {
        super(jFrame, true);
        this.setTitle(title);
        this.initUI();
        this.setResizable(false);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    private void initUI() {
        JPanel outer = new JPanel();
        Box vbox = Box.createVerticalBox();
        Box hbox = Box.createHorizontalBox();
        Box hbox0 = Box.createHorizontalBox();
        Box hbox1 = Box.createHorizontalBox();
        Box hbox2 = Box.createHorizontalBox();
        Box hbox3 = Box.createHorizontalBox();
        Box hbox4 = Box.createHorizontalBox();

        JLabel dbField = new JLabel("名称");
        dbField.setPreferredSize(new Dimension(labelWidth, 25));
        hbox.add(dbField);
        dbInput = new JTextField();
        dbInput.setPreferredSize(new Dimension(inputWidth, 25));
        hbox.add(Box.createHorizontalStrut(paddingLeft));
        hbox.add(dbInput);

        JLabel hostField = new JLabel("数据库");
        hostField.setPreferredSize(new Dimension(labelWidth, 25));
        hbox0.add(hostField);
        urlInput = new JTextField();
        urlInput.setPreferredSize(new Dimension(inputWidth - 40, 25));
        portInput = new JTextField("1521");
        portInput.setPreferredSize(new Dimension(40, 25));
        hbox0.add(Box.createHorizontalStrut(paddingLeft));
        hbox0.add(urlInput);
        hbox0.add(portInput);

        JLabel connectField = new JLabel("连接方式");
        connectField.setPreferredSize(new Dimension(labelWidth, 25));
        hbox1.add(connectField);
        connectInput = new JTextField();
        connectInput.setPreferredSize(new Dimension(inputWidth - 130, 25));
        JLabel typeLabel = new JLabel("连接方式");
        typeLabel.setPreferredSize(new Dimension(labelWidth, 25));
        ButtonGroup connnectType = new ButtonGroup();
        serverName = new JRadioButton("SNAME", true);
        JRadioButton sid = new JRadioButton("SID");
        connnectType.add(serverName);
        connnectType.add(sid);
        serverName.setPreferredSize(new Dimension(80, 25));
        sid.setPreferredSize(new Dimension(50, 25));
        hbox1.add(Box.createHorizontalStrut(paddingLeft));
        hbox1.add(connectInput);
        hbox1.add(serverName);
        hbox1.add(sid);

        JLabel nameLabel = new JLabel("用户名");
        userNameInput = new JTextField();
        nameLabel.setPreferredSize(new Dimension(labelWidth, 25));
        userNameInput.setPreferredSize(new Dimension(inputWidth, 25));
        hbox2.add(nameLabel);
        hbox2.add(Box.createHorizontalStrut(paddingLeft));
        hbox2.add(userNameInput);

        JLabel passwordLabel = new JLabel("密码");
        passwordInput = new JPasswordField();
        passwordLabel.setPreferredSize(new Dimension(labelWidth, 25));
        passwordInput.setPreferredSize(new Dimension(inputWidth, 25));
        hbox3.add(passwordLabel);
        hbox3.add(Box.createHorizontalStrut(paddingLeft));
        hbox3.add(passwordInput);

        JLabel savePasswordLabel = new JLabel("保存");
        JCheckBox savePasswordInput = new JCheckBox();
        hbox4.add(savePasswordLabel);
        hbox4.add(savePasswordInput);
        JButton login = new JButton("登陆");
        hbox4.add(login);

        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireLogin();
            }
        });

        savePasswordInput.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox checkBox = (JCheckBox)e.getItem();
                GlobalConfig.getGlobalConfig().updateConfig(GlobalConfig.KEY__SAVE_PASSWORD, Boolean.toString(checkBox.isSelected()));
            }
        });

        vbox.add(Box.createVerticalStrut(10));
        vbox.add(hbox);
        vbox.add(Box.createVerticalStrut(10));
        vbox.add(hbox0);
        vbox.add(Box.createVerticalStrut(10));
        vbox.add(hbox1);
        vbox.add(Box.createVerticalStrut(10));
        vbox.add(hbox2);
        vbox.add(Box.createVerticalStrut(10));
        vbox.add(hbox3);
        vbox.add(Box.createVerticalStrut(10));
        vbox.add(hbox4);
        vbox.add(Box.createVerticalStrut(10));

        outer.add(vbox);
        this.setSize(350, 270);
        this.add(outer);
        this.setLocationRelativeTo(null);
    }

    private void fireLogin() {
        String dbName = dbInput.getText();
        String host = urlInput.getText();
        int port;
        try {
            port = Integer.parseInt(portInput.getText());
        } catch (Exception e) {
            LogPanel.writeLog(e.getLocalizedMessage());
            return;
        }
        String connect = connectInput.getText();
        String userName = userNameInput.getText();
        char[] password = passwordInput.getPassword();
        boolean SName = serverName.isSelected();
        if (dbName == null || dbName.length() == 0 || host == null || host.length() == 0 || userName == null || userName.length() == 0 || password == null || password.length == 0 || connect== null || connect.length() == 0) {
            return;
        }
        String passWord = new String(password);
        LoginInfo info = new LoginInfo();
        info.setDbName(dbName);
        info.setHost(host);
        info.setPort(port);
        info.setConnect(connect);
        info.setSNameconnect(SName);
        info.setUserName(userName);
        info.setPassword(passWord);
        try {
            DBUtil.getDBUtil().initConnection(info.getDBUrl(), userName, passWord);
            loginDialog.setVisible(false);
            JComboBox<LoginInfo> loginCombo = MyDBTool.getLoginCombo();
            loginCombo.addItem(info);
            info.setNewConnection(true);
            loginCombo.setSelectedItem(info);
            GlobalConfig.getGlobalConfig().saveLoginInfo(info);
        } catch (Exception e) {
            LogPanel.writeLog(e.getLocalizedMessage());
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }

    /**
     * 覆盖父类的方法，实现按键监听
     */
    @Override
    protected JRootPane createRootPane() {
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireLogin();
            }
        }, enter, JComponent.WHEN_IN_FOCUSED_WINDOW);

        rootPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginDialog.setVisible(false);
            }
        }, esc, JComponent.WHEN_IN_FOCUSED_WINDOW);

        return rootPane;
    }

}
