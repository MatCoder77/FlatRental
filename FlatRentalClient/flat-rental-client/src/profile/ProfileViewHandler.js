import React, {Component} from "react";
import { withRouter } from 'react-router-dom';
import LoadingIcon from "../commons/LoadingIcon";
import {getUser} from "../infrastructure/RestApiHandler";
import ProfileView from "./ProfileView";


class ProfileViewHandler extends Component{
    constructor(props) {
        super(props);
        this.state = {
            userId: this.props.match.params.user,
            isLoading: true
        };
        this.loadUser = this.loadUser.bind(this);
    }

    loadUser(id) {
        let promise = getUser(id);

        if (!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });
        promise
            .then(response => {
                this.setState({
                    user: response,
                    isLoading: false
                });
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

    componentDidMount() {
        this.loadUser(this.state.userId);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        let previousUserId = prevState.userId;
        let currentUserId = this.props.match.params.user;
        if (previousUserId != currentUserId) {
            this.setState({
                userId: currentUserId,
                isLoading: true
            }, () => {this.loadUser(this.state.userId)})
        }
    }

    render() {
        if (this.state.isLoading) {
            return (<LoadingIcon/>);
        }
        return (
            <ProfileView key={this.state.userId} user={this.state.user} currentUser={this.props.currentUser}/>
        );
    }
}

export default withRouter(ProfileViewHandler);