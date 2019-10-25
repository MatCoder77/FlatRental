import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import {getAnnouncement} from "../infrastructure/RestApiHandler";
import * as DTOUtils from "../infrastructure/DTOUtils";
import { withRouter } from 'react-router-dom';
import LoadingIcon from "../commons/LoadingIcon";
import AnnouncementView from "./AnnouncementView";


class AnnouncementViewHandler extends Component{
    constructor(props) {
        super(props);
        this.state = {
            formData: {},
            announcementId: this.props.match.params.id,
            isLoading: true
        };
        this.loadAnnouncement = this.loadAnnouncement.bind(this);
    }

    loadAnnouncement(id) {
        let promise = getAnnouncement(id);

        if (!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });
        promise
            .then(response => {
                let announcementData = response;
                let flattenData = DTOUtils.flatten(announcementData);
                this.setState({
                    formData: flattenData,
                    announcementType: announcementData.type,
                    isLoading: false
                });
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

    componentDidMount() {
        this.loadAnnouncement(this.state.announcementId);
    }

    render() {
        if (this.state.isLoading) {
            return (<LoadingIcon/>);
        }
        return (
            <AnnouncementView data={this.state.formData} currentUser={this.props.currentUser}/>
        );
    }
}

export default withRouter(AnnouncementViewHandler);