"use strict";

/**
 * 手机端页面自适应高清方案(UI 视觉效果图推荐尺寸 640x1136 750x1334)
 *
 * @link https://segmentfault.com/a/1190000007350680
 *
 * @param {Boolean} [normal = false] - 默认开启页面压缩以使页面高清;
 * @param {Number} [baseFontSize = 100] - 基础fontSize, 默认100px;
 * @param {Number} [fontScale = 1] - 有的业务希望能放大一定比例的字体;
 */
const win = window;
/*export default */
win.wrap = (normal, baseFontSize, fontScale) => {
    const _baseFontSize = baseFontSize || 100;
    const _fontScale = fontScale || 1;

    const doc = win.document;
    const ua = navigator.userAgent;
    const matches = ua.match(/Android[\S\s]+AppleWebkit\/(\d{3})/i);
    const UCVersion = ua.match(/U3\/((\d+|\.){5,})/i);
    const isUCHd = UCVersion && parseInt(UCVersion[1].split(".").join(""), 10) >= 80;
    const isIos = navigator.appVersion.match(/(iphone|ipad|ipod)/gi);
    let dpr = win.devicePixelRatio || 1;
    if (!isIos && !(matches && matches[1] > 534) && !isUCHd) {
        dpr = 1; // 如果非iOS, 非Android4.3以上, 非UC内核, 就不执行高清, dpr设为1
    }
    const scale = normal ? 1 : 1 / dpr;

    // eslint-disable-next-line quotes
    let metaEl = doc.querySelector('meta[name="view port"]');
    if (!metaEl) {
        metaEl = doc.createElement("meta");
        metaEl.setAttribute("name", "viewport");
        doc.head.appendChild(metaEl);
    }
    metaEl.setAttribute("content", `width=device-width,user-scalable=no,initial-scale=${scale},maximum-scale=${scale},minimum-scale=${scale}`);
    doc.documentElement.style.fontSize = normal ? "50px" : `${(_baseFontSize / 2) * dpr * _fontScale}px`;
};
// 640x1136 750x1334 尺寸的视觉稿 baseFontSize 为 100,      则 1rem = 100px, 因为drp=2(即1rem=100px)
// 1242x2208         尺寸的视觉稿 baseFontSize 为 66.66667, 则 1rem = 100px, 因为drp=3(即1rem=150px), 所以设置 66.66667
win.wrap(false, 100, 1);
