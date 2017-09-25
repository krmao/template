let commonObj = {
    scrollToExtend: function () {
        $.fn.scrollTo = function (options) {
            let defaults = {
                toT: 0,
                durTime: 500,
                delay: 30,
                callback: null
            };
            let opts = $.extend(defaults, options),
                timer = null,
                _this = this,
                curTop = _this.scrollTop(),
                subTop = opts.toT - curTop,
                index = 0,
                dur = Math.round(opts.durTime / opts.delay),
                smoothScroll = function (t) {
                    index++;
                    let per = Math.round(subTop / dur);
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
    commonMixin: function () {
        return {
            methods: {
                backTop: function () {
                    commonObj.scrollToExtend();
                    $('body').scrollTo({toT: 0});
                },
                bindScroll: function (_self) {
                    let _this = this;
                    let winHeight = window.innerHeight;
                    window.addEventListener('scroll', function () {
                        let scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
                        _self.basicInfo.showBackTopFlag = scrollTop > winHeight;
                    });
                }
            }
        };
    }
};
