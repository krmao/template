import React from "react";
import logo from "./logo.svg";
import "./App.css";
import Repository from "./repository/Repository";

function App() {
    Repository.getDirList(
        {userName: "krmao", userPwd: "123456"},
        function(data) {
            console.log("oh got success:", data);
        },
        function(error) {
            console.log("oh got error:", error);
        }
    );
    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <p>
                    Edit <code>src/App.js</code> and save to reload.
                </p>
                <a className="App-link" href="https://reactjs.org" target="_blank" rel="noopener noreferrer">
                    Learn React
                </a>
            </header>
        </div>
    );
}

export default App;
