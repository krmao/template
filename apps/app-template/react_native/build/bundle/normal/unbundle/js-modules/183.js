__d(function (global, _require, module, exports, _dependencyMap) {
	var Class = _require(_dependencyMap[0], './class');

	function Transform(xx, yx, xy, yy, x, y) {
		if (xx && typeof xx == 'object') {
			yx = xx.yx;
			yy = xx.yy;
			y = xx.y;
			xy = xx.xy;
			x = xx.x;
			xx = xx.xx;
		}

		this.xx = xx == null ? 1 : xx;
		this.yx = yx || 0;
		this.xy = xy || 0;
		this.yy = yy == null ? 1 : yy;
		this.x = (x == null ? this.x : x) || 0;
		this.y = (y == null ? this.y : y) || 0;

		this._transform();

		return this;
	}

	;
	module.exports = Class({
		initialize: Transform,
		_transform: function _transform() {},
		xx: 1,
		yx: 0,
		x: 0,
		xy: 0,
		yy: 1,
		y: 0,
		transform: function transform(xx, yx, xy, yy, x, y) {
			var m = this;

			if (xx && typeof xx == 'object') {
				yx = xx.yx;
				yy = xx.yy;
				y = xx.y;
				xy = xx.xy;
				x = xx.x;
				xx = xx.xx;
			}

			if (!x) x = 0;
			if (!y) y = 0;
			return this.transformTo(m.xx * xx + m.xy * yx, m.yx * xx + m.yy * yx, m.xx * xy + m.xy * yy, m.yx * xy + m.yy * yy, m.xx * x + m.xy * y + m.x, m.yx * x + m.yy * y + m.y);
		},
		transformTo: Transform,
		translate: function translate(x, y) {
			return this.transform(1, 0, 0, 1, x, y);
		},
		move: function move(x, y) {
			this.x += x || 0;
			this.y += y || 0;

			this._transform();

			return this;
		},
		scale: function scale(x, y) {
			if (y == null) y = x;
			return this.transform(x, 0, 0, y, 0, 0);
		},
		rotate: function rotate(deg, x, y) {
			if (x == null || y == null) {
				x = (this.left || 0) + (this.width || 0) / 2;
				y = (this.top || 0) + (this.height || 0) / 2;
			}

			var rad = deg * Math.PI / 180,
			    sin = Math.sin(rad),
			    cos = Math.cos(rad);
			this.transform(1, 0, 0, 1, x, y);
			var m = this;
			return this.transformTo(cos * m.xx - sin * m.yx, sin * m.xx + cos * m.yx, cos * m.xy - sin * m.yy, sin * m.xy + cos * m.yy, m.x, m.y).transform(1, 0, 0, 1, -x, -y);
		},
		moveTo: function moveTo(x, y) {
			var m = this;
			return this.transformTo(m.xx, m.yx, m.xy, m.yy, x, y);
		},
		rotateTo: function rotateTo(deg, x, y) {
			var m = this;
			var flip = m.yx / m.xx > m.yy / m.xy ? -1 : 1;
			if (m.xx < 0 ? m.xy >= 0 : m.xy < 0) flip = -flip;
			return this.rotate(deg - Math.atan2(flip * m.yx, flip * m.xx) * 180 / Math.PI, x, y);
		},
		scaleTo: function scaleTo(x, y) {
			var m = this;
			var h = Math.sqrt(m.xx * m.xx + m.yx * m.yx);
			m.xx /= h;
			m.yx /= h;
			h = Math.sqrt(m.yy * m.yy + m.xy * m.xy);
			m.yy /= h;
			m.xy /= h;
			return this.scale(x, y);
		},
		resizeTo: function resizeTo(width, height) {
			var w = this.width,
			    h = this.height;
			if (!w || !h) return this;
			return this.scaleTo(width / w, height / h);
		},
		inversePoint: function inversePoint(x, y) {
			var a = this.xx,
			    b = this.yx,
			    c = this.xy,
			    d = this.yy,
			    e = this.x,
			    f = this.y;
			var det = b * c - a * d;
			if (det == 0) return null;
			return {
				x: (d * (e - x) + c * (y - f)) / det,
				y: (a * (f - y) + b * (x - e)) / det
			};
		},
		point: function point(x, y) {
			var m = this;
			return {
				x: m.xx * x + m.xy * y + m.x,
				y: m.yx * x + m.yy * y + m.y
			};
		}
	});
},183,[181],"node_modules/art/core/transform.js");