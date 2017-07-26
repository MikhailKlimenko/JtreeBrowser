package jtreeFiles;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;
import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;


public class JTreeProgram {


    private JFrame jFrame;
    private JTree jTree;
    private DefaultTreeModel treeModel;
    private FileSystemView fileSystemView;
    private Action action;
    private JPopupMenu jPopupMenu;
    private TreePath treePath;
    private Desktop desktop;
    private File file;
    private Integer count = 0;


    public JTreeProgram() {

        jFrame = new JFrame("JTreeProgram");
        jFrame.setSize(new Dimension(800, 800));

        DefaultMutableTreeNode fistDMTN = new DefaultMutableTreeNode("Computer");

        fileSystemView = FileSystemView.getFileSystemView();
        desktop = Desktop.getDesktop();

        treeModel = new DefaultTreeModel(getRoot(fistDMTN));
        jTree = new JTree(treeModel);
        jTree.setCellRenderer(new JTreeIconRenderer());
        jTree.setRootVisible(false);
        jTree.addTreeExpansionListener(new ExpansionListener(jTree, treeModel, fileSystemView));
        jTree.setOpaque(false);
        jTree.expandRow(0);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().add(jTree);

        jPopupMenu = new JPopupMenu();
        action=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (treePath == null) {
                    return;
                }
                if (jTree.isExpanded(treePath)) {
                    jTree.collapsePath(treePath);
                } else jTree.expandPath(treePath);
            }
        };
        jPopupMenu.add(action);
        jPopupMenu.addSeparator();

        Action action1=new AbstractAction("Create folder") {
            @Override
            public void actionPerformed(ActionEvent e) {
                while (!((new File(file + "/Folder" + count++)).mkdir())) {
                    count++;
                }
                jTree.repaint();

            }
        };

        Action action2=new AbstractAction("Open") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Desktop.isDesktopSupported()) {
                        desktop.open(file);
                    }

                } catch (IOException d) {

                }
            }
        };
        jPopupMenu.add(action1);
        jPopupMenu.add(action2);
        jTree.add(jPopupMenu);
        jTree.addMouseListener(new PopupTrigger());

        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }

    class PopupTrigger extends MouseAdapter
    {
        public void mouseReleased(MouseEvent e)
        {
            if (e.isPopupTrigger())
            {
                int x = e.getX();
                int y = e.getY();
                TreePath path = jTree.getPathForLocation(x, y);
                System.out.println(jTree.getPathForLocation(x, y).getLastPathComponent());
                file = new File(jTree.getPathForLocation(x, y).getLastPathComponent().toString());
                if (path != null)
                {
                    if (jTree.isExpanded(path))     action.putValue(Action.NAME, "Collapse");
                    else                            action.putValue(Action.NAME, "Expand");
                    jPopupMenu.show(jTree, x, y);
                    treePath = path;
                }
            }
        }
    }


    public DefaultMutableTreeNode getRoot(DefaultMutableTreeNode firstDMTN) {
        DefaultMutableTreeNode secondDMTN;
        for (File fileSystem : fileSystemView.getRoots()) {
            secondDMTN = new DefaultMutableTreeNode(fileSystem);
            firstDMTN.add(secondDMTN);
            for (File files : fileSystemView.getFiles(fileSystem, true)) {
                if (files.isDirectory()) {
                    DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(files);
                    secondDMTN.add(defaultMutableTreeNode);
                }
            }
        }
        return firstDMTN;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JTreeProgram::new);
    }
}
