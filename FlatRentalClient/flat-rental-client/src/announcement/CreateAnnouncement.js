import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import AnnouncementStepWizard from "./AnnouncementStepWizard";
import { withRouter } from 'react-router-dom';


class CreateAnnouncement extends Component{
    constructor(props) {
        super(props);
        this.announcementType = this.props.match.params.announcementType;
    }

    render() {
        return (
          <AnnouncementStepWizard announcementType={this.announcementType}/>
        );
    }
}

export default withRouter(CreateAnnouncement);