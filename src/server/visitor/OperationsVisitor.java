package server.visitor;
import java.io.File;
/**
 * Not being used
 */
public class OperationsVisitor  {
    public OperationsVisitor() {
    }
//
//    @Override
//    public void visit(CreateFile file, File path) {
//        new File(path.getPath() + file.getPath()+"/"+file.getName()).mkdirs();
//    }
//
//    @Override
//    public void visit(CreateFolder folder,File path) {
//        new File(path.getPath() + folder.getPath()+"/"+folder.getName()).mkdirs();
//    }
//
//    @Override
//    public void visit(DeleteFile file, File path) {
//        new File(path.getPath() + "/" + file.getPath() + "/" + file.getName()).delete();
//
//    }
//
//    @Override
//    public void visit(DeleteFolder folder, File path) {
//        new File(path.getPath() + "/" + folder.getPath() + "/" + folder.getName()).delete();
//    }
//
//    @Override
//    public void visit(EditFile file, File path) {
//
//    }
//
//    @Override
//    public void visit(RenameFolder folder, File path) {
//                File dirC = new File(path.getPath() + "/" + folder.getPath() + "/" + folder.getName());
//        File newDirC = new File(dirC.getParent() + "/" + folder.getNewName());
//        dirC.renameTo(newDirC);
//    }
//
//    @Override
//    public void visit(RenameFile file, File path) {
//        File fileC = new File(path.getPath() + "/" + file.getPath() + "/" + file.getName());
//        File newfileC = new File(fileC.getParent() + "/" + file.getNewName());
//        fileC.renameTo(newfileC);
//    }
//
//    @Override
//    public void visit(Operation operation, File path) {
//        operation.visit();
//    }
}
