__d(function(e,t,s,i,n){'use strict';var a=t(n[0]),r=t(n[1]),l=t(n[2]),u=1,o=(function(e){function t(e){babelHelpers.classCallCheck(this,t);var s=babelHelpers.possibleConstructorReturn(this,(t.__proto__||Object.getPrototypeOf(t)).call(this)),i=e||{x:0,y:0};return'number'==typeof i.x&&'number'==typeof i.y?(s.x=new a(i.x),s.y=new a(i.y)):(l(i.x instanceof a&&i.y instanceof a,"AnimatedValueXY must be initialized with an object of numbers or AnimatedValues."),s.x=i.x,s.y=i.y),s._listeners={},s}return babelHelpers.inherits(t,e),babelHelpers.createClass(t,[{key:"setValue",value:function(e){this.x.setValue(e.x),this.y.setValue(e.y)}},{key:"setOffset",value:function(e){this.x.setOffset(e.x),this.y.setOffset(e.y)}},{key:"flattenOffset",value:function(){this.x.flattenOffset(),this.y.flattenOffset()}},{key:"extractOffset",value:function(){this.x.extractOffset(),this.y.extractOffset()}},{key:"__getValue",value:function(){return{x:this.x.__getValue(),y:this.y.__getValue()}}},{key:"resetAnimation",value:function(e){this.x.resetAnimation(),this.y.resetAnimation(),e&&e(this.__getValue())}},{key:"stopAnimation",value:function(e){this.x.stopAnimation(),this.y.stopAnimation(),e&&e(this.__getValue())}},{key:"addListener",value:function(e){var t=this,s=String(u++),i=function(s){s.value;e(t.__getValue())};return this._listeners[s]={x:this.x.addListener(i),y:this.y.addListener(i)},s}},{key:"removeListener",value:function(e){this.x.removeListener(this._listeners[e].x),this.y.removeListener(this._listeners[e].y),delete this._listeners[e]}},{key:"removeAllListeners",value:function(){this.x.removeAllListeners(),this.y.removeAllListeners(),this._listeners={}}},{key:"getLayout",value:function(){return{left:this.x,top:this.y}}},{key:"getTranslateTransform",value:function(){return[{translateX:this.x},{translateY:this.y}]}}]),t})(r);s.exports=o},197,[181,185,18]);