$(function () {
    console.log('project init start ...');
    preRequest();
    // mockData();
});


function preRequest() {
    $.ajax({
        type: 'POST',
        url: '',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({mobile: ''}),
        success: function (res) {
        },
        error: function (xmlHttpRequest, status, statusText) {
        }
    });
}

function requestVerifyCode() {
    var inputPhone = $('#input-phone');
    var inputVerify = $('#input-verify');
    var phoneNum = inputPhone.val();
    if (!checkPhoneNum(phoneNum)) {
        alert('请输入正确的手机号');
    } else {
        if (!isRunning) {
            console.log('开始请求服务端发送短信-->');

            $.ajax({
                type: 'POST',
                url: '',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({mobile: phoneNum}),
                success: function (res) {
                    console.log(res);
                    if (res.result === 1) {
                        console.log('验证码发送成功！');
                        processTimerCounting();
                    } else {
                        console.log('验证码发送失败！');
                        alert('验证码发送失败！');
                    }
                },
                error: function (xmlHttpRequest, status, statusText) {
                    console.log('xmlHttpRequest:\t' + xmlHttpRequest + ' ,\nstatus:         ' + status + ' ,\nstatusText:     ' + statusText);
                    console.log('验证码发送失败！');
                    alert('验证码发送失败！');
                }
            });
            inputVerify.focus();
        } else {
            inputPhone.focus();
            console.log('倒计时还没有结束');
        }
    }
}

function checkPhoneNum(phoneNum) {
    var mobileRegx = /^1[3|4|5|7|8][0-9]{9}$/;
    return phoneNum !== '' && mobileRegx.test(phoneNum);
}

var isRunning = false;

function processTimerCounting() {
    var btnVerify = $('#input-verify-btn');

    var second = 60;//需求为60s
    var timer = null;
    isRunning = true;

    btnVerify.html(second + 's后再次获取');
    btnVerify.css({color: '#cccccc'});

    timer = setInterval(function () {
        second -= 1;
        if (second > 0) {
            btnVerify.html(second + 's后再次获取');
            isRunning = true;
        } else {
            clearInterval(timer);
            btnVerify.html('获取验证码');
            btnVerify.css({color: '#fc4748'});
            isRunning = false;
        }
    }, 1000);
}

function onSubmit() {
    console.log('onSubmit ... ');

    var phoneNum = $('#input-phone').val();
    var verifyNum = $('#input-verify').val();

    if (!checkPhoneNum(phoneNum)) {
        alert('请输入正确的手机号');
        return;
    }
    if (verifyNum === '') {
        alert('请输入正确的验证码');
        return;
    }

    $.ajax({
        type: 'POST',
        url: '/bdActivity/receiveAward/0.htm',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'mobile': phoneNum,
            'captcha': verifyNum,
            'source': getUrlParam('utm_source') //商务合作渠道 对应url 参数 utm_source
        }),
        success: function (res) {
            processGetSuccessData(res);
        },
        error: function (xmlHttpRequest, status, statusText) {
            console.log('xmlHttpRequest:\t' + xmlHttpRequest + ' ,\nstatus:         ' + status + ' ,\nstatusText:     ' + statusText);
            alert('系统异常，请稍后再试！');
        }
    });
}

function processGetSuccessData(res) {
    console.log(res);
    document.body.scrollTop = 0;//页面滚动到顶部

    var _this = this;
    switch (res.result) {
        case 1:
            //领券成功
            _this.isHome = false;//首页
            _this.isSuccess = true;//领券成功
            _this.isShowGeted = false;//已领过
            _this.isShowOldFriend = false;//老用户
            var quanList = res.obj;
            /**/
            for (var i = 0; i < quanList.length; i++) {
                var quan = quanList[i];
                var activityCode = quan.activityCode;
                var validStartDate = quan.validStartDate ? quan.validStartDate : new Date().getTime();
                var validEndDate = quan.validEndDate ? quan.validEndDate : new Date().getTime();
                quan.validityTime = '有效期：' + formatDate(new Date(validStartDate)) + '-' + formatDate(new Date(validEndDate));
                if (activityCode === '60115XCLYQ') {
                    quan.titleExtra = '元洗车抵用券';
                    quan.tips = '仅限5座（含5座SUV）洗车';
                }
                if (activityCode === '60164LYQ') {
                    quan.titleExtra = '元打蜡抵用券';
                    quan.tips = '仅限5座机器打蜡';
                }
                if (activityCode === '601100JKQ') {
                    quan.titleExtra = '元保养抵用券';
                    quan.tips = '仅限半合成及以上小保养服务';
                }
            }
            _this.quanList = quanList;
            showSuccessDialog(_this.quanList);
            break;
        case 1001:
            //已领过
            _this.isHome = true;//首页
            _this.isSuccess = false;//领券成功
            _this.isShowGeted = true;//已领过
            _this.isShowOldFriend = false;//老用户
            showReceivedDlg();
            break;
        case 1002:
            //老用户
            _this.isHome = true;//首页
            _this.isSuccess = false;//领券成功
            _this.isShowGeted = false;//已领过
            _this.isShowOldFriend = true;//老用户
            showOldFriendDlg();
            break;
        case 0:
            alert('系统异常，请稍后再试！');
            break;
        case 2:
            alert('对不起，太抢手，券已抢光！');
            break;
        case 3:
            alert('请输入正确的验证码！');
            break;
        default :
            alert('请输入正确的验证码！');
    }
}

function getUrlParam(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
    var r = window.location.search.substr(1).match(reg);
    if (r !== null) return unescape(r[2]);
    return null;
}

function formatDate(da) {
    var year = da.getFullYear();
    var month = da.getMonth() + 1;
    var date = da.getDate();
    return year + '.' + month + '.' + date;
}

//======================================================================================================================
//  dialog
//======================================================================================================================

/**
 * Created by jinliangshan on 2017/9/1.
 */

// $(function(){

/**
 * 已领过
 */
function showReceivedDlg() {
    showFailDialog('你已领取过', '不要太贪心喔');
}

/**
 * 老朋友
 */
function showOldFriendDlg() {
    showFailDialog('Hi,老朋友', '新人礼券已经不适合你啦');
}

function showFailDialog(status, tip) {
    console.log(status + tip);

    $('.dialog .tip-container .status').text(status);
    $('.dialog .tip-container .tip').text(tip);

    $('.mask').show();
    $('.dialog').show();
}

function dismissFailDialog() {
    $('.mask').hide();
    $('.dialog').hide();
}

$('.dialog .close').click(function () {
    dismissFailDialog();
});

// });

/**
 * Created by jinliangshan on 2017/9/1.
 */
// $(function(){

function showSuccessDialog(couponList) {
    var item = $('.dialog-success .section02 .item-coupon');
    var parent = $('.dialog-success .section02 >ul');
    var clone;

    parent.empty();
    for (var i = 0; i < couponList.length; i++) {
        clone = item.clone();
        clone.appendTo(parent);
        clone.find('.price').html(couponList[i].faceValue);
        clone.find('.price-tips').html(couponList[i].titleExtra);
        clone.find('.tips').first().html(couponList[i].tips);
        clone.find('.tips .valid-date').html(couponList[i].validityTime);
    }

    $('.mask').show();
    $('.dialog-success').show();
}

// $('.mask').click(function () {
//     $('.mask').hide()
//     $('.dialog-success').hide()
// });

$('.dialog-success .close').click(function () {
    $('.mask').hide();
    $('.dialog-success').hide();
});
// });
