import React, { Component } from 'react';
import {Steps, Form, Input, Button, notification, InputNumber, Row, Col, DatePicker, Checkbox } from 'antd';
import * as CONS from "../Constants";
import {Link} from "react-router-dom";
import './Step.css';
import ComboBox from "../commons/ComboBox";
import Text from "antd/lib/typography/Text";
import moment from "moment";
import {getBuildingTypes} from "../infrastructure/RestApiHandler";
import FirstStepContainer from "./FirstStepContainer";
import ThirdStepContainer from "./ThirdStepContainer";
import SecondStepContainer from "./SecondStepContainer";

const FormItem = Form.Item;
const Step = Steps.Step;

const formItemLayout = {
    labelCol: {
        xs: { span: 12 },
        sm: { span: 4 },
    },
    wrapperCol: {
        xs: { span: 28 },
        sm: { span: 18 },
    },
};

const today = moment(new Date())



const steps = [{
    title: 'General Information',
    content: (
        <FirstStepContainer {...formItemLayout}/>
        ),
}, {
    title: 'Localization',
    content: (
        <SecondStepContainer {...formItemLayout}/>
    ),
}, {
    title: 'Detail Information',
    content: (
        <ThirdStepContainer {...formItemLayout}/>
        ),
},{
    title: 'Summary',
    content: 'Last-content',
}];


class CreateAnnouncementStepWizard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            current: 0
        };
    }

    next() {
        const current = this.state.current + 1;
        this.setState({ current });
    }

    prev() {
        const current = this.state.current - 1;
        this.setState({ current });
    }


    render() {
        const { current } = this.state;
        return (
            <div className="step-wizard-container">
            <Steps current={current}>
                {steps.map(item => <Step key={item.title} title={item.title} />)}
            </Steps>
            <div className="steps-content">{steps[current].content}</div>
                <div className="steps-action">
                    {current > 0 && (
                        <Button style={{ marginLeft: 8 }} onClick={() => this.prev()}>
                            Previous
                        </Button>
                    )}
                    {current < steps.length - 1 && (
                        <Button type="primary" onClick={() => this.next()}>
                            Next
                        </Button>
                    )}
                    {current === steps.length - 1 && (
                        <Button type="primary">
                            Done
                        </Button>
                    )}

                </div>
            </div>
        );
    }
}

export default CreateAnnouncementStepWizard;