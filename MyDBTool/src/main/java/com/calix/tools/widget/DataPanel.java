package com.calix.tools.widget;

import com.calix.tools.param.QueryResult;
import com.calix.tools.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.Vector;

/**
 * Created by calix on 16-10-28
 */
class DataPanel extends JPanel {

    private static DataTable dataView;

    private final JTabbedPane tabbedPane;

    DataPanel() {
        tabbedPane = new JTabbedPane();
        this.setLayout(new BorderLayout());
        this.add(tabbedPane, BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(0, 200));
    }

    void cleanData() {
        tabbedPane.removeAll();
    }

    void loadData(QueryResult queryResult) {
        dataView = new DataTable(queryResult);
        DataTab dataTab = new DataTab(dataView);
        dataTab.resultToolbar.queryInfo.setText(getPageInfo(queryResult.getStartIndex(), queryResult.getPageSize(), queryResult.getTotalSize()));
        String title = queryResult.getSql();
        if (title.length() > 25) {
            title = title.substring(0, 24) + "...";
        }
        tabbedPane.addTab(title, dataTab);
    }

    private class DataTab extends JPanel {
        private DataTable dataTable;
        private final ResultToolbar resultToolbar;

        DataTab(DataTable table) {
            super();
            this.setLayout(new BorderLayout());
            resultToolbar = new ResultToolbar();
            this.add(resultToolbar, BorderLayout.NORTH);
            this.add(new JScrollPane(table), BorderLayout.CENTER);
            dataTable = table;
        }
    }

    private class DataTable extends JTable {
        private QueryResult queryResult;

        DataTable(QueryResult values) {
            super(new DataTableMode(values.getColumns(), values.getDatas()));
            this.setAutoResizeMode(AUTO_RESIZE_OFF);
            this.setCellSelectionEnabled(true);
            setColumnWidth(values);
            queryResult = values;
        }

        void appendDatas(QueryResult values) {
            DataTableMode mode = ((DataTableMode) dataView.getModel());
            for (Vector<String> value : values.getDatas()) {
                mode.addRow(value);
            }
            queryResult = values;
        }

        void setColumnWidth(QueryResult values) {
            if (values.getDatas().size() == 0) {
                return;
            }
            Vector<String> columns = values.getColumns();
            TableColumnModel columnMode = this.getColumnModel();
            for (int i = 0; i < columnMode.getColumnCount(); i++) {
                String column = columns.get(i);
                column = column == null ? "NULL" : column;
                int width = Math.min(200, Math.max(column.length() * 9, 40));
                columnMode.getColumn(i).setPreferredWidth(width);
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    }

    private class DataTableMode extends DefaultTableModel {
        DataTableMode(Vector<String> columns, Vector<Vector<String>> datas) {
            super(datas, columns);
        }
    }

    private class ResultToolbar extends JToolBar {
        private final JLabel queryInfo;

        ResultToolbar() {
            this.setFloatable(false);
            JButton next = new JButton(new ImageIcon(getClass().getResource("/icons/more.png")));
            next.setToolTipText("下一页");
            queryInfo = new JLabel();

            JToolBar blank = new JToolBar();
            blank.setFloatable(false);
            blank.setBorderPainted(false);
            blank.setLayout(new FlowLayout(FlowLayout.RIGHT));

            JButton save = new JButton(new ImageIcon(getClass().getResource("/icons/save.png")));
            save.setToolTipText("导出");

            this.add(next);
            this.addSeparator();
            this.add(queryInfo);
            this.add(blank);
            this.add(save);

            next.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        DataTab dataTab = (DataTab) tabbedPane.getSelectedComponent();
                        dataView = dataTab.dataTable;

                        QueryResult lastResult = dataView.queryResult;
                        String sql = lastResult.getSql();
                        int startIndex = lastResult.getStartIndex();
                        int pageSize = lastResult.getPageSize();
                        int totalSize = lastResult.getTotalSize();
                        if (totalSize <= startIndex + pageSize) {
                            LogPanel.writeLog("没有更多数据");
                            return;
                        }
                        startIndex = startIndex + pageSize;
                        QueryResult result = DBUtil.getDBUtil().query(sql, startIndex, pageSize);
                        queryInfo.setText(getPageInfo(startIndex, pageSize, result.getTotalSize()));
                        result.setSql(sql);
                        dataView.appendDatas(result);
                    } catch (Exception e1) {
                        LogPanel.writeLog(e1.getLocalizedMessage());
                    }
                }
            });

        }
    }

    private String getPageInfo(int startIndex, int pageSize, int total) {
        int start = 0, end = startIndex + pageSize;
        end = Math.min(end, total);
        if (end > 0) {
            start = 1;
        }
        return MessageFormat.format("第{0}-{1}条，共{2}条", start, end, total);
    }
}
