import React from 'react'
import Head from 'next/head'
import Link from 'next/link'

import css from './index.scss'

export default class extends React.Component {

    render() {
        return (
            <div className={css.root}>
                <Head>
                    <title>大神灭霸</title>
                    <meta name="viewport" content="initial-scale=1.0, width=device-width" key="viewport"/>
                </Head>

                <p>Hello world!</p>

                <div>
                    Click{' '}
                    <Link href={{pathname: '/about', query: {name: 'krmao'}}} passHref>
                        <a>
                            <img src="../static/logo.jpg" width={'30px'} height={'30px'} alt="logo"/>
                        </a>
                    </Link>{' '}
                    to read more
                </div>
            </div>
        )
    }
}