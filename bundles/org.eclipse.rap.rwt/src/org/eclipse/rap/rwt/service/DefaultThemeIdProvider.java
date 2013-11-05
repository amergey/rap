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

import java.util.Map;

import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.lifecycle.EntryPointManager;
import org.eclipse.rap.rwt.internal.lifecycle.EntryPointRegistration;
import org.eclipse.rap.rwt.internal.service.ContextProvider;

/**
 * Default theme id provider implementation retrieving theme id from entry point properties
 */
public class DefaultThemeIdProvider implements ThemeIdProvider {

  public String getThemeId() {
    ApplicationContextImpl applicationContext = ContextProvider.getApplicationContext();
    EntryPointManager entryPointManager = applicationContext.getEntryPointManager();
    String servletPath = ContextProvider.getRequest().getServletPath();
    EntryPointRegistration registration = entryPointManager.getRegistrationByPath( servletPath );
    if( registration != null ) {
      Map<String, String> properties = registration.getProperties();
      return properties.get( WebClient.THEME_ID );
    }
    return null;
  }
}
