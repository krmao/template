import React from "react";
import ZoomControl from "react-bmapgl/Control/ZoomControl";
import Map, {MapApiLoaderHOC} from "react-bmapgl/Map";
import Marker from "react-bmapgl/Overlay/Marker";

class MapAddressBaidu extends React.Component {
    render() {
        let centerLatLng = {lat: 31.14826, lng: 121.67196};
        return (
            <React.Fragment>
                <Map
                    style={{width: "100%", height: "100%", marginTop: "10px", zIndex: 0}}
                    center={centerLatLng}
                    enableScrollWheelZoom={true}
                    zoom={14}
                >
                    <ZoomControl />
                    <Marker position={centerLatLng} enableDragging />
                </Map>
            </React.Fragment>
        );
    }
}

export default MapApiLoaderHOC({ak: "63MXyvTnyUzmSEIjUhZQcxDhuH8c5yPI"})(MapAddressBaidu);
