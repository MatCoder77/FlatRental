import React, { Component } from 'react';
import { login} from "../infrastructure/RestApiHandler";
import './LoginForm.css';
import { Link } from 'react-router-dom';
import { ACCESS_TOKEN} from "../Constants";
import { Form, Input, Button, Icon, notification } from 'antd';
import {injectIntl} from 'react-intl';
import {FormattedMessage} from "react-intl";
const FormItem = Form.Item;

class LoginForm extends Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);

        this.somethingWentWrongMessage = this.props.intl.formatMessage({ id: 'text.something_went_wrong_msg' });
        this.incorrectUserOrPasswordMessage = this.props.intl.formatMessage({ id: 'text.login_failed' });
        this.inputUsernameOrEmailMessage = this.props.intl.formatMessage({ id: 'text.input_username_or_email' });
        this.inputPasswordMessage = this.props.intl.formatMessage({ id: 'text.input_password_msg' });
        this.password = this.props.intl.formatMessage({ id: 'text.input_password_msg' });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const loginRequest = Object.assign({}, values);
                login(loginRequest)
                    .then(response => {
                        localStorage.setItem(ACCESS_TOKEN, response.accessToken);
                        this.props.onLogin();
                    }).catch(error => {
                    if(error.status === 401) {
                        notification.error({
                            message: 'Flat Rental',
                            description: this.incorrectUserOrPasswordMessage
                        });
                    } else {
                        notification.error({
                            message: 'Flat Rental',
                            description: error.message || this.somethingWentWrongMessage
                        });
                    }
                });
            }
        });
    }

    render() {
        const { getFieldDecorator } = this.props.form;
        return (
            <Form onSubmit={this.handleSubmit} className="login-form">
                <FormItem>
                    {getFieldDecorator('usernameOrEmail', {
                        rules: [{ required: true, message: this.inputUsernameOrEmailMessage }],
                    })(
                        <Input
                            prefix={<Icon type="user" />}
                            size="large"
                            name="usernameOrEmail"
                            placeholder= {this.props.intl.formatMessage({ id: 'labels.username_or_email' })} />
                    )}
                </FormItem>
                <FormItem>
                    {getFieldDecorator('password', {
                        rules: [{ required: true, message: this.inputPasswordMessage }],
                    })(
                        <Input
                            prefix={<Icon type="lock" />}
                            size="large"
                            name="password"
                            type="password"
                            placeholder= {this.props.intl.formatMessage({ id: 'labels.password' })}  />
                    )}
                </FormItem>
                <FormItem>
                    <Button type="primary" htmlType="submit" size="large" className="login-form-button"><FormattedMessage id="labels.login_button"/></Button>
                    <FormattedMessage id="labels.or"/> <Link to="/signup"><FormattedMessage id="labels.register_now"/></Link>
                </FormItem>
            </Form>
        );
    }
}

export default injectIntl(LoginForm);