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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.*;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.internal.service.ContextProvider;
import org.eclipse.rap.rwt.internal.service.ServiceContext;
import org.eclipse.rap.rwt.internal.theme.ThemeManager;
import org.eclipse.rap.rwt.internal.theme.ThemeUtil;
import org.eclipse.ui.actions.CompoundContributionItem;


public class ThemeChooserContributionItem extends CompoundContributionItem {

  public ThemeChooserContributionItem() {
  }

  public ThemeChooserContributionItem( String id ) {
    super( id );
  }

  @Override
  protected IContributionItem[] getContributionItems() {
    String currentThemeId = ThemeUtil.getCurrentThemeId();
    
    ThemeManager themeManager = ContextProvider.getApplicationContext().getThemeManager();
    
    List<IContributionItem> contributionItems = new ArrayList<IContributionItem>();
    for( String availableTheme : themeManager.getRegisteredThemeIds() ) {
      ThemeChooserAction action = new ThemeChooserAction( availableTheme );
      action.setChecked( currentThemeId.equals( availableTheme ) );
      contributionItems.add( new ActionContributionItem( action ) );
    }
    
    return contributionItems.toArray(new IContributionItem[contributionItems.size()]);
  }
  
  private class ThemeChooserAction extends Action {
    public ThemeChooserAction(String text){
      super(text,IAction.AS_RADIO_BUTTON);
    }

    @Override
    public void run() {
      try {
        RWT.getSettingStore().setAttribute( ThemeUtil.CURR_THEME_ATTR, getText() );
        
        RWT.getClient().getService( JavaScriptExecutor.class ).execute( "location.reload( false );" );
      } catch( IOException e ) {
        throw new RuntimeException( e );
      }
    }
  }
}
