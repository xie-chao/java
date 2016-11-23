package com.calix.tools.widget;

import com.calix.tools.param.QueryResult;
import com.calix.tools.util.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 * Created by calix on 16-11-4
 * SQL输入框
 */
class SQLPanel extends JScrollPane {

    private static DataPanel dataPanel;

    private static SQLPanel sqlContent;

    private static JTextArea editContent;

    private SQLPanel(JTextArea edit) {
        super(edit);
        editContent = edit;
        edit.setMargin(new Insets(3, 3, 3, 3));
        edit.setTabSize(4);
        edit.setMinimumSize(new Dimension(0, 200));
        edit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F8) {
                    doExecute();
                }
            }
        });
    }

    static SQLPanel getSqlPanel(DataPanel panel) {
        dataPanel = panel;
        if (sqlContent == null) {
            sqlContent = new SQLPanel(new JTextArea());
        }
        return sqlContent;
    }

    void doExecute() {
        try {
            Vector<QueryResult> results = execute(null);
            if (results == null) {
                return;
            }
            dataPanel.cleanData();
            for (QueryResult result : results) {
                dataPanel.loadData(result);
            }
        } catch (Exception e1) {
            LogPanel.writeLog(e1.getLocalizedMessage());
        }
    }

    private String[] getSelected() {
        String selected = editContent.getSelectedText();
        if (selected == null || selected.length() == 0) {
            selected = editContent.getText();
        }
        return formatSql(selected);
    }

    private String[] formatSql(String sqlText) {
        if (sqlText == null || sqlText.length() == 0) {
            return new String[0];
        }
        return sqlText.split(";");
    }

    private Vector<QueryResult> execute(String currentSql) throws Exception {
        Vector<QueryResult> results = new Vector<>();
        String[] sqls;
        if (currentSql == null || currentSql.length() == 0) {
            sqls = getSelected();
        } else {
            sqls = new String[]{currentSql};
        }
        if (sqls.length == 0) {
            return null;
        }
        DBUtil dbUtil = DBUtil.getDBUtil();
        for (String sql : sqls) {
            sql = sql.trim();
            QueryResult result;
            if (sql.startsWith("select")) {
                result = dbUtil.query(sql);
            } else {
                result = dbUtil.execute(sql);
            }
            if (result.getErrorMsg() != null) {
                LogPanel.writeLog(result.getErrorMsg());
                return null;
            }
            result.setSql(sql);
            results.add(result);
        }
        return results;
    }
}
