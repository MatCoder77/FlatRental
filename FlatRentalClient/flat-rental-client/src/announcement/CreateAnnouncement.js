import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import AnnouncementStepWizard from "./AnnouncementStepWizard";
import { withRouter } from 'react-router-dom';


class CreateAnnouncement extends Component{
    constructor(props) {
        super(props);
        this.state = {
            announcementType: this.props.match.params.announcementType
        };
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        let currentType = this.props.match.params.announcementType;
        if (currentType && currentType != prevState.announcementType) {
            this.setState({
                announcementType: currentType
            });
        }
    }

    render() {
        return (
          <AnnouncementStepWizard key={this.state.announcementType} announcementType={this.state.announcementType}/>
        );
    }
}

export default withRouter(CreateAnnouncement);