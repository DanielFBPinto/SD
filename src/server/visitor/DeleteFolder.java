package server.visitor;

public class DeleteFolder {
    private final String name;
    private final String path;

    public DeleteFolder(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
