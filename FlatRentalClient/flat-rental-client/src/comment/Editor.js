import {Button, Comment, Form, Input} from "antd";
import * as DTOUtils from "../infrastructure/DTOUtils";
import React from "react";
import {createComment} from "../infrastructure/RestApiHandler";
import {FormattedMessage, injectIntl} from "react-intl";

const { TextArea } = Input;

class Editor extends Comment {
    constructor(props) {
        super(props);
        this.state = {
            submitting: false,
            editorContent: "",
            repliedCommentId: null
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.createComment = this.createComment.bind(this);
    }

    handleChange = event => {
        this.setState({
            editorContent: event.target.value,
        });
    };

    createComment() {
        let commentDTO = {
            content: this.state.editorContent,
            parentCommentId: this.props.repliedCommentId,
            announcementId: this.props.announcementId
        };
        let promise = createComment(commentDTO);

        if (!promise) {
            return;
        }
        this.setState({
            submitting: true,
        });
        promise
            .then(response => {
                let comments = response;
                let flattenData = DTOUtils.flatten(comments);
                this.setState({
                    submitting: false,
                    editorContent: ""
                });
                this.props.onSubmit(flattenData[""]);
                this.props.onCommentAdded();
            }).catch(error => {
            this.setState({
                submitting: false
            })
        });
    }

    handleSubmit = () => {
        if (!this.state.editorContent) {
            return;
        }
        this.createComment();
    };

    render() {
        const {intl} = this.props;
        return (
            <div>
                <Form.Item>
                    <TextArea rows={4} onChange={this.handleChange} value={this.state.editorContent} />
                </Form.Item>
                <Form.Item>
                    <Button htmlType="submit" loading={this.state.submitting} onClick={this.handleSubmit} type="primary">
                        {intl.formatMessage({id: 'labels.add_comment'})}
                    </Button>
                </Form.Item>
            </div>
        );
    }
}

export default injectIntl(Editor);