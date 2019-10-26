import {Modal, Card, Col, List, Row, Button, Input, Form} from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';
import './Profile.css';
import { withRouter } from 'react-router-dom';

import { Layout, Menu, Breadcrumb, Icon } from 'antd';
import {changePassword, getFavourites, getUserAnnouncements} from "../infrastructure/RestApiHandler";
import AnnouncementList from "../announcementlist/AnnouncementList";
import LoadingIcon from "../commons/LoadingIcon";
import AvatarUploader from "./AvatarUploader";
import PhoneNumberModal from "./PhoneNumberModal";
import EmailModal from "./EmailModal";
import PasswordModal from "./PasswordModal";

const { Header, Content, Footer, Sider } = Layout;
const { SubMenu } = Menu;
const FormItem = Form.Item;

class Profile extends React.Component {

    constructor(props) {
        super(props);

        this.setContent = this.setContent.bind(this);
        this.loadData = this.loadData.bind(this);
        this.showModal = this.showModal.bind(this);
        this.handleCancel = this.handleCancel.bind(this);

        this.state = {
            currentOption: "1",
            formData: [],
            userId: this.props.match.params.user,
            isPhoneNumberModalVisible: false,
            isEmailModalVisible: false,
            isPasswordModalVisible: false
        }
    }

    componentDidMount() {
        this.loadData(getUserAnnouncements, 'userAnnouncements', this.state.userId);
        this.loadData(getFavourites, 'userFavourites');
    }

    setContent(selected) {
        const selectedKey = selected.key;
        this.setState({
            currentOption: selectedKey
        });
    }

    loadData(supplierFunction, fieldName, param) {
        let promise = supplierFunction(param);

        if (!promise) {
            return;
        }

        // this.setState({
        //     isLoading: true
        // });

        promise
            .then(response => {
                const {formData} = this.state;
                formData[fieldName] = response;
                this.setState({
                    formData
                    // isLoading: false
                });
            }).catch(error => {
            // this.setState({
            //     isLoading: false
            // })
        });
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


    // changePhoneNumber(newPhoneNumber) {
    //     let promise =
    // }

    render() {
        const userAnnouncementsList = (this.state.formData.userAnnouncements ? <AnnouncementList key="3" announcementsList={this.state.formData.userAnnouncements}/> : <LoadingIcon/>);
        const userFavouritesList = (this.state.formData.userFavourites ? <AnnouncementList key="2" announcementsList={this.state.formData.userFavourites}/> : <LoadingIcon/>);
        const avatarUploader = (
            <div>
                <AvatarUploader loadCurrentUser={this.props.loadCurrentUser} updateCurrentUser={this.props.updateCurrentUser} currentUser={this.props.currentUser}/>
            </div>
        );
        const userAnnouncementPanel = (
            <div>
                <Card title={this.props.intl.formatMessage({id: "labels.announcements"})}>
                {userAnnouncementsList}
                </Card>
            </div>
        );
        const userFavouritesPanel = (
            <div>
                <Card title={this.props.intl.formatMessage({id: "labels.favourite"})}>
                    {userFavouritesList}
                </Card>
            </div>
        );
        const accountSettingsPanel = (
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
                                            description={this.props.currentUser.phoneNumber}
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
                            {avatarUploader}
                        </Col>
                    </Row>
                </Card>
            </div>
        );
        const contentByMenuItemKey = new Map([["1", "Pierwszy Item"], ["2", userFavouritesPanel], ["3", userAnnouncementPanel], ["4", accountSettingsPanel]]);
        return (
            <div>
                <Layout style={{ padding: '24px 0', background: '#fff' }}>
                    <Sider width={200} style={{ background: '#fff' }}>
                        <Menu

                            mode="inline"
                            defaultSelectedKeys={['1']}
                            defaultOpenKeys={['1']}
                            onSelect={this.setContent}
                            style={{ height: '100%' }}
                        >
                                <Menu.Item key="1"><FormattedMessage id={"labels.profile"}/></Menu.Item>
                                <Menu.Item key="2"><FormattedMessage id={"labels.favourite"}/></Menu.Item>
                                <Menu.Item key="3"><FormattedMessage id={"labels.announcements"}/></Menu.Item>
                                <Menu.Item key="4"><FormattedMessage id={"labels.settings"}/></Menu.Item>
                        </Menu>
                    </Sider>
                    <Content style={{ padding: '0 24px', minHeight: 280 }}>{contentByMenuItemKey.get(this.state.currentOption)}</Content>
                </Layout>
            </div>
        );
    }
}

export default injectIntl(withRouter(Profile))