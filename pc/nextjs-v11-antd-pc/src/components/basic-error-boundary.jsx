import React from "react";
import EnvUtil from "@utils/basic-env-util";
import PropTypes from "prop-types";

export default class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = {error: null, errorInfo: null};
    }

    static getDerivedStateFromError(error) {
        return {error: error};
    }

    componentDidCatch(error, errorInfo) {
        this.setState({
            error: error,
            errorInfo: errorInfo
        });
    }

    render() {
        if (this.state.errorInfo) {
            if (EnvUtil.getEnv() === EnvUtil.PROD) {
                const ReplaceComplement = this.props.replaceComplement;
                if (!!ReplaceComplement) {
                    return <ReplaceComplement />;
                } else {
                    return <h2>Something went wrong.</h2>;
                }
            } else {
                return (
                    <div
                        style={{
                            width: "100%",
                            height: "100%",
                            padding: 20,
                            background: "#e7e7e7"
                        }}>
                        <h2>Something went wrong.</h2>
                        <details
                            open={false}
                            style={{
                                overflowY: "scroll",
                                overflowX: "scroll",
                                maxHeight: 200,
                                padding: 20,
                                whiteSpace: "pre-wrap"
                            }}>
                            {this.state.error?.toString()}
                            <br />
                            {this.state.errorInfo?.componentStack}
                        </details>
                    </div>
                );
            }
        }
        return this.props.children;
    }
}

ErrorBoundary.propTypes = {
    /**
     * 生产环境显示的降级 UI
     *
     *  const GoogleMapAddress = () => (
     *      <MapAddressGoogle
     *          centerLatLng={centerLatLng}
     *          zoomLevel={14}
     *          onMapClickListener={(e) => {
     *              this._onMapClickListener(e);
     *          }}
     *      />
     *  );
     */
    replaceComplement: PropTypes.oneOfType([PropTypes.func])
};
