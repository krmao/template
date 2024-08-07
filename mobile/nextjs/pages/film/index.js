import Link from "next/link";
import Head from "next/head";
import fetch from "isomorphic-unfetch";
import React from "react";
import css from "./index.scss";
import {withRouter} from "next/router";
import {Button, Carousel, Toast, WhiteSpace, WingBlank} from "antd-mobile";

/**
 * https://my.oschina.net/chkui/blog/3003767
 *
 * 1 客户端(浏览器)首次请求打开页面 前后端的执行过程
 * > 1.1 客户端(浏览器)向服务端(Node Server)请求获取 film.html 文件
 *      > 1.1.1 Request URL: http://localhost:5388/film
 * > 1.2 服务端(Node Server)监听到客户端(浏览器)的该次请求, 生成并返回可被客户端(浏览器)直接渲染的 film.html 文件, 经历了了以下生命周期
 *      > 1.2.1 [LIFECYCLE](App) getInitialProps
 *      > 1.2.2 [LIFECYCLE](Film) getInitialProps
 *      > 1.2.3 [LIFECYCLE](Document) getInitialProps
 *      > 1.2.4 [LIFECYCLE](App) constructor
 *      > 1.2.5 [LIFECYCLE](App) render
 *      > 1.2.6 [LIFECYCLE](Film) constructor
 *      > 1.2.7 [LIFECYCLE](Film) getDerivedStateFromProps
 *      > 1.2.8 [LIFECYCLE](Film) render
 *      > 1.2.9 [LIFECYCLE](Document) constructor
 *      > 1.2.10 [LIFECYCLE](Document) render
 * > 1.3 客户端(浏览器)获取到服务端(Node Server)返回的 film.html 文件之后, 解析并下载 film.html 中引用的静态文件(js/css/image), 然后执行渲染
 *      > 1.3.1 客户端(浏览器)解析并下载 film.html 中引用的静态文件(js/css/image)
 *          > 1.3.1.1 Request URL: http://localhost:5388/_next/static/development/pages/film.js?ts=1600326499490
 *          > 1.3.1.2 Request URL: http://localhost:5388/_next/static/development/pages/_app.js?ts=1600326499490
 *          > 1.3.1.3 Request URL: http://localhost:5388/_next/static/runtime/webpack.js?ts=1600326499490
 *          > 1.3.1.4 Request URL: http://localhost:5388/_next/static/chunks/styles.js?ts=1600326499490
 *          > 1.3.1.5 Request URL: http://localhost:5388/_next/static/runtime/main.js?ts=1600326499490
 *          > 1.3.1.6 Request URL: http://localhost:5388/_next/static/css/styles.chunk.css
 *          > 1.3.1.7 Request URL: http://localhost:5388/_next/static/development/dll/dll_599a58a60c43245180de.js?ts=1600326499490
 *          > 1.3.1.8 Request URL: http://localhost:5388/_next/static/chunks/0.js
 > 1.3.2 客户端(浏览器)执行渲染, 经历了了以下生命周期
 *          > 1.3.2.1 [LIFECYCLE](App) constructor
 *          > 1.3.2.2 [LIFECYCLE](App) render
 *          > 1.3.2.3 [LIFECYCLE](Film) constructor
 *          > 1.3.2.4 [LIFECYCLE](Film) getDerivedStateFromProps
 *          > 1.3.2.5 [LIFECYCLE](Film) render
 *          > 1.3.2.6 [LIFECYCLE](Film) componentDidMount
 *
 * 2 客户端(浏览器)内页跳转(next/link)执行过程
 * > 2.1 客户端(浏览器)向服务端(Node Server)请求获取 detail.js 静态文件, 注意这里并不是 detail.html 文件
 *      > 2.1.1 Request URL: http://localhost:5388/_next/static/development/pages/detail.js
 * > 2.3 客户端(浏览器)获取到服务端(Node Server)返回的 detail.js 文件之后, 解析并下载 detail.js 中引用的静态文件(js/css/image), 然后执行渲染
 *      > 2.3.1 客户端(浏览器)加载 detail.js 文件并解析下载其引用的静态文件(js/css/image)
 *      > 2.3.2 客户端(浏览器)执行渲染, 经历了了以下生命周期
 *          > 2.3.2.1 [LIFECYCLE](App) getInitialProps
 *          > 2.3.2.2 [LIFECYCLE](FilmDetail) getInitialProps
 *              > 2.3.2.2.1 Request URL: https://api.tvmaze.com/search/shows?q=marvel
 *          > 2.3.2.3 [LIFECYCLE](App) render
 *          > 2.3.2.4 [LIFECYCLE](FilmDetail) constructor
 *          > 2.3.2.5 [LIFECYCLE](FilmDetail) getDerivedStateFromProps
 *          > 2.3.2.6 [LIFECYCLE](FilmDetail) render
 *          > 2.3.2.7 [LIFECYCLE](Film) componentWillUnmount
 *          > 2.3.2.8 [LIFECYCLE](FilmDetail) componentDidMount
 */
class Film extends React.Component {

    /**
     * 浏览器下载该 js 之前在服务端(Node 后端)预先执行的代码, 这样下载到浏览器后就不用再次执行
     *
     * @return 将返回的数据存放到 props 中传递给构造函数 constructor
     */
    static async getInitialProps(context) {
        console.log("[LIFECYCLE](Film) getInitialProps");
        const res = await fetch("https://api.tvmaze.com/search/shows?q=marvel");
        const data = await res.json();
        return {
            shows: data
        };
    }

    constructor(props) {
        super(props);
        this.state = {
            data: ["1", "2", "3"],
            imgHeight: 176
        };
        console.log("[LIFECYCLE](Film) constructor"); // props=", props, "state=", this.state);
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
        console.log("[LIFECYCLE](Film) getDerivedStateFromProps");
        if (!prevState || prevState.shows !== nextProps.shows) {
            return {
                shows: nextProps.shows
            };
        } else {
            return null;
        }
    }

    componentDidMount() {
        console.log("[LIFECYCLE](Film) componentDidMount");
        /*Toast.loading("Loading...", 30, () => {
            console.log("Load complete !!!");
        });

        setTimeout(() => {
            Toast.hide();
        }, 3000);*/

        setTimeout(() => {
            this.setState({
                data: ["AiyWuByWklrrUDlFignR", "TekJlZRVCjLFexlOCuWn", "IJOtIlfsYdTyaDTRVrLI"]
            });
        }, 100);
    }

    shouldComponentUpdate(nextProps, nextState) {
        let shouldComponentUpdate = this.state !== nextState;
        console.log("[LIFECYCLE](Film) shouldComponentUpdate=", shouldComponentUpdate, "nextProps=", nextProps, "currentState=", this.state, "nextState=", nextState);
        return shouldComponentUpdate;
    }

    getSnapshotBeforeUpdate() {
        console.log("[LIFECYCLE](Film) getSnapshotBeforeUpdate");
        return null;
    }

    componentDidUpdate(prevProps, prevState) {
        console.log("[LIFECYCLE](Film) prevProps=", prevProps, "prevState=", prevState);
    }

    static getDerivedStateFromError() {
        console.log("[LIFECYCLE](Film) getDerivedStateFromError");
    }

    componentDidCatch() {
        console.log("[LIFECYCLE](Film) componentDidCatch");
    }

    componentWillUnmount() {
        console.log("[LIFECYCLE](Film) componentWillUnmount");
    }

    render() {
        console.log("[LIFECYCLE](Film) render");

        const customIcon = () => (
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64" className="am-icon am-icon-md">
                <path fillRule="evenodd" d="M59.177 29.5s-1.25 0-1.25 2.5c0 14.47-11.786 26.244-26.253 26.244C17.206 58.244 5.417 46.47 5.417 32c0-13.837 11.414-25.29 25.005-26.26v6.252c0 .622-.318 1.635.198 1.985a1.88 1.88 0 0 0 1.75.19l21.37-8.545c.837-.334 1.687-1.133 1.687-2.384C55.425 1.99 53.944 2 53.044 2h-21.37C15.134 2 1.667 15.46 1.667 32c0 16.543 13.467 30 30.007 30 16.538 0 29.918-13.458 29.993-30 .01-2.5-1.24-2.5-1.24-2.5h-1.25"/>
            </svg>
        );

        return (
            <React.Fragment>
                <Head>
                    <title>FILM</title>
                </Head>
                <div className={css.page}>
                    <h1 className={css.title}>Marvel TV Shows</h1>
                    <div className={css.banner}>
                        <Carousel
                            autoplay={false}
                            dots={true}
                            infinite={true}
                            beforeChange={(from, to) => console.log(`slide from ${from} to ${to}`)}
                            afterChange={index => console.log("slide to", index)}
                        >
                            {this.state.data.map(val => (
                                <a
                                    key={val}
                                    href="http://www.alipay.com"
                                    style={{display: "inline-block", width: "100%", height: this.state.imgHeight}}
                                >
                                    <img
                                        src={`https://zos.alipayobjects.com/rmsportal/${val}.png`}
                                        alt=""
                                        style={{width: "100%", verticalAlign: "top", height: "120px"}}
                                        onLoad={() => {
                                            // fire window resize event to change height
                                            window.dispatchEvent(new Event("resize"));
                                            this.setState({imgHeight: "auto"});
                                        }}
                                    />
                                </a>
                            ))}
                        </Carousel>
                    </div>
                    <div className={css.content}>
                        {
                            this.props && this.props.shows
                                ? this.props.shows.map(({show}) => {
                                    return (
                                        <li key={show.id}>
                                            <Link href={`/film/detail?id=${show.id}`}>
                                                <a>{show.name}</a>
                                            </Link>
                                        </li>
                                    );
                                })
                                : null
                        }
                    </div>
                    <div className={css.toastContainer}>
                        <WingBlank size={"sm"}>
                            <WhiteSpace size={"sm"}/>
                            <Button size={"small"} type={"primary"} onClick={() => Toast.info("This is a toast tips !!!", 1)}>text only</Button>
                            <WhiteSpace size={"sm"}/>
                            <Button size={"small"} type={"primary"} onClick={() => Toast.info("Toast without mask !!!", 2, null, false)}>without mask</Button>
                            <WhiteSpace size={"sm"}/>
                            <Button size={"small"} type={"primary"} onClick={() => Toast.info(customIcon(), 1)}>custom icon</Button>
                            <WhiteSpace size={"sm"}/>
                            <Button size={"small"} type={"primary"} onClick={() => Toast.success("Load success !!!", 1)}>success</Button>
                            <WhiteSpace size={"sm"}/>
                            <Button size={"small"} type={"primary"} onClick={() => Toast.fail("Load failed !!!", 1)}>fail</Button>
                            <WhiteSpace size={"sm"}/>
                            <Button size={"small"} type={"ghost"} onClick={() => Toast.offline("Network connection failed !!!", 1)}>network failure</Button>
                            <WhiteSpace size={"sm"}/>
                            <Button size={"small"} type={"warning"} onClick={() => Toast.loading("Loading...", 1, () => console.log("Load complete !!!"))}>loading</Button>
                            <WhiteSpace size={"sm"}/>
                        </WingBlank>
                    </div>
                </div>
            </React.Fragment>
        );
    }
}

export default withRouter(Film);
