// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclMethod extends ClassDecl {

    private String name;
    private Extend Extend;
    private VarDeclList VarDeclList;
    private MethodDeclList MethodDeclList;

    public ClassDeclMethod (String name, Extend Extend, VarDeclList VarDeclList, MethodDeclList MethodDeclList) {
        this.name=name;
        this.Extend=Extend;
        if(Extend!=null) Extend.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.MethodDeclList=MethodDeclList;
        if(MethodDeclList!=null) MethodDeclList.setParent(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public Extend getExtend() {
        return Extend;
    }

    public void setExtend(Extend Extend) {
        this.Extend=Extend;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public MethodDeclList getMethodDeclList() {
        return MethodDeclList;
    }

    public void setMethodDeclList(MethodDeclList MethodDeclList) {
        this.MethodDeclList=MethodDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Extend!=null) Extend.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(MethodDeclList!=null) MethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Extend!=null) Extend.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Extend!=null) Extend.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclMethod(\n");

        buffer.append(" "+tab+name);
        buffer.append("\n");

        if(Extend!=null)
            buffer.append(Extend.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclList!=null)
            buffer.append(MethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclMethod]");
        return buffer.toString();
    }
}
