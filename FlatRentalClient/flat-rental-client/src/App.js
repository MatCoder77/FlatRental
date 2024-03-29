import React, { Component } from 'react';
import { Route, withRouter, Switch } from 'react-router-dom';
import { getCurrentUser} from "./infrastructure/RestApiHandler";
import {ACCESS_TOKEN, CURRENT_USER, default as CONS} from "./infrastructure/Constants";
import {ConfigProvider, Empty, Layout, notification} from 'antd';
import './App.css';
import LoadingIcon from "./commons/LoadingIcon";
import RegistrationFrom from "./registration/RegistrationFrom";
import Login from "./login/Login";
import PageHeader from "./pageheader/PageHeader";
import MainPage from "./mainpage/MainPage";
import CreateAnnouncement from "./announcement/CreateAnnouncement";
import EditAnnouncement from "./announcement/EditAnnouncement";
import DeleteAnnouncement from "./announcement/DeleteAnnouncement";
import AnnouncementList from "./announcementlist/AnnouncementList";
import AnnouncementViewHandler from "./announcement/AnnouncementViewHandler";
import AccountCenter from "./profile/AccountCenter";
import {FormattedMessage, injectIntl} from 'react-intl';
import ProfileViewHandler from "./profile/ProfileViewHandler";
import MainAnnouncementListHandler from "./announcementlist/MainAnnouncementListHandler";

const { Content } = Layout;


class App extends Component {
    constructor(props) {
        super(props);
        let currentUser = localStorage.getItem(CURRENT_USER);
        this.state = {
            currentUser: currentUser ? JSON.parse(currentUser) : null,
            isAuthenticated: false,
            isLoading: false
        }
        this.handleLogout = this.handleLogout.bind(this);
        this.loadCurrentUser = this.loadCurrentUser.bind(this);
        this.handleLogin = this.handleLogin.bind(this);
        this.updateCurrentUserProperty = this.updateCurrentUserProperty.bind(this);

        notification.config({
            placement: 'topRight',
            top: 70,
            duration: 3,
        });
    }

    loadCurrentUser() {
        this.setState({
            isLoading: true
        });
        getCurrentUser()
            .then(response => {
                this.setState({
                    currentUser: response,
                    isAuthenticated: true,
                    isLoading: false
                });
                localStorage.setItem(CURRENT_USER, JSON.stringify(response));
            }).catch(error => {
            this.setState({
                isLoading: false
            });
        });
    }

    updateCurrentUserProperty(propertyName, value) {
        console.log(this.state.currentUser);
        let {currentUser} = this.state;
        currentUser[propertyName] = value;
        this.setState({
            currentUser
        });
        console.log(this.state.currentUser);
    }

    componentDidMount() {
        this.loadCurrentUser();
    }

    handleLogout(redirectTo="/", notificationType="success", description=this.props.intl.formatMessage({id: "labels.successful_logout"})) {
        localStorage.removeItem(ACCESS_TOKEN);
        localStorage.removeItem(CURRENT_USER);
        this.setState({
            currentUser: null,
            isAuthenticated: false
        });

        this.props.history.push(redirectTo);

        notification[notificationType]({
            message: 'Flat Rental',
            description: description,
        });
    }

    handleLogin() {
        notification.success({
            message: 'Flat Rental',
            description: this.props.intl.formatMessage({ id: 'labels.successful_login' })
        });
        this.loadCurrentUser();
        this.props.history.push("/");
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIcon />
        }
        return (
            <ConfigProvider renderEmpty={() => (<Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description={this.props.intl.formatMessage({ id: 'labels.not_found' })}/>)}>
                <Layout className="app-container">
                    <PageHeader isAuthenticated={this.state.isAuthenticated}
                                currentUser={this.state.currentUser}
                                onLogout={this.handleLogout} />

                    <Content className="app-content">
                        <div className="container">
                            <Switch>
                                <Route exact path="/"
                                       render={(props) => <MainPage isAuthenticated={this.state.isAuthenticated}
                                                                    currentUser={this.state.currentUser} handleLogout={this.handleLogout} {...props} />}>
                                </Route>
                                <Route path="/login" render={(props) => <Login onLogin={this.handleLogin} {...props} />}/>
                                <Route path="/signup" component={RegistrationFrom}/>
                                <Route exact path="/announcement/create/:announcementType" render={(props) => <CreateAnnouncement/>}/>
                                <Route exact path="/announcement/edit/:announcementId" render={(props) => <EditAnnouncement/>}/>
                                <Route exact path="/announcement/delete/:announcementId" render={(props) => <DeleteAnnouncement/>}/>
                                <Route exact path="/announcement/list" render={(props) => <MainAnnouncementListHandler currentUser={this.state.currentUser}/>}/>
                                <Route exact path="/announcement/view/:id" render={(props) => <AnnouncementViewHandler currentUser={this.state.currentUser} {...props}/>}/>
                                <Route exact path="/account" render={(props) => <AccountCenter updateCurrentUser={this.updateCurrentUserProperty} currentUser={this.state.currentUser} {...props} handleLogout={this.handleLogout}/>}/>
                                <Route exact path="/profile/:user" render={(props) => <ProfileViewHandler currentUser={this.state.currentUser} {...props}/>}/>
                            </Switch>
                        </div>
                    </Content>
                </Layout>
            </ConfigProvider>
        );
    }
}

export default injectIntl(withRouter(App));