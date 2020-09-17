import React from "react";

/**
 * 404.js Cannot Have getInitialProps
 *
 * https://github.com/vercel/next.js/blob/master/errors/404-get-initial-props.md
 */
export default class extends React.Component {
    render() {
        return <p>404 error occurred on client</p>;
    }
}
