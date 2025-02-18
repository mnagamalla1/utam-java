/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root
 * or https://opensource.org/licenses/MIT
 */
package utam.core.framework.consumer;

import utam.core.element.ElementLocation;
import utam.core.framework.base.BasePageObject;
import utam.core.framework.base.PageMarker;

/**
 * used to test loader config
 */
@PageMarker.Find(css = "root")
public class TestLoaderConfigPageObjectOverride extends BasePageObject implements
    TestLoaderConfigPageObject {

  @Override
  public ElementLocation getRoot() {
    return getRootLocator();
  }

  @Override
  public Object load() {
    // nothing
    return this;
  }
}
