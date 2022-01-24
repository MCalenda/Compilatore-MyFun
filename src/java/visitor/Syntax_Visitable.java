package visitor;

import javax.swing.tree.DefaultMutableTreeNode;
//OK
public interface Syntax_Visitable {
    DefaultMutableTreeNode accept(Syntax_Int_Visitor v);
}
