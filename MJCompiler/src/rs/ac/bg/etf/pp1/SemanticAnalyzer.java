package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected = false;
	Obj currentMethod = null;
	boolean returnFound = false; 
	int nVars;
	Struct lastVisitedType;
	boolean inDoWhile = false;
	int formParamCount = 0;
	List<Struct> exprList = new ArrayList<>();
	List<Obj> varArgsMethod = new ArrayList<>();
	boolean inRecord = false;

	public static final Struct boolType = new Struct(Struct.Bool);

	Logger log = Logger.getLogger(getClass());

	public SemanticAnalyzer() {
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
	}

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit(RecordDecl recordDecl) {
		Tab.chainLocalSymbols(recordDecl.getRecordName().obj.getType());
		Tab.closeScope();
		inRecord = false;
	}
	
	public void visit(RecordName recordName) {
		if (Tab.currentScope.findSymbol(recordName.getName()) == null) {
			recordName.obj = Tab.insert(Obj.Type, recordName.getName(), new Struct(Struct.Class));
		} else {
			recordName.obj = Tab.noObj;
			report_error("Simbol sa imenom " + recordName.getName() + " vec postoji u trenutnom opsegu", recordName);
		}
		Tab.openScope();
		
		inRecord = true;
	}

	public void visit(Program program) {
		Obj mainMeth = Tab.currentScope.findSymbol("main");
		if (mainMeth != null) {
			if (mainMeth.getKind() != Obj.Meth) {
				report_error("Main nije metoda.", program);
			} else {
				if (mainMeth.getType() != Tab.noType) {
					report_error("Main metoda nije tipa void.", program);
				}
				if (mainMeth.getLevel() != 0) {
					report_error("Main metoda ima argumente.", program);
				}
			}
		} else {
			report_error("U programu ne postoji main simbol.", program);
		}

		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(ProgName progName) {
		if (Tab.currentScope.findSymbol(progName.getName()) == null) {
			progName.obj = Tab.insert(Obj.Prog, progName.getName(), Tab.noType);
		} else {
			progName.obj = Tab.noObj;
			report_error("Simbol sa imenom " + progName.getName() + " vec postoji u trenutnom opsegu", progName);
		}
		Tab.openScope();
	}

	public void visit(Type type) {
		Obj typeObj = Tab.find(type.getTypeName());
		if (typeObj == Tab.noObj) {
			report_error("Nije pronadjen simbol sa imenom " + type.getTypeName() + " u tabeli simbola", type);
			type.struct = Tab.noType;
		} else {
			if (Obj.Type == typeObj.getKind()) {
				type.struct = typeObj.getType();
				if(type.struct.getKind() == Struct.Class) {
					DumpSymbolTableVisitor v = new DumpSymbolTableVisitor();
					v.visitObjNode(typeObj);
					report_info("Detektovano koriscenje rekorda "+v.getOutput(), type);
				}
			} else {
				report_error("Simbol sa imenom " + type.getTypeName() + " ne predstavlja tip", type);
				type.struct = Tab.noType;
			}
		}
		lastVisitedType = type.struct;
	}

	public void visit(MethodDecl methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Funkcija " + currentMethod.getName() + " nema return iskaz, a nije void!", null);
		}

		Tab.chainLocalSymbols(currentMethod);

		Tab.closeScope();

		int nPars = 0;
		for (Obj obj : currentMethod.getLocalSymbols()) {
			if (obj.getFpPos() > 0)
				nPars++;
		}
		currentMethod.setLevel(nPars);

		returnFound = false;
		currentMethod = null;
	}

	public void visit(TypeMethod methodTypeName) {
		if (Tab.currentScope.findSymbol(methodTypeName.getName()) == null) {
			currentMethod = Tab.insert(Obj.Meth, methodTypeName.getName(), lastVisitedType);
			methodTypeName.obj = currentMethod;
		} else {
			currentMethod = Tab.noObj;
			report_error("Simbol sa imenom " + methodTypeName.getName() + " vec postoji u trenutnom opsegu",
					methodTypeName);
		}
		Tab.openScope();
	}

	public void visit(VoidMethod voidMethodName) {
		if (Tab.currentScope.findSymbol(voidMethodName.getName()) == null) {
			currentMethod = Tab.insert(Obj.Meth, voidMethodName.getName(), Tab.noType);
			voidMethodName.obj = currentMethod;
		} else {
			currentMethod = Tab.noObj;
			report_error("Simbol sa imenom " + voidMethodName.getName() + " vec postoji u trenutnom opsegu",
					voidMethodName);
		}
		Tab.openScope();
	}

	public void visit(ConstAssignNum constAssignNum) {
		if (Tab.currentScope.findSymbol(constAssignNum.getName()) == null) {
			Tab.insert(Obj.Con, constAssignNum.getName(), lastVisitedType).setAdr(constAssignNum.getValue());
		} else {
			report_error("Simbol sa imenom " + constAssignNum.getName() + " vec postoji u trenutnom opsegu",
					constAssignNum);
		}
		if (lastVisitedType != Tab.intType) {
			report_error("Nekopatibilni tipovi u dodeli konstanti", constAssignNum);
	 	}

	}

	public void visit(ConstAssignBool constAssignBool) {
		if (Tab.currentScope.findSymbol(constAssignBool.getName()) == null) {
			Tab.insert(Obj.Con, constAssignBool.getName(), lastVisitedType).setAdr(constAssignBool.getValue() ? 1 : 0);
		} else {
			report_error("Simbol sa imenom " + constAssignBool.getName() + " vec postoji u trenutnom opsegu",
					constAssignBool);
		}

		if (lastVisitedType != boolType) {
			report_error("Nekopatibilni tipovi u dodeli konstanti", constAssignBool);
		}
	}

	public void visit(ConstAssignChar constAssignChar) {
		if (Tab.currentScope.findSymbol(constAssignChar.getName()) == null) {
			Tab.insert(Obj.Con, constAssignChar.getName(), lastVisitedType).setAdr(constAssignChar.getValue());
		} else {
			report_error("Simbol sa imenom " + constAssignChar.getName() + " vec postoji u trenutnom opsegu",
					constAssignChar);
		}
		if (lastVisitedType != Tab.charType) {
			report_error("Nekopatibilni tipovi u dodeli konstanti", constAssignChar);
		}
	}

	public void visit(VarAssign varAssign) {
		if (Tab.currentScope.findSymbol(varAssign.getName()) == null) {
			Tab.insert(inRecord ? Obj.Fld : Obj.Var, varAssign.getName(),
					varAssign.getArray() instanceof IsArray ? new Struct(Struct.Array, lastVisitedType)
							: lastVisitedType);
		} else {
			report_error("Simbol sa imenom " + varAssign.getName() + " vec postoji u trenutnom opsegu", varAssign);
		}
	}

	public void visit(SingelStatementReturnArg returnExpr) {
		returnFound = true;
		Struct currMethType = currentMethod.getType();
		if (!returnExpr.getExpr().struct.assignableTo(currMethType)) {
			report_error("Tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije "
					+ currentMethod.getName(), returnExpr);
		}
	}

//	public void visit(ProcCall procCall){
//		Obj func = procCall.getDesignator().obj;
//		if (Obj.Meth == func.getKind()) { 
//			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + procCall.getLine(), null);
//			//RESULT = func.getType();
//		} 
//		else {
//			report_error("Greska na liniji " + procCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
//			//RESULT = Tab.noType;
//		}     	
//	}    

	public void visit(BaseExpDesignator baseExpDesignator) {
		baseExpDesignator.struct = baseExpDesignator.getDesignator().obj.getType();
	}

	public void visit(BaseExpNum baseExpNum) {
		baseExpNum.struct = Tab.intType;
	}

	public void visit(BaseExpChar baseExpChar) {
		baseExpChar.struct = Tab.charType;
	}

	public void visit(BaseExpBool baseExpBool) {
		baseExpBool.struct = boolType;
	}

	public void visit(BaseExpNewArr baseExpNewArr) {
		baseExpNewArr.struct = new Struct(Struct.Array, lastVisitedType);
		if (baseExpNewArr.getExpr().struct != Tab.intType) {
			report_error("Dimenzija niza nije tipa int.", baseExpNewArr);
		}
	}

	public void visit(BaseExpExpr baseExpExpr) {
		baseExpExpr.struct = baseExpExpr.getExpr().struct;
	}

	public void visit(FactorList factorList) {
		Struct t1 = factorList.getFactor().struct;
		Struct t2 = factorList.getBaseExp().struct;
		if (t1.equals(t2) && t1 == Tab.intType)
			factorList.struct = t1;
		else {
			report_error("Nekompatibilni tipovi u izrazu uz stepenovanje.", factorList);
			factorList.struct = Tab.noType;
		}
	}

	public void visit(SingleBaseExp singleBaseExp) {
		singleBaseExp.struct = singleBaseExp.getBaseExp().struct;
	}

	public void visit(TermList termList) {
		Struct t1 = termList.getFactor().struct;
		Struct t2 = termList.getTerm().struct;
		if (t1.equals(t2) && t1 == Tab.intType)
			termList.struct = t1;
		else {
			report_error("Nekompatibilni tipovi u izrazu uz Mulop.", termList);
			termList.struct = Tab.noType;
		}
	}

	public void visit(SingleFactor singleFactor) {
		singleFactor.struct = singleFactor.getFactor().struct;
	}

	public void visit(AddOpTermsList addOpTermsList) {
		Struct t1 = addOpTermsList.getExpr().struct;
		Struct t2 = addOpTermsList.getTerm().struct;
		if (t1.equals(t2) && t1 == Tab.intType)
			addOpTermsList.struct = t1;
		else {
			addOpTermsList.struct = Tab.noType;
			report_error("Nekompatibilni tipovi u izrazu uz Addop.", addOpTermsList);
		}
	}

	public void visit(MinusTerm minusTerm) {
		Struct t = minusTerm.getTerm().struct;
		if (t == Tab.intType) {
			minusTerm.struct = t;
		} else {
			minusTerm.struct = Tab.noType;
			report_error("Nekompatibilni tipovi u izrazu uz unarni minus.", minusTerm);
		}
	}

	public void visit(SingleTerm singleTerm) {
		singleTerm.struct = singleTerm.getTerm().struct;
	}

	public void visit(SingelStatementRetrunNoArg returnNoArg) {
		if (currentMethod.getType() != Tab.noType) {
			report_error("Return statment-u nedostaje argument.", returnNoArg);
		}
	}

	public void visit(SingelStatementPrintNumConst print) {
		Struct t = print.getExpr().struct;
		if (t != boolType && t != Tab.intType && t != Tab.charType) {
			report_error("Expr u print statment-u je nedozvoljenog tipa.", print);
		}
	}

	public void visit(DesignatorStatementAssignop assignop) {
		int k = assignop.getDesignator().obj.getKind();
		if (k != Obj.Var && k != Obj.Elem && k != Obj.Fld) {
			report_error(
					"Designator kome se dodeljuje vrednost nije promenljiva, element niza ili polje unutar objekta.",
					assignop);
		}
		Struct t = assignop.getExpr().struct;
		if (!t.assignableTo(assignop.getDesignator().obj.getType())) {
			report_error("Tip Expr-a nije kopatibila pri dodeli sa tipom Designator-a", assignop);
		}

	}

	public void visit(DesignatorStatementInc inc) {
		Struct t = inc.getDesignator().obj.getType();
		if (t != Tab.intType) {
			report_error("Designator uz inkrement nije tipa int.", inc);
		}
		int k = inc.getDesignator().obj.getKind();
		if (k != Obj.Var && k != Obj.Elem && k != Obj.Fld) {
			report_error("Designator uz inkrement nije promenljiva, element niza ili polje unutar objekta.", inc);
		}
	}

	public void visit(DesignatorStatementDec dec) {
		Struct t = dec.getDesignator().obj.getType();
		if (t != Tab.intType) {
			report_error("Designator uz inkrement nije tipa int.", dec);
		}
		int k = dec.getDesignator().obj.getKind();
		if (k != Obj.Var && k != Obj.Elem && k != Obj.Fld) {
			report_error("Designator uz inkrement nije promenljiva, element niza ili polje unutar objekta.", dec);
		}
	}

	public void visit(SingelStatementPrintNoNumConst print) {
		Struct t = print.getExpr().struct;
		if (t != boolType && t != Tab.intType && t != Tab.charType) {
			report_error("Expr u print statment-u je nedozvoljenog tipa.", print);
		}
	}

	public void visit(SingelStatementRead read) {
		Struct t = read.getDesignator().obj.getType();
		if (t != boolType && t != Tab.intType && t != Tab.charType) {
			report_error("Designator u read statementu nije tipa int, char, bool.", read);
		}
		int k = read.getDesignator().obj.getKind();
		if (k != Obj.Var && k != Obj.Elem && k != Obj.Fld) {
			report_error("Designator u read statementu nije promenljiva, element niza ili polje unutar objekta.", read);
		}
	}

//	public void visit(FuncCall funcCall){
//		Obj func = funcCall.getDesignator().obj;
//		if (Obj.Meth == func.getKind()) { 
//			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
//			funcCall.struct = func.getType();
//		} 
//		else {
//			report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
//			funcCall.struct = Tab.noType;
//		}
//
//	}

	public void visit(DesignatorIdent designatorIdent) {
		Obj obj = Tab.find(designatorIdent.getName());
		if (obj == Tab.noObj) {
			report_error("Simbol sa imenom " + designatorIdent.getName() + " nije deklarisan!", designatorIdent);
		} else {
			DumpSymbolTableVisitor v = new DumpSymbolTableVisitor();
			v.visitObjNode(obj);
			if(obj.getKind() == Obj.Var) {
				if(obj.getLevel() == 0) {
					report_info("Detektovano koriscenje globalne promenjive "+v.getOutput(), designatorIdent);
				}
				else if(obj.getFpPos() > 0) {
					report_info("Detektovano koriscenje formalni parametar "+v.getOutput(), designatorIdent);

				} else {
					report_info("Detektovano koriscenje lokalne promenjive "+v.getOutput(), designatorIdent);

				}
			}
			else if (obj.getKind() == Obj.Meth) {
				report_info("Detektovan poziv metode "+v.getOutput(), designatorIdent);

			}
		}
		designatorIdent.obj = obj;
	}

	public void visit(DesignatorExpr designatorExpr) {
		Obj arrayObj = designatorExpr.getDesignatorArr().getDesignator().obj;
		designatorExpr.obj = new Obj(Obj.Elem, "elem", arrayObj.getType().getElemType());
		boolean b = false;
		if (arrayObj.getType().getKind() != Struct.Array) {
			b = true;
			report_error("Pristupa se indeksiranje designator-u koji nije niz.", designatorExpr);
		}
		if (designatorExpr.getExpr().struct != Tab.intType) {
			b = true;
			report_error("Index niza nije tipa int.", designatorExpr);
		}
		if(!b) {
			DumpSymbolTableVisitor v = new DumpSymbolTableVisitor();
			v.visitObjNode(arrayObj);
			report_info("Detektovano pristup elementu niza "+v.getOutput(), designatorExpr);

		}
	}

	public void visit(DoWhileBeggining start) {
		inDoWhile = true;
	}

	public void visit(DoWhileEnd end) {
		inDoWhile = false;
	}

	public void visit(SingelStatementBreak StatmentBreak) {
		if (inDoWhile == false)
			report_error("Break statment moze da se nadje samo unutar do-while petlje.", StatmentBreak);
	}

	public void visit(SingelStatementContinur StatmentContinue) {
		if (inDoWhile == false)
			report_error("Continue statment moze da se nadje samo unutar do-while petlje.", StatmentContinue);
	}

	public void visit(CondFactRelOp cond) {
		Struct t1 = cond.getExpr().struct;
		Struct t2 = cond.getExpr1().struct;
		if (!t1.compatibleWith(t2)) {
			report_error("Nekompatibilni tipovi u izrazu uz RelOp.", cond);
		} else if (t1.getKind() == Struct.Array
				&& !(cond.getRelop() instanceof Equ || cond.getRelop() instanceof Nequ)) {
			report_error("Uz promenljive tipa niza, od relacionih operatora, mogu se koristiti samo != i ==.", cond);
		} else if (t1.getKind() == Struct.Class
				&& !(cond.getRelop() instanceof Equ || cond.getRelop() instanceof Nequ)) {
			report_error("Uz promenljive tipa klase, od relacionih operatora, mogu se koristiti samo != i ==.", cond);
		}
	}

	public void visit(CondFactNoRelOp cond) {
		Struct t = cond.getExpr().struct;
		if (!t.equals(boolType)) {
			report_error("Nije bool", cond);
		}
	}

	public void visit(ActParsList a) {
		exprList.add(a.getExpr().struct);
	}

	public void visit(SignleActPars a) {
		exprList.add(a.getExpr().struct);
	}

	public void visit(DesignatorStatementActPars designator) {
		Obj obj = designator.getDesignator().obj;
		if (obj.getKind() != Obj.Meth) {
			report_error("Designator mora označavati globalnu funkciju glavnog programa.", designator);
		}

		int maxLevel = -1;
		Collection<Obj> formalPars = obj.getLocalSymbols();

		if (!varArgsMethod.contains(obj)) {
			if (obj.getLevel() == exprList.size()) {
				for (Obj iter : formalPars) {
					if (iter.getAdr() < exprList.size()) {
						if (!exprList.get(iter.getAdr()).assignableTo(iter.getType())) {
							report_error(
									"Tip svakog stvarnog argumenta mora biti kompatibilan pri dodeli sa tipom svakog formalnog \r\n"
											+ "argumenta na odgovarajućoj poziciji.",
									designator);
						}
					}
				}
			} else {
				report_error("Broj formalnih i stvarnih argumenata metode nije isti!" + obj.getLevel() + " i "
						+ exprList.size(), designator);
			}
		}
		// poslednji formalni argument je niz
		else {
			// razlika u stvarnih i formalnih
			Obj varParArr = null;
			int test = formalPars.size();
			int diff = exprList.size() - obj.getLevel() + 1;
			// postoji bar jedan vise
			if (diff > -1) {
				// uzmi poslednjeg kao referenti
				for (Obj iter : formalPars) {
					if (iter.getAdr() == obj.getLevel() - 1) {
						varParArr = iter;
						break;
					}
				}

				for (Obj iter : formalPars) {
					// proveri sve formalne
					if (iter.getAdr() < obj.getLevel() - 1) {
						if (!exprList.get(iter.getAdr()).assignableTo(iter.getType())) {
							report_error(
									"Tip svakog stvarnog argumenta mora biti kompatibilan pri dodeli sa tipom svakog formalnog \r\n"
											+ "argumenta na odgovarajućoj poziciji.",
									designator);
						}
					}
				}
//					proveri ostale argumente
				for (int i = exprList.size() - diff; i < exprList.size(); i++) {
					if (!exprList.get(i).assignableTo(varParArr.getType().getElemType())) {
						report_error(
								"Tip argumenta koji ide u promenjive argumente mora biti kompatibilian pri dodeli tipa sa formalnim parametrom",
								designator);
					}
				}

			} else {
				report_error("Broj formalnih i stvarnih argumenata metode nije isti!" + obj.getLevel() + " i "
						+ exprList.size(), designator);
			}
		}

		exprList.clear();
	}

	public void visit(DesignatorStatementEmptyParen designator) {
		Obj obj = designator.getDesignator().obj;
		if (obj.getKind() != Obj.Meth) {
			report_error("Designator mora označavati globalnu funkciju glavnog programa.", designator);
		}

		if (obj.getLevel() != exprList.size()) {
			report_error("Argument u funkciji koja ne ocekuje ni jedna argument.", designator);
		}
	}

	public void visit(BaseExpDesignatorActPars baseDesignator) {
		Obj obj = baseDesignator.getDesignator().obj;
		baseDesignator.struct = obj.getType();
		if (obj.getKind() != Obj.Meth) {
			report_error("Designator mora označavati globalnu funkciju glavnog programa.", baseDesignator);
		}

		int maxLevel = -1;
		Collection<Obj> formalPars = obj.getLocalSymbols();

		if (!varArgsMethod.contains(obj)) {
			if (obj.getLevel() == exprList.size()) {
				for (Obj iter : formalPars) {
					if (iter.getAdr() < exprList.size()) {
						if (!exprList.get(iter.getAdr()).assignableTo(iter.getType())) {
							report_error(
									"Tip svakog stvarnog argumenta mora biti kompatibilan pri dodeli sa tipom svakog formalnog \r\n"
											+ "argumenta na odgovarajućoj poziciji.",
									baseDesignator);
						}
					}
				}
			} else {
				report_error("Broj formalnih i stvarnih argumenata metode nije isti!" + obj.getLevel() + " i "
						+ exprList.size(), baseDesignator);
			}
		}
		// poslednji formalni argument je niz
		else {
			// razlika u stvarnih i formalnih
			Obj varParArr = null;
			int test = formalPars.size();
			int diff = exprList.size() - obj.getLevel();
			// postoji bar jedan vise
			if (diff > -1) {
				// uzmi poslednjeg kao referenti
				for (Obj iter : formalPars) {
					if (iter.getAdr() == obj.getLevel()-1) {
						varParArr = iter;
						break;
					}
				}

				for (Obj iter : formalPars) {
					// proveri sve formalne
					if (iter.getAdr() < obj.getLevel() - 1) {
						if (!exprList.get(iter.getAdr()).assignableTo(iter.getType())) {
							report_error(
									"Tip svakog stvarnog argumenta mora biti kompatibilan pri dodeli sa tipom svakog formalnog \r\n"
											+ "argumenta na odgovarajućoj poziciji.",
									baseDesignator);
						}
					}
				}
//					proveri ostale argumente
				for (int i = exprList.size() - diff; i < exprList.size(); i++) {
					if (!exprList.get(i).assignableTo(varParArr.getType().getElemType())) {
						report_error(
								"Tip argumenta koji ide u promenjive argumente mora biti kompatibilian pri dodeli tipa sa formalnim parametrom",
								baseDesignator);
					}
				}

			} else {
				report_error("Broj formalnih i stvarnih argumenata metode nije isti!" + obj.getLevel() + " i "
						+ exprList.size(), baseDesignator);
			}
		}

		exprList.clear();
	}

	public void visit(BaseExpDesignatorNoActPars baseDesignator) {
		Obj obj = baseDesignator.getDesignator().obj;
		baseDesignator.struct = obj.getType();
		if (obj.getKind() != Obj.Meth) {
			report_error("Designator mora označavati globalnu funkciju glavnog programa.", baseDesignator);
		}
	}

	public void visit(VarArgs varArgs) {
		Tab.insert(Obj.Var, varArgs.getName(), new Struct(Struct.Array, lastVisitedType)).setFpPos(Tab.currentScope.getnVars());
		varArgsMethod.add(currentMethod);
	}

	public void visit(FormPar formPar) {
		Tab.insert(Obj.Var, formPar.getName(),
				formPar.getArray() instanceof IsArray ? new Struct(Struct.Array, lastVisitedType) : lastVisitedType)
				.setFpPos(Tab.currentScope.getnVars());
	}

	
	public void visit(IfConditionErr err) {
		report_info("Uspesan oporavak od sintaksne greske u uslovu if-a do )", err);
	}
	
	public void visit(DesignatorStatementAssignopErr err) {
		report_info("Uspesan oporavak od sintaksne greske pri dodeli vrednosti do ;", err);
	}
	
	public void visit(BaseExpNewClass newClass) {
		if(newClass.getType().struct.getKind() != Struct.Class) {
			report_error("Sa new samo moze instancirati record", newClass);
		}
		newClass.struct = newClass.getType().struct;
	}
	
	public void visit(DesignatorDot designator) {
		designator.obj = designator.getDesignator().obj.getType().getMembersTable().searchKey(designator.getName());
		if(designator.obj == null) {
			report_error("Ne postoji polje "+ designator.getName() + " u rekordu", designator);
		}
	}
	
	public boolean passed() {
		return !errorDetected;
	}
}
