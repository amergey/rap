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
package org.eclipse.rap.rwt.service;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;


/**
 * A theme id provider is responsible for giving the theme id to activate. The default provider
 * implementation return id configured in entry point properties.
 * <p>
 * To contribute a custom provider implementation, an implementation of this interface must be
 * provided in an {@link ApplicationConfiguration} (see
 * {@link Application#setThemeIdProvider(ThemeIdProvider)}).
 * <p>
 * For example custom provider implementation based on {@link SettingStore} could be use to
 * configure theme per users without having to change URL
 * 
 * @since 2.2
 */
public interface ThemeIdProvider {

  /**
   * Returns the id of theme to activate
   * 
   * @return the id of the theme, if <code>null</code> default theme used is
   *         {@link RWT#DEFAULT_THEME_ID}
   */
  String getThemeId();
}
