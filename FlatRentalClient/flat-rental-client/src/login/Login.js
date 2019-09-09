import React, {Component} from "react";
import {Form} from "antd";
import {FormattedMessage} from "react-intl";
import LoginFrom from "./LoginFrom";

class Login extends Component {
    render() {
        const AntWrappedLoginForm = Form.create()(LoginFrom)
        return (
            <div className="login-container">
                <h1 className="page-title"><FormattedMessage id="labels.login"/></h1>
                <div className="login-content">
                    <AntWrappedLoginForm onLogin={this.props.onLogin} />
                </div>
            </div>
        );
    }
}

export default Login