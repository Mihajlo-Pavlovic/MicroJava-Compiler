// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class VarDeclNoErr extends VarDecl {

    private Type Type;
    private VarAssignList VarAssignList;

    public VarDeclNoErr (Type Type, VarAssignList VarAssignList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarAssignList=VarAssignList;
        if(VarAssignList!=null) VarAssignList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarAssignList getVarAssignList() {
        return VarAssignList;
    }

    public void setVarAssignList(VarAssignList VarAssignList) {
        this.VarAssignList=VarAssignList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarAssignList!=null) VarAssignList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarAssignList!=null) VarAssignList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarAssignList!=null) VarAssignList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclNoErr(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarAssignList!=null)
            buffer.append(VarAssignList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclNoErr]");
        return buffer.toString();
    }
}
