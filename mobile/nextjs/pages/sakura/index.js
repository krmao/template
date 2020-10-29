import Head from "next/head";
import fetch from "isomorphic-unfetch";
import React from "react";
import css from "./index.scss";
import {withRouter} from "next/router";
import AnimateSakura from "../../components/library/utils/animate_sakura.js";

class Sakura extends React.Component {

    static async getInitialProps(context) {
        console.log("[LIFECYCLE](Sakura) getInitialProps");
        const {id} = context.query;
        const res = await fetch(`https://api.tvmaze.com/shows/${id}`);
        const show = await res.json();
        return {show};
    }

    constructor(props) {
        super(props);
        this.state = {
            sakuraAnimateFlag: true,
            sakuraArr: [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
        };
        this.sakuraImgArr = [];
        console.log("[LIFECYCLE](Sakura) constructor");
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        console.log("[LIFECYCLE](Sakura) getDerivedStateFromProps");
        if (!prevState || prevState.shows !== nextProps.shows) {
            return {
                shows: nextProps.shows
            };
        } else {
            return null;
        }
    }

    componentDidMount() {
        console.log("[LIFECYCLE](Sakura) componentDidMount");
        let that = this;
       /* setTimeout(() => {
            that.setState({
                sakuraAnimateFlag: false
            });
        }, 7000);*/

    }

    shouldComponentUpdate(nextProps, nextState) {
        let shouldComponentUpdate = this.state !== nextState;
        console.log("[LIFECYCLE](Sakura) shouldComponentUpdate=", shouldComponentUpdate, "nextProps=", nextProps, "currentState=", this.state, "nextState=", nextState);
        return shouldComponentUpdate;
    }

    getSnapshotBeforeUpdate() {
        console.log("[LIFECYCLE](Sakura) getSnapshotBeforeUpdate");
        return null;
    }

    componentDidUpdate(prevProps, prevState) {
        console.log("[LIFECYCLE](Sakura) prevProps=", prevProps, "prevState=", prevState);
    }

    static getDerivedStateFromError() {
        console.log("[LIFECYCLE](Sakura) getDerivedStateFromError");
    }

    componentDidCatch() {
        console.log("[LIFECYCLE](Sakura) componentDidCatch");
    }

    componentWillUnmount() {
        console.log("[LIFECYCLE](Sakura) componentWillUnmount");
    }

    render() {
        console.log("[LIFECYCLE](Sakura) render");
        let props = this.props;
        return (
            <React.Fragment>
                <Head>
                    <title>SAKURA</title>
                </Head>
                <div className={css.page}>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                    <AnimateSakura sakuraDepData={{sakuraImgArr: this.sakuraImgArr}} animated={true}/>
                </div>
            </React.Fragment>
        );
    }
}

export default withRouter(Sakura);
