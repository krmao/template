class FSJSInterface {

    onBackPressed() {
        console.log("[html] onBackPressed()");
    }

    onResume() {
        console.log("[html] onResume()");
    }

    onPause() {
        console.log("[html] onPause()");
    }

    onResult(dataString) {
        console.log("[html] onResult()");
    }

    onNetworkStateChanged(available) {

    }

    /**
     * app is visible
     * @param visible true/false
     */
    onApplicationVisibleChanged(visible) {

    }
}

export default FSJSInterface
