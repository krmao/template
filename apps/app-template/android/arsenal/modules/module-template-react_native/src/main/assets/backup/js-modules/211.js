__d(function(e,o,t,n,u){'use strict';var i=o(u[0]),d=o(u[1]),m=o(u[2]),s=o(u[3]).KeyboardObserver,r=o(u[4]),a={addListener:function(e,o){d(!1,'Dummy method used for documentation')},removeListener:function(e,o){d(!1,'Dummy method used for documentation')},removeAllListeners:function(e){d(!1,'Dummy method used for documentation')},dismiss:function(){d(!1,'Dummy method used for documentation')},scheduleLayoutAnimation:function(e){d(!1,'Dummy method used for documentation')}};(a=new m(s)).dismiss=r,a.scheduleLayoutAnimation=function(e){var o=e.duration,t=e.easing;o&&i.configureNext({duration:o,update:{duration:o,type:t&&i.Types[t]||'keyboard'}})},t.exports=a},211,[212,18,72,20,213]);