import Link from "next/link";
import Head from "next/head";
import fetch from "isomorphic-unfetch";
import React from "react";
import css from "./index.scss";

class Index extends React.Component {
    static async getInitialProps() {
        const res = await fetch("https://api.tvmaze.com/search/shows?q=marvel");
        const data = await res.json();
        return {
            shows: data
        };
    }

    constructor(props) {
        super(props);
        console.log("router->", this.props.router);
    }

    render() {
        return (
            <div>
                <Head>
                    <title>TT婚纱摄影</title>
                    <meta name="viewport" content="width=device-width" />
                </Head>
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
        );
    }
}

export default Index;
