package com.rssoftware.ou.expression;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

public class ExpressionParser {
	private static final JexlEngine jexl;
	
	static {
		jexl = new JexlEngine();
		jexl.setCache(512);
		jexl.setStrict(true);
		jexl.setSilent(false);
	}
	
	public static Object executeExpression(String objectBindingName, Object inputObject, String expression){
	    Expression e = jexl.createExpression( expression );
	    
	    // Create a context and add data
	    JexlContext jc = new MapContext();
	    jc.set(objectBindingName, inputObject);
	    
	    // Now evaluate the expression, getting the result
	    return e.evaluate(jc);
	}
}
