__d(function(n,t,e,c,i){'use strict';t(i[0]);var o=!1,u=0,a={installReactHook:function(){!0},setEnabled:function(n){o!==n&&(o=n)},isEnabled:function(){return o},beginEvent:function(t,e){o&&(t='function'==typeof t?t():t,n.nativeTraceBeginSection(131072,t,e))},endEvent:function(){o&&n.nativeTraceEndSection(131072)},beginAsyncEvent:function(t){var e=u;return o&&(u++,t='function'==typeof t?t():t,n.nativeTraceBeginAsyncSection(131072,t,e)),e},endAsyncEvent:function(t,e){o&&(t='function'==typeof t?t():t,n.nativeTraceEndAsyncSection(131072,t,e))},counterEvent:function(t,e){o&&(t='function'==typeof t?t():t,n.nativeTraceCounter&&n.nativeTraceCounter(131072,t,e))}};e.exports=a},24,[18]);