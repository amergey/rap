/*******************************************************************************
 * Copyright (c) 2009, 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.swt.internal.widgets.datetimekit;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.widgets.controlkit.ControlThemeAdapterImpl;
import org.eclipse.swt.widgets.DateTime;


public final class DateTimeThemeAdapter extends ControlThemeAdapterImpl {

  public int getSpinnerButtonWidth( DateTime dateTime ) {
    int upButtonWidth = getCssDimension( "DateTime-UpButton",
                                         "width",
                                         dateTime );
    int downButtonWidth = getCssDimension( "DateTime-DownButton",
                                           "width",
                                           dateTime );
    return Math.max( upButtonWidth, downButtonWidth );
  }

  public int getDropDownButtonWidth( DateTime dateTime ) {
    int dropDownButtonWidth = getCssDimension( "DateTime-DropDownButton",
                                               "width",
                                               dateTime );
    return dropDownButtonWidth;
  }

  public Rectangle getFieldPadding( DateTime dateTime ) {
    return getCssBoxDimensions( "DateTime-Field", "padding", dateTime );
  }
}
