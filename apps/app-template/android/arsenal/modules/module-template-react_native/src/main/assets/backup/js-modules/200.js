__d(function(t,i,s,e,a){'use strict';i(a[0]),i(a[1]);var o=i(a[2]),n=i(a[3]),h=i(a[4]),r=i(a[5]).shouldUseNativeDriver;function l(t,i){return void 0===t||null===t?i:t}var _=(function(i){function s(t){babelHelpers.classCallCheck(this,s);var i=babelHelpers.possibleConstructorReturn(this,(s.__proto__||Object.getPrototypeOf(s)).call(this));if(i._overshootClamping=l(t.overshootClamping,!1),i._restDisplacementThreshold=l(t.restDisplacementThreshold,.001),i._restSpeedThreshold=l(t.restSpeedThreshold,.001),i._initialVelocity=l(t.velocity,0),i._lastVelocity=l(t.velocity,0),i._toValue=t.toValue,i._delay=l(t.delay,0),i._useNativeDriver=r(t),i.__isInteraction=void 0===t.isInteraction||t.isInteraction,i.__iterations=void 0!==t.iterations?t.iterations:1,void 0!==t.stiffness||void 0!==t.damping||void 0!==t.mass)h(void 0===t.bounciness&&void 0===t.speed&&void 0===t.tension&&void 0===t.friction,'You can define one of bounciness/speed, tension/friction, or stiffness/damping/mass, but not more than one'),i._stiffness=l(t.stiffness,100),i._damping=l(t.damping,10),i._mass=l(t.mass,1);else if(void 0!==t.bounciness||void 0!==t.speed){h(void 0===t.tension&&void 0===t.friction&&void 0===t.stiffness&&void 0===t.damping&&void 0===t.mass,'You can define one of bounciness/speed, tension/friction, or stiffness/damping/mass, but not more than one');var e=n.fromBouncinessAndSpeed(l(t.bounciness,8),l(t.speed,12));i._stiffness=e.stiffness,i._damping=e.damping,i._mass=1}else{var a=n.fromOrigamiTensionAndFriction(l(t.tension,40),l(t.friction,7));i._stiffness=a.stiffness,i._damping=a.damping,i._mass=1}return h(i._stiffness>0,'Stiffness value must be greater than 0'),h(i._damping>0,'Damping value must be greater than 0'),h(i._mass>0,'Mass value must be greater than 0'),i}return babelHelpers.inherits(s,i),babelHelpers.createClass(s,[{key:"__getNativeAnimationConfig",value:function(){return{type:'spring',overshootClamping:this._overshootClamping,restDisplacementThreshold:this._restDisplacementThreshold,restSpeedThreshold:this._restSpeedThreshold,stiffness:this._stiffness,damping:this._damping,mass:this._mass,initialVelocity:l(this._initialVelocity,this._lastVelocity),toValue:this._toValue,iterations:this.__iterations}}},{key:"start",value:function(t,i,e,a,o){var n=this;if(this.__active=!0,this._startPosition=t,this._lastPosition=this._startPosition,this._onUpdate=i,this.__onEnd=e,this._lastTime=Date.now(),this._frameTime=0,a instanceof s){var h=a.getInternalState();this._lastPosition=h.lastPosition,this._lastVelocity=h.lastVelocity,this._initialVelocity=this._lastVelocity,this._lastTime=h.lastTime}var r=function(){n._useNativeDriver?n.__startNativeAnimation(o):n.onUpdate()};this._delay?this._timeout=setTimeout(r,this._delay):r()}},{key:"getInternalState",value:function(){return{lastPosition:this._lastPosition,lastVelocity:this._lastVelocity,lastTime:this._lastTime}}},{key:"onUpdate",value:function(){var t=Date.now();t>this._lastTime+64&&(t=this._lastTime+64);var i=(t-this._lastTime)/1e3;this._frameTime+=i;var s=this._damping,e=this._mass,a=this._stiffness,o=-this._initialVelocity,n=s/(2*Math.sqrt(a*e)),h=Math.sqrt(a/e),r=h*Math.sqrt(1-n*n),l=this._toValue-this._startPosition,_=0,d=0,m=this._frameTime;if(n<1){var f=Math.exp(-n*h*m);_=this._toValue-f*((o+n*h*l)/r*Math.sin(r*m)+l*Math.cos(r*m)),d=n*h*f*(Math.sin(r*m)*(o+n*h*l)/r+l*Math.cos(r*m))-f*(Math.cos(r*m)*(o+n*h*l)-r*l*Math.sin(r*m))}else{var c=Math.exp(-h*m);_=this._toValue-c*(l+(o+h*l)*m),d=c*(o*(m*h-1)+m*l*(h*h))}if(this._lastTime=t,this._lastPosition=_,this._lastVelocity=d,this._onUpdate(_),this.__active){var u=!1;this._overshootClamping&&0!==this._stiffness&&(u=this._startPosition<this._toValue?_>this._toValue:_<this._toValue);var p=Math.abs(d)<=this._restSpeedThreshold,v=!0;if(0!==this._stiffness&&(v=Math.abs(this._toValue-_)<=this._restDisplacementThreshold),u||p&&v)return 0!==this._stiffness&&(this._lastPosition=this._toValue,this._lastVelocity=0,this._onUpdate(this._toValue)),void this.__debouncedOnEnd({finished:!0});this._animationFrame=requestAnimationFrame(this.onUpdate.bind(this))}}},{key:"stop",value:function(){babelHelpers.get(s.prototype.__proto__||Object.getPrototypeOf(s.prototype),"stop",this).call(this),this.__active=!1,clearTimeout(this._timeout),t.cancelAnimationFrame(this._animationFrame),this.__debouncedOnEnd({finished:!1})}}]),s})(o);s.exports=_},200,[181,197,199,201,18,184]);