import Link from "next/link";
import Head from "next/head";
import fetch from "isomorphic-unfetch";
import React from "react";
import css from "./index.scss";
import {withRouter} from "next/router";

class Test2 extends React.Component {

    /**
     * 服务端首先执行 第一步
     * > 浏览器下载该 js 之前在服务端(Node 后端)预先执行的代码, 这样下载到浏览器后就不用再次执行
     *
     * @return 将返回的数据存放到 props 中传递给构造函数 constructor
     */
    static async getInitialProps() {
        console.log("[LIFECYCLE](Test2) getInitialProps");
        const res = await fetch("https://api.tvmaze.com/search/shows?q=marvel");
        const data = await res.json();
        return {
            shows: data
        };
    }

    constructor(props) {
        super(props);
        this.state = {};
        console.log("[LIFECYCLE](Test2) constructor"); // props=", props, "state=", this.state);
    }

    /**
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
        console.log("[LIFECYCLE](Test2) getDerivedStateFromProps");// nextProps=", nextProps, "prevState=", prevState);
        if (!prevState || prevState.shows !== nextProps.shows) {
            return {
                shows: nextProps.shows
            };
        } else {
            return null;
        }
    }

    componentDidMount() {
        console.log("[LIFECYCLE](Test2) componentDidMount");
    }

    shouldComponentUpdate(nextProps, nextState) {
        let shouldComponentUpdate = this.state !== nextState;
        console.log("[LIFECYCLE](Test2) shouldComponentUpdate=", shouldComponentUpdate, "nextProps=", nextProps, "currentState=", this.state, "nextState=", nextState);
        return shouldComponentUpdate;
    }

    getSnapshotBeforeUpdate() {
        console.log("[LIFECYCLE](Test2) getSnapshotBeforeUpdate");
        return null;
    }

    componentDidUpdate(prevProps, prevState) {
        console.log("[LIFECYCLE](Test2) prevProps=", prevProps, "prevState=", prevState);
    }

    static getDerivedStateFromError() {
        console.log("[LIFECYCLE](Test2) getDerivedStateFromError");
    }

    componentDidCatch() {
        console.log("[LIFECYCLE](Test2) componentDidCatch");
    }

    componentWillUnmount() {
        console.log("[LIFECYCLE](Test2) componentWillUnmount");
    }

    render() {
        console.log("[LIFECYCLE](Test2) render");
        return (
            <React.Fragment>
                <Head>
                    <title>TEST 2</title>
                    <meta name="viewport" content="width=device-width"/>
                </Head>
                <div className={css.container}>
                    <h1 className={css.title}>Marvel TV Shows</h1>
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

export default withRouter(Test2);
