import { Comment, Icon, Tooltip, Avatar } from 'antd';
import React, {Component} from "react";
import moment from 'moment';
import {FormattedMessage, injectIntl} from "react-intl";
import Editor from "./Editor";
import {hasRole, MODERATOR} from "../infrastructure/PermissionsUtils";
import * as DTOUtils from "../infrastructure/DTOUtils";
import {deleteComment} from "../infrastructure/RestApiHandler";


class AnnouncementComment extends Component {
    constructor(props) {
        super(props);

        this.state = {
            action: null,
            isCommentEditorShown: false
        };

        this.replyToComment = this.replyToComment.bind(this);
        this.deleteComment = this.deleteComment.bind(this);
        console.log(this.props.data);
    }

    replyToComment() {
        const isShown = !this.state.isCommentEditorShown;
        this.setState({
            isCommentEditorShown: isShown
        });
    }

    deleteComment() {
        let promise = deleteComment(this.props.data.id);
        if (!promise) {
            return;
        }
        promise
            .then(response => {
                this.props.onReply();
            }).catch(error => {});
    }

    render() {
        const { intl } = this.props;
        const { likes, dislikes, action } = this.state;

        const likeAction = (
            <span key="comment-basic-like">
            <Tooltip title={intl.formatMessage({id: 'labels.like'})}>
            <Icon
                type="like"
                theme={action === 'liked' ? 'filled' : 'outlined'}
                onClick={this.like}
            />
            </Tooltip>
            <span style={{ paddingLeft: 8, cursor: 'auto' }}>{likes}</span>
            </span>
        );

        const dislikeAction = (
            <span key=' key="comment-basic-dislike"'>
            <Tooltip title={intl.formatMessage({id: 'labels.dislike'})}>
            <Icon
              type="dislike"
              theme={action === 'disliked' ? 'filled' : 'outlined'}
              onClick={this.dislike}
            />
            </Tooltip>
            <span style={{ paddingLeft: 8, cursor: 'auto' }}>{dislikes}</span>
            </span>
        );

        const replyToAction = (
            <span key="comment-basic-reply-to" onClick={this.replyToComment}>{intl.formatMessage({id: 'labels.reply'})}</span>
        );

        const deleteAction = (
            <span key="comment-basic-delete" onClick={this.deleteComment}>{intl.formatMessage({id: 'labels.delete'})}</span>
        );

        let actions = [likeAction, dislikeAction];
        if (this.props.nestingLevel < 2 ) {
            actions.push(replyToAction);
        }
        if (hasRole(MODERATOR, this.props.currentUser)) {
            actions.push(deleteAction);
        }

        return (
            <Comment
                actions={actions}
                author={<a>{this.props.data['info.createdBy.name'] + " " + this.props.data['info.createdBy.surname']}</a>}
                avatar={
                    <Avatar
                        src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                        alt= {""}
                    />
                }
                content={
                    <p>
                        {this.props.data.content}
                    </p>
                }
                datetime={
                    <Tooltip title={moment(this.props.data['info.createdAt']).format('YYYY-MM-DD HH:mm:ss')}>
                        <span>{moment(this.props.data['info.createdAt']).fromNow()}</span>
                    </Tooltip>
                }
            >
                {this.props.data.subcomments.map(subcomment => <AnnouncementComment data={subcomment}
                                                                                    currentUser={this.props.currentUser}
                                                                                    nestingLevel={this.props.nestingLevel + 1}
                                                                                    onReply={this.props.onReply} intl={this.props.intl}/>)}
                {this.state.isCommentEditorShown && <Editor
                    repliedCommentId={this.props.data.id}
                    announcementId={this.props.data.announcementId}
                    onSubmit={this.props.onReply}
                />}
            </Comment>

        );

    }

}

export default injectIntl(AnnouncementComment);
