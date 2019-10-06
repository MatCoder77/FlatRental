import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import AnnouncementStepWizard from "./AnnouncementStepWizard";
import { withRouter } from 'react-router-dom';


class DeleteAnnouncement extends Component{
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <AnnouncementStepWizard/>
        );
    }
}

export default withRouter(DeleteAnnouncement);