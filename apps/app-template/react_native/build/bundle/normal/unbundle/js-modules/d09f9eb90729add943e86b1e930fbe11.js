__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Class = _require(_dependencyMap[0], 'art/core/class.js');

  var Path = _require(_dependencyMap[1], 'art/core/path.js');

  var MOVE_TO = 0;
  var CLOSE = 1;
  var LINE_TO = 2;
  var CURVE_TO = 3;
  var ARC = 4;
  var SerializablePath = Class(Path, {
    initialize: function initialize(path) {
      this.reset();

      if (path instanceof SerializablePath) {
        this.path = path.path.slice(0);
      } else if (path) {
        if (path.applyToPath) {
          path.applyToPath(this);
        } else {
          this.push(path);
        }
      }
    },
    onReset: function onReset() {
      this.path = [];
    },
    onMove: function onMove(sx, sy, x, y) {
      this.path.push(MOVE_TO, x, y);
    },
    onLine: function onLine(sx, sy, x, y) {
      this.path.push(LINE_TO, x, y);
    },
    onBezierCurve: function onBezierCurve(sx, sy, p1x, p1y, p2x, p2y, x, y) {
      this.path.push(CURVE_TO, p1x, p1y, p2x, p2y, x, y);
    },
    _arcToBezier: Path.prototype.onArc,
    onArc: function onArc(sx, sy, ex, ey, cx, cy, rx, ry, sa, ea, ccw, rotation) {
      if (rx !== ry || rotation) {
        return this._arcToBezier(sx, sy, ex, ey, cx, cy, rx, ry, sa, ea, ccw, rotation);
      }

      this.path.push(ARC, cx, cy, rx, sa, ea, ccw ? 0 : 1);
    },
    onClose: function onClose() {
      this.path.push(CLOSE);
    },
    toJSON: function toJSON() {
      return this.path;
    }
  });
  module.exports = SerializablePath;
},"d09f9eb90729add943e86b1e930fbe11",["7f4f8b3a9932865ee435f23e3e085604","d2b369ec28179057734134c426b3cd40"],"ARTSerializablePath");