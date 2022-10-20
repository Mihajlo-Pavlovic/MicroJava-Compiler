// generated with ast extension for cup
// version 0.8
// 28/5/2022 13:1:56


package rs.ac.bg.etf.pp1.ast;

public class SingleStatementDoWhile extends SingelStatement {

    private DoWhileBeggining DoWhileBeggining;
    private Statement Statement;
    private DoWhileEnd DoWhileEnd;
    private Condition Condition;

    public SingleStatementDoWhile (DoWhileBeggining DoWhileBeggining, Statement Statement, DoWhileEnd DoWhileEnd, Condition Condition) {
        this.DoWhileBeggining=DoWhileBeggining;
        if(DoWhileBeggining!=null) DoWhileBeggining.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.DoWhileEnd=DoWhileEnd;
        if(DoWhileEnd!=null) DoWhileEnd.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
    }

    public DoWhileBeggining getDoWhileBeggining() {
        return DoWhileBeggining;
    }

    public void setDoWhileBeggining(DoWhileBeggining DoWhileBeggining) {
        this.DoWhileBeggining=DoWhileBeggining;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public DoWhileEnd getDoWhileEnd() {
        return DoWhileEnd;
    }

    public void setDoWhileEnd(DoWhileEnd DoWhileEnd) {
        this.DoWhileEnd=DoWhileEnd;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoWhileBeggining!=null) DoWhileBeggining.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(DoWhileEnd!=null) DoWhileEnd.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoWhileBeggining!=null) DoWhileBeggining.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(DoWhileEnd!=null) DoWhileEnd.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoWhileBeggining!=null) DoWhileBeggining.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(DoWhileEnd!=null) DoWhileEnd.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleStatementDoWhile(\n");

        if(DoWhileBeggining!=null)
            buffer.append(DoWhileBeggining.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DoWhileEnd!=null)
            buffer.append(DoWhileEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleStatementDoWhile]");
        return buffer.toString();
    }
}
