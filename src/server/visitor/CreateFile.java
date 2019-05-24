package server.visitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateFile implements Visitor {
    private final String name;
    private final String path;
    private final byte[] fileContent;

    public CreateFile(String name, String path, byte[] fileContent) {
        this.name = name;
        this.path = path;
        this.fileContent = fileContent;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    /**
     * Cria um file dado um nome, caminho e conteúdo
     * @param file
     */
    @Override
    public void visit(File file) {
        try {
            File f = new File(file.getPath() + "/" + this.path + "/" + this.name);
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            out.write(fileContent);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
