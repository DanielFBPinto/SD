package server.visitor;

public class DeleteFile {
    private final String name;
    private final String path;

    public DeleteFile(String name, String path) {
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
