package server.visitor;

import java.io.File;

public class CreateFolder implements Visitor {
    private final String name;
    private final String path;


    public CreateFolder(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /**
     * Cria uma folder dado um caminho e um nome
     * @param file
     */
    @Override
    public void visit(File file) {
        new File(file.getPath() + "/" + this.path + "/" + this.name).mkdirs();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
