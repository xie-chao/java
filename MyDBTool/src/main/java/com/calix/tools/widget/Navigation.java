package com.calix.tools.widget;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by calix on 16-10-26
 * 服务器列表
 */
public class Navigation extends JPanel {

    private static Navigation navigation;

    private final JTree navigateTree;

    private final List<FileNode> tableChildren = new ArrayList<>();
    private final List<FileNode> indexChildren = new ArrayList<>();

    static Navigation getNavigation(String dbName) {
        if (navigation == null) {
            navigation = new Navigation(dbName);
        }
        return navigation;
    }

    void updateRootName(String name) {
        FileNode root = (FileNode)navigateTree.getModel().getRoot();
        root.setName(name);
        navigateTree.updateUI();
    }

    private Navigation(String dbName) {
        List<FileNode> nodes = new ArrayList<>();
        FileNode tables = new FileNode("Tables");
        tables.add(new FileNode());
        nodes.add(tables);
        FileNode index = new FileNode("Indexs");
        index.add(new FileNode());
        nodes.add(index);

        final FileNode treeRoot = new FileNode(dbName);
        treeRoot.setChildren(nodes);
        initNavigation(treeRoot);

        navigateTree = new JTree(treeRoot);
        JScrollPane scrollPanel = new JScrollPane(navigateTree);
        this.setLayout(new BorderLayout());
        this.add(scrollPanel, BorderLayout.CENTER);

        navigateTree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                FileNode node = (FileNode)event.getPath().getLastPathComponent();
                if ("Tables".equals(node.getName())) {

                    return;
                } else if ("Indexs".equals(node.getName())) {

                    return;
                }
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {

            }
        });
    }

    private void initNavigation(FileNode node) {
        if (node == null || !node.hasChildren()) {
            return;
        }
        for (FileNode child : node.getChildren()) {
            node.add(child);
            initNavigation(child);
        }

    }

}
