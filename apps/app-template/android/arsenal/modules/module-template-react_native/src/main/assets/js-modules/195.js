__d(function(t,e,r,a,n){'use strict';var o=e(n[0]),i=e(n[1]),s=e(n[2]),_=(function(t){function e(t){babelHelpers.classCallCheck(this,e);var r=babelHelpers.possibleConstructorReturn(this,(e.__proto__||Object.getPrototypeOf(e)).call(this));return r._transforms=t,r}return babelHelpers.inherits(e,t),babelHelpers.createClass(e,[{key:"__makeNative",value:function(){babelHelpers.get(e.prototype.__proto__||Object.getPrototypeOf(e.prototype),"__makeNative",this).call(this),this._transforms.forEach(function(t){for(var e in t){var r=t[e];r instanceof o&&r.__makeNative()}})}},{key:"__getValue",value:function(){return this._transforms.map(function(t){var e={};for(var r in t){var a=t[r];e[r]=a instanceof o?a.__getValue():a}return e})}},{key:"__getAnimatedValue",value:function(){return this._transforms.map(function(t){var e={};for(var r in t){var a=t[r];e[r]=a instanceof o?a.__getAnimatedValue():a}return e})}},{key:"__attach",value:function(){var t=this;this._transforms.forEach(function(e){for(var r in e){var a=e[r];a instanceof o&&a.__addChild(t)}})}},{key:"__detach",value:function(){var t=this;this._transforms.forEach(function(e){for(var r in e){var a=e[r];a instanceof o&&a.__removeChild(t)}}),babelHelpers.get(e.prototype.__proto__||Object.getPrototypeOf(e.prototype),"__detach",this).call(this)}},{key:"__getNativeConfig",value:function(){var t=[];return this._transforms.forEach(function(e){for(var r in e){var a=e[r];a instanceof o?t.push({type:'animated',property:r,nodeTag:a.__getNativeTag()}):t.push({type:'static',property:r,value:a})}}),s.validateTransform(t),{type:'transform',transforms:t}}}]),e})(i);r.exports=_},195,[183,185,184]);