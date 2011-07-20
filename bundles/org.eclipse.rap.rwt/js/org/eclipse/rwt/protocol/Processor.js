/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

namespace( "org.eclipse.rwt.protocol" );
 
org.eclipse.rwt.protocol.Processor = {

  processMessage : function( messageObject ) {
    // NOTE : Temporary implementation, as this function should parse json-text directly later
    var operations = messageObject.operations;
    for( var i = 0; i < operations.length; i++ ) {
      this.processOperation( operations[ i ] );
    }
  },

  processOperation : function( operation ) {
    try {
      switch( operation.action ) {
        case "create":
          this._processCreate( operation.target, operation.type, operation.properties );
        break; 
        case "destroy":
          this._processDestroy( operation.target );
        break; 
        case "set":
          this._processSet( operation.target, operation.properties );
        break; 
        case "call":
          this._processCall( operation.target, operation.method, operation.properties );
        break; 
        case "listen":
          this._processListen( operation.target, operation.properties );
        break; 
        case "execute":
          this._processExecute( operation.target, operation.scriptType, operation.content );
        break; 
      }
    } catch( ex ) {
      this._processError( ex, operation );
    }
  },

  // TODO [tb] : move  
  addStatesForStyles : function( targetOject, styleMap ) {
    for( key in styleMap ) {
      targetOject.addState( "rwt_" + key );
    }
  },
  
  ////////////
  // Internals

  _processCreate : function( targetId, type, properties ) {
    var adapter = org.eclipse.rwt.protocol.AdapterRegistry.getAdapter( type );
    var styleMap = this._createStylesMap( properties.style );
    var targetObject = new adapter.constructor( styleMap );
    this._addTarget( targetObject, targetId, adapter.isControl, type );
 },
  
  _processDestroy : function( targetId ) {
    var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
    widgetManager.dispose( targetId );      
  },

  _processSet : function( targetId, properties ) {
    var adapter = this._getAdapter( targetId );
    if( adapter.knownProperties instanceof Array ) {
      var targetObject = this._getTarget( targetId );
      for( var i = 0; i < adapter.knownProperties.length; i++ ) {
        var property = adapter.knownProperties[ i ];
        var value = properties[ property ];
        if( value !== undefined ) {
          if( adapter.propertyHandler && adapter.propertyHandler[ property ] ) {
            adapter.propertyHandler[ property ].call( window, targetObject, value );
          } else {
            var setterName = this._getSetterName( adapter, property );
            targetObject[ setterName ]( value );
          }
        }
      }
    }
  },

  _processCall : function( targetId, method, properties ) {
    var adapter = this._getAdapter( targetId );
    if( adapter.knownActions.indexOf( method ) !== -1 ) {
      var targetObject = this._getTarget( targetId );
      targetObject[ method ].call( targetObject, properties );
    }
  },

  _processListen : function( targetId, properties ) {
    var adapter = this._getAdapter( targetId );
    if( adapter.knownEvents instanceof Array ) {
      var targetObject = this._getTarget( targetId );
      for( var i = 0; i < adapter.knownEvents.length; i++ ) {
        var type = adapter.knownEvents[ i ];
        if( properties[ type ] === true ) {
          this._addListener( targetObject, type );
        } if( properties[ type ] === false ) {
          this._removeListener( targetObject, type );            
        }
      }
    }
 },

 _processExecute : function( targetId, scriptType, content ) {
   if( scriptType === "text/javascript" ) {
     try {
       eval( content );
     } catch( ex ) {
       // ignored
       // TODO [tb] : handle in processOperation
     }
   }
 },

  ////////////
  // Internals

  _processError : function( error, operation ) {
    var msg = "Operation \"" + operation.action + "\"";
    msg += " on target \"" +  operation.target + "\"";
    var target = this._getTarget( operation.target );
    msg += " of type \"" +  ( target && target.classname ? target.classname : target ) + "\"";
    msg += " failed:";
    msg += "\n" + error;
    throw new Error( msg );
  },

  _getSetterName : function( adapter, property ) {
    var clientProperty = property;
    if( adapter.propertyMapping && adapter.propertyMapping[ property ] ) {
      clientProperty = adapter.propertyMapping[ property ];
    } 
    return "set" + qx.lang.String.toFirstUp( clientProperty );
  },

  _addTarget : function( target, targetId, isControl, type ) {
    var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
    widgetManager.add( target, targetId, isControl === true, type );
  },

  _getTarget : function( targetId ) {
    // TODO [tb] : support any kind of target-object, not just widgets
    var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
    var widget = widgetManager.findWidgetById( targetId );
    return widget;
  },

  _getAdapter : function( targetId ) {
    // TODO [tb] : support objects not implementing setUserData 
    var target = this._getTarget( targetId );
    var type = target.getUserData( "rwtType" );
    var adapter = org.eclipse.rwt.protocol.AdapterRegistry.getAdapter( type );
    return adapter;
  },
  
  _createStylesMap : function( style ) {
    var result = {};
    for( var i = 0; i < style.length; i++ ) {
      result[ style[ i ] ] = true;
    }
    return result;
  },

  _addListener : function( targetObject, eventType ) {
    var list = this._listenerMap[ eventType ];
    for( var i = 0; i < list.length; i++ ) {
      targetObject.addEventListener( list[ i ].nativeType, 
                                     list[ i ].listener, 
                                     list[ i ].context );
    }
  },

  _removeListener : function( targetObject, eventType ) {
    var list = this._listenerMap[ eventType ];
    for( var i = 0; i < list.length; i++ ) {
      targetObject.removeEventListener( list[ i ].nativeType, 
                                        list[ i ].listener, 
                                        list[ i ].context );
    }
  },
  
  _listenerMap : {
    "focus" : [ 
      { 
        nativeType : "focusin", 
        context : org.eclipse.swt.EventUtil, 
        listener : org.eclipse.swt.EventUtil.focusGained 
      },
      { 
        nativeType : "focusout", 
        context : org.eclipse.swt.EventUtil, 
        listener : org.eclipse.swt.EventUtil.focusLost 
      }
    ],
    "mouse" : [
      { 
        nativeType : "mousedown", 
        context : org.eclipse.swt.EventUtil, 
        listener : org.eclipse.swt.EventUtil.mouseDown 
      },
      { 
        nativeType : "mouseup", 
        context : org.eclipse.swt.EventUtil, 
        listener : org.eclipse.swt.EventUtil.mouseUp
      }
    ]
  }

};