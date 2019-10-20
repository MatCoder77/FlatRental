import React, {Component} from "react";
import moment from 'moment';
import {FormattedMessage, injectIntl} from "react-intl";
import { Comment, Avatar, Form, Button, List, Input } from 'antd';
import {createComment, getComments} from "../infrastructure/RestApiHandler";
import * as DTOUtils from "../infrastructure/DTOUtils";
import LoadingIcon from "../commons/LoadingIcon";
import Editor from "./Editor";
import AnnouncementComment from "./AnnouncementComment";
import CommentList from "./CommentList";

const { TextArea } = Input;

class CommentsSection extends Component {
    constructor(props) {
        super(props);

        this.loadComments = this.loadComments.bind(this);
        this.updateComments = this.updateComments.bind(this);
        this.state = {
            comments: [],
            isLoading: true,
        };

        this.loadComments(this.props.announcementId);
    }

    loadComments(id, noLoading) {
        let promise = getComments(id);

        if (!promise) {
            return;
        }

        if (!noLoading) {
            this.setState({
                isLoading: true
            });
        }

        promise
            .then(response => {
                let comments = response;
                let flattenData = DTOUtils.flatten(comments);
                this.setState({
                    comments: flattenData[""],
                    isLoading: false
                });
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

    updateComments(comments) {
        if (comments) {
            this.setState({
                comments: comments
            });
        } else {
            this.loadComments(this.props.announcementId, true);
        }

    }

    render() {
        if (this.state.isLoading) {
            return (<LoadingIcon/>);
        }
        return (
            <div>
                {this.state.comments.length > 0 && <CommentList comments={this.state.comments} onReply={this.updateComments} currentUser={this.props.currentUser}/>}
                <Comment
                    avatar={
                        <Avatar
                            src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                            alt="Han Solo"
                        />
                    }
                    content={
                        <Editor
                            repliedCommentId={null}
                            announcementId={this.props.announcementId}
                            onSubmit={this.updateComments}
                        />
                    }
                />
            </div>
        );
    }
}
export default injectIntl(CommentsSection);