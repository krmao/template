__d(function(e,t,o,r,g){'use strict';var a=t(g[0]),n=t(g[1]),i={setGlobalOptions:function(e){void 0!==e.debug&&n(a.FrameRateLogger,'Trying to debug FrameRateLogger without the native module!'),a.FrameRateLogger&&a.FrameRateLogger.setGlobalOptions(e)},setContext:function(e){a.FrameRateLogger&&a.FrameRateLogger.setContext(e)},beginScroll:function(){a.FrameRateLogger&&a.FrameRateLogger.beginScroll()},endScroll:function(){a.FrameRateLogger&&a.FrameRateLogger.endScroll()}};o.exports=i},210,[20,18]);