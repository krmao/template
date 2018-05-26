__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Platform = _require(_dependencyMap[0], 'Platform');

  var invariant = _require(_dependencyMap[1], 'fbjs/lib/invariant');

  var MetroHMRClient = _require(_dependencyMap[2], 'metro/src/lib/bundle-modules/HMRClient');

  var HMRClient = {
    enable: function enable(platform, bundleEntry, host, port) {
      invariant(platform, 'Missing required parameter `platform`');
      invariant(bundleEntry, 'Missing required paramenter `bundleEntry`');
      invariant(host, 'Missing required paramenter `host`');

      var HMRLoadingView = _require(_dependencyMap[3], 'HMRLoadingView');

      var wsHostPort = port !== null && port !== '' ? host + ":" + port : host;
      bundleEntry = bundleEntry.replace(/\.(bundle|delta)/, '.js');
      var wsUrl = "ws://" + wsHostPort + "/hot?" + ("platform=" + platform + "&") + ("bundleEntry=" + bundleEntry);
      var hmrClient = new MetroHMRClient(wsUrl);
      hmrClient.on('connection-error', function (e) {
        var error = "Hot loading isn't working because it cannot connect to the development server.\n\nTry the following to fix the issue:\n- Ensure that the packager server is running and available on the same network";

        if (Platform.OS === 'ios') {
          error += "\n- Ensure that the Packager server URL is correctly set in AppDelegate";
        } else {
          error += "\n- Ensure that your device/emulator is connected to your machine and has USB debugging enabled - run 'adb devices' to see a list of connected devices\n- If you're on a physical device connected to the same machine, run 'adb reverse tcp:8081 tcp:8081' to forward requests from your device\n- If your device is on the same Wi-Fi network, set 'Debug server host & port for device' in 'Dev settings' to your machine's IP address and the port of the local dev server - e.g. 10.0.1.1:8081";
        }

        error += "\n\nURL: " + host + ":" + port + "\n\nError: " + e.message;
        throw new Error(error);
      });
      hmrClient.on('update-start', function () {
        HMRLoadingView.showMessage('Hot Loading...');
      });
      hmrClient.on('update', function () {
        if (Platform.OS === 'ios') {
          var RCTRedBox = _require(_dependencyMap[4], 'NativeModules').RedBox;

          RCTRedBox && RCTRedBox.dismiss && RCTRedBox.dismiss();
        } else {
          var RCTExceptionsManager = _require(_dependencyMap[4], 'NativeModules').ExceptionsManager;

          RCTExceptionsManager && RCTExceptionsManager.dismissRedbox && RCTExceptionsManager.dismissRedbox();
        }
      });
      hmrClient.on('update-done', function () {
        HMRLoadingView.hide();
      });
      hmrClient.on('error', function (data) {
        HMRLoadingView.hide();
        throw new Error(data.type + " " + data.message);
      });
      hmrClient.enable();
    }
  };
  module.exports = HMRClient;
},107,[32,18,108,111,24],"HMRClient");