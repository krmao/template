'use strict';

var commonObj = {
    scrollToExtend: function scrollToExtend() {
        $.fn.scrollTo = function (options) {
            var defaults = {
                toT: 0,
                durTime: 500,
                delay: 30,
                callback: null
            };
            var opts = $.extend(defaults, options),
                timer = null,
                _this = this,
                curTop = _this.scrollTop(),
                subTop = opts.toT - curTop,
                index = 0,
                dur = Math.round(opts.durTime / opts.delay),
                smoothScroll = function smoothScroll(t) {
                index++;
                var per = Math.round(subTop / dur);
                if (index >= dur) {
                    _this.scrollTop(t);
                    window.clearInterval(timer);
                    if (opts.callback && typeof opts.callback === 'function') {
                        opts.callback();
                    }
                } else {
                    _this.scrollTop(curTop + index * per);
                }
            };
            timer = window.setInterval(function () {
                smoothScroll(opts.toT);
            }, opts.delay);
            return _this;
        };
    },
    commonMixin: function commonMixin() {
        return {
            methods: {
                backTop: function backTop() {
                    commonObj.scrollToExtend();
                    $('body').scrollTo({ toT: 0 });
                },
                bindScroll: function bindScroll(_self) {
                    var _this = this;
                    var winHeight = window.innerHeight;
                    window.addEventListener('scroll', function () {
                        var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
                        _self.basicInfo.showBackTopFlag = scrollTop > winHeight;
                    });
                }
            }
        };
    }
};