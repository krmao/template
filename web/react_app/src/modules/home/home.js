import React from "react";
import {Link} from "react-router-dom";

import logo from "../../logo.svg";
import "./home.css";

function home() {
    return (
        <div className="home">
            <header className="home-header">
                <img src={logo} className="home-logo" alt="logo" />
                <p>
                    Edit <code>src/home.js</code> and save to reload.
                </p>
                <a className="home-link" href="https://reactjs.org" target="_blank" rel="noopener noreferrer">
                    Learn React
                </a>
                <Link className="home-link" to="/about">
                    Jump To About
                </Link>
                <Link className="home-link" to="/mine">
                    Jump To Mine
                </Link>
                <Link className="home-link" to="/settings">
                    Jump To Settings
                </Link>
            </header>
        </div>
    );
}

export default home;
