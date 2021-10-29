/**
 * 导航器
 *
 * https://www.nextjs.cn/docs/routing/introduction
 * https://www.nextjs.cn/docs/api-reference/next/router
 * https://www.nextjs.cn/docs/api-reference/next/link
 */
import Router from "next/router";

export default class BasicNavigator {
    // 浏览器返回必刷新
    static pushWithLocationHref = (url) => (window.location.href = `${window.location.origin}/${url}`);
    // 仿 spa, 浏览器返回不刷新
    static pushWithRouter = (url, as, options) => Router.push(url, as, options);
    static push = (url, as, options) => BasicNavigator.pushWithRouter(url, as, options);
    static backWithHistory = () => window.history.back();
    static backWithRouter = () => Router.back();
    static back = () => BasicNavigator.back();
}
