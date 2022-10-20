// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class DesignatorExpr extends Designator {

    private DesignatorArr DesignatorArr;
    private Expr Expr;

    public DesignatorExpr (DesignatorArr DesignatorArr, Expr Expr) {
        this.DesignatorArr=DesignatorArr;
        if(DesignatorArr!=null) DesignatorArr.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public DesignatorArr getDesignatorArr() {
        return DesignatorArr;
    }

    public void setDesignatorArr(DesignatorArr DesignatorArr) {
        this.DesignatorArr=DesignatorArr;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorArr!=null) DesignatorArr.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorArr!=null) DesignatorArr.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorArr!=null) DesignatorArr.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorExpr(\n");

        if(DesignatorArr!=null)
            buffer.append(DesignatorArr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorExpr]");
        return buffer.toString();
    }
}
