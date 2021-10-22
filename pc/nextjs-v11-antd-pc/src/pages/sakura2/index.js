import Head from "next/head";
import fetch from "isomorphic-unfetch";
import React from "react";
import {Linear, Sine, TweenLite, TweenMax} from "gsap";
import css from "./index.module.scss";
import {withRouter} from "next/router";

// https://codepen.io/MAW/pen/KdmwMb
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
        let falling = true;

        let total = 300;
        let container = document.getElementById("container"), w = window.innerWidth*0.8, h = window.innerHeight*0.6;

        console.log("[LIFECYCLE](Sakura) componentDidMount", container);
        TweenLite.set(container, {perspective: 600});

        function R(min, max) {
            return min + Math.random() * (max - min);
        }

        function anim(element) {
            TweenMax.to(element, R(10, 30), {y: h + 100, ease: Linear.easeNone, repeat: -1, delay: -10});
            TweenMax.to(element, R(8, 20), {x: "-=200", rotationZ: R(0, 180), repeat: -1, yoyo: true, ease: Sine.easeInOut});
            TweenMax.to(element, R(4, 10), {rotationX: R(0, 360), rotationY: R(0, 360), repeat: -1, yoyo: true, ease: Sine.easeInOut, delay: -5});
        }

        for (let i = 0; i < total; i++) {
            let Div = document.createElement("div");
            TweenLite.set(Div, {attr: {class: i % 2 === 0 ? css.dot_a : css.dot_b}, x: R(0, w), y: R(-100, 0), z: R(-200, 200)});
            container.appendChild(Div);
            anim(Div);
        }
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
        // let props = this.props;
        return (
            <React.Fragment>
                <Head>
                    <title>SAKURA</title>
                </Head>
                <div className={css.page} id="container">
                </div>
            </React.Fragment>
        );
    }
}

export default withRouter(Sakura);
