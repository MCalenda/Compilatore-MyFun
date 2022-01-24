package visitor;

import org.w3c.dom.Element;

public interface ISemanticVisitable {
    void accept(ISemanticVisitor v);
}
