/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root
 * or https://opensource.org/licenses/MIT
 */
package utam.compiler.grammar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.testng.Assert.expectThrows;
import static utam.compiler.grammar.UtamMethod.ERR_BEFORE_LOAD_HAS_NO_ARGS;
import static utam.compiler.grammar.UtamPageObject.ERR_DISALLOWED_ELEMENT;
import static utam.compiler.helpers.MethodContext.BEFORE_LOAD_METHOD_NAME;

import org.testng.annotations.Test;
import utam.compiler.helpers.TranslationContext;
import utam.compiler.representation.PageObjectValidationTestHelper;
import utam.compiler.representation.PageObjectValidationTestHelper.MethodInfo;
import utam.core.declarative.representation.PageObjectMethod;
import utam.core.framework.consumer.UtamError;

/**
 * Provides tests for UtamMethod class with beforeLoad method.
 *
 * @author igor.khorev
 */
public class UtamPageObjectBeforeLoadTests {

  private static final String METHOD_NAME = BEFORE_LOAD_METHOD_NAME;

  private static TranslationContext getContext(String filename) {
    return new DeserializerUtilities().getContext("beforeload/" + filename);
  }

  private static MethodInfo getExpectedMethod() {
    return new MethodInfo(METHOD_NAME, "Object");
  }

  @Test
  public void testBeforeLoad() {
    MethodInfo methodInfo = getExpectedMethod();
    methodInfo.addCodeLine("RootElement root0 = this.getRootElement()");
    methodInfo.addCodeLine("Boolean statement0 = root0.isPresent()");
    methodInfo.addCodeLine("root0.getText()");
    methodInfo.addCodeLine("return this");
    TranslationContext context = getContext("rootApply");
    PageObjectMethod method = context.getMethod(METHOD_NAME);
    PageObjectValidationTestHelper.validateMethod(method, methodInfo);
  }

  @Test
  public void testBeforeLoadWaitDocumentUrl() {
    MethodInfo methodInfo = getExpectedMethod();
    methodInfo.addCodeLine("this.waitFor(() -> {\n"
        + "String pstatement0 = this.getDocument().getUrl();\n"
        + "Boolean pmatcher0 = (pstatement0!= null && pstatement0.contains(\"home\"));\n"
        + "return pmatcher0;\n"
        + "})");
    methodInfo.addCodeLine("RootElement root1 = this.getRootElement()");
    methodInfo.addCodeLine("String statement1 = root1.getText()");
    methodInfo.addCodeLine("return statement1");
    TranslationContext context = getContext("chainWaitFor");
    PageObjectMethod method = context.getMethod(METHOD_NAME);
    assertThat(method.getDeclaration().getCodeLine(), is("Object load()"));
    PageObjectValidationTestHelper.validateMethod(method, methodInfo);
  }

  @Test
  public void testBeforeLoadWait() {
    MethodInfo methodInfo = getExpectedMethod();
    methodInfo.addCodeLine("this.waitFor(() -> {\n"
        + "RootElement proot0 = this.getRootElement();\n"
        + "Boolean pstatement0 = proot0.isPresent();\n"
        + "return pstatement0;\n"
        + "})");
    methodInfo.addCodeLine("return this");
    TranslationContext context = getContext("rootWaitFor");
    PageObjectValidationTestHelper.validateMethod(context.getMethod(METHOD_NAME), methodInfo);
  }

  @Test
  public void testBeforeLoadWaitNoElement() {
    MethodInfo methodInfo = getExpectedMethod();
    methodInfo.addCodeLine("this.waitFor(() -> {\n"
        + "RootElement proot0 = this.getRootElement();\n"
        + "Boolean pstatement0 = proot0.isPresent();\n"
        + "return pstatement0;\n"
        + "})");
    methodInfo.addCodeLine("return this");
    TranslationContext context = getContext("rootWaitForNoElement");
    PageObjectValidationTestHelper.validateMethod(context.getMethod(METHOD_NAME), methodInfo);
  }

  @Test
  public void testCantHaveArgs() {
    UtamError e = expectThrows(UtamError.class,
        () -> new DeserializerUtilities().getContext("validate/beforeload/withArgs"));
    assertThat(e.getMessage(), containsString(ERR_BEFORE_LOAD_HAS_NO_ARGS));
  }

  @Test
  public void testUnallowedElement() {
    UtamError e = expectThrows(UtamError.class,
        () -> new DeserializerUtilities().getContext("validate/beforeload/wrongElement"));
    assertThat(e.getMessage(), containsString(ERR_DISALLOWED_ELEMENT));
  }
}
