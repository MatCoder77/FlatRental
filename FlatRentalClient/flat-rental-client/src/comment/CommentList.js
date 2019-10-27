import {List} from "antd";
import AnnouncementComment from "./AnnouncementComment";
import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";


class CommentList extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        console.log(this.props.placeholder);
        const comments = this.props.comments;
        if (!comments || !comments.length) {
            return <div>{this.props.placeholder}</div>
        }
        return (
            <List
                dataSource={comments}
                itemLayout="horizontal"
                pagination={{
                    onChange: page => {
                        console.log(page);
                    },
                    pageSize: 10,
                }}
                renderItem={item => <AnnouncementComment data={item}
                                                         onReply={this.props.onReply}
                                                         nestingLevel={0}
                                                         currentUser={this.props.currentUser}
                                                         onCommentAdded={this.props.onCommentAdded}
                                                         onCommentRemoved={this.props.onCommentRemoved}
                                                         displayRate={this.props.displayRate}
                />}
            />
        );
    }
}

export default injectIntl(CommentList);