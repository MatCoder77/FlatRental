import React, { Component } from 'react';
import { Route, withRouter, Switch } from 'react-router-dom';
import { getCurrentUser} from "./infrastructure/RestApiHandler";
import { ACCESS_TOKEN} from "./Constants";
import { Layout, notification } from 'antd';
import './App.css';
import LoadingIcon from "./commons/LoadingIcon";
import RegistrationFrom from "./registration/RegistrationFrom";
import Login from "./login/Login";
import CreateAnnouncementStepWizard from "./announcement/CreateAnnouncementStepWizard";
import PageHeader from "./pageheader/PageHeader";
import MainPage from "./mainpage/MainPage";

const { Content } = Layout;


class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false
        }
        this.handleLogout = this.handleLogout.bind(this);
        this.loadCurrentUser = this.loadCurrentUser.bind(this);
        this.handleLogin = this.handleLogin.bind(this);

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
            }).catch(error => {
            this.setState({
                isLoading: false
            });
        });
    }

    componentDidMount() {
        this.loadCurrentUser();
    }

    handleLogout(redirectTo="/", notificationType="success", description="You're successfully logged out.") {
        localStorage.removeItem(ACCESS_TOKEN);

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
            description: "You're successfully logged in.",
        });
        this.loadCurrentUser();
        this.props.history.push("/");
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIcon />
        }
        return (
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
                            <Route exact path="/announcement/new" render={(props) => <CreateAnnouncementStepWizard/>}/>
                            {/*<Route path="/users/:username"*/}
                            {/*       render={(props) => <Profile isAuthenticated={this.state.isAuthenticated} currentUser={this.state.currentUser} {...props}  />}>*/}
                            {/*</Route>*/}
                            {/*<PrivateRoute authenticated={this.state.isAuthenticated} path="/poll/new" component={NewPoll} handleLogout={this.handleLogout}></PrivateRoute>*/}
                            {/*<Route component={NotFound}></Route>*/}
                        </Switch>
                    </div>
                </Content>
            </Layout>
        );
    }
}

export default withRouter(App);