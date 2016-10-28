package com.calix.code.widget;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by calix on 16-10-26
 * 导航栏
 */
public class Navigation {

    private final FileNode treeRoot = new FileNode("Remote Sever");

    JTree getNavigation() {
        final JTree tree = new JTree(treeRoot);
        treeRoot.setChildren(nodes);
        loadTreeNode(treeRoot);

        return tree;
    }

    private static  List<FileNode> nodes = new ArrayList<FileNode>();

    static {
        File home = new File("/home/calix/");
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isFile() && !f.isHidden()) {
                    return false;
                }
                return true;
            }
        };
        File[] dirs = home.listFiles(filter);
        for (File dir : dirs) {
            FileNode directory = new FileNode(dir.getName());
            nodes.add(directory);
        }
    }

    private void loadTreeNode(FileNode node) {
        if (node == null || !node.hasChildren()) {
            return;
        }
        for (FileNode child : node.getChildren()) {
            node.add(child);
            loadTreeNode(child);
        }

    }

}
