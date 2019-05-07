package server.visitor;
import java.io.File;
public interface Visitor {
    public void visit(CreateFile file,File path);
    public void visit(CreateFolder folder,File path);
    public void visit(DeleteFile file,File path);
    public void visit(DeleteFolder folder,File path);
    public void visit(EditFile file,File path);
    public void visit(RenameFolder folder,File path);
    public void visit(RenameFile file,File path);

}
