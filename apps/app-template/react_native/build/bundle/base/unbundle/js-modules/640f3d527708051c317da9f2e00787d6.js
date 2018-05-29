__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var EventEmitter = _require(_dependencyMap[0], 'eventemitter3');

  var MetroClient = function (_EventEmitter) {
    babelHelpers.inherits(MetroClient, _EventEmitter);

    function MetroClient(url) {
      babelHelpers.classCallCheck(this, MetroClient);

      var _this = babelHelpers.possibleConstructorReturn(this, (MetroClient.__proto__ || Object.getPrototypeOf(MetroClient)).call(this));

      _this._url = url;
      return _this;
    }

    babelHelpers.createClass(MetroClient, [{
      key: "enable",
      value: function enable() {
        var _this2 = this;

        if (this._ws) {
          this.disable();
        }

        this._ws = new global.WebSocket(this._url);

        this._ws.onerror = function (error) {
          _this2.emit('connection-error', error);
        };

        this._ws.onclose = function () {
          _this2.emit('close');
        };

        this._ws.onmessage = function (message) {
          var data = JSON.parse(message.data);

          switch (data.type) {
            case 'update-start':
              _this2.emit('update-start');

              break;

            case 'update':
              var _data$body = data.body;
              var modules = _data$body.modules,
                  sourceMappingURLs = _data$body.sourceMappingURLs,
                  sourceURLs = _data$body.sourceURLs;

              _this2.emit('update');

              modules.forEach(function (_ref, i) {
                var id = _ref.id,
                    code = _ref.code;
                code += '\n\n' + sourceMappingURLs[i];
                var injectFunction = typeof global.nativeInjectHMRUpdate === 'function' ? global.nativeInjectHMRUpdate : eval;
                injectFunction(code, sourceURLs[i]);
              });
              break;

            case 'update-done':
              _this2.emit('update-done');

              break;

            case 'error':
              _this2.emit('error', data.body);

              break;

            default:
              _this2.emit('error', {
                type: 'unknown-message',
                message: data
              });

          }
        };
      }
    }, {
      key: "disable",
      value: function disable() {
        if (this._ws) {
          this._ws.close();

          this._ws = undefined;
        }
      }
    }]);
    return MetroClient;
  }(EventEmitter);

  module.exports = MetroClient;
},"640f3d527708051c317da9f2e00787d6",["403d3a3adcf0a85f44100f9d486f85cb"],"node_modules/metro/src/lib/bundle-modules/MetroClient.js");