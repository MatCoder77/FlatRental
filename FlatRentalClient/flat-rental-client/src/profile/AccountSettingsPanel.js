import { Card, Col, List, Row } from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';
import { withRouter } from 'react-router-dom';
import AvatarUploader from "./AvatarUploader";
import PhoneNumberModal from "./PhoneNumberModal";
import EmailModal from "./EmailModal";
import PasswordModal from "./PasswordModal";

class AccountSettingsPanel extends React.Component {

    constructor(props) {
        super(props);

        this.showModal = this.showModal.bind(this);
        this.handleCancel = this.handleCancel.bind(this);

        this.state = {
            isPhoneNumberModalVisible: false,
            isEmailModalVisible: false,
            isPasswordModalVisible: false
        }
    }

    showModal(modalVisibilityProperty) {
        this.setState({
            [modalVisibilityProperty]: true
        })
    }

    handleCancel(modalVisibilityProperty) {
        this.setState({
            [modalVisibilityProperty]: false
        })
    }

    render() {
        return (
            <div>
                <Card title={this.props.intl.formatMessage({id: "labels.settings"})}>
                    <Row gutter={40} type="flex" justify="space-between">
                        <Col span={14}>
                            <List>
                                <List.Item
                                    actions={[<a key="list-loadmore-edit" onClick={() => this.showModal('isPhoneNumberModalVisible')}><FormattedMessage id="labels.edit"/></a>]}
                                >
                                    <List.Item.Meta
                                        title={this.props.intl.formatMessage({id: "labels.phoneNumber"})}
                                        description={this.props.currentUser.phoneNumber.match(/.{1,3}/g).join(' ')}
                                    />
                                    <PhoneNumberModal visible={this.state.isPhoneNumberModalVisible}
                                                      value={this.props.currentUser.phoneNumber}
                                                      handleCancel={this.handleCancel}
                                                      handleOk={this.handleCancel}
                                                      updateCurrentUser={this.props.updateCurrentUser}
                                    />
                                </List.Item>
                                <List.Item
                                    actions={[<a key="list-loadmore-edit" onClick={() => this.showModal('isEmailModalVisible')}><FormattedMessage id="labels.edit"/></a>]}
                                >
                                    <List.Item.Meta
                                        title={this.props.intl.formatMessage({id: "labels.email"})}
                                        description={this.props.currentUser.email}
                                    />
                                    <EmailModal visible={this.state.isEmailModalVisible}
                                                value={this.props.currentUser.email}
                                                handleCancel={this.handleCancel}
                                                handleOk={this.handleCancel}
                                                updateCurrentUser={this.props.updateCurrentUser}
                                    />
                                </List.Item>
                                <List.Item
                                    actions={[<a key="list-loadmore-reset">Resetuj</a>, <a key="list-loadmore-edit" onClick={() => this.showModal('isPasswordModalVisible')}><FormattedMessage id="labels.edit"/></a>]}
                                >
                                    <List.Item.Meta
                                        title={this.props.intl.formatMessage({id: "labels.password"})}
                                        description="********************"
                                    />
                                    <PasswordModal
                                        visible={this.state.isPasswordModalVisible}
                                        handleCancel={this.handleCancel}
                                        handleOk={this.handleCancel}
                                        handleLogout={this.props.handleLogout}
                                    />
                                </List.Item>
                            </List>
                        </Col>
                        <Col span={6}>
                            <div>
                                <AvatarUploader loadCurrentUser={this.props.loadCurrentUser} updateCurrentUser={this.props.updateCurrentUser} currentUser={this.props.currentUser}/>
                            </div>
                        </Col>
                    </Row>
                </Card>
            </div>
        );
    }
}

export default injectIntl(withRouter(AccountSettingsPanel))