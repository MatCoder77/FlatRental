import {Avatar, Button, Card, Col, Icon, List, Row, Upload, PageHeader, Rate, Statistic} from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';
import { withRouter } from 'react-router-dom';

import {getSurrogateAvatar} from "./ProfileUtils";
import CommentsSection from "../comment/CommentsSection";
import { Typography } from 'antd';

const { Title, Paragraph } = Typography;


class ProfileView extends React.Component {

    constructor(props) {
        super(props);

        this.loadData = this.loadData.bind(this);

        this.state = {
            formData: [],
            user: this.props.user,
            isNumberVisible: false,
            currentOpinionsCounter: this.props.user.statistics.opinionsCounter
        }

        this.showNumber = this.showNumber.bind(this);
        this.onCommentAdded = this.onCommentAdded.bind(this);
        this.onCommentRemoved = this.onCommentRemoved.bind(this);
        console.log("Rating: " + this.state.user.statistics.rating);
    }

    componentDidMount() {
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

    showNumber() {
        this.setState({
            isNumberVisible: true
        })
    }

    onCommentAdded() {
        const currentCommentsCounter = this.state.commentsCounter
        this.setState({
            commentsCounter: currentCommentsCounter + 1
        });
    }

    onCommentRemoved(numberOfDeletedComments) {
        const currentCommentsCounter = this.state.commentsCounter
        this.setState({
            commentsCounter: currentCommentsCounter - numberOfDeletedComments
        });
    }

    render() {
        const summaryRate = (
            <div>
                <Statistic title={this.props.intl.formatMessage({ id: 'labels.rate' })} value={Math.round(this.state.user.statistics.rating * 100) / 100} suffix="/ 5" />
                <Rate disabled={true} allowHalf={true} value={this.state.user.statistics.rating}/>
            </div>
        );

        const noRates = (
            <div>
                <Statistic title={this.props.intl.formatMessage({ id: 'labels.rate' })} value={this.props.intl.formatMessage({ id: 'labels.no_rates' })}/>
            </div>
        );

        return (
            <div style={{marginLeft: '60px', marginRight: '60px'}}>
                    <div>
                        <Row gutter={30} type="flex" justify="space-between">
                            <Col span={14}>
                                <Title>{this.state.user.name + " " + this.state.user.surname}</Title>
                                <div style={{marginBottom: '1.25em'}} className="ant-descriptions-title">{this.props.intl.formatMessage({ id: 'labels.about_user' })}</div>
                                <Paragraph>
                                    {this.state.user.about ? this.state.user.about : this.props.intl.formatMessage({ id: 'labels.no_about' })}
                                </Paragraph>
                                <div style={{marginBottom: '0.75em'}} className="ant-descriptions-title">{this.props.intl.formatMessage({ id: 'labels.contact_details' })}</div>
                                <List split={false}>
                                    <List.Item
                                        style={{padding: '0.5em 0'}}
                                        actions={this.state.isNumberVisible ? [] : [<a key="list-loadmore-show" onClick={this.showNumber}><FormattedMessage id="labels.show"/></a>]}
                                    >
                                        <List.Item.Meta
                                            title={this.props.intl.formatMessage({id: "labels.phoneNumber"})}
                                            description={this.state.isNumberVisible ? this.state.user.phoneNumber.match(/.{1,3}/g).join(' ') : this.state.user.phoneNumber.substr(0, 3) + " XXX XXX"}
                                        />
                                    </List.Item>
                                    <List.Item
                                        style={{padding: '0.5em 0'}}
                                    >
                                        <List.Item.Meta
                                            title={this.props.intl.formatMessage({id: "labels.email"})}
                                            description={this.props.user.email}
                                        />
                                    </List.Item>
                                </List>
                            </Col>
                            <Col span={6}>
                                {this.state.user.avatarUrl ? <Avatar size={128} src={this.state.user.avatarUrl}/> : getSurrogateAvatar(this.state.user.name, 128, 64)}
                                {this.state.user.statistics.rating ? summaryRate : noRates}
                            </Col>
                        </Row>
                        <div style={{marginTop: '0.5em'}} className="ant-descriptions-title">{this.props.intl.formatMessage({ id: 'labels.opinions' })}</div>
                        <CommentsSection userId={this.state.user.id}
                                         currentUser={this.props.currentUser}
                                         onCommentAdded={this.onCommentAdded}
                                         onCommentRemoved={this.onCommentRemoved}
                                         placeholder={this.props.intl.formatMessage({ id: 'labels.no_opinion' })}
                                         displayRate={true}
                        />
                    </div>
            </div>
        );
    }
}

export default injectIntl(withRouter(ProfileView))