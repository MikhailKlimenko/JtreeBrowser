package jtreeFiles;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/** Lazy load and noFrozen         */
public class SwingWorkerLoading extends SwingWorker<DefaultMutableTreeNode,File>{

    private JTree jTree;
    private FileSystemView fileSystemView;
    private DefaultTreeModel model;
    private DefaultMutableTreeNode top;
    private Boolean flag = false;

    public SwingWorkerLoading(DefaultMutableTreeNode node, JTree jTree, FileSystemView fileSystemView, DefaultTreeModel model) {
        this.jTree = jTree;
        this.fileSystemView = fileSystemView;
        this.model = model;
        this.top = node;
        removeAllChildren();
    }

    /** remove children (BOOLEAN CHILDREN)  */
    public void removeAllChildren() {
        final DefaultMutableTreeNode topChild;
        try {
             topChild = (DefaultMutableTreeNode) top.getFirstChild();
            if (topChild != null)
            {
                top.removeAllChildren();
                jTree.setEnabled(false);
            }
            if (((File)top.getUserObject()).listFiles() == null)
            {
                reloadModel(top);
            }
        } catch (NoSuchElementException l) {
            flag = true;
        }
    }

    @Override
    protected DefaultMutableTreeNode doInBackground() throws Exception {

        if (!flag) {
            File file = (File) top.getUserObject();
            if (file.isDirectory()) {
                File[] files = fileSystemView.getFiles(file, true);
                if (top.isLeaf() && files != null) {
                    for (File child : files) {
                        publish(child);
                    }
                }
            }

            return top;
        } else
        {
            return null;
        }
    }



    /**  sort files   */
    @Override
    protected void process(List<File> chunks) {
        chunks.sort((o1, o2) -> {
            if (o1.isDirectory() && o2.isDirectory()) {
                return 0;
            } else if (!o1.isDirectory() && !o2.isDirectory()) {
                return o1.getPath().compareTo(o2.getPath());
            } else if (o1.isDirectory() && !o2.isDirectory()) {
                return -1;
            } else if (!o1.isDirectory() && o2.isDirectory()) {
                return +1;
            }
            return 0;
        });

        for (File child : chunks) {
            DefaultMutableTreeNode childs = new DefaultMutableTreeNode(child);
            if (child.isDirectory() && child.listFiles() != null && child.listFiles().length != 0) {

                    childs.add(new DefaultMutableTreeNode(new Boolean(true)));
            }
            top.add(childs);
        }
        reloadModel(top);
    }

    @Override
    protected void done() {
        jTree.setEnabled(true);
    }


    /** reset jtree  */
    public void reloadModel(DefaultMutableTreeNode top) {
        //reset jtree
        if (!flag) {
            model.reload(top);
        }
    }
}
