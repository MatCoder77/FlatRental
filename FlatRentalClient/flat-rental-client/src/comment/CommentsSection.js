import React, {Component} from "react";
import {injectIntl} from "react-intl";
import { Comment, Avatar, Input } from 'antd';
import {getComments, getCommentsForProfile} from "../infrastructure/RestApiHandler";
import * as DTOUtils from "../infrastructure/DTOUtils";
import LoadingIcon from "../commons/LoadingIcon";
import Editor from "./Editor";
import CommentList from "./CommentList";
import {getSurrogateAvatar} from "../profile/ProfileUtils";

class CommentsSection extends Component {
    constructor(props) {
        super(props);

        this.loadComments = this.loadComments.bind(this);
        this.updateComments = this.updateComments.bind(this);
        this.state = {
            comments: [],
            isLoading: true,
        };

        this.loadComments(this.props.announcementId ? this.props.announcementId : this.props.userId);
    }

    loadComments(id, noLoading) {
        let promise = this.props.announcementId ? getComments(id) : getCommentsForProfile(this.props.userId);

        if (!promise) {
            return;
        }

        if (!noLoading) {
            this.setState({
                isLoading: true
            });
        }

        promise.then(response => {
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

    updateComments() {
        this.loadComments(this.props.announcementId ? this.props.announcementId : this.props.userId,  true);
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
                            userId={this.props.userId}
                            announcementId={this.props.announcementId}
                            onSubmit={this.updateComments}
                            onCommentAdded={this.props.onCommentAdded}
                            displayRate={this.props.displayRate}
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
                {<CommentList comments={this.state.comments}
                                                                placeholder={this.props.placeholder}
                                                                onReply={this.updateComments}
                                                                currentUser={this.props.currentUser}
                                                                onCommentAdded={this.props.onCommentAdded}
                                                                onCommentRemoved={this.props.onCommentRemoved}
                                                                displayRate={this.props.displayRate}/>}
                {this.props.currentUser && (this.props.userId ? this.props.userId !== this.props.currentUser.id : true) ? addCommentFrom : ""}
            </div>
        );
    }
}
export default injectIntl(CommentsSection);