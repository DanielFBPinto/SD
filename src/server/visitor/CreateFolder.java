package server.visitor;

import java.io.File;

public class CreateFolder {
    private final String name;
    private final String path;


    public CreateFolder(String name, String path) {
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
