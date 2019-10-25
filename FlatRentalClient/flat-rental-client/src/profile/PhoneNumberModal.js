import {Modal, Card, Col, List, Row, Button, Input, Form} from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';
import './Profile.css';

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

    render() {
        return (
            <Modal
                visible={this.props.visible}
                title={this.props.intl.formatMessage({id: "labels.phone_number_change"})}
                onOk={() => {this.props.handleOk('isPhoneNumberModalVisible')}}
                onCancel={() => {this.props.handleCancel('isPhoneNumberModalVisible')}}
                footer={[
                    <Button key="back" onClick={() => {this.props.handleCancel('isPhoneNumberModalVisible')}}>
                        <FormattedMessage id="labels.cancel"/>
                    </Button>,
                    <Button key="submit" type="primary" onClick={() => this.props.handleOk(this.state.phoneNumber)} disabled={!this.isFormValid()}>
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