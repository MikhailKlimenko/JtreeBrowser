package jtreeFiles;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ExpansionListener implements TreeExpansionListener {

    private JTree jTree;
    private DefaultTreeModel treeModel;
    private FileSystemView fileSystemView;

    public ExpansionListener(JTree jTree, DefaultTreeModel treeModel, FileSystemView fileSystemView) {
        this.jTree = jTree;
        this.treeModel = treeModel;
        this.fileSystemView = fileSystemView;
    }

    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException t) {
            }
            SwingWorkerLoading swingWorkerLoading = new SwingWorkerLoading(defaultMutableTreeNode, jTree, fileSystemView, treeModel);
            swingWorkerLoading.execute();

        });
        thread.start();
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {

    }
}
