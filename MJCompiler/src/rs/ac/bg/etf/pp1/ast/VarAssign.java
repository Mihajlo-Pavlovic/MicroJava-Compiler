// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class VarAssign implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String name;
    private Array Array;

    public VarAssign (String name, Array Array) {
        this.name=name;
        this.Array=Array;
        if(Array!=null) Array.setParent(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public Array getArray() {
        return Array;
    }

    public void setArray(Array Array) {
        this.Array=Array;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Array!=null) Array.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Array!=null) Array.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Array!=null) Array.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarAssign(\n");

        buffer.append(" "+tab+name);
        buffer.append("\n");

        if(Array!=null)
            buffer.append(Array.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarAssign]");
        return buffer.toString();
    }
}
