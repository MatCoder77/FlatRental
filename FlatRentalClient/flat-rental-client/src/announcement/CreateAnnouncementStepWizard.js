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
        <FirstStepContainer/>
        ),
}, {
    title: 'Localization',
    content: 'Second-content',
}, {
    title: 'Detail Information',
    content: (
        <div className="step-container">
            <h1 className="page-title">Detail Information</h1>
            <div className="step-container-content">
                <Form className="step-form" {...formItemLayout} layout="horizontal">
                    <FormItem label="Built year">
                        <InputNumber min={1800} max={today.year()}/>
                    </FormItem>
                    <FormItem
                        label="Building type"
                        help="">
                        <ComboBox/>
                    </FormItem>
                    <FormItem
                        label="Building material"
                        help="">
                        <ComboBox/>
                    </FormItem>
                    <FormItem
                        label="Windows type"
                        help="">
                        <ComboBox/>
                    </FormItem>
                    <FormItem
                        label="Heating type"
                        help="">
                        <ComboBox/>
                    </FormItem>
                    <FormItem
                        label="Parking type"
                        help="">
                        <ComboBox/>
                    </FormItem>
                    <FormItem
                        label="Apartment State"
                        help="">
                        <ComboBox/>
                    </FormItem>
                    <FormItem label="Amenities" layout="horizontal" hasFeedback required={true} help="">
                        <Checkbox.Group style={{ width: '100%' }} >
                            <Row>
                                <Col span={8}>
                                    <Checkbox value="Balkon">Balkon</Checkbox>
                                </Col>
                                <Col span={8}>
                                    <Checkbox value="Klimatyzacja">Klimatyzacja</Checkbox>
                                </Col>
                                <Col span={8}>
                                    <Checkbox value="Ogrzewane podłogi">Ogrzewane podłogi</Checkbox>
                                </Col>
                                <Col span={8}>
                                    <Checkbox value="Taras">Taras</Checkbox>
                                </Col>
                                <Col span={8}>
                                    <Checkbox value="Blblblr">Blblblr</Checkbox>
                                </Col>
                            </Row>
                        </Checkbox.Group>
                    </FormItem>

                    <FormItem label="Total Area">
                        <Input addonAfter="m2" size="large" name="large" autoComplete="off" placeholder="Total area in m2"/>
                    </FormItem>
                    <FormItem label="Number of rooms" required={true}>
                        <InputNumber min={1} max={10}/>
                    </FormItem>
                    <FormItem label="Price per month" required={true}>
                        <Input  addonAfter="PLN"/>
                    </FormItem>
                    <FormItem label="Deposit" required={true}>
                        <Input addonAfter="PLN"/>
                    </FormItem>
                    <FormItem label="Floor / Max Floor" required={true}>
                        <Row gutter={1}><Col span={3}><Input/></Col><Col span={1}> <center>/</center> </Col><Col span={3}><Input/></Col></Row>
                    </FormItem>
                    <FormItem label="Available from" required={true}>
                        <DatePicker defaultValue={today}/>
                    </FormItem>
                </Form>
            </div>
        </div>),
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