__d(function(e,r,t,a,s){'use strict';r(s[0]);var n=r(s[1]),o=r(s[2]).Networking,l=r(s[3]);function u(e){var r=[];for(var t in e)r.push([t,e[t]]);return r}var i=1;var c=(function(e){function r(){babelHelpers.classCallCheck(this,r);var e=babelHelpers.possibleConstructorReturn(this,(r.__proto__||Object.getPrototypeOf(r)).call(this,o));return e.isAvailable=!0,e}return babelHelpers.inherits(r,e),babelHelpers.createClass(r,[{key:"sendRequest",value:function(e,r,t,a,s,n,c,b,f,p){var v=l(s);v&&v.formData&&(v.formData=v.formData.map(function(e){return babelHelpers.extends({},e,{headers:u(e.headers)})}));var h=i++;o.sendRequest(e,t,h,u(a),babelHelpers.extends({},v,{trackingName:r}),n,c,b,p),f(h)}},{key:"abortRequest",value:function(e){o.abortRequest(e)}},{key:"clearCookies",value:function(e){o.clearCookies(e)}}]),r})(n);c=new c,t.exports=c},70,[71,72,20,73]);