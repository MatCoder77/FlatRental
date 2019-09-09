import React, { Component } from 'react';
import { signup, checkUsernameAvailability, checkEmailAvailability } from "../infrastructure/RestApiHandler";
import './RegistrationForm.css';
import { Link } from 'react-router-dom';
import * as CONS from "../Constants"
import {FormattedMessage, injectIntl} from 'react-intl';
import { Form, Input, Button, notification } from 'antd';
const FormItem = Form.Item;

class RegistrationFrom extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: {
                value: ''
            },
            surname: {
                value: ''
            },
            username: {
                value: ''
            },
            email: {
                value: ''
            },
            password: {
                value: ''
            },
            phoneNumber: {
                value: ''
            }
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.validateUsernameAvailability = this.validateUsernameAvailability.bind(this);
        this.validateEmailAvailability = this.validateEmailAvailability.bind(this);
        this.isFormInvalid = this.isFormInvalid.bind(this);

        this.succesfullyRegisteredMessage = this.props.intl.formatMessage({ id: 'text.successfully_registered_msg' });
        this.somethingWentWrongMessage = this.props.intl.formatMessage({ id: 'text.something_went_wrong_msg' });
        this.nameIsTooShortMessage = this.props.intl.formatMessage({ id: 'text.name_too_short_msg' }, { min: CONS.NAME_MIN_LENGTH });
        this.nameIsTooLongMessage = this.props.intl.formatMessage({ id: 'text.name_too_long_msg' }, { max: CONS.NAME_MAX_LENGTH });
        this.surnameIsTooShortMessage = this.props.intl.formatMessage({ id: 'text.surname_too_short_msg' }, { min: CONS.SURNAME_MIN_LENGTH });
        this.surnameIsTooLongMessage = this.props.intl.formatMessage({ id: 'text.surname_too_long_msg' }, { max: CONS.SURNAME_MAX_LENGTH });
        this.usernameIsTooShortMessage = this.props.intl.formatMessage({ id: 'text.username_too_short_msg' }, { min: CONS.USERNAME_MIN_LENGTH });
        this.usernameIsTooLongMessage = this.props.intl.formatMessage({ id: 'text.username_too_long_msg' }, { max: CONS.USERNAME_MAX_LENGTH });
        this.usernameIsNotUniqueMessage = this.props.intl.formatMessage({ id: 'text.username_not_unique_msg' });
        this.emailIsTooShortMessage = this.props.intl.formatMessage({ id: 'text.email_too_short_msg' }, { min: CONS.EMAIL_MIN_LENGTH });
        this.emailIsTooLongMessage = this.props.intl.formatMessage({ id: 'text.email_too_long_msg' }, { max: CONS.EMAIL_MAX_LENGTH });
        this.emailIsNotUniqueMessage = this.props.intl.formatMessage({ id: 'text.email_not_unique_msg' });
        this.incorrectEmail = this.props.intl.formatMessage({ id: 'text.email_not_correct_msg' });
        this.passwordIsTooShortMessage = this.props.intl.formatMessage({ id: 'text.password_too_short_msg' }, { min: CONS.PASSWORD_MIN_LENGTH });
        this.passwordIsTooLongMessage = this.props.intl.formatMessage({ id: 'text.password_too_long_msg' }, { max: CONS.PASSWORD_MAX_LENGTH });
        this.phoneNumberIsIncorrect = this.props.intl.formatMessage({ id: 'text.phone_number_incorrect_msg' });
    }

    handleInputChange(event, validationFun) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName] : {
                value: inputValue,
                ...validationFun(inputValue)
            }
        });
    }

    handleSubmit(event) {
        event.preventDefault();

        const signupRequest = {
            name: this.state.name.value,
            surname: this.state.surname.value,
            username: this.state.username.value,
            password: this.state.password.value,
            email: this.state.email.value,
            phoneNumber: this.state.phoneNumber.value
        };
        signup(signupRequest)
            .then(response => {
                notification.success({
                    message: 'Flat Rental',
                    description: this.succesfullyRegisteredMessage,
                });
                this.props.history.push("/login");
            }).catch(error => {
            notification.error({
                message: 'Flat Rental',
                description: error.message || this.somethingWentWrongMessage
            });
        });
    }

    isFormInvalid() {
        return !(this.state.name.validateStatus === 'success' &&
                this.state.surname.validateStatus === 'success' &&
                this.state.username.validateStatus === 'success' &&
                this.state.email.validateStatus === 'success' &&
                this.state.password.validateStatus === 'success' &&
                this.state.phoneNumber.validateStatus === 'success'
        );
    }

    render() {
        const { intl } = this.props;
        return (
            <div className="signup-container">
                <h1 className="page-title"><FormattedMessage id="labels.signup"/></h1>
                <div className="signup-content">
                    <Form onSubmit={this.handleSubmit} className="signup-form" layout="horizontal">
                        <FormItem label={intl.formatMessage({ id: 'labels.name' })}
                            hasFeedback
                            validateStatus={this.state.name.validateStatus}
                            help={this.state.name.errorMsg}>
                            <Input
                                size="large"
                                name="name"
                                autoComplete="off"
                                placeholder={intl.formatMessage({id: 'placeholders.name'})}
                                value={this.state.name.value}
                                onChange={(event) => this.handleInputChange(event, this.validateName)} />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({id: 'labels.surname'})}
                            hasFeedback
                            validateStatus={this.state.surname.validateStatus}
                            help={this.state.surname.errorMsg}>
                            <Input
                                size="large"
                                name="surname"
                                autoComplete="off"
                                placeholder={intl.formatMessage({ id: 'placeholders.surname' })}
                                value={this.state.surname.value}
                                onChange={(event) => this.handleInputChange(event, this.validateSurname)} />
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.username' })}
                                  hasFeedback
                                  validateStatus={this.state.username.validateStatus}
                                  help={this.state.username.errorMsg}>
                            <Input
                                size="large"
                                name="username"
                                autoComplete="off"
                                placeholder={intl.formatMessage({ id: 'placeholders.username' })}
                                value={this.state.username.value}
                                onBlur={this.validateUsernameAvailability}
                                onChange={(event) => this.handleInputChange(event, this.validateUsername)} />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({ id: 'labels.email' })}
                            hasFeedback
                            validateStatus={this.state.email.validateStatus}
                            help={this.state.email.errorMsg}>
                            <Input
                                size="large"
                                name="email"
                                type="email"
                                autoComplete="off"
                                placeholder={intl.formatMessage({ id: 'placeholders.email' })}
                                value={this.state.email.value}
                                onBlur={this.validateEmailAvailability}
                                onChange={(event) => this.handleInputChange(event, this.validateEmail)} />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({ id: 'labels.password' })}
                            hasFeedback
                            validateStatus={this.state.password.validateStatus}
                            help={this.state.password.errorMsg}>
                            <Input
                                size="large"
                                name="password"
                                type="password"
                                autoComplete="off"
                                placeholder={intl.formatMessage({ id: 'placeholders.password'}, {min: CONS.PASSWORD_MIN_LENGTH, max: CONS.PASSWORD_MAX_LENGTH})}
                                value={this.state.password.value}
                                onChange={(event) => this.handleInputChange(event, this.validatePassword)} />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({ id: 'labels.phoneNumber' })}
                            hasFeedback
                            validateStatus={this.state.phoneNumber.validateStatus}
                            help={this.state.phoneNumber.errorMsg}>
                            <Input
                                size="large"
                                name="phoneNumber"
                                autoComplete="off"
                                placeholder={intl.formatMessage({ id: 'placeholders.phoneNumber' })}
                                value={this.state.phoneNumber.value}
                                onChange={(event) => this.handleInputChange(event, this.validatePhoneNumber)} />
                        </FormItem>
                        <FormItem>
                            <Button type="primary"
                                    htmlType="submit"
                                    size="large"
                                    className="signup-form-button"
                                    disabled={this.isFormInvalid()}><FormattedMessage id="buttons.signup"/></Button>
                            <FormattedMessage id="text.already_registered"/> <Link to="/login"><FormattedMessage id="text.login_now"/></Link>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }

    // Validation Functions

    validateName = (name) => {
        if(name.length < CONS.NAME_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.nameIsTooShortMessage
            }
        } else if (name.length > CONS.NAME_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.nameIsTooLongMessage
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    }

    validateSurname = (surname) => {
        if(surname.length < CONS.SURNAME_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.surnameIsTooShortMessage
            }
        } else if (surname.length > CONS.NAME_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.surnameIsTooLongMessage
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
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
    }

    validateUsername = (username) => {
        if(username.length < CONS.USERNAME_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.usernameIsTooShortMessage
            }
        } else if (username.length > CONS.USERNAME_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.usernameIsTooLongMessage
            }
        } else {
            return {
                validateStatus: null,
                errorMsg: null
            }
        }
    }

    validateUsernameAvailability() {
        // First check for client side errors in username
        const usernameValue = this.state.username.value;
        const usernameValidation = this.validateUsername(usernameValue);

        if(usernameValidation.validateStatus === 'error') {
            this.setState({
                username: {
                    value: usernameValue,
                    ...usernameValidation
                }
            });
            return;
        }

        this.setState({
            username: {
                value: usernameValue,
                validateStatus: 'validating',
                errorMsg: null
            }
        });

        checkUsernameAvailability(usernameValue)
            .then(response => {
                if(response.isAvailable) {
                    this.setState({
                        username: {
                            value: usernameValue,
                            validateStatus: 'success',
                            errorMsg: null
                        }
                    });
                } else {
                    this.setState({
                        username: {
                            value: usernameValue,
                            validateStatus: 'error',
                            errorMsg: this.usernameIsNotUniqueMessage
                        }
                    });
                }
            }).catch(error => {
            // Marking validateStatus as success, Form will be rechecked at server
            this.setState({
                username: {
                    value: usernameValue,
                    validateStatus: 'success',
                    errorMsg: null
                }
            });
        });
    }

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

    validatePhoneNumber = (phoneNumber) => {
        if(/^\d+$/.test(phoneNumber)) {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        } else {
            return {
                validateStatus: 'error',
                errorMsg: this.phoneNumberIsIncorrect
            }
        }
    }

}

export default injectIntl(RegistrationFrom);

