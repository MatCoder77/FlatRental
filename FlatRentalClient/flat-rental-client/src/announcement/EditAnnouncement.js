import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import AnnouncementStepWizard from "./AnnouncementStepWizard";
import {getAnnouncement} from "../infrastructure/RestApiHandler";
import * as DTOUtils from "../infrastructure/DTOUtils";
import { withRouter } from 'react-router-dom';
import LoadingIcon from "../commons/LoadingIcon";


class EditAnnouncement extends Component{
    constructor(props) {
        super(props);
        this.state = {
            formData: {},
            announcementId: this.props.match.params.announcementId,
            announcementType: undefined,
            isLoading: true
        };
        this.loadAnnouncement = this.loadAnnouncement.bind(this);
        this.loadAnnouncement(this.state.announcementId);
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
                const {formData} = this.state;
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

    render() {
        if (this.state.isLoading) {
            return (<LoadingIcon/>);
        }
        return (
            <AnnouncementStepWizard announcementType={this.state.announcementType} formData={this.state.formData}/>
        );
    }
}

export default withRouter(EditAnnouncement);