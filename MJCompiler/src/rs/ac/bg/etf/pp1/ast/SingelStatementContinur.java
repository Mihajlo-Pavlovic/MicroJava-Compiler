// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class SingelStatementContinur extends SingelStatement {

    public SingelStatementContinur () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingelStatementContinur(\n");

        buffer.append(tab);
        buffer.append(") [SingelStatementContinur]");
        return buffer.toString();
    }
}
