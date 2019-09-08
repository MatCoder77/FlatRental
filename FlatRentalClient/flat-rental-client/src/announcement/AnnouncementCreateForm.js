import React, { Component } from 'react';
import { Steps, Form, Input, Button, notification } from 'antd';
import * as CONS from "../Constants";
import {Link} from "react-router-dom";

const FormItem = Form.Item;
const Step = Steps.Step;

const steps = [{
    title: 'First',
    content: (<div className="signup-container">
    <h1 className="page-title">Sign Up</h1>
    <div className="signup-content">
        <Form className="XXX-form" layout="horizontal">
            <FormItem label="Name"
                      hasFeedback
                      help="vdfvd">
                <Input
                    size="large"
                    name="name"
                    autoComplete="off"
                    placeholder="Your name"
                    value="432"
                />
            </FormItem>
            <FormItem
                label="Surname"
                hasFeedback
                help="dfs">
                <Input
                    size="large"
                    name="surname"
                    autoComplete="off"
                    placeholder="Your surname"
                    value="dfsfs"
                />
            </FormItem>
        </Form>
    </div>
</div>),
}, {
    title: 'Second',
    content: 'Second-content',
}, {
    title: 'Last',
    content: 'Last-content',
}];


class AnnouncementCreateForm extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
            <Steps current={0}>
                {steps.map(item => <Step key={item.title} title={item.title} />)}
            </Steps>
            <div className="steps-content">{steps[0].content}</div>
            </div>
        );
    }
}

export default AnnouncementCreateForm;