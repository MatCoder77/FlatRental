import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import { withRouter } from 'react-router-dom';
import { List, Avatar, Icon } from 'antd';
import {API_BASE_URL} from "../infrastructure/Constants";

const IconText = ({ type, text }) => (
    <span>
    <Icon type={type} style={{ marginRight: 8 }} />
        {text}
  </span>
);


class AnnouncementList extends Component{
    constructor(props) {
        super(props);
        this.state = {
            formData: {}
        };
        this.updateFormData = this.updateFormData.bind(this);
        this.updateFormData('announcementsList', this.props.location.state.announcementsList);
        this.navigateToAnnouncement = this.navigateToAnnouncement.bind(this);
    }

    updateFormData(fieldName, fieldValue) {
        const {formData} = this.state;
        formData[fieldName] = fieldValue;
        this.setState({formData});
        console.log(this.state.formData);
    }

    navigateToAnnouncement() {
        this.props.history.push({
            pathname: '/',
            // search: '?query=abc',
            //state: { announcementsList: data }
        });
    }

    render() {
        console.log("INSIDE");
        console.log(this.props);
        return (
            <List
                itemLayout="vertical"
                size="large"
                pagination={{
                    onChange: page => {
                        console.log(page);
                    },
                    pageSize: 3,
                }}
                dataSource={this.state.formData.announcementsList}
                footer={
                    <div>
                        <b>ant design</b> footer part
                    </div>
                }
                renderItem={item => (
                    <List.Item
                        onClick={this.navigateToAnnouncement}
                        key={item.title}
                        actions={[
                            <IconText type="star-o" text="156" key="list-vertical-star-o" />,
                            <IconText type="like-o" text="156" key="list-vertical-like-o" />,
                            <IconText type="message" text="2" key="list-vertical-message" />,
                        ]}
                        extra={
                            <img
                                width={272}
                                alt="logo"
                                src={API_BASE_URL + "/file/download/" + item.announcementImages[0].filename}
                            />
                        }
                    >
                        <List.Item.Meta
                            avatar={<Avatar src={item.avatar} />}
                            title={<a href={item.href}>{item.title}</a>}
                            description={item.description}
                        />
                        {item.content}
                    </List.Item>
                )}
            />
        );
    }
}

export default withRouter(AnnouncementList);