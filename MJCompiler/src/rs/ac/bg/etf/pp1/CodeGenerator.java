package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	private boolean inDoWhile = false;
	private Stack<List<Integer>> fixAdrAnd = new Stack<>();
	private List<Integer> fixAdrOr = new ArrayList<>();
	private Stack<Integer> fixAdrElse = new Stack<>();
	private Stack<Integer> doWhileStart = new Stack<>();
	private Stack<List<Integer>> fixBreak = new Stack<>();
	private Stack<List<Integer>>  fixContinue = new Stack<>();

	private List<Obj> varArgsMethod = new ArrayList<>();
	private Obj lastVisitedMethod = null;
	private boolean inVarArgsMethod = false;
	private int argCnt = 0;
	private int formArgCnt = 0;
	private List<Object> varArgs = new ArrayList<>();
	int op;

	public CodeGenerator() {

		Tab.chrObj.setAdr(Code.pc);

		Code.put(Code.enter);
		Code.put(Tab.chrObj.getLevel());
		Code.put(Tab.chrObj.getLocalSymbols().size());

		Code.put(Code.load_n);

		Code.put(Code.exit);
		Code.put(Code.return_);

		Tab.ordObj.setAdr(Code.pc);

		Code.put(Code.enter);
		Code.put(Tab.ordObj.getLevel());
		Code.put(Tab.ordObj.getLocalSymbols().size());

		Code.put(Code.load_n);

		Code.put(Code.exit);
		Code.put(Code.return_);

		Tab.lenObj.setAdr(Code.pc);

		Code.put(Code.enter);
		Code.put(Tab.lenObj.getLevel());
		Code.put(Tab.lenObj.getLocalSymbols().size());

		Code.put(Code.load_n);
		Code.put(Code.arraylength);

		Code.put(Code.exit);
		Code.put(Code.return_);

	}

	public int getMainPc() {
		return mainPc;
	}

	@Override
	public void visit(VoidMethod methodTypeName) {
		methodTypeName.obj.setAdr(Code.pc);
		if ("main".equalsIgnoreCase(methodTypeName.getName())) {
			mainPc = Code.pc;
		}
		lastVisitedMethod = methodTypeName.obj;
		// Generate the entry.
		Code.put(Code.enter);
		Code.put(methodTypeName.obj.getLevel());
		Code.put(methodTypeName.obj.getLocalSymbols().size());

	}

	@Override
	public void visit(TypeMethod methodTypeName) {
		methodTypeName.obj.setAdr(Code.pc);
		lastVisitedMethod = methodTypeName.obj;
		// Generate the entry.
		Code.put(Code.enter);
		Code.put(methodTypeName.obj.getLevel());
		Code.put(methodTypeName.obj.getLocalSymbols().size());

	}

	@Override
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	@Override
	public void visit(SingelStatementReturnArg ReturnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	@Override
	public void visit(SingelStatementRetrunNoArg ReturnNoExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	@Override
	public void visit(DesignatorStatementAssignop assignment) {
		Code.store(assignment.getDesignator().obj);
	}

	@Override
	public void visit(DesignatorStatementInc inc) {
		Obj obj = inc.getDesignator().obj;
		if (obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(obj);
	}

	@Override
	public void visit(DesignatorStatementDec dec) {
		Obj obj = dec.getDesignator().obj;
		if (obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(obj);
	}

	@Override
	public void visit(BaseExpNum constVal) {
		Code.loadConst(constVal.getValue());
	}

	@Override
	public void visit(BaseExpChar constVal) {
		Code.loadConst(constVal.getValue());
	}

	@Override
	public void visit(BaseExpBool constVal) {
		Code.loadConst(constVal.getValue() ? 1 : 0);
	}

	@Override
	public void visit(BaseExpDesignator baseExpDesignator) {
		Code.load(baseExpDesignator.getDesignator().obj);
	}

	@Override
	public void visit(BaseExpNewArr baseExpNewArr) {
		Code.put(Code.newarray);
		if (baseExpNewArr.getType().struct == Tab.charType) {
			Code.put(0);
		} else {
			Code.put(1);
		}
	}

	@Override
	public void visit(BaseExpDesignatorActPars funcCall) {
		for (Obj o : varArgsMethod)
			if (o.getName().equals(funcCall.getDesignator().obj.getName())) {
				inVarArgsMethod = true;
				formArgCnt = funcCall.getDesignator().obj.getLevel() - 1;
				break;
			}
		if (inVarArgsMethod) {
			// da bih znao da li je char
			Obj type = null;
			Obj obj = funcCall.getDesignator().obj;
			for (Obj iter : obj.getLocalSymbols()) {
				if (iter.getFpPos() == obj.getLevel()) {
					obj = iter;
					break;
				}
			}
			// kreiraj niz
			Code.loadConst(argCnt - formArgCnt);
			Code.put(Code.newarray);
			if (obj.getType().getElemType() == Tab.charType) {
				Code.put(0);
			} else {
				Code.put(1);
			}
			// sada na dnu niz
			// napunim niz
			int cnt = argCnt - formArgCnt;			
			for (int i = cnt; i > 0; i--) {
				Code.put(Code.dup_x1);
				Code.put(Code.dup_x2);
				Code.put(Code.pop);
				Code.loadConst(i-1);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
				// ovde if od tipa
				if(obj.getType().getElemType() != Tab.charType)
					Code.put(Code.astore);
				else
					Code.put(Code.bastore);
			}
		}
		callMethod(funcCall.getDesignator().obj);

		inVarArgsMethod = false;
		argCnt = 0;
		formArgCnt = 0;
		varArgs.clear();

	}

	@Override
	public void visit(BaseExpDesignatorNoActPars funcCall) {
		callMethod(funcCall.getDesignator().obj);
	}

	@Override
	public void visit(DesignatorStatementActPars funcCall) {
		for (Obj o : varArgsMethod)
			if (o.getName().equals(funcCall.getDesignator().obj.getName())) {
				inVarArgsMethod = true;
				formArgCnt = funcCall.getDesignator().obj.getLevel() - 1;
				break;
			}
		if (inVarArgsMethod) {
			// da bih znao da li je char
			Obj type = null;
			Obj obj = funcCall.getDesignator().obj;
			for (Obj iter : obj.getLocalSymbols()) {
				if (iter.getFpPos() == obj.getLevel()) {
					obj = iter;
					break;
				}
			}
			// kreiraj niz
			Code.loadConst(argCnt - formArgCnt);
			Code.put(Code.newarray);
			if (obj.getType().getElemType() == Tab.charType) {
				Code.put(0);
			} else {
				Code.put(1);
			}
			// sada na dnu niz
			// napunim niz
			int cnt = argCnt - formArgCnt;
			for (int i = cnt; i > 0; i--) {
				Code.put(Code.dup_x1);
				Code.put(Code.dup_x2);
				Code.put(Code.pop);
				Code.loadConst(i-1);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
				// ovde if od tipa
				if(obj.getType().getElemType() != Tab.charType)
					Code.put(Code.astore);
				else
					Code.put(Code.bastore);
			}
		}

		Obj obj = funcCall.getDesignator().obj;
		callMethod(obj);
		if (obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}

		inVarArgsMethod = false;
		argCnt = 0;
		formArgCnt = 0;
		varArgs.clear();
	}

	@Override
	public void visit(DesignatorStatementEmptyParen funcCall) {
		Obj obj = funcCall.getDesignator().obj;
		callMethod(funcCall.getDesignator().obj);
		if (obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}

	private void callMethod(Obj functionObj) {
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	@Override
	public void visit(SingelStatementPrintNoNumConst printStmt) {
		Code.loadConst(1);
		if (printStmt.getExpr().struct == Tab.charType)
			Code.put(Code.bprint);
		else
			Code.put(Code.print);
	}

	@Override
	public void visit(SingelStatementPrintNumConst printStmt) {
		Code.loadConst(printStmt.getValue());
		if (printStmt.getExpr().struct == Tab.charType)
			Code.put(Code.bprint);
		else
			Code.put(Code.print);
	}

	@Override
	public void visit(SingelStatementRead readStmt) {
		Obj obj = readStmt.getDesignator().obj;
		if (obj.getType() == Tab.charType)
			Code.put(Code.bread);
		else
			Code.put(Code.read);
		Code.store(obj);
	}

	public void visit(DesignatorArr designatorArr) {
		Code.load(designatorArr.getDesignator().obj);
	}

	@Override
	public void visit(AddOpTermsList addExpr) {
		if (addExpr.getAddop() instanceof Plus)
			Code.put(Code.add);
		else
			Code.put(Code.sub);
	}

	@Override
	public void visit(TermList termList) {
		if (termList.getMulop() instanceof Mul)
			Code.put(Code.mul);
		else if (termList.getMulop() instanceof Div)
			Code.put(Code.div);
		else
			Code.put(Code.rem);
	}

	@Override
	public void visit(MinusTerm minusTerm) {
		Code.put(Code.neg);
	}

	@Override
	public void visit(CondFactRelOp cond) {
		op = Code.eq;
		if (cond.getRelop() instanceof Nequ) {
			op = Code.ne;
		} else if (cond.getRelop() instanceof Les) {
			op = Code.lt;
		} else if (cond.getRelop() instanceof Lea) {
			op = Code.le;
		} else if (cond.getRelop() instanceof Gre) {
			op = Code.gt;
		} else if (cond.getRelop() instanceof Gea) {
			op = Code.ge;
		}
	}

	@Override
	public void visit(CondFactNoRelOp cond) {
		Code.loadConst(1);
		op = Code.eq;
	}

	@Override
	public void visit(SingelStatementIf e) {
		for (int adr : fixAdrAnd.pop()) {
			Code.fixup(adr);
		}
	}

	@Override
	public void visit(Else e) {
		Code.putJump(0);
		fixAdrElse.push(Code.pc - 2);

		for (int adr : fixAdrAnd.pop()) {
			Code.fixup(adr);
		}
	}

	@Override
	public void visit(SingelStatementIfElse e) {
		Code.fixup(fixAdrElse.pop());
	}

	@Override
	public void visit(And and) {
		Code.putFalseJump(op, 0);
		fixAdrAnd.peek().add(Code.pc - 2);
	}

	@Override
	public void visit(Or or) {
		if (!inDoWhile) {
			putTrueJump(op, 0);
			fixAdrOr.add(Code.pc - 2);
		} else {
			putTrueJump(op, doWhileStart.peek());
		}

		for (int adr : fixAdrAnd.pop()) {
			Code.fixup(adr);
		}
	}

	public static void putTrueJump(int op, int adr) {
		Code.put(Code.jcc + op);
		Code.put2(adr - Code.pc + 1);
	}

	@Override
	public void visit(IfConditionNoErr ifCondition) {
		Code.putFalseJump(op, 0);
		fixAdrAnd.peek().add(Code.pc - 2);

		for (int adr : fixAdrOr) {
			Code.fixup(adr);
		}
		fixAdrOr.clear();
	}

	@Override
	public void visit(SingleCondTerm cond) {
		fixAdrAnd.push(new ArrayList<>());
	}

	@Override
	public void visit(DoWhileBeggining start) {
		doWhileStart.push(Code.pc);
		fixContinue.push(new ArrayList<>());
		fixBreak.push(new ArrayList<>());
	}

	@Override
	public void visit(SingelStatementBreak br) {
		Code.putJump(0);
		fixBreak.peek().add(Code.pc - 2);
	}

	@Override
	public void visit(SingelStatementContinur cont) {
		Code.putJump(0);
		fixContinue.peek().add(Code.pc - 2);
	}

	@Override
	public void visit(DoWhileEnd start) {
		inDoWhile = true;
		for (int adr : fixContinue.pop()) {
			Code.fixup(adr);
		}
	}

	@Override
	public void visit(SingleStatementDoWhile end) {
		inDoWhile = false;
		putTrueJump(op, doWhileStart.pop());

		for (int adr : fixAdrAnd.pop()) {
			Code.fixup(adr);
		}

		for (int adr : fixBreak.pop()) {
			Code.fixup(adr);
		}
	}

	@Override
	public void visit(VarArgs varArgs) {
		varArgsMethod.add(lastVisitedMethod);

	}

	@Override
	public void visit(ActParsList a) {
		argCnt++;
	}

	@Override
	public void visit(SignleActPars a) {
		argCnt++;
	}
	
	@Override
	public void visit(BaseExpNewClass newClass) {
		Code.put(Code.new_);
		Code.put2(newClass.getType().struct.getNumberOfFields());
	}
	
	@Override
	public void visit(DesignatorDot designator) {
		Code.load(designator.getDesignator().obj);
	}
}
