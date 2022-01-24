package visitor;

import javax.swing.tree.DefaultMutableTreeNode;

import tree.leaves.*;
import tree.nodes.*;

public class Syntax_Visitor implements Syntax_Int_Visitor {

    public Syntax_Visitor() {
    }

    @Override
    public DefaultMutableTreeNode visit(ProgramNode programNode) {

        if (programNode.varDecList != null) {
            DefaultMutableTreeNode VarDeclList = new DefaultMutableTreeNode("VarDeclList");
            for (VarDeclNode varDeclNode : programNode.varDecList) {
                VarDeclList.add(varDeclNode.accept(this));
            }
            programNode.add(VarDeclList);
        }
        if (programNode.funList != null) {
            DefaultMutableTreeNode FunList = new DefaultMutableTreeNode("FunList");
            for (FunNode funNode : programNode.funList) {
                FunList.add(funNode.accept(this));
            }
            programNode.add(FunList);
        }

        programNode.add(programNode.main.accept(this));
        return programNode;
    }

    @Override
    public DefaultMutableTreeNode visit(VarDeclNode varDeclNode) {

        if (varDeclNode.type != null) {
            varDeclNode.add(varDeclNode.type.accept(this));

            DefaultMutableTreeNode idInitList = new DefaultMutableTreeNode("idInitList");
            for (IdInitNode idInitNode : varDeclNode.idInitList) {
                idInitList.add(idInitNode.accept(this));
            }
            varDeclNode.add(idInitList);
        } else {
            varDeclNode.add(new DefaultMutableTreeNode("@ VAR"));
            DefaultMutableTreeNode idListInitObbl = new DefaultMutableTreeNode("IdListInitObbl");
            for (IdInitObblNode idInitObblNode : varDeclNode.IdListInitObbl) {
                idListInitObbl.add(idInitObblNode.accept(this));
            }
            varDeclNode.add(idListInitObbl);
        }

        return varDeclNode;
    }

    @Override
    public DefaultMutableTreeNode visit(TypeNode typeNode) {

        typeNode.add(new DefaultMutableTreeNode("@ " + typeNode.type));

        return typeNode;
    }

    @Override
    public DefaultMutableTreeNode visit(IdInitNode idInitNode) {

        if (idInitNode.exprNode != null) {
            idInitNode.add(new DefaultMutableTreeNode("@ " + idInitNode.leafID.value));
            idInitNode.add(idInitNode.exprNode.accept(this));
        } else {
            idInitNode.add(new DefaultMutableTreeNode("@ " + idInitNode.leafID.value));
        }

        return idInitNode;
    }

    @Override
    public DefaultMutableTreeNode visit(ExprNode exprNode) {
        exprNode.add(new DefaultMutableTreeNode("@ " + exprNode.op));

        if (exprNode.val_One instanceof ExprNode)
            exprNode.add(((ExprNode) exprNode.val_One).accept(this));
        if (exprNode.val_One instanceof CallFunNode)
            exprNode.add(((CallFunNode) exprNode.val_One).accept(this));
        if (exprNode.val_One instanceof LeafID)
            exprNode.add(new DefaultMutableTreeNode("@ " + ((LeafID) exprNode.val_One).toString()));
        if (exprNode.val_One instanceof LeafStringConst)
            exprNode.add(new DefaultMutableTreeNode("@ " + ((LeafStringConst) exprNode.val_One).toString()));
        if (exprNode.val_One instanceof LeafRealConst)
            exprNode.add(new DefaultMutableTreeNode("@ " + ((LeafRealConst) exprNode.val_One).toString()));
        if (exprNode.val_One instanceof LeafIntegerConst)
            exprNode.add(new DefaultMutableTreeNode("@ " + ((LeafIntegerConst) exprNode.val_One).toString()));
        if (exprNode.val_One instanceof LeafBool)
            exprNode.add(new DefaultMutableTreeNode("@ " + ((LeafBool) exprNode.val_One).toString()));

        if (exprNode.val_Two != null) {
            if (exprNode.val_Two instanceof ExprNode)
                exprNode.add(((ExprNode) exprNode.val_Two).accept(this));
            if (exprNode.val_Two instanceof LeafID)
                exprNode.add(new DefaultMutableTreeNode("@ " + ((LeafID) exprNode.val_Two).toString()));
            if (exprNode.val_Two instanceof LeafStringConst)
                exprNode.add(new DefaultMutableTreeNode("@ " + ((LeafStringConst) exprNode.val_Two).toString()));
            if (exprNode.val_Two instanceof LeafRealConst)
                exprNode.add(new DefaultMutableTreeNode("@ " + ((LeafRealConst) exprNode.val_Two).toString()));
            if (exprNode.val_Two instanceof LeafIntegerConst)
                exprNode.add(new DefaultMutableTreeNode("@ " + ((LeafIntegerConst) exprNode.val_Two).toString()));
            if (exprNode.val_Two instanceof LeafBool)
                exprNode.add(new DefaultMutableTreeNode("@ " + ((LeafBool) exprNode.val_Two).toString()));
        }

        return exprNode;
    }

    @Override
    public DefaultMutableTreeNode visit(MainNode mainNode) {
        if (mainNode.varDeclList != null) {
            DefaultMutableTreeNode VarDeclList = new DefaultMutableTreeNode("VarDeclList");
            for (VarDeclNode varDeclNode : mainNode.varDeclList) {
                VarDeclList.add(varDeclNode.accept(this));
            }
            mainNode.add(VarDeclList);
        }
        if (mainNode.statList != null) {
            DefaultMutableTreeNode StatList = new DefaultMutableTreeNode("StatList");
            for (StatNode statNode : mainNode.statList) {
                StatList.add(statNode.accept(this));
            }
            mainNode.add(StatList);
        }

        return mainNode;
    }

    @Override
    public DefaultMutableTreeNode visit(StatNode statNode) {
        if (statNode.ifStatNode != null) {
            statNode.add(statNode.ifStatNode.accept(this));
        } else if (statNode.whileStatNode != null) {
            statNode.add(statNode.whileStatNode.accept(this));
        } else if (statNode.readStatNode != null) {
            statNode.add(statNode.readStatNode.accept(this));
        } else if (statNode.writeStatNode != null) {
            statNode.add(statNode.writeStatNode.accept(this));
        } else if (statNode.assignStatNode != null) {
            statNode.add(statNode.assignStatNode.accept(this));
        } else if (statNode.callFunNode != null) {
            statNode.add(statNode.callFunNode.accept(this));
        } else if (statNode.resultNode != null) {
            statNode.add(statNode.resultNode.accept(this));
        }
        return statNode;

    }

    @Override
    public DefaultMutableTreeNode visit(IfStatNode ifStatNode) {
        ifStatNode.add(ifStatNode.expr.accept(this));

        if (ifStatNode.varDeclList != null) {
            DefaultMutableTreeNode VarDeclList = new DefaultMutableTreeNode("VarDeclList");
            for (VarDeclNode varDeclNode : ifStatNode.varDeclList) {
                VarDeclList.add(varDeclNode.accept(this));
            }

            ifStatNode.add(VarDeclList);
        }
        if (ifStatNode.statList != null) {
            DefaultMutableTreeNode StatList = new DefaultMutableTreeNode("StatList");
            for (StatNode statNode : ifStatNode.statList) {
                StatList.add(statNode.accept(this));
            }
            ifStatNode.add(StatList);
        }

        if (ifStatNode.elseNode != null)
            ifStatNode.add(ifStatNode.elseNode.accept(this));

        return ifStatNode;
    }

    @Override
    public DefaultMutableTreeNode visit(FunNode funNode) {
        funNode.add(new DefaultMutableTreeNode("@ " + funNode.leafID.value));

        DefaultMutableTreeNode paramDeclList = new DefaultMutableTreeNode("ParamDeclList");
        for (ParamDecNode paramDecNode : funNode.paramDecList) {
            paramDeclList.add(paramDecNode.accept(this));
        }

        funNode.add(paramDeclList);

        if (funNode.type != null) {
            funNode.add(funNode.type.accept(this));
        }

        DefaultMutableTreeNode VarDeclList = new DefaultMutableTreeNode("VarDeclList");
        for (VarDeclNode varDeclNode : funNode.varDecList) {
            VarDeclList.add(varDeclNode.accept(this));
        }
        funNode.add(VarDeclList);

        DefaultMutableTreeNode StatList = new DefaultMutableTreeNode("StatList");
        for (StatNode statNode : funNode.statList) {
            StatList.add(statNode.accept(this));
        }
        funNode.add(StatList);

        return funNode;
    }

    @Override
    public DefaultMutableTreeNode visit(AssignStatNode assignStatNode) {
        assignStatNode.add(new DefaultMutableTreeNode("@ " + assignStatNode.leafID.value));
        assignStatNode.add(assignStatNode.expr.accept(this));
        return assignStatNode;
    }

    @Override
    public DefaultMutableTreeNode visit(CallFunNode callFunNode) {
        callFunNode.add(new DefaultMutableTreeNode("@ " + callFunNode.leafID.value));
        if (callFunNode.exprList != null) {
            DefaultMutableTreeNode ExprList = new DefaultMutableTreeNode("ExprList");
            for (ExprNode expr : callFunNode.exprList) {
                ExprList.add(expr.accept(this));
            }
            callFunNode.add(ExprList);
        }
        return callFunNode;
    }

    @Override
    public DefaultMutableTreeNode visit(WhileStatNode whileStatNode) {
        whileStatNode.add(whileStatNode.expr.accept(this));

        DefaultMutableTreeNode VarDeclList = new DefaultMutableTreeNode("VarDeclList");
        for (VarDeclNode varDeclNode : whileStatNode.varDeclList) {
            VarDeclList.add(varDeclNode.accept(this));
        }
        whileStatNode.add(VarDeclList);

        DefaultMutableTreeNode StatList = new DefaultMutableTreeNode("StatList");
        for (StatNode statNode : whileStatNode.statList) {
            StatList.add(statNode.accept(this));
        }
        whileStatNode.add(StatList);

        return whileStatNode;
    }

    @Override
    public DefaultMutableTreeNode visit(WriteStatNode writeStatNode) {
        writeStatNode.add((new DefaultMutableTreeNode("@ " + writeStatNode.op)));
        writeStatNode.add(writeStatNode.expr.accept(this));
        return writeStatNode;
    }

    @Override
    public DefaultMutableTreeNode visit(ReadStatNode readStatNode) {
        DefaultMutableTreeNode IdList = new DefaultMutableTreeNode("IdList");
        for (LeafID leafID : readStatNode.IdList) {
            IdList.add((new DefaultMutableTreeNode("@ " + leafID.value)));
        }
        readStatNode.add(IdList);
        if (readStatNode.expr != null) {
            readStatNode.add(readStatNode.expr.accept(this));
        }
        return readStatNode;
    }

    @Override
    public DefaultMutableTreeNode visit(ParamDecNode paramDecNode) {
        paramDecNode.add((new DefaultMutableTreeNode("@ OUT: " + paramDecNode.out)));
        paramDecNode.add(paramDecNode.type.accept(this));
        paramDecNode.add((new DefaultMutableTreeNode("@ " + paramDecNode.leafID.value)));

        return paramDecNode;
    }

    @Override
    public DefaultMutableTreeNode visit(ElseNode elseNode) {
        if (elseNode.varDeclList != null) {
            DefaultMutableTreeNode VarDeclList = new DefaultMutableTreeNode("VarDeclList");
            for (VarDeclNode varDeclNode : elseNode.varDeclList) {
                VarDeclList.add(varDeclNode.accept(this));
            }

            elseNode.add(VarDeclList);
        }
        if (elseNode.statList != null) {
            DefaultMutableTreeNode StatList = new DefaultMutableTreeNode("StatList");
            for (StatNode statNode : elseNode.statList) {
                StatList.add(statNode.accept(this));
            }
            elseNode.add(StatList);
        }
        return elseNode;
    }

    @Override
    public DefaultMutableTreeNode visit(IdInitObblNode idInitObblNode) {

        idInitObblNode.add(new DefaultMutableTreeNode("@ " + idInitObblNode.leafID.value));
        idInitObblNode.add(idInitObblNode.value.accept(this));

        return idInitObblNode;
    }

    @Override
    public DefaultMutableTreeNode visit(ConstNode constNode) {
        constNode.add(new DefaultMutableTreeNode("@ " + constNode.value.toString()));
        return constNode;
    }

    @Override
    public DefaultMutableTreeNode visit(ReturnNode resultNode) {
        resultNode.add(resultNode.expr.accept(this));
        return resultNode;
    }
}
