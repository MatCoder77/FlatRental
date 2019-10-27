import {Button, Comment, Form, Input, Rate} from "antd";
import * as DTOUtils from "../infrastructure/DTOUtils";
import React from "react";
import {createComment, createCommentForProfile} from "../infrastructure/RestApiHandler";
import {FormattedMessage, injectIntl} from "react-intl";

const { TextArea } = Input;

class Editor extends Comment {
    constructor(props) {
        super(props);
        this.state = {
            submitting: false,
            editorContent: "",
            repliedCommentId: null,
            rate: undefined
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.createComment = this.createComment.bind(this);
        this.onRateChange = this.onRateChange.bind(this);
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
            announcementId: this.props.announcementId,
            userId: this.props.userId,
            rate: this.state.rate
        };
        let promise = createComment(commentDTO);

        if (!promise) {
            return;
        }
        this.setState({
            submitting: true,
        });
        promise.then(response => {
                this.setState({
                    submitting: false,
                    editorContent: "",
                    rate: null
                });
                this.props.onSubmit();
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
        if(this.props.displayRate !== false && !this.state.rate) {
            return;
        }
        this.createComment();
    };

    onRateChange(number) {
        this.setState({
            rate: number
        })
    }

    render() {
        const {intl} = this.props;
        return (
            <div>
                <Form.Item>
                    {this.props.displayRate && <span><FormattedMessage id={"labels.rate"}/>: <Rate value={this.state.rate} onChange={this.onRateChange}/></span>}
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