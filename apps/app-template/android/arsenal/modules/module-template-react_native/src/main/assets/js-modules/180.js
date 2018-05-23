__d(function(e,t,n,i,a){'use strict';var s=t(a[0]),r=t(a[1]),v=t(a[2]),l=t(a[3]),o=t(a[1]).shouldUseNativeDriver;function c(e,t,n){var i=[];l(n[0]&&n[0].nativeEvent,'Native driven events only support animated values contained inside `nativeEvent`.'),(function e(t,n){if(t instanceof s)t.__makeNative(),i.push({nativeEventPath:n,animatedValueTag:t.__getNativeTag()});else if('object'==typeof t)for(var a in t)e(t[a],n.concat(a))})(n[0].nativeEvent,[]);var a=v.findNodeHandle(e);return i.forEach(function(e){r.API.addAnimatedEventToView(a,t,e)}),{detach:function(){i.forEach(function(e){r.API.removeAnimatedEventFromView(a,t,e.animatedValueTag)})}}}var _=(function(){function e(t){var n=arguments.length>1&&void 0!==arguments[1]?arguments[1]:{};babelHelpers.classCallCheck(this,e),this._listeners=[],this._argMapping=t,n.listener&&this.__addListener(n.listener),this._callListeners=this._callListeners.bind(this),this._attachedEvent=null,this.__isNative=o(n)}return babelHelpers.createClass(e,[{key:"__addListener",value:function(e){this._listeners.push(e)}},{key:"__removeListener",value:function(e){this._listeners=this._listeners.filter(function(t){return t!==e})}},{key:"__attach",value:function(e,t){l(this.__isNative,'Only native driven events need to be attached.'),this._attachedEvent=c(e,t,this._argMapping)}},{key:"__detach",value:function(e,t){l(this.__isNative,'Only native driven events need to be detached.'),this._attachedEvent&&this._attachedEvent.detach()}},{key:"__getHandler",value:function(){var e=this;return this.__isNative?this._callListeners:function(){for(var t=arguments.length,n=Array(t),i=0;i<t;i++)n[i]=arguments[i];var a=function e(t,n,i){if('number'==typeof n&&t instanceof s)t.setValue(n);else if('object'==typeof t)for(var a in t)e(t[a],n[a],a)};e.__isNative||e._argMapping.forEach(function(e,t){a(e,n[t])}),e._callListeners.apply(e,babelHelpers.toConsumableArray(n))}}},{key:"_callListeners",value:function(){for(var e=arguments.length,t=Array(e),n=0;n<e;n++)t[n]=arguments[n];this._listeners.forEach(function(e){return e.apply(void 0,t)})}},{key:"_validateMapping",value:function(){}}]),e})();n.exports={AnimatedEvent:_,attachNativeEvent:c}},180,[181,184,43,18]);