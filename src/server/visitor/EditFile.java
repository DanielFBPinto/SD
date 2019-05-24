package server.visitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditFile implements Visitor {
    private final String name;
    private final String path;
    private final byte[] fileContent;

    public EditFile(String name, String path, byte[] fileContent) {
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
     * Altera o conteúdo de um ficheiro dado o caminho, nome e novo conteúdo para o mesmo
     * @param file
     */
    @Override
    public void visit(File file) {
        try {
            File f = new File(file.getPath() + "/" + this.path + "/" + this.name);
            FileOutputStream out = new FileOutputStream(f);
            out.write(fileContent);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
