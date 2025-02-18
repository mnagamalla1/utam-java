/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root
 * or https://opensource.org/licenses/MIT
 */
package utam.core.framework.consumer;

import java.util.function.Supplier;
import org.openqa.selenium.SearchContext;

/**
 * can act as UTAM PO container, only supported for Selenium!
 *
 * @author elizaveta.ivanova
 * @since 228
 */
public interface Container {

  Supplier<SearchContext> getScope();
}
