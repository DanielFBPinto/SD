package server.visitor;

public class RenameFile {
    private final String name;
    private final String path;
    private final String newName;

    public RenameFile(String name, String path, String newName) {
        this.name = name;
        this.path = path;
        this.newName = newName;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getNewName() {
        return newName;
    }
}
