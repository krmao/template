__d(function(t,o,e,i,n){'use strict';var s=o(n[0]).ActionSheetManager,a=o(n[1]),l=o(n[2]),c={showActionSheetWithOptions:function(t,o){a('object'==typeof t&&null!==t,'Options must be a valid object'),a('function'==typeof o,'Must provide a valid callback'),s.showActionSheetWithOptions(babelHelpers.extends({},t,{tintColor:l(t.tintColor)}),o)},showShareActionSheetWithOptions:function(t,o,e){a('object'==typeof t&&null!==t,'Options must be a valid object'),a('function'==typeof o,'Must provide a valid failureCallback'),a('function'==typeof e,'Must provide a valid successCallback'),s.showShareActionSheetWithOptions(babelHelpers.extends({},t,{tintColor:l(t.tintColor)}),o,e)}};e.exports=c},277,[20,18,134]);