package jtreeFiles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.io.File;

/**  Icon render  */
public class JTreeIconRenderer extends JLabel implements TreeCellRenderer {

    public JTreeIconRenderer() {

        super();

        textSelectionColor = Color.WHITE;
        textNonSelectionColor = Color.BLACK;
        bkSelectionColor = Color.BLUE;
        bkNonSelectionColor = Color.white;

        setOpaque(true);

    }
    protected boolean selected;
    protected Color textSelectionColor;
    protected Color textNonSelectionColor;
    protected Color bkSelectionColor;
    protected Color bkNonSelectionColor;


    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus)
    {
        DefaultMutableTreeNode top = (DefaultMutableTreeNode) value;
        File[] files = File.listRoots();
        Object obj = top.getUserObject();


        if (obj instanceof Boolean) {
            //indicator load
            setText("Загрузка данных");
            setIcon(new ImageIcon("images/loadIcon.jpg"));
        } else if (obj.toString().contains(".") && (!((File) obj).isDirectory())) {
            setText(FileSystemView.getFileSystemView().getSystemDisplayName((File) obj));
            ImageIcon imageIcon = new ImageIcon(("images/iconFiles/" + obj.toString().substring(obj.toString().lastIndexOf(".") + 1) + ".png"));
            if (imageIcon.getIconHeight() == -1 && imageIcon.getIconWidth() == -1) {
                imageIcon = new ImageIcon("images/iconFiles/file.png");
            }
            setIcon(imageIcon);
        } else if (!obj.toString().equals("Computer")) {
            if (expanded) {
                setText(FileSystemView.getFileSystemView().getSystemDisplayName((File) obj));
                setIcon(new ImageIcon("images/openFolder.jpg"));

            } else
            {
                setIcon(new ImageIcon("images/closedFolder.jpg"));
                setText(FileSystemView.getFileSystemView().getSystemDisplayName((File) obj));
            }

            for (File f : files) {
                if (f.equals(obj)) {
                    setIcon(new ImageIcon("images/disk.png"));
                    setText(FileSystemView.getFileSystemView().getSystemDisplayName((File) obj));
                }
            }
        }

        setForeground(sel ? textSelectionColor : textNonSelectionColor);
        setBackground(sel ? bkSelectionColor : bkNonSelectionColor);
        setBorder(new EmptyBorder(3,3,3,3));

        selected = sel;
        return this;
    }
}
