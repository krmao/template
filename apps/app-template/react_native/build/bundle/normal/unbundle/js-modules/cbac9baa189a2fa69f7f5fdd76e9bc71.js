__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var EventEmitter = _require(_dependencyMap[0], 'EventEmitter');

  var Platform = _require(_dependencyMap[1], 'Platform');

  var RCTDeviceEventEmitter = _require(_dependencyMap[2], 'RCTDeviceEventEmitter');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  var eventEmitter = new EventEmitter();
  var dimensionsInitialized = false;
  var dimensions = {};

  var Dimensions = function () {
    function Dimensions() {
      babelHelpers.classCallCheck(this, Dimensions);
    }

    babelHelpers.createClass(Dimensions, null, [{
      key: "set",
      value: function set(dims) {
        if (dims && dims.windowPhysicalPixels) {
          dims = JSON.parse(JSON.stringify(dims));
          var windowPhysicalPixels = dims.windowPhysicalPixels;
          dims.window = {
            width: windowPhysicalPixels.width / windowPhysicalPixels.scale,
            height: windowPhysicalPixels.height / windowPhysicalPixels.scale,
            scale: windowPhysicalPixels.scale,
            fontScale: windowPhysicalPixels.fontScale
          };

          if (Platform.OS === 'android') {
            var screenPhysicalPixels = dims.screenPhysicalPixels;
            dims.screen = {
              width: screenPhysicalPixels.width / screenPhysicalPixels.scale,
              height: screenPhysicalPixels.height / screenPhysicalPixels.scale,
              scale: screenPhysicalPixels.scale,
              fontScale: screenPhysicalPixels.fontScale
            };
            delete dims.screenPhysicalPixels;
          } else {
            dims.screen = dims.window;
          }

          delete dims.windowPhysicalPixels;
        }

        babelHelpers.extends(dimensions, dims);

        if (dimensionsInitialized) {
          eventEmitter.emit('change', {
            window: dimensions.window,
            screen: dimensions.screen
          });
        } else {
          dimensionsInitialized = true;
        }
      }
    }, {
      key: "get",
      value: function get(dim) {
        invariant(dimensions[dim], 'No dimension set for key ' + dim);
        return dimensions[dim];
      }
    }, {
      key: "addEventListener",
      value: function addEventListener(type, handler) {
        invariant(type === 'change', 'Trying to subscribe to unknown event: "%s"', type);
        eventEmitter.addListener(type, handler);
      }
    }, {
      key: "removeEventListener",
      value: function removeEventListener(type, handler) {
        invariant(type === 'change', 'Trying to remove listener for unknown event: "%s"', type);
        eventEmitter.removeListener(type, handler);
      }
    }]);
    return Dimensions;
  }();

  var dims = global.nativeExtensions && global.nativeExtensions.DeviceInfo && global.nativeExtensions.DeviceInfo.Dimensions;
  var nativeExtensionsEnabled = true;

  if (!dims) {
    var DeviceInfo = _require(_dependencyMap[4], 'DeviceInfo');

    dims = DeviceInfo.Dimensions;
    nativeExtensionsEnabled = false;
  }

  invariant(dims, 'Either DeviceInfo native extension or DeviceInfo Native Module must be registered');
  Dimensions.set(dims);

  if (!nativeExtensionsEnabled) {
    RCTDeviceEventEmitter.addListener('didUpdateDimensions', function (update) {
      Dimensions.set(update);
    });
  }

  module.exports = Dimensions;
},"cbac9baa189a2fa69f7f5fdd76e9bc71",["6ab4a8e3cdc2c08a793730abd92b6eff","9493a89f5d95c3a8a47c65cfed9b5542","1060a7fdd4114915bad6b6943cf86a21","8940a4ad43b101ffc23e725363c70f8d","bbcc69856ded422ceb1f7386b087a4ad"],"Dimensions");