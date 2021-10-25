/**
 * https://www.nextjs.cn/docs/api-reference/next/router
 */
import Router from "next/router";

export default class BasicNavigator {
    static push = (url, mode) => {
        if (mode === "spa") {
            // noinspection JSIgnoredPromiseFromCall
            Router.push(url);
        } else {
            window.location.href = `${window.location.origin}/${url}`;
        }
    };

    static back = (url, mode) => {
        if (mode === "spa") {
            Router.back();
        } else {
            window.history.back();
        }
    };
}
