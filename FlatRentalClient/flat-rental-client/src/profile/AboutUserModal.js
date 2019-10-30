import {Modal, Button, Input, Form, notification} from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';
import './AccountCenter.css';
import {changeAboutUser, changePhoneNumber} from "../infrastructure/RestApiHandler";

const FormItem = Form.Item;
const { TextArea } = Input;

class AboutUserModal extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            aboutUser: this.props.value
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleCancel = this.handleCancel.bind(this);
    }

    handleInputChange(event) {
        const inputValue = event.target.value;
        this.setState({
            aboutUser : inputValue,
        });
    }

    handleSubmit() {
        let promise = changeAboutUser(this.state.aboutUser);
        if (!promise) {
            return;
        }
        promise.then(response => {
            notification.success({
                message: 'Flat Rental',
                description: this.props.intl.formatMessage({id: "labels.profile_description_changed_successfully"}),
            });
            this.props.updateCurrentUser('about', this.state.aboutUser);
        }).catch(error => {
            notification.error({
                message: 'Flat Rental',
                description: error.message || this.somethingWentWrongMessage
            });
        });
        this.props.handleCancel('isAboutUserModalVisible');
    }

    handleCancel() {
        this.props.handleCancel('isAboutUserModalVisible');
    }

    render() {
        return (
            <Modal
                visible={this.props.visible}
                title={this.props.intl.formatMessage({id: "labels.about_user_change"})}
                onOk={this.handleSubmit}
                onCancel={this.handleCancel}
                footer={[
                    <Button key="back" onClick={this.handleCancel}>
                        <FormattedMessage id="labels.cancel"/>
                    </Button>,
                    <Button key="submit" type="primary" onClick={this.handleSubmit}>
                        <FormattedMessage id="labels.edit"/>
                    </Button>,
                ]}
            >
                <Form>
                    <FormItem
                        label={this.props.intl.formatMessage({id: 'labels.about_user'})}>
                        <TextArea
                            size="large"
                            rows={10}
                            autoComplete="off"
                            placeholder={this.props.intl.formatMessage({id: 'placeholders.profile_description'})}
                            value={this.state.aboutUser}
                            onChange={(event) => this.handleInputChange(event)}/>
                    </FormItem>
                </Form>
            </Modal>
        );
    }
}

export default injectIntl(AboutUserModal);