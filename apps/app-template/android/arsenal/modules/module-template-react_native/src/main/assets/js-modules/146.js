__d(function(e,n,i,s,t){'use strict';var a=n(t[0]),o=(n(t[1]),n(t[2])),c=n(t[3]),l=new a,r=!1,d={},h=(function(){function e(){babelHelpers.classCallCheck(this,e)}return babelHelpers.createClass(e,null,[{key:"set",value:function(e){if(e&&e.windowPhysicalPixels){var n=(e=JSON.parse(JSON.stringify(e))).windowPhysicalPixels;e.window={width:n.width/n.scale,height:n.height/n.scale,scale:n.scale,fontScale:n.fontScale};var i=e.screenPhysicalPixels;e.screen={width:i.width/i.scale,height:i.height/i.scale,scale:i.scale,fontScale:i.fontScale},delete e.screenPhysicalPixels,delete e.windowPhysicalPixels}babelHelpers.extends(d,e),r?l.emit('change',{window:d.window,screen:d.screen}):r=!0}},{key:"get",value:function(e){return c(d[e],'No dimension set for key '+e),d[e]}},{key:"addEventListener",value:function(e,n){c('change'===e,'Trying to subscribe to unknown event: "%s"',e),l.addListener(e,n)}},{key:"removeEventListener",value:function(e,n){c('change'===e,'Trying to remove listener for unknown event: "%s"',e),l.removeListener(e,n)}}]),e})(),v=e.nativeExtensions&&e.nativeExtensions.DeviceInfo&&e.nativeExtensions.DeviceInfo.Dimensions,u=!0;v||(v=n(t[4]).Dimensions,u=!1);c(v,'Either DeviceInfo native extension or DeviceInfo Native Module must be registered'),h.set(v),u||o.addListener('didUpdateDimensions',function(e){h.set(e)}),i.exports=h},146,[35,28,34,18,147]);