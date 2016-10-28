package com.calix.code.widget;

import javax.swing.*;
import java.awt.*;

public class RemoteFileTool extends JFrame {

    private JScrollPane navigationPanel;
    private JScrollPane viewPane;
    private JTree fileTree;

//    private JTree navigate;

    private RemoteFileTool() {
        initComponents();
        initConfig();
    }

    /**
     * 初始化参数
     */
    private void initConfig() {
        this.setTitle("aaaaaaaaaaaa");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int width = 700, height = 500;
        this.setBounds((screenWidth - width) >> 1, (screenHeight - height) >> 1, 700, 500);
        this.setVisible(true);
    }

    /**
     * 初始化组件
     */
    private void initComponents() {
        fileTree = new Navigation().getNavigation();

        navigationPanel = new JScrollPane(fileTree);
        viewPane = new JScrollPane();


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPanel, viewPane);
        splitPane.setDividerLocation(250);

        this.add(splitPane);

    }

    public static void main(String args[]) {
        new RemoteFileTool();
//        System.out.print(6 >> 1);
    }

}
