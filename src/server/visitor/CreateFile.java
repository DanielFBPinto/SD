package server.visitor;

public class CreateFile {
    private final String name;
    private final String path;

    public CreateFile(String name, String path) {
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
