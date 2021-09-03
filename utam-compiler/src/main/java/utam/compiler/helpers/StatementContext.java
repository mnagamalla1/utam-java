/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root
 * or https://opensource.org/licenses/MIT
 */
package utam.compiler.helpers;

import static utam.compiler.helpers.StatementContext.StatementType.PREDICATE_LAST_STATEMENT;
import static utam.compiler.helpers.StatementContext.StatementType.PREDICATE_STATEMENT;
import static utam.compiler.helpers.StatementContext.StatementType.REGULAR_STATEMENT;

import java.util.HashMap;
import java.util.Map;
import utam.compiler.grammar.UtamMethodAction.ConstOperand;
import utam.compiler.helpers.TypeUtilities.ListOf;
import utam.compiler.representation.ComposeMethodStatement.Operand;
import utam.core.declarative.representation.MethodParameter;
import utam.core.declarative.representation.TypeProvider;

/**
 * helper for statements processing
 *
 * @author elizaveta.ivanova
 * @since 236
 */
public class StatementContext {

  // changing this affects unit tests results!!!
  private static final String STATEMENT_VARIABLE_PREFIX = "statement";
  private static final String MATCHER_VARIABLE_PREFIX = "matcher";

  private final StatementType statementType;
  private final ReturnType declaredStatementReturn;
  // accumulates statement args
  private final Map<String, MethodParameter> statementArgs = new HashMap<>();
  // statement index in the compose method, starting from 0
  private final int statementIndex;
  private final TypeProvider previousStatementReturn;

  public StatementContext(
      TypeProvider previousStatementReturn,
      int statementIndex,
      StatementType statementType,
      ReturnType declaredStatementReturn) {
    this.statementIndex = statementIndex;
    this.statementType = statementType;
    this.declaredStatementReturn = declaredStatementReturn;
    this.previousStatementReturn = previousStatementReturn;
  }

  // for testing
  StatementContext() {
    this(null, 0, REGULAR_STATEMENT, new ReturnType("dummy"));
  }

  public Operand getChainOperand() {
    String operandString = STATEMENT_VARIABLE_PREFIX + (statementIndex - 1);
    boolean isList = previousStatementReturn instanceof ListOf;
    return new ConstOperand(operandString, isList);
  }

  public TypeProvider getPreviousStatementReturnType() {
    return previousStatementReturn;
  }

  public String getVariableName() {
    return getPredicateVariablePrefix() + STATEMENT_VARIABLE_PREFIX + statementIndex;
  }

  public String getMatcherVariableName() {
    return getPredicateVariablePrefix() + MATCHER_VARIABLE_PREFIX + statementIndex;
  }

  private String getPredicateVariablePrefix() {
    return isInsidePredicate()?"p" : "";
  }

  public boolean isInsidePredicate() {
    return statementType == PREDICATE_STATEMENT || statementType == PREDICATE_LAST_STATEMENT;
  }

  public String getElementVariableName(String elementName) {
    return getPredicateVariablePrefix() + elementName + statementIndex;
  }

  public TypeProvider getDeclaredStatementReturnOrNull(TranslationContext context) {
    return declaredStatementReturn.getReturnTypeOrNull(context);
  }

  public boolean isReturnSelf() {
    return declaredStatementReturn.isReturnSelf();
  }

  public boolean hasDeclaredReturn() {
    return declaredStatementReturn.isReturnTypeSet();
  }

  public TypeProvider getDeclaredReturnOrDefault(TranslationContext context,
      ReturnType declaredMethodReturn,
      TypeProvider defaultReturnType) {
    TypeProvider declaredStatementReturn = this.declaredStatementReturn
        .getReturnTypeOrNull(context);
    if (declaredStatementReturn != null) {
      return declaredStatementReturn;
    }
    if (statementType == StatementType.LAST_STATEMENT) {
      return declaredMethodReturn.getReturnTypeOrDefault(context, defaultReturnType);
    }
    return defaultReturnType;
  }

  Map<String, MethodParameter> getStatementArgsMap() {
    return statementArgs;
  }

  public boolean isLastPredicateStatement() {
    return statementType == PREDICATE_LAST_STATEMENT;
  }

  public boolean isLastStatement() {
    return statementType == StatementType.LAST_STATEMENT;
  }

  public boolean isFirstStatement() {
    return statementIndex == 0;
  }

  public boolean isFlatMap() {
    return previousStatementReturn instanceof ListOf
        && this.declaredStatementReturn.isReturnAllSet();
  }

  public enum StatementType {
    REGULAR_STATEMENT,
    LAST_STATEMENT,
    PREDICATE_STATEMENT,
    PREDICATE_LAST_STATEMENT
  }
}
