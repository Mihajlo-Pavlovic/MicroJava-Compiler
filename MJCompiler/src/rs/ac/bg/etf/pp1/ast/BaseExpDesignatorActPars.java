// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class BaseExpDesignatorActPars extends BaseExp {

    private Designator Designator;
    private EnterMethodWArgs EnterMethodWArgs;
    private ActPars ActPars;

    public BaseExpDesignatorActPars (Designator Designator, EnterMethodWArgs EnterMethodWArgs, ActPars ActPars) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.EnterMethodWArgs=EnterMethodWArgs;
        if(EnterMethodWArgs!=null) EnterMethodWArgs.setParent(this);
        this.ActPars=ActPars;
        if(ActPars!=null) ActPars.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public EnterMethodWArgs getEnterMethodWArgs() {
        return EnterMethodWArgs;
    }

    public void setEnterMethodWArgs(EnterMethodWArgs EnterMethodWArgs) {
        this.EnterMethodWArgs=EnterMethodWArgs;
    }

    public ActPars getActPars() {
        return ActPars;
    }

    public void setActPars(ActPars ActPars) {
        this.ActPars=ActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(EnterMethodWArgs!=null) EnterMethodWArgs.accept(visitor);
        if(ActPars!=null) ActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(EnterMethodWArgs!=null) EnterMethodWArgs.traverseTopDown(visitor);
        if(ActPars!=null) ActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(EnterMethodWArgs!=null) EnterMethodWArgs.traverseBottomUp(visitor);
        if(ActPars!=null) ActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BaseExpDesignatorActPars(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnterMethodWArgs!=null)
            buffer.append(EnterMethodWArgs.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActPars!=null)
            buffer.append(ActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BaseExpDesignatorActPars]");
        return buffer.toString();
    }
}
