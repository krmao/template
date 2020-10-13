import React from "react";
import css from "./404.scss";

/**
 * 404.js Cannot Have getInitialProps
 *
 * https://github.com/vercel/next.js/blob/master/errors/404-get-initial-props.md
 */
export default class extends React.Component {
    render() {
        return <div className={css.page}>
            <img className={css.image_content} src="/static/404.jpeg" alt="404"/>
        </div>;
    }
}
