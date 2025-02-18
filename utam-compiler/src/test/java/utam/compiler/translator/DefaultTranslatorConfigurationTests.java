/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root
 * or https://opensource.org/licenses/MIT
 */
package utam.compiler.translator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;
import org.testng.annotations.Test;
import utam.core.declarative.translator.GuardrailsMode;
import utam.core.declarative.translator.ProfileConfiguration;
import utam.core.declarative.translator.TranslationTypesConfig;
import utam.core.declarative.translator.TranslatorConfig;
import utam.core.declarative.translator.TranslatorSourceConfig;
import utam.core.declarative.translator.TranslatorTargetConfig;

/**
 * @author elizaveta.ivanova
 * @since 228
 */
public class DefaultTranslatorConfigurationTests {

  @Test
  public void testConstructor() {
    TranslatorTargetConfig targetConfig = new DefaultTargetConfigurationTests.Mock();
    TranslatorSourceConfig sourceConfig = new DefaultSourceConfigurationTests.Mock();
    TranslationTypesConfig typesConfig = new TranslationTypesConfigJava();
    ProfileConfiguration profileConfiguration = new StringValueProfileConfig("name", "value");
    TranslatorConfig config = new DefaultTranslatorConfiguration(
        "myModule",
        GuardrailsMode.ERROR,
        typesConfig,
        sourceConfig,
        targetConfig,
        Collections.singletonList(profileConfiguration)
    );
    assertThat(config, is(notNullValue()));
    assertThat(config.getConfiguredTarget(), is(sameInstance(targetConfig)));
    assertThat(config.getConfiguredSource(), is(sameInstance(sourceConfig)));
    assertThat(config.getModuleName(), is(equalTo("myModule")));
    assertThat(config.getTranslationTypesConfig(), is(sameInstance(typesConfig)));
    assertThat(config.getConfiguredProfiles().iterator().next(), is(equalTo(profileConfiguration)));
  }
}
