// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class FormParsListMany extends FormParsList {

    private FormPars FormPars;
    private LastFormPar LastFormPar;

    public FormParsListMany (FormPars FormPars, LastFormPar LastFormPar) {
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
        this.LastFormPar=LastFormPar;
        if(LastFormPar!=null) LastFormPar.setParent(this);
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
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
        if(FormPars!=null) FormPars.accept(visitor);
        if(LastFormPar!=null) LastFormPar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
        if(LastFormPar!=null) LastFormPar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        if(LastFormPar!=null) LastFormPar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsListMany(\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LastFormPar!=null)
            buffer.append(LastFormPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsListMany]");
        return buffer.toString();
    }
}
