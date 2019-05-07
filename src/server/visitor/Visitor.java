package server.visitor;
import java.io.File;
import java.rmi.Remote;

public interface Visitor extends Remote {
//    public void visit(CreateFile file,File path);
//    public void visit(CreateFolder folder,File path);
//    public void visit(DeleteFile file,File path);
//    public void visit(DeleteFolder folder,File path);
//    public void visit(EditFile file,File path);
//    public void visit(RenameFolder folder,File path);
//    public void visit(RenameFile file,File path);
 //   public void visit(Operation operation,File path);
    public void visit(File file);
}
