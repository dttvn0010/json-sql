package demo;

import java.util.Map;
import java.util.Stack;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;

public class Eval {

	static boolean isLong(Object obj) {
		return obj instanceof Long || obj instanceof Short || obj instanceof Integer;
	}
	
	static boolean isNumber(Object obj) {
		return isLong(obj) || obj instanceof Float || obj instanceof Double;
	}
	
	
	static Object eq(Object obj1, Object obj2){
		if(obj1 instanceof String && obj2 instanceof String) {
			return obj1.equals(obj2);			
		}
		
		if(isNumber(obj1) && isNumber(obj2)) {
			double val1 = Double.valueOf(String.valueOf(obj1));
			double val2 = Double.valueOf(String.valueOf(obj2));
			return val1 == val2;
		}
		System.out.println("Cannot compare = ");
		return null;
	}
	
	static Object ne(Object obj1, Object obj2){
		if(obj1 instanceof String && obj2 instanceof String) {
			return !obj1.equals(obj2);			
		}
		if(isNumber(obj1) && isNumber(obj2)) {
			double val1 = Double.valueOf(String.valueOf(obj1));
			double val2 = Double.valueOf(String.valueOf(obj2));
			return val1 != val2;
		}
		System.out.println("Cannot compare <> ");
		return null;
	}
	
	static Object add(Object obj1, Object obj2) {
		if(obj1 instanceof String && obj2 instanceof String) {
			return (String)obj1 + (String)obj2;			
		}
		
		if(isLong(obj1) && isLong(obj2)) {
			long val1 = Long.valueOf(String.valueOf(obj1));
			long val2 = Long.valueOf(String.valueOf(obj2));
			return val1 + val2;
		}
		
		if(isNumber(obj1) && isNumber(obj2)) {
			double val1 = Double.valueOf(String.valueOf(obj1));
			double val2 = Double.valueOf(String.valueOf(obj2));
			return val1 + val2;
		}
		System.out.println("Cannot add ");
		return null;
	}
	
	static Object sub(Object obj1, Object obj2){
		if(isLong(obj1) && isLong(obj2)) {
			long val1 = Long.valueOf(String.valueOf(obj1));
			long val2 = Long.valueOf(String.valueOf(obj2));
			return val1 - val2;
		}
		
		if(isNumber(obj1) && isNumber(obj2)) {
			double val1 = Double.valueOf(String.valueOf(obj1));
			double val2 = Double.valueOf(String.valueOf(obj2));
			return val1 - val2;
		}
		System.out.println("Cannot substract ");
		return null;
	}
	
	static Object mul(Object obj1, Object obj2){
		if(isLong(obj1) && isLong(obj2)) {
			long val1 = Long.valueOf(String.valueOf(obj1));
			long val2 = Long.valueOf(String.valueOf(obj2));
			return val1 * val2;
		}
		
		if(isNumber(obj1) && isNumber(obj2)) {
			double val1 = Double.valueOf(String.valueOf(obj1));
			double val2 = Double.valueOf(String.valueOf(obj2));
			return val1 * val2;
		}
		System.out.println("Cannot multiply ");
		return null;
	}
	
	static Object div(Object obj1, Object obj2){
		if(isNumber(obj1) && isNumber(obj2)) {
			double val1 = Double.valueOf(String.valueOf(obj1));
			double val2 = Double.valueOf(String.valueOf(obj2));
			return val1 / val2;
		}
		System.out.println("Cannot divide ");
		return null;
	}
	
	static Object gt(Object obj1, Object obj2) {
		if(isNumber(obj1) && isNumber(obj2)) {
			double val1 = Double.valueOf(String.valueOf(obj1));
			double val2 = Double.valueOf(String.valueOf(obj2));
			return val1 > val2;
		}
		System.out.println("Cannot compare > ");
		return null;
	}
	
	static Object lt(Object obj1, Object obj2) {
		if(isNumber(obj1) && isNumber(obj2)) {
			double val1 = Double.valueOf(String.valueOf(obj1));
			double val2 = Double.valueOf(String.valueOf(obj2));
			return val1 < val2;
		}
		System.out.println("Cannot compare < ");
		return null;
	}
	
	static Object lte(Object obj1, Object obj2) {
		if(isNumber(obj1) && isNumber(obj2)) {
			double val1 = Double.valueOf(String.valueOf(obj1));
			double val2 = Double.valueOf(String.valueOf(obj2));
			return val1 <= val2;
		}
		System.out.println("Cannot compare <= ");
		return null;
	}
	
	static Object gte(Object obj1, Object obj2) {
		if(isNumber(obj1) && isNumber(obj2)) {
			double val1 = Double.valueOf(String.valueOf(obj1));
			double val2 = Double.valueOf(String.valueOf(obj2));
			return val1 >= val2;
		}
		System.out.println("Cannot compare >= ");
		return null;
	}
	
	static Object and(Object obj1, Object obj2) {
		if(obj1 instanceof Boolean && obj2 instanceof Boolean ) {
			return (boolean) obj1 && (boolean) obj2;
		}
		return null;
	}
	
	static Object or(Object obj1, Object obj2) {
		if(obj1 instanceof Boolean && obj2 instanceof Boolean ) {
			return (boolean) obj1 || (boolean) obj2;
		}
		return null;
	}
	
	static Object not(Object obj) {
		if(obj instanceof Boolean ) {
			return ! (boolean) obj;
		}
		return null;
	}
		
	
	static Object evaluate(Expression expr, Map<String, Object> record) throws JSQLParserException {
		final Stack<Object> stack = new Stack<Object>();

		ExpressionVisitorAdapter deparser = new ExpressionVisitorAdapter() {
		    @Override
		    public void visit(EqualsTo addition) {
				super.visit(addition); 
				
				Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(eq(obj1, obj2));
		    }
		    
		    @Override
		    public void visit(NotEqualsTo expr) {
		    	super.visit(expr);
		    	Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(ne(obj1, obj2));
		    }
		    
		    @Override
		    public void visit(GreaterThan expr) {
		    	super.visit(expr);
		    	Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(gt(obj1, obj2));
		    }
		    
		    @Override
		    public void visit(GreaterThanEquals expr) {
		    	super.visit(expr);
		    	Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(gte(obj1, obj2));
		    }
		    
		    @Override
		    public void visit(MinorThan expr) {
		    	super.visit(expr);
		    	Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(lt(obj1, obj2));
		    }
		    
		    
		    @Override
		    public void visit(MinorThanEquals expr) {
		    	super.visit(expr);
		    	Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(lte(obj1, obj2));
		    }

		    @Override
		    public void visit(AndExpression expr) {
				super.visit(expr); 
				
				Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(and(obj1, obj2));
		    }
		    
		    @Override
		    public void visit(OrExpression expr) {
		    	super.visit(expr);
		    	
		    	Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(or(obj1, obj2));
		    }
		    
		    @Override
		    public void visit(NotExpression notExpr) {
		    	super.visit(notExpr);
		    	
		    	Object obj = stack.pop();
				
				stack.push(not(obj));
		    }
		    
		    @Override
		    public void visit(Addition expr) {
		    	super.visit(expr);
		    	Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(add(obj1, obj2));
		    }
		    
		    @Override
		    public void visit(Subtraction expr) {
		    	super.visit(expr);
		    	Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(sub(obj1, obj2));
		    }
		    
		    @Override
		    public void visit(Division expr) {
		    	super.visit(expr);
		    	Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(div(obj1, obj2));
		    }
		    
		    @Override
		    public void visit(Multiplication expr) {
		    	super.visit(expr);
		    	Object obj2 = stack.pop();
				Object obj1 = stack.pop();
				
				stack.push(mul(obj1, obj2));
		    }

		    @Override
		    public void visit(LongValue val) {
				super.visit(val); 
				stack.push(val.getValue());
		    }
		    
		    @Override
		    public void visit(DoubleValue val) {
		    	super.visit(val);
		    	stack.push(val.getValue());
		    }
		    
		    @Override
		    public void visit(StringValue val) {
		    	super.visit(val);
		    	stack.push(val.getValue());
		    }
		    
		    @Override
		    public void visit(Column col) {
		    	super.visit(col);
		    	stack.push(record.get(col.getColumnName()));
		    }
		};
		expr.accept(deparser);
		
		return stack.pop();
	}
}
