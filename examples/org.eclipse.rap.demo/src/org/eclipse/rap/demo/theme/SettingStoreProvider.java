/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.demo.theme;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.theme.ThemeUtil;
import org.eclipse.rap.rwt.service.DefaultThemeIdProvider;
import org.eclipse.rap.rwt.service.SettingStore;


public class SettingStoreProvider extends DefaultThemeIdProvider {

  public SettingStoreProvider() {
  }

  public String getThemeId() {
    SettingStore store = RWT.getSettingStore();
    
    String configuredThemeId = store.getAttribute( ThemeUtil.CURR_THEME_ATTR );
    if(configuredThemeId == null){
      configuredThemeId = super.getThemeId();
    } 
    
    return configuredThemeId;
  }
}
