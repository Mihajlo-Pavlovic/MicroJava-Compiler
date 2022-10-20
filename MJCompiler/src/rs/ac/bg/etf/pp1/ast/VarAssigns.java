// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class VarAssigns extends VarAssignList {

    private VarAssignList VarAssignList;
    private VarAssign VarAssign;

    public VarAssigns (VarAssignList VarAssignList, VarAssign VarAssign) {
        this.VarAssignList=VarAssignList;
        if(VarAssignList!=null) VarAssignList.setParent(this);
        this.VarAssign=VarAssign;
        if(VarAssign!=null) VarAssign.setParent(this);
    }

    public VarAssignList getVarAssignList() {
        return VarAssignList;
    }

    public void setVarAssignList(VarAssignList VarAssignList) {
        this.VarAssignList=VarAssignList;
    }

    public VarAssign getVarAssign() {
        return VarAssign;
    }

    public void setVarAssign(VarAssign VarAssign) {
        this.VarAssign=VarAssign;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarAssignList!=null) VarAssignList.accept(visitor);
        if(VarAssign!=null) VarAssign.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarAssignList!=null) VarAssignList.traverseTopDown(visitor);
        if(VarAssign!=null) VarAssign.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarAssignList!=null) VarAssignList.traverseBottomUp(visitor);
        if(VarAssign!=null) VarAssign.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarAssigns(\n");

        if(VarAssignList!=null)
            buffer.append(VarAssignList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarAssign!=null)
            buffer.append(VarAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarAssigns]");
        return buffer.toString();
    }
}
