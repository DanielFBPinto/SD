package server.visitor;

import java.io.File;

public class DeleteFolder implements Visitor {
    private final String name;
    private final String path;

    public DeleteFolder(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /**
     * Apaga uma pasta e o seu conteúdo dado um caminho e um nome
     * @param file
     */
    @Override
    public void visit(File file) {
        File rec = new File(file.getPath() + "/" + this.getPath() + "/" + this.getName());
        deleteFolder(rec);
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

}
