let commonObj = {
    isApp: function () {
        // let ua = window.navigator.userAgent;//获取ua
        // if(ua.indexOf("MongoToC")>=0){
        //     return true;
        // }else{
        //     return false;
        // }
    },
    getChannel: function () {
        // let userAgent = window.navigator.userAgent,endType;
        // if(userAgent.toLowerCase().match(/MicroMessenger/i) == 'micromessenger'){//判断是否是微信
        //     endType = 'wxCode';
        // }else if(userAgent.indexOf('MongoToC') > 0){//判断是否来自APP
        //     endType = 'appCode';
        // }else{//来自H5
        //     endType = 'h5Code';
        // }
        // return endType;
    },
    isAppVersionMinor_4: function () {
        // try{
        //     let ua = JSON.parse(window.navigator.userAgent),
        //         appVersion = ua.appVersion;
        //     console.log(ua);
        //     if(appVersion){
        //         let ava = appVersion.replace(".","");
        //         if(ava>=40){
        //             return false;
        //         }else return true;
        //     }else return true;
        // }catch(e){
        //     console.log('ua未获取到');
        // }
    },
    scrollToExtend: function () {
        $.fn.scrollTo = function (options) {
            let defaults = {
                toT: 0,    //滚动目标位置
                durTime: 500,  //过渡动画时间
                delay: 30,     //定时器时间
                callback: null   //回调函数
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
                    // 返回顶部
                    commonObj.scrollToExtend();
                    $('body').scrollTo({toT: 0});
                },
                bindScroll: function (_self) {
                    // 监听滚动事件
                    let _this = this;
                    let winHeight = window.innerHeight;
                    window.addEventListener('scroll', function () {
                        let scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
                        if (scrollTop > winHeight) {
                            _self.basicInfo.showBackTopFlag = true;
                        } else {
                            _self.basicInfo.showBackTopFlag = false;
                        }
                    });
                }
            }
        };
    }
};
