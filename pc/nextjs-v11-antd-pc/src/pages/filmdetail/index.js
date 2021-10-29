import Head from "next/head";
import fetch from "isomorphic-unfetch";
import React from "react";
import css from "./index.module.scss";

class FilmDetail extends React.Component {
    static async getInitialProps(context) {
        // console.log("[LIFECYCLE](FilmDetail) getInitialProps");
        const {id} = context.query;
        const res = await fetch(`https://api.tvmaze.com/shows/${id}`);
        const show = await res.json();
        return {show};
    }

    constructor(props) {
        super(props);
        this.state = {};
        console.log("[LIFECYCLE](FilmDetail) constructor");
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        console.log("[LIFECYCLE](FilmDetail) getDerivedStateFromProps");
        if (!prevState || prevState.shows !== nextProps.shows) {
            return {
                shows: nextProps.shows
            };
        } else {
            return null;
        }
    }

    componentDidMount() {
        console.log("[LIFECYCLE](FilmDetail) componentDidMount");
    }

    shouldComponentUpdate(nextProps, nextState) {
        let shouldComponentUpdate = this.state !== nextState;
        console.log(
            "[LIFECYCLE](FilmDetail) shouldComponentUpdate=",
            shouldComponentUpdate,
            "nextProps=",
            nextProps,
            "currentState=",
            this.state,
            "nextState=",
            nextState
        );
        return shouldComponentUpdate;
    }

    getSnapshotBeforeUpdate() {
        console.log("[LIFECYCLE](FilmDetail) getSnapshotBeforeUpdate");
        return null;
    }

    componentDidUpdate(prevProps, prevState) {
        console.log("[LIFECYCLE](FilmDetail) prevProps=", prevProps, "prevState=", prevState);
    }

    static getDerivedStateFromError() {
        console.log("[LIFECYCLE](FilmDetail) getDerivedStateFromError");
    }

    componentDidCatch() {
        console.log("[LIFECYCLE](FilmDetail) componentDidCatch");
    }

    componentWillUnmount() {
        console.log("[LIFECYCLE](FilmDetail) componentWillUnmount");
    }

    render() {
        console.log("[LIFECYCLE](FilmDetail) render");
        let props = this.props;
        return (
            <React.Fragment>
                <Head>
                    <title>FILM DETAIL</title>
                </Head>
                <div className={css.page}>
                    <h1 className={css.title}>{props && props.show && props.show.name ? props.show.name : ""}</h1>
                    <p className={css.content}>
                        {props && props.show && props.show.summary ? (
                            <div dangerouslySetInnerHTML={{__html: props.show.summary}} />
                        ) : (
                            ""
                        )}
                    </p>

                    <div className={css.image_container}>
                        <img className={css.image_qr_code} src="/template_qr_code.png" alt="二维码" />
                        {props && props.show && props.show.image != null && props.show.image.medium != null ? (
                            <img className={css.image_content} src={props.show.image.medium} alt={""} />
                        ) : null}
                    </div>
                </div>
            </React.Fragment>
        );
    }
}

export default FilmDetail;
