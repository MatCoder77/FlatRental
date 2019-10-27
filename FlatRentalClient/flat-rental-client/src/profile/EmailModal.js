import {Modal, Card, Col, List, Row, Button, Input, Form, notification} from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';
import './AccountCenter.css';
import * as CONS from "../infrastructure/Constants";
import {changeEmail, checkEmailAvailability} from "../infrastructure/RestApiHandler";

const FormItem = Form.Item;

class EmailModal extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            email: {value: this.props.value, errorMsg: null, validateStatus: 'success'},
            password: {value: undefined, errorMsg: null, validateStatus: undefined}
        };
        this.validateEmail = this.validateEmail.bind(this);
        this.validateEmailAvailability = this.validateEmailAvailability.bind(this);
        this.isFormValid = this.isFormValid.bind(this);
        this.validateIfNotEmpty = this.validateIfNotEmpty.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.onCancel = this.onCancel.bind(this);
        this.changeEmail = this.changeEmail.bind(this);

        this.emailIsTooShortMessage = this.props.intl.formatMessage({ id: 'text.email_too_short_msg' }, { min: CONS.EMAIL_MIN_LENGTH });
        this.emailIsTooLongMessage = this.props.intl.formatMessage({ id: 'text.email_too_long_msg' }, { max: CONS.EMAIL_MAX_LENGTH });
        this.emailIsNotUniqueMessage = this.props.intl.formatMessage({ id: 'text.email_not_unique_msg' });
        this.incorrectEmail = this.props.intl.formatMessage({ id: 'text.email_not_correct_msg' });
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

    validateEmail = (email) => {
        if(!email) {
            return {
                validateStatus: 'error',
                errorMsg: this.emailIsTooShortMessage
            }
        }

        const EMAIL_REGEX = RegExp('[^@ ]+@[^@ ]+\\.[^@ ]+');
        if(!EMAIL_REGEX.test(email)) {
            return {
                validateStatus: 'error',
                errorMsg: this.incorrectEmail
            }
        }

        if(email.length > CONS.EMAIL_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.emailIsTooLongMessage
            }
        }

        return {
            validateStatus: null,
            errorMsg: null
        }
    };

    validateEmailAvailability() {
        // First check for client side errors in email
        const emailValue = this.state.email.value;
        const emailValidation = this.validateEmail(emailValue);

        if(emailValidation.validateStatus === 'error') {
            this.setState({
                email: {
                    value: emailValue,
                    ...emailValidation
                }
            });
            return;
        }

        this.setState({
            email: {
                value: emailValue,
                validateStatus: 'validating',
                errorMsg: null
            }
        });

        checkEmailAvailability(emailValue)
            .then(response => {
                if(response.isAvailable) {
                    this.setState({
                        email: {
                            value: emailValue,
                            validateStatus: 'success',
                            errorMsg: null
                        }
                    });
                } else {
                    this.setState({
                        email: {
                            value: emailValue,
                            validateStatus: 'error',
                            errorMsg: this.emailIsNotUniqueMessage
                        }
                    });
                }
            }).catch(error => {
            // Marking validateStatus as success, Form will be recchecked at server
            this.setState({
                email: {
                    value: emailValue,
                    validateStatus: 'success',
                    errorMsg: null
                }
            });
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

    isFormValid() {
        return this.state.email.validateStatus === 'success' && this.state.password.validateStatus === 'success';
    }

    onSubmit() {
        this.changeEmail();
        this.setState({
            password: {value: undefined, errorMsg: null, validateStatus: undefined}
        });
        this.props.handleOk(this.state.email.value);
        this.props.handleCancel('isEmailModalVisible');
    }

    changeEmail() {
        let newEmail = this.state.email.value;
        let promise = changeEmail(this.state.password.value, newEmail);
        if (!promise) {
            return;
        }
        promise.then(response => {
            notification.success({
                message: 'Flat Rental',
                description: this.props.intl.formatMessage({id: "labels.email_changed_successfully"}),
            });
            this.props.updateCurrentUser('email', newEmail);
        }).catch(error => {
            notification.error({
                message: 'Flat Rental',
                description: error.message || this.somethingWentWrongMessage
            });
        });
    }

    onCancel() {
        this.setState({
            password: {value: undefined, errorMsg: null, validateStatus: undefined}
        });
        this.props.handleCancel('isEmailModalVisible');
    }

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
                        label={this.props.intl.formatMessage({ id: 'labels.email' })}
                        hasFeedback
                        validateStatus={this.state.email.validateStatus}
                        help={this.state.email.errorMsg}>
                        <Input
                            size="large"
                            name="email"
                            type="email"
                            autoComplete="off"
                            placeholder={this.props.intl.formatMessage({ id: 'placeholders.email' })}
                            value={this.state.email.value}
                            onBlur={this.validateEmailAvailability}
                            onChange={(event) => this.handleInputChange(event, this.validateEmail)} />
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

export default injectIntl(EmailModal);