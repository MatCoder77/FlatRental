import { Card, Layout, Menu} from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';
import './AccountCenter.css';
import { withRouter } from 'react-router-dom';

import { getFavourites, getUserAnnouncements} from "../infrastructure/RestApiHandler";
import AnnouncementList from "../announcementlist/AnnouncementList";
import LoadingIcon from "../commons/LoadingIcon";
import AccountSettingsPanel from "./AccountSettingsPanel";
import ProfileView from "./ProfileView";

const { Content, Sider } = Layout;

class AccountCenter extends React.Component {

    constructor(props) {
        super(props);

        this.setContent = this.setContent.bind(this);
        this.loadData = this.loadData.bind(this);

        this.state = {
            currentOption: "1",
            formData: [],
            userId: this.props.currentUser.id
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

    render() {
        const userAnnouncementsList = (this.state.formData.userAnnouncements ? <AnnouncementList currentUser={this.props.currentUser} key="3" announcementsList={this.state.formData.userAnnouncements}/> : <LoadingIcon/>);
        const userFavouritesList = (this.state.formData.userFavourites ? <AnnouncementList currentUser={this.props.currentUser} key="2" announcementsList={this.state.formData.userFavourites}/> : <LoadingIcon/>);
        const profilePanel = (
            <Card title={this.props.intl.formatMessage({id: "labels.profile"})}>
                <ProfileView user={this.props.currentUser} currentUser={this.props.currentUser}/>
            </Card>
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
            <AccountSettingsPanel loadCurrentUser={this.props.loadCurrentUser} updateCurrentUser={this.props.updateCurrentUser} currentUser={this.props.currentUser}/>
        );
        const contentByMenuItemKey = new Map([["1", profilePanel], ["2", userFavouritesPanel], ["3", userAnnouncementPanel], ["4", accountSettingsPanel]]);

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

export default injectIntl(withRouter(AccountCenter))