import {Avatar, Card, Upload, Button, Row, Col} from "antd";
import React from "react";
import {FormattedMessage, injectIntl} from 'react-intl';

import { Layout, Menu, Breadcrumb, Icon } from 'antd';
import {getSurrogateAvatar} from "./ProfileUtils";
import {changeAvatar, uploadFile} from "../infrastructure/RestApiHandler";
import {CURRENT_USER} from "../infrastructure/Constants";

class AvatarUploader extends React.Component {

    constructor(props) {
        super(props);

        this.loadData = this.loadData.bind(this);
        this.state = {
            avatarUrl: this.props.currentUser.avatarUrl
        }
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

    render() {

        const dummyRequest = ({ file, onSuccess }) => {
            let promise = uploadFile(file);
            setTimeout(() => {
                onSuccess(promise);
            }, 0);

            if (!promise) {
                return;
            }

            promise.then(response => {
                let promise2 = changeAvatar(response.fileName);
                if (!promise2) {
                    return;
                }
                promise2.then(response2 => {
                    let avatarUrl = response2.uri;
                    this.setState({
                        avatarUrl: avatarUrl
                        // isLoading: false
                    });
                    this.props.updateCurrentUser('avatarUrl', avatarUrl);
                }).catch(error => {});
            }).catch(error => {});
        };

        return (
            <div>
                <Row type="flex" justify="space-around">
                    <Col span={24}>
                        {this.state.avatarUrl ? <Avatar size={128} src={this.state.avatarUrl}/> : getSurrogateAvatar(this.props.currentUser.name, 128, 64)}
                    </Col>
                    <Col span={24}>
                        <Upload
                            customRequest={dummyRequest}
                            showUploadList={false}
                        >
                            <div style={{marginTop: '20px'}}>
                                <Button>
                                    <Icon type="upload" /> <FormattedMessage id={"labels.change_avatar"}/>
                                </Button>
                            </div>
                        </Upload>
                    </Col>
                </Row>
            </div>
        );
    }
}

export default injectIntl(AvatarUploader)