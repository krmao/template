import logo from './logo.svg';
import './App.css';
import MapAddressBaidu from "./MapComponentBaidu";

function App() {
    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    Edit <code>src/App.js</code> and save to reload.
                </p>
                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
                <div style={{width: "800px", height: "300px", margin: "30px", zIndex: 0}}>
                    <MapAddressBaidu
                        style={{width: "100%", height: "100%", marginTop: "10px", zIndex: 0}}
                        centerLatLng={{lat: 31.14826, lng: 121.67196}}
                        zoomLevel={14}
                        onMapClickListener={(e) => {
                        }}
                    />
                </div>
            </header>
        </div>
    );
}

export default App;
