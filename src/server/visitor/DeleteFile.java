package server.visitor;

import java.io.File;

public class DeleteFile implements Visitor {
    private final String name;
    private final String path;

    public DeleteFile(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /**
     * Apaga o conteúdo de um dado ficheiro, dado o caminho e o nome do mesmo
     * @param file
     */
    @Override
    public void visit(File file) {
        new File(file.getPath() + "/" + this.path + "/" + this.name).delete();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
