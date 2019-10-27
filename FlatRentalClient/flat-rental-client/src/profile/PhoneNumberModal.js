import {Modal, Card, Col, List, Row, Button, Input, Form, notification} from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';
import './AccountCenter.css';
import {changePhoneNumber} from "../infrastructure/RestApiHandler";

const FormItem = Form.Item;

class PhoneNumberModal extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            formData: [],
            validateStatus: 'success',
            errorMsg: null,
            phoneNumber: this.props.value
        };

        this.isFormValid = this.isFormValid.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleCancel = this.handleCancel.bind(this);

        this.phoneNumberIsIncorrect = this.props.intl.formatMessage({ id: 'text.phone_number_incorrect_msg' });
    }

    handleInputChange(event) {
        const inputValue = event.target.value;
        const validationResult = this.validatePhoneNumber(inputValue);
        this.setState({
            phoneNumber : inputValue,
            validateStatus: validationResult.validateStatus,
            errorMsg: validationResult.errorMsg
        });
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
    };

    isFormValid() {
        return this.state.validateStatus === 'success';
    }

    handleSubmit() {
        let promise = changePhoneNumber(this.state.phoneNumber);
        if (!promise) {
            return;
        }
        promise.then(response => {
            notification.success({
                message: 'Flat Rental',
                description: this.props.intl.formatMessage({id: "labels.phone_number_changed_successfully"}),
            });
            this.props.updateCurrentUser('phoneNumber', this.state.phoneNumber);
        }).catch(error => {
            notification.error({
                message: 'Flat Rental',
                description: error.message || this.somethingWentWrongMessage
            });
        });
        this.props.handleCancel('isPhoneNumberModalVisible');
    }

    handleCancel() {
        this.props.handleCancel('isPhoneNumberModalVisible');
    }

    render() {
        return (
            <Modal
                visible={this.props.visible}
                title={this.props.intl.formatMessage({id: "labels.phone_number_change"})}
                onOk={this.handleSubmit}
                onCancel={this.handleCancel}
                footer={[
                    <Button key="back" onClick={this.handleCancel}>
                        <FormattedMessage id="labels.cancel"/>
                    </Button>,
                    <Button key="submit" type="primary" onClick={this.handleSubmit} disabled={!this.isFormValid()}>
                        <FormattedMessage id="labels.edit"/>
                    </Button>,
                ]}
            >
                <Form>
                    <FormItem
                        label={this.props.intl.formatMessage({id: 'labels.phoneNumber'})}
                        hasFeedback
                        validateStatus={this.state.validateStatus}
                        help={this.state.errorMsg}>
                        <Input
                            size="large"
                            name="phoneNumber"
                            autoComplete="off"
                            placeholder={this.props.intl.formatMessage({id: 'placeholders.phoneNumber'})}
                            value={this.state.phoneNumber}
                            onChange={(event) => this.handleInputChange(event)}/>
                    </FormItem>
                </Form>
            </Modal>
        );
    }
}

export default injectIntl(PhoneNumberModal);