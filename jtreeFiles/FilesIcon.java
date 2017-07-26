package jtreeFiles;

import java.io.File;

public class FilesIcon {
    private String name;
    private File file;
    private Object object;

    public FilesIcon(File file) {
        this.file = file;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {

        return file.getName().length() > 0 ? file.getName() : file.getPath();
    }
}
