package com.calix.tools.widget;

import javax.swing.*;
import java.awt.*;

/**
 * Created by calix on 16-11-7
 *
 */
public class LogPanel extends JTextArea {

    private static LogPanel logPanel = new LogPanel();

    private LogPanel() {
        this.setAutoscrolls(true);
        this.setEditable(false);
        this.setLineWrap(true);
        this.setPreferredSize(new Dimension(0, 100));
    }

    static JScrollPane getLogPanel() {
        return new JScrollPane(logPanel);
    }

    public static void writeLog(Object log) {
        logPanel.append(log == null ? "null" : log.toString());
        logPanel.append("\r\n");
        logPanel.setCaretPosition(logPanel.getDocument().getLength());
    }
}
