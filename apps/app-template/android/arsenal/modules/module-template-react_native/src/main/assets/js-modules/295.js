__d(function(e,t,n,s,i){'use strict';t(i[0]);var o=t(i[1]),l=(t(i[2]),t(i[3])),r=(l.ActionSheetManager,l.ShareModule),a=(function(){function e(){babelHelpers.classCallCheck(this,e)}return babelHelpers.createClass(e,null,[{key:"share",value:function(e){var t=arguments.length>1&&void 0!==arguments[1]?arguments[1]:{};return o('object'==typeof e&&null!==e,'Content to share must be a valid object'),o('string'==typeof e.url||'string'==typeof e.message,'At least one of URL and message is required'),o('object'==typeof t&&null!==t,'Options must be a valid object'),o(!e.title||'string'==typeof e.title,'Invalid title: title should be a string.'),r.share(e,t.dialogTitle)}},{key:"sharedAction",get:function(){return'sharedAction'}},{key:"dismissedAction",get:function(){return'dismissedAction'}}]),e})();n.exports=a},295,[28,18,134,20]);