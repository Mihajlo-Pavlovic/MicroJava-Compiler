// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class FactorList extends Factor {

    private Factor Factor;
    private Expop Expop;
    private BaseExp BaseExp;

    public FactorList (Factor Factor, Expop Expop, BaseExp BaseExp) {
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
        this.Expop=Expop;
        if(Expop!=null) Expop.setParent(this);
        this.BaseExp=BaseExp;
        if(BaseExp!=null) BaseExp.setParent(this);
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public Expop getExpop() {
        return Expop;
    }

    public void setExpop(Expop Expop) {
        this.Expop=Expop;
    }

    public BaseExp getBaseExp() {
        return BaseExp;
    }

    public void setBaseExp(BaseExp BaseExp) {
        this.BaseExp=BaseExp;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Factor!=null) Factor.accept(visitor);
        if(Expop!=null) Expop.accept(visitor);
        if(BaseExp!=null) BaseExp.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
        if(Expop!=null) Expop.traverseTopDown(visitor);
        if(BaseExp!=null) BaseExp.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        if(Expop!=null) Expop.traverseBottomUp(visitor);
        if(BaseExp!=null) BaseExp.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorList(\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expop!=null)
            buffer.append(Expop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(BaseExp!=null)
            buffer.append(BaseExp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorList]");
        return buffer.toString();
    }
}
