package visitor;

import javax.swing.tree.DefaultMutableTreeNode;

public interface Syntax_Int_Visitable {

    DefaultMutableTreeNode accept(Syntax_Int_Visitor v);
    
}
