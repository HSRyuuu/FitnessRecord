package com.example.fitnessrecord.global.redis.xxx;

import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class CustomSpringELParser {
  private CustomSpringELParser(){
  }

  public static Object getDynamicValue(String[] parameterNames, Object[] args, String key){
    SpelExpressionParser parser = new SpelExpressionParser();
    StandardEvaluationContext context = new StandardEvaluationContext();

    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }
    return parser.parseExpression(key).getValue(context, Object.class);
  }

}
