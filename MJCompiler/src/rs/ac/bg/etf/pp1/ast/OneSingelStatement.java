// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class OneSingelStatement extends Statement {

    private SingelStatement SingelStatement;

    public OneSingelStatement (SingelStatement SingelStatement) {
        this.SingelStatement=SingelStatement;
        if(SingelStatement!=null) SingelStatement.setParent(this);
    }

    public SingelStatement getSingelStatement() {
        return SingelStatement;
    }

    public void setSingelStatement(SingelStatement SingelStatement) {
        this.SingelStatement=SingelStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SingelStatement!=null) SingelStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SingelStatement!=null) SingelStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SingelStatement!=null) SingelStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneSingelStatement(\n");

        if(SingelStatement!=null)
            buffer.append(SingelStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneSingelStatement]");
        return buffer.toString();
    }
}
