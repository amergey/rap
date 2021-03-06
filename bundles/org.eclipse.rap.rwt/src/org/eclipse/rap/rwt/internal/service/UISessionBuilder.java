/*******************************************************************************
 * Copyright (c) 2012, 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.rwt.internal.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.SingletonManager;
import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.theme.ThemeUtil;


public class UISessionBuilder {

  private final ServiceContext serviceContext;
  private final UISessionImpl uiSession;

  public UISessionBuilder( ServiceContext serviceContext ) {
    this.serviceContext = serviceContext;
    ApplicationContextImpl applicationContext = serviceContext.getApplicationContext();
    HttpServletRequest request = serviceContext.getRequest();
    HttpSession httpSession = request.getSession( true );
    String connectionId = request.getParameter( UrlParameters.PARAM_CONNECTION_ID );
    uiSession = new UISessionImpl( applicationContext, httpSession, connectionId );
  }

  public UISessionImpl buildUISession() {
    uiSession.attachToHttpSession();
    serviceContext.setUISession( uiSession );
    SingletonManager.install( uiSession );
    setCurrentTheme();
    selectClient();
    return uiSession;
  }

  private void setCurrentTheme() {
    ApplicationContextImpl applicationContext = uiSession.getApplicationContext();
    String themeId = applicationContext.getThemeIdProvider().getThemeId();
    if( themeId != null && themeId.length() > 0 ) {
      verifyThemeId( themeId );
      ThemeUtil.setCurrentThemeId( uiSession, themeId );
    } else {
      ThemeUtil.setCurrentThemeId( uiSession, RWT.DEFAULT_THEME_ID );
    }
  }

  private void selectClient() {
    ApplicationContextImpl applicationContext = uiSession.getApplicationContext();
    applicationContext.getClientSelector().selectClient( serviceContext.getRequest(), uiSession );
  }

  private void verifyThemeId( String themeId ) {
    ApplicationContextImpl applicationContext = uiSession.getApplicationContext();
    if( !applicationContext.getThemeManager().hasTheme( themeId ) ) {
      throw new IllegalArgumentException( "Illegal theme id: " + themeId );
    }
  }
}
