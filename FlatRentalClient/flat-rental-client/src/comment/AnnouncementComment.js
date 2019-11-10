import {Comment, Icon, Tooltip, Avatar, Rate, Row, Col} from 'antd';
import React, {Component} from "react";
import moment from 'moment';
import {FormattedMessage, injectIntl} from "react-intl";
import Editor from "./Editor";
import {hasRole, MODERATOR} from "../infrastructure/PermissionsUtils";
import * as DTOUtils from "../infrastructure/DTOUtils";
import {deleteComment} from "../infrastructure/RestApiHandler";
import {getSurrogateAvatar} from "../profile/ProfileUtils";
import { Link, withRouter } from 'react-router-dom';


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
                let numberOfDeletedComments = response.message;
                this.props.onReply();
                this.props.onCommentRemoved(numberOfDeletedComments);
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
                author={<a><Link style={{color: 'rgba(0, 0, 0, 0.45)'}} to={"/profile/" + this.props.data['info.createdBy.id']}>{this.props.data['info.createdBy.name'] + " " + this.props.data['info.createdBy.surname']}</Link></a>}
                avatar={ this.props.data['info.createdBy.avatarUrl'] ? <Avatar src={this.props.data['info.createdBy.avatarUrl']}/> : getSurrogateAvatar(this.props.data['info.createdBy.name'])}
                content={
                    <div>
                        {this.props.nestingLevel === 0 && this.props.displayRate && <Rate value={this.props.data.rate} disabled style={{marginBottom: '8px', marginTop: '-5px'}}/>}
                    <p>
                        {this.props.data.content}
                    </p>
                    </div>
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
                                                                                    onReply={this.props.onReply} intl={this.props.intl}
                                                                                    onCommentAdded={this.props.onCommentAdded}
                                                                                    onCommentRemoved={this.props.onCommentRemoved}
                                                                                    displayRate={false}
                />)}
                {this.state.isCommentEditorShown &&
                <Editor
                    onCommentAdded={this.props.onCommentAdded}
                    repliedCommentId={this.props.data.id}
                    userId={this.props.data.userId}
                    announcementId={this.props.data.announcementId}
                    onSubmit={this.props.onReply}
                    displayRate={false}
                />}
            </Comment>

        );

    }

}

export default withRouter(injectIntl(AnnouncementComment));
