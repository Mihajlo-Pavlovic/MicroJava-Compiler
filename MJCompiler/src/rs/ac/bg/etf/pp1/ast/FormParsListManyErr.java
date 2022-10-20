// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class FormParsListManyErr extends FormParsList {

    private LastFormPar LastFormPar;

    public FormParsListManyErr (LastFormPar LastFormPar) {
        this.LastFormPar=LastFormPar;
        if(LastFormPar!=null) LastFormPar.setParent(this);
    }

    public LastFormPar getLastFormPar() {
        return LastFormPar;
    }

    public void setLastFormPar(LastFormPar LastFormPar) {
        this.LastFormPar=LastFormPar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LastFormPar!=null) LastFormPar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LastFormPar!=null) LastFormPar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LastFormPar!=null) LastFormPar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsListManyErr(\n");

        if(LastFormPar!=null)
            buffer.append(LastFormPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsListManyErr]");
        return buffer.toString();
    }
}
