package visitor;

import symbol_table.SymbolTable;
import symbol_table.SymbolTableEntry;
import symbol_table.Type;
import tree.leaves.*;
import tree.nodes.*;
import java.util.Stack;


public class Semantic_Visitor implements Semantic_Int_Visitor {

    public Stack<SymbolTable> stack = new Stack<>();

    @Override
    public void visit(ProgramNode programNode) {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.symbolTableName = "Global";
        stack.push(symbolTable);

        if (programNode.varDecList != null) {
            for (VarDeclNode varDeclNode : programNode.varDecList) {
                varDeclNode.accept(this);
            }
        }

        if (programNode.funList != null) {
            for (FunNode funNode : programNode.funList) {
                funNode.accept(this);
            }
        }
    }

    @Override
    public void visit(VarDeclNode varDeclNode) {
        varDeclNode.type.accept(this);

        if (varDeclNode.idInitList != null){
            for (IdInitNode idInitNode : varDeclNode.idInitList) {
                try {
                    SymbolTable picked = stack.peek();
                    if (picked.getFatherSymTab() != null){
                        //controlla solo il padre, containsKey() verifica gia nella tabella attuale
                        SymbolTableEntry symbolTableEntry = picked.getFatherSymTab().get(idInitNode.leafID.value);
                        if (symbolTableEntry != null && symbolTableEntry.type == Type.function) {
                            System.err.println("Semantic error: Cannot declare a variable with ID: " + idInitNode.leafID.value + ". There is a function with same ID.");
                            System.exit(0);
                        } else {
                            picked.createEntry_variable(idInitNode.leafID.value, varDeclNode.type.type);
                        }
                    } else {
                        picked.createEntry_variable(idInitNode.leafID.value, varDeclNode.type.type);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                idInitNode.accept(this);
                idInitNode.setType(varDeclNode.type.type);
            }

        }
        SymbolTable picked = stack.peek();
        System.out.println(picked.toString());
        

    }

    @Override
    public void visit(TypeNode typeNode) {

    }

    @Override
    public void visit(IdInitNode idInitNode) {

    }

    @Override
    public void visit(ExprNode exprNode) {
        
    }

    @Override
    public void visit(MainNode mainNode) {
        
    }

    @Override
    public void visit(StatNode statNode) {
        
    }

    @Override
    public void visit(IfStatNode ifStatNode) {
        
    }

    @Override
    public void visit(FunNode funNode) {
        
    }

    @Override
    public void visit(AssignStatNode assignStatNode) {
        
    }

    @Override
    public void visit(CallFunNode callFunNode) {
        
    }

    @Override
    public void visit(WhileStatNode whileStatNode) {
        
    }

    @Override
    public void visit(WriteStatNode writeStatNode) {
        
    }

    @Override
    public void visit(ReadStatNode readStatNode) {
        
    }

    @Override
    public void visit(ParamDecNode paramDecNode) {
        
    }

    @Override
    public void visit(ElseNode elseNode) {
        
    }

    @Override
    public void visit(IdInitObblNode idInitObblNode) {
        
    }

    @Override
    public void visit(ConstNode constNode) {
        
    }

    @Override
    public void visit(ReturnNode resultNode) {
        
    }
}
