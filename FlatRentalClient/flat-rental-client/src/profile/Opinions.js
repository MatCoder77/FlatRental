import {Modal, Card, Col, List, Row, Button, Input, Form} from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';
import './AccountCenter.css';
import { withRouter } from 'react-router-dom';

import {changePassword, getFavourites, getUserAnnouncements} from "../infrastructure/RestApiHandler";

class Opinions extends React.Component {

    constructor(props) {
        super(props);

        this.loadData = this.loadData.bind(this);

        this.state = {
            userId: this.props.match.params.user
        }
    }

    componentDidMount() {
        this.loadData(getUserAnnouncements, 'userAnnouncements', this.state.userId);
        this.loadData(getFavourites, 'userFavourites');
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
        return (
            <div>
                <Card title={this.props.intl.formatMessage({id: "labels.opinions"})}>

                </Card>
            </div>
        );
    }
}

export default injectIntl(withRouter(Opinions))