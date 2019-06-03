/**
 * 
 * @author lixiaolong
 * @description native to web and web to native for get info and changeinfo
 */
(function(window) {

	'use strict';

	var callBackList = [];
	var callNavList = [];
	var baseConfig = {
		scheme: "lb://",
		debuger: true

	}

	var postUrl = function(strUrl, param, callid) {
		var lb = {};
		lb.callbackId = callid;
		lb.name = strUrl;
		lb.param = param;

		if(navigator.userAgent.match(/appCode/i) ? true : false) {
			var appVersion = JSON.parse(navigator.userAgent).appVersion;
			if(appVersion.length == 3) {
				appVersion = appVersion + ".0"
			}
			if(param.version > appVersion) {
				console.log(strUrl + "方法只能在大于等于" + param.version + "的版本中使用")
				return;
			}
			var c = document.createElement("div")
			c.innerHTML = '<iframe style="display: none;" src="' + baseConfig.scheme +
				encodeURIComponent(encodeURIComponent(JSON.stringify(lb))) + '"/>'
			document.querySelector("body").appendChild(c)
			setTimeout(function() {
				document.querySelector("body").removeChild(c)
			}, 3000)

		}
	}

	var mergeObjs = function(def, obj) {
		if(!Object.assign) {
			Object.defineProperty(Object, "assign", {
				enumerable: false,
				configurable: true,
				writable: true,
				value: function(target, firstSource) {
					"use strict";
					if(target === undefined || target === null)
						throw new TypeError("Cannot convert first argument to object");
					var to = Object(target);
					for(var i = 1; i < arguments.length; i++) {
						var nextSource = arguments[i];
						if(nextSource === undefined || nextSource === null) continue;
						var keysArray = Object.keys(Object(nextSource));
						for(var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++) {
							var nextKey = keysArray[nextIndex];
							var desc = Object.getOwnPropertyDescriptor(nextSource, nextKey);
							if(desc !== undefined && desc.enumerable) to[nextKey] = nextSource[nextKey];
						}
					}
					return to;
				}
			});
		}
		return Object.assign(def, obj)
	}

	var jsb = function(target, option, param, cf, f1, f2) {
		console.log(arguments)
		var callid = callBackList.length;
		if(typeof arguments[1] !== "function") {
			param = mergeObjs(param, option)
		} else if(typeof arguments[1] == "function") {
			cf = arguments[1];
		}
		if(!cf) {
			cf = new Function();
		}

		callbackListFunction(callid, cf)
		log(target, param);
		postUrl(target, param, callid)
	}

	var config = {
		camera: {
			version: "3.0.0",
			name: "camera"
		},
		qrcode: {
			version: "3.0.0",
			name: "qrcode"
		},
		barcode: {
			version: "3.0.0",
			name: "barcode"
		},
		qrcodeImage: {
			version: "3.0.0",
			name: "qrcodeImage"
		},
		tel: {
			version: "3.0.0",
			name: "tel"
		},
		email: {
			version: "3.0.0",
			name: "email"
		},
		sms: {
			version: "3.0.0",
			name: "sms"
		},
		geolocation: {
			version: "3.0.0",
			name: "geolocation"
		},
		dialog: {
			version: "3.0.0",
			name: "dialog"
		},
		toast: {
			version: "3.0.0",
			name: "toast"
		},
		loading: {
			version: "3.0.0",
			name: "loading"
		},
		userInfo: {
			version: "3.0.0",
			name: "userInfo"
		},
		appInfo: {
			version: "3.0.0",
			name: "appInfo"
		},
		redirect: {
			version: "3.0.0",
			name: "redirect"
		},
		alipay: {
			version: "3.0.0",
			name: "alipay"
		},
		shareInfo: {
			version: "3.1.0",
			name: "shareInfo"
		},
		carInfo: {
			version: "3.1.0",
			name: "carInfo"
		},
		pay: {
			version: "3.1.0",
			name: "checkoutcounter"
		},
		deviceInfo: {
			version: "3.1.0",
			name: "deviceInfo"
		},
		closeWebview: {
			version: "3.1.0",
			name: "closeWebview"
		},

		initVehicle: {
			version: "3.2.0",
			name: "initVehicle"
		},
		switchVehicle: {
			version: "3.2.0",
			name: "switchVehicle"
		},
		callAppNavi: {
			version: "3.3.0",
			name: "callAppNavi"
		},
		uploadImage: {
			version: "3.3.0",
			name: "uploadImage"
		},
		showMenu: {
			version: "4.0.0",
			name: "showMenu"
		},
		menuShareMsg: {
			version: "4.0.0",
			name: "menuShareMsg"
		},
		newWebview: {
			version: "4.0.0",
			name: "newWebview"
		},
		hideMenu: {
			version: "4.0.0",
			name: "hideMenu"
		},

		openIM: {
			version: "4.0.0",
			name: "openIM"
		},
		shareMsg: {
			version: "4.2.0",
			name: "shareMsg"
		}
	};

	var lb = {
		callByNative: function(o) {
			nativeCallback(o)
		},
		init: init

	};

	function init() {
		if(!Object.keys) {
			Object.keys = (function() {
				var hasOwnProperty = Object.prototype.hasOwnProperty,
					hasDontEnumBug = !({
						toString: null
					}).propertyIsEnumerable('toString'),
					dontEnums = [
						'toString',
						'toLocaleString',
						'valueOf',
						'hasOwnProperty',
						'isPrototypeOf',
						'propertyIsEnumerable',
						'constructor'
					],
					dontEnumsLength = dontEnums.length;

				return function(obj) {
					if(typeof obj !== 'object' && typeof obj !== 'function' || obj === null) throw new TypeError('Object.keys called on non-object');

					var result = [];

					for(var prop in obj) {
						if(hasOwnProperty.call(obj, prop)) result.push(prop);
					}

					if(hasDontEnumBug) {
						for(var i = 0; i < dontEnumsLength; i++) {
							if(hasOwnProperty.call(obj, dontEnums[i])) result.push(dontEnums[i]);
						}
					}
					return result;
				}
			})()
		};

		Object.keys(config).forEach((key) => {
			lb[key] = function(option, cf) {
				var param = config[key]
				jsb(config[key].name, option, param, cf);
			}
		});
	}

	function callbackListFunction(callid, cf) {
		callBackList[callid] = cf || new Function();

	}

	function nativeCallback(result) {

		callBackList[result.callbackId * 1](result);

	}

	function log(name, param) {
		if(baseConfig.debuger) {
			console.log("调用的方法是--->" + name);
			console.log("调用的参数是 --->" + JSON.stringify(param));
		}
		/*	layer.open({
				content: msg,
				time: 2
			});*/

	}
	if(typeof define === 'function' && define.amd) {
		// AMD
		define(lb);
	} else {
		window.lb = lb;
	}
	window.onBackPressed = function() {
		lb.toast({text:"onBackPressed"})
	}

	window.onResume = function() {
		lb.toast({text:"onResume"})
	}

	window.onPause = function() {
		lb.toast({text:"onPause"})
	}

	window.onResult = function(dataString) {
		lb.toast({text:"onResult"})
	}

	window.onNetworkStateChanged = function(available) {
		lb.toast({text:"onNetworkStateChanged:"+available})
	}

	/**
	 * app is visible
	 * @param visible true/false
	 */
	window.onApplicationVisibleChanged = function(visible) {
		console.log('[html] onApplicationVisibleChanged():visible=' + visible)
		lb.toast({text:"onApplicationVisibleChanged:"+visible})
	}

})(window);

lb.init();