import React, { Component } from 'react';
import {Steps, Form, Input, Button, notification, InputNumber, Row, Col, DatePicker, Checkbox } from 'antd';
import * as CONS from "../Constants";
import {Link} from "react-router-dom";
import './Step.css';
import ComboBox from "../commons/ComboBox";
import Text from "antd/lib/typography/Text";
import moment from "moment";
import {getBuildingTypes} from "../infrastructure/RestApiHandler";

const FormItem = Form.Item;

const today = moment(new Date())

class FirstStepContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
        }
    }



    componentDidMount() {

    }

    render() {
        return (
            <div className="step-container">
                <h1 className="page-title">General Information</h1>
                <div className="step-container-content">
                    <Form className="step-form" layout="horizontal">
                        <FormItem label="Title" layout="horizontal" hasFeedback required={true} help="">
                            <Input size="large" name="title" autoComplete="off" placeholder="Title of announcement"/>
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
            </div>
        );
    }

}

export default FirstStepContainer;