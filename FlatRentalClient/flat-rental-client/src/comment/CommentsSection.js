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
import {getSurrogateAvatar} from "../profile/ProfileUtils";

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

        const addCommentFrom = (
            <div>
                {this.props.currentUser &&
                <Comment
                    avatar={ this.props.currentUser.avatarUrl ? <Avatar src={this.props.currentUser.avatarUrl}/> : getSurrogateAvatar(this.props.currentUser.name)}
                    content={
                        <Editor
                            repliedCommentId={null}
                            announcementId={this.props.announcementId}
                            onSubmit={this.updateComments}
                            onCommentAdded={this.props.onCommentAdded}
                        />
                    }
                />}
            </div>
        );

        if (this.state.isLoading) {
            return (<LoadingIcon/>);
        }
        return (
            <div>
                {this.state.comments.length > 0 && <CommentList comments={this.state.comments}
                                                                onReply={this.updateComments}
                                                                currentUser={this.props.currentUser}
                                                                onCommentAdded={this.props.onCommentAdded}
                                                                onCommentRemoved={this.props.onCommentRemoved}/>}
                {this.props.currentUser ? addCommentFrom : ""}
            </div>
        );
    }
}
export default injectIntl(CommentsSection);