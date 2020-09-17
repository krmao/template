import Link from "next/link";
import Head from "next/head";
import fetch from "isomorphic-unfetch";
import React from "react";
import css from "./index.scss";
import {withRouter} from "next/router";

/**
 * https://my.oschina.net/chkui/blog/3003767
 *
 * 1 客户端(浏览器)首次请求打开页面 前后端的执行过程
 * > 1.1 客户端(浏览器)向服务端(Node Server)请求获取 test.html 文件
 *      > 1.1.1 Request URL: http://localhost:5388/test
 * > 1.2 服务端(Node Server)监听到客户端(浏览器)的该次请求, 生成并返回可被客户端(浏览器)直接渲染的 test.html 文件, 经历了了以下生命周期
 *      > 1.2.1 [LIFECYCLE](App) getInitialProps
 *      > 1.2.2 [LIFECYCLE](Test) getInitialProps
 *      > 1.2.3 [LIFECYCLE](Document) getInitialProps
 *      > 1.2.4 [LIFECYCLE](App) constructor
 *      > 1.2.5 [LIFECYCLE](App) render
 *      > 1.2.6 [LIFECYCLE](Test) constructor
 *      > 1.2.7 [LIFECYCLE](Test) getDerivedStateFromProps
 *      > 1.2.8 [LIFECYCLE](Test) render
 *      > 1.2.9 [LIFECYCLE](Document) constructor
 *      > 1.2.10 [LIFECYCLE](Document) render
 * > 1.3 客户端(浏览器)获取到服务端(Node Server)返回的 test.html 文件之后, 解析并下载 test.html 中引用的静态文件(js/css/image), 然后执行渲染
 *      > 1.3.1 客户端(浏览器)解析并下载 test.html 中引用的静态文件(js/css/image)
 *          > 1.3.1.1 Request URL: http://localhost:5388/_next/static/development/pages/test.js?ts=1600326499490
 *          > 1.3.1.2 Request URL: http://localhost:5388/_next/static/development/pages/_app.js?ts=1600326499490
 *          > 1.3.1.3 Request URL: http://localhost:5388/_next/static/runtime/webpack.js?ts=1600326499490
 *          > 1.3.1.4 Request URL: http://localhost:5388/_next/static/chunks/styles.js?ts=1600326499490
 *          > 1.3.1.5 Request URL: http://localhost:5388/_next/static/runtime/main.js?ts=1600326499490
 *          > 1.3.1.6 Request URL: http://localhost:5388/_next/static/css/styles.chunk.css
 *          > 1.3.1.7 Request URL: http://localhost:5388/_next/static/development/dll/dll_599a58a60c43245180de.js?ts=1600326499490
 *          > 1.3.1.8 Request URL: http://localhost:5388/_next/static/chunks/0.js
 *          > 1.3.1.9 Request URL: http://localhost:5388/_next/webpack-hmr
 *          > 1.3.1.10 Request URL: http://localhost:5388/_next/on-demand-entries-ping?page=/test
        > 1.3.2 执行渲染, 经历了了以下生命周期
 *          > 1.3.2.1 [LIFECYCLE](App) constructor
 *          > 1.3.2.2 [LIFECYCLE](App) render
 *          > 1.3.2.3 [LIFECYCLE](Test) constructor
 *          > 1.3.2.4 [LIFECYCLE](Test) getDerivedStateFromProps
 *          > 1.3.2.5 [LIFECYCLE](Test) render
 *          > 1.3.2.6 [LIFECYCLE](Test) componentDidMount
 *
 * 2 客户端(浏览器)内页跳转(next/link)执行过程
 * > 2.1 客户端(浏览器)向服务端(Node Server)请求获取 test2.html 文件
 *      > 2.1.1 Request URL: http://localhost:5388/_next/static/development/pages/test2.js
 * > 2.2 服务端(Node Server)监听到客户端(浏览器)的该次请求, 由于非首屏无需预渲染, 直接返回 test2.html 文件
 * > 2.3 客户端(浏览器)获取到服务端(Node Server)返回的 test2.html 文件之后, 解析并下载 test2.html 中引用的静态文件(js/css/image), 然后执行渲染
 *      > 2.3.1 客户端(浏览器)解析并下载 test2.html 中引用的静态文件(js/css/image)
 *          > 2.3.1.1 Request URL: https://api.tvmaze.com/search/shows?q=marvel
 *          > 2.3.1.2 Request URL: http://localhost:5388/_next/on-demand-entries-ping?page=/test2
 *      > 2.3.2 执行渲染, 经历了了以下生命周期
 *          > 2.3.2.1 [LIFECYCLE](App) getInitialProps
 *          > 2.3.2.2 [LIFECYCLE](Test2) getInitialProps
 *          > 2.3.2.3 [LIFECYCLE](App) render
 *          > 2.3.2.4 [LIFECYCLE](Test2) constructor
 *          > 2.3.2.5 [LIFECYCLE](Test2) getDerivedStateFromProps
 *          > 2.3.2.6 [LIFECYCLE](Test2) render
 *          > 2.3.2.7 [LIFECYCLE](Test) componentWillUnmount
 *          > 2.3.2.8 [LIFECYCLE](Test2) componentDidMount
 */
class Test extends React.Component {

    /**
     * 浏览器下载该 js 之前在服务端(Node 后端)预先执行的代码, 这样下载到浏览器后就不用再次执行
     *
     * @return 将返回的数据存放到 props 中传递给构造函数 constructor
     */
    static async getInitialProps() {
        console.log("[LIFECYCLE](Test) getInitialProps");
        const res = await fetch("https://api.tvmaze.com/search/shows?q=marvel");
        const data = await res.json();
        return {
            shows: data
        };
    }

    constructor(props) {
        super(props);
        this.state = {};
        console.log("[LIFECYCLE](Test) constructor"); // props=", props, "state=", this.state);
    }

    /**
     * 两者不相等, 更新 state
     *
     * 将传入的props映射到state上面
     * 在每次re-rendering之前被调用
     * 替代componentWillReceiveProps
     * 不能通过this访问到class的属性
     * 如果props传入的内容不需要影响 state，那么就需要返回一个null，这个返回值是必须的，所以尽量将其写到函数的末尾
     * props在很短的时间内多次变化，也只会触发一次render，也就是只触发一次getDerivedStateFromProps。这样的优点不言而喻。
     * 为了响应props的变化，就需要在componentDidUpdate中根据新的props和state来进行异步操作，比如从服务端拉取数据。
     *
     * @param nextProps
     * @param prevState
     */
    static getDerivedStateFromProps(nextProps, prevState) {
        console.log("[LIFECYCLE](Test) getDerivedStateFromProps");
        if (!prevState || prevState.shows !== nextProps.shows) {
            return {
                shows: nextProps.shows
            };
        } else {
            return null;
        }
    }

    componentDidMount() {
        console.log("[LIFECYCLE](Test) componentDidMount");
    }

    shouldComponentUpdate(nextProps, nextState) {
        let shouldComponentUpdate = this.state !== nextState;
        console.log("[LIFECYCLE](Test) shouldComponentUpdate=", shouldComponentUpdate, "nextProps=", nextProps, "currentState=", this.state, "nextState=", nextState);
        return shouldComponentUpdate;
    }

    getSnapshotBeforeUpdate() {
        console.log("[LIFECYCLE](Test) getSnapshotBeforeUpdate");
        return null;
    }

    componentDidUpdate(prevProps, prevState) {
        console.log("[LIFECYCLE](Test) prevProps=", prevProps, "prevState=", prevState);
    }

    static getDerivedStateFromError() {
        console.log("[LIFECYCLE](Test) getDerivedStateFromError");
    }

    componentDidCatch() {
        console.log("[LIFECYCLE](Test) componentDidCatch");
    }

    componentWillUnmount() {
        console.log("[LIFECYCLE](Test) componentWillUnmount");
    }

    render() {
        console.log("[LIFECYCLE](Test) render");
        return (
            <React.Fragment>
                <Head>
                    <title>TEST 1</title>
                    <meta name="viewport" content="width=device-width"/>
                </Head>
                <div className={css.container}>
                    <h1 className={css.title}><Link href={"/test2"}>
                        <a>Marvel TV Shows</a>
                    </Link></h1>
                    <ul>
                        {this.props && this.props.shows
                            ? this.props.shows.map(({show}) => {
                                return (
                                    <li key={show.id}>
                                        <Link href={`/film/detail?id=${show.id}`}>
                                            <a>{show.name}</a>
                                        </Link>
                                    </li>
                                );
                            })
                            : null}
                    </ul>
                </div>
            </React.Fragment>
        );
    }
}

export default withRouter(Test);
