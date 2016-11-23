package com.calix.tools.widget;

import com.calix.tools.param.GlobalConfig;
import com.calix.tools.param.LoginInfo;
import com.calix.tools.util.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by calix on 16-11-4
 * 简单的数据库工具
 */
public class MyDBTool extends JFrame {

    private MyDBTool myDBTool;

    private SQLPanel sqlPanel;

    private MySplitPanel hSplit;

    private static JComboBox<LoginInfo> loginCombo;

    private Map<JSplitPane, Double> resizePanel = new HashMap<>();

    private MyDBTool() {
        myDBTool = this;
        initUI();
        initConfig();
    }

    /**
     * 初始化参数
     */
    private void initConfig() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("简单的Oracle工具");
        this.setLocationRelativeTo(null);
        this.setBounds((GlobalConfig.SCREEN_WIDTH - GlobalConfig.WIDTH) >> 1, (GlobalConfig.SCREEN_HEIGHT - GlobalConfig.HEIGHT) >> 1, GlobalConfig.WIDTH, GlobalConfig.HEIGHT);
        this.pack();
        this.setMinimumSize(new Dimension(GlobalConfig.WIDTH, GlobalConfig.HEIGHT));
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                releaseResources();
                super.windowClosing(e);
            }

        });
        this.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e) {
                resizeUI();
            }
        });
    }

    /**
     * 初始化组件
     */
    private void initUI() {
        DBToolBar toolbar = new DBToolBar();
        this.add(toolbar, BorderLayout.NORTH);
        DataPanel dataPanel = new DataPanel();
        sqlPanel = SQLPanel.getSqlPanel(dataPanel);

        MySplitPanel vSplit1 = new MySplitPanel(JSplitPane.VERTICAL_SPLIT, sqlPanel, dataPanel);
        vSplit1.setDividerLocation(250);
        MySplitPanel vSplit2 = new MySplitPanel(JSplitPane.VERTICAL_SPLIT, vSplit1, LogPanel.getLogPanel());
        hSplit = new MySplitPanel(JSplitPane.HORIZONTAL_SPLIT, vSplit2, null);

        resizePanel.put(vSplit2, 0.8);
        resizePanel.put(hSplit, 0.65);

        this.add(hSplit, BorderLayout.CENTER);
    }

    private void resizeUI() {
        for (Map.Entry<JSplitPane, Double> entry : resizePanel.entrySet()) {
            Double value = entry.getValue();
            if (value == null) {
                continue;
            }
            entry.getKey().setDividerLocation(value);
        }
    }

    private void releaseResources() {
        DBUtil.releaseAfter(0);
    }

    private class DBToolBar extends JToolBar {

        private final JButton login;
        private final JButton execute;
        private final JButton favour;

        DBToolBar() {
            this.setFloatable(false);
            this.setMargin(new Insets(0,2,0,2));
            LoginInfo[] infos = GlobalConfig.getGlobalConfig().readLoginConfig();
            loginCombo = new JComboBox<>(infos);
            loginCombo.setMinimumSize(new Dimension(150, 30));
            loginCombo.setMaximumSize(new Dimension(150, 30));
            loginCombo.setPreferredSize(new Dimension(150, 30));

            login = new JButton(GlobalConfig.getImageIcon("adduser.png"));
            login.setMargin(new Insets(-2,0,-4,0));
            login.setBorderPainted(false);
            login.setToolTipText("新增数据库连接");

            execute = new JButton(GlobalConfig.getImageIcon("search.png"));
            execute.setMargin(new Insets(-2,0,-4,0));
            execute.setBorderPainted(false);
            execute.setToolTipText("执行(F8)");

            JToolBar blank = new JToolBar();
            blank.setFloatable(false);
            blank.setBorderPainted(false);

            favour = new JButton(GlobalConfig.getImageIcon("favor.png"));
            favour.setMargin(new Insets(-2,0,-4,0));
            favour.setToolTipText("笔记");
            favour.setBorderPainted(false);

            loginCombo.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        LoginInfo info = (LoginInfo)e.getItem();
                        if (info.isNewConnection()) {
                            return;
                        }
                        try {
                            if (info.getPassword() == null || info.getPassword().length() == 0) {
                                LoginDialog.showLoginDialog(myDBTool, info);
                            } else {
                                DBUtil.getDBUtil().initConnection(info.getDBUrl(), info.getUserName(), info.getPassword());
                            }
                        } catch (Exception e1) {
                            LogPanel.writeLog(e1.getLocalizedMessage());
                        }
                    }
                }
            });

            login.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    LoginDialog.showLoginDialog(myDBTool, new LoginInfo());
                }
            });

            execute.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    sqlPanel.doExecute();
                }
            });

            favour.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    boolean show;
                    TipPanel tip = (TipPanel)hSplit.getRightComponent();
                    if (tip == null) {
                        tip = TipPanel.getTipPanel();
                        hSplit.setRightComponent(tip);
                        show = true;
                    } else {
                        show = !tip.isVisible();
                    }
                    tip.setVisible(show);
                    resizeUI();
                }
            });

            this.add(new JLabel(GlobalConfig.getImageIcon("listview.png")));
            this.add(loginCombo);
            loginCombo.setSelectedIndex(-1);
            this.addSeparator();
            this.add(login);
            this.addSeparator(new Dimension(100,30));
            this.add(blank);
            this.addSeparator();
            this.add(execute);
            blank.setLayout(new FlowLayout(FlowLayout.RIGHT));
            this.add(favour);
        }

    }

    public static JComboBox<LoginInfo> getLoginCombo() {
        return loginCombo;
    }

    public static void setLoginCombo(JComboBox<LoginInfo> loginCombo) {
        MyDBTool.loginCombo = loginCombo;
    }

    public static void main(String args[]) {
        new MyDBTool();
    }
}
