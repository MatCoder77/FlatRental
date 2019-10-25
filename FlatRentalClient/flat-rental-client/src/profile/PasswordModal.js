import {Modal, Card, Col, List, Row, Button, Input, Form, notification} from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';
import './Profile.css';
import * as CONS from "../infrastructure/Constants";
import {changePassword} from "../infrastructure/RestApiHandler";

const FormItem = Form.Item;

class PasswordModal extends React.Component {

    constructor(props) {
        super(props);

        this.isFormValid = this.isFormValid.bind(this);
        this.validateIfNotEmpty = this.validateIfNotEmpty.bind(this);
        this.validatePassword = this.validatePassword.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.onCancel = this.onCancel.bind(this);

        this.state = {
            password: {value: undefined, errorMsg: null, validateStatus: undefined},
            newPassword: {value: undefined, errorMsg: null, validateStatus: undefined}
        };

        this.passwordIsTooShortMessage = this.props.intl.formatMessage({ id: 'text.password_too_short_msg' }, { min: CONS.PASSWORD_MIN_LENGTH });
        this.passwordIsTooLongMessage = this.props.intl.formatMessage({ id: 'text.password_too_long_msg' }, { max: CONS.PASSWORD_MAX_LENGTH });

    }

    handleInputChange(event, validationFun) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;
        const validationResult = validationFun ? validationFun(inputValue) : undefined;

        this.setState({
            [inputName] : {
                value: inputValue,
                ...validationResult
            }
        });
    }

    validateIfNotEmpty = (input) => {
        if (input) {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
        return {
            validateStatus: undefined,
            errorMsg: null,
        };
    };

    validatePassword = (password) => {
        if(password.length < CONS.PASSWORD_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.passwordIsTooShortMessage
            }
        } else if (password.length > CONS.PASSWORD_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.passwordIsTooLongMessage
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    }

    isFormValid() {
        return this.state.password.validateStatus === 'success' && this.state.newPassword.validateStatus === 'success';
    }

    onSubmit() {
        this.state.password = {value: undefined, errorMsg: null, validateStatus: undefined};
        this.props.handleOk(this.state.password.value);
    }

    onCancel() {
        this.state.password = {value: undefined, errorMsg: null, validateStatus: undefined};
        this.props.handleCancel('isPasswordModalVisible');
    }

    // changePassword() {
    //     let promise = changePassword(this.state.password, this.state.newPassword);
    //     if (!promise) {
    //         return;
    //     }
    //     promise.then(response => {
    //         notification.success({
    //             message: 'Flat Rental',
    //             description: this.props.intl.formatMessage({id: "labels.password_changed_successfully"}),
    //         });
    //         this.props.handleLogout();
    //         }).catch(error => {
    //         notification.error({
    //             message: 'Flat Rental',
    //             description: error.message || this.somethingWentWrongMessage
    //         });
    //     });
    // }

    render() {
        return (
            <Modal
                visible={this.props.visible}
                title={this.props.intl.formatMessage({id: "labels.email_change"})}
                onOk={this.onSubmit}
                onCancel={this.onCancel}
                footer={[
                    <Button key="back" onClick={this.onCancel}>
                        <FormattedMessage id="labels.cancel"/>
                    </Button>,
                    <Button key="submit" type="primary" onClick={this.onSubmit} disabled={!this.isFormValid()}>
                        <FormattedMessage id="labels.edit"/>
                    </Button>,
                ]}
            >
                <Form>
                    <FormItem
                        label={this.props.intl.formatMessage({ id: 'labels.new_password' })}
                        validateStatus={this.state.newPassword.validateStatus}
                        help={this.state.newPassword.errorMsg}
                        hasFeedback>
                        <Input
                            size="large"
                            name="newPassword"
                            type="password"
                            autoComplete="off"
                            placeholder={this.props.intl.formatMessage({ id: 'placeholders.password'}, {min: CONS.PASSWORD_MIN_LENGTH, max: CONS.PASSWORD_MAX_LENGTH})}
                            value={this.state.newPassword.value}
                            onChange={(event) => this.handleInputChange(event, this.validatePassword)} />
                    </FormItem>
                    <FormItem
                        label={this.props.intl.formatMessage({ id: 'labels.password' })}>
                        <Input
                            size="large"
                            name="password"
                            type="password"
                            autoComplete="off"
                            placeholder={this.props.intl.formatMessage({ id: 'placeholders.confirm_change_with_password' })}
                            value={this.state.password.value}
                            onChange={(event) => this.handleInputChange(event, this.validateIfNotEmpty)} />
                    </FormItem>
                </Form>
            </Modal>
        );
    }
}

export default injectIntl(PasswordModal);