import React, { Component } from 'react';
import {Steps, Form, Input, Button, notification, InputNumber, Row, Col, DatePicker, Checkbox } from 'antd';
import * as CONS from "../Constants";
import {Link} from "react-router-dom";
import './Step.css';
import ComboBox from "../commons/ComboBox";
import Text from "antd/lib/typography/Text";
import moment from "moment";
import {getBuildingTypes} from "../infrastructure/RestApiHandler";
import {FormattedMessage, injectIntl} from "react-intl";

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
        const { intl } = this.props;
        return (
            <div className="step-container">
                <h1 className="page-title"><FormattedMessage id="labels.general_info"/></h1>
                <div className="step-container-content">
                    <Form className="step-form" layout="horizontal" {...this.props}>
                        <FormItem label={intl.formatMessage({ id: 'labels.title' })} layout="horizontal" hasFeedback required={true} help="">
                            <Input size="large" name="title" autoComplete="off" placeholder={intl.formatMessage({ id: 'placeholders.title' })}/>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.total_area' })}>
                            <Input addonAfter="m2" size="large" name="large" autoComplete="off" placeholder={intl.formatMessage({ id: 'placeholders.total_area' })}/>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.number_of_rooms' })} required={true}>
                            <InputNumber min={1} max={10}/>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.price_per_month' })} required={true}>
                            <Input  addonAfter="PLN" placeholder={intl.formatMessage({ id: 'placeholders.price_per_month' })}/>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.deposit' })} required={true}>
                            <Input addonAfter="PLN" placeholder={intl.formatMessage({ id: 'placeholders.deposit' })}/>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.floor_max_floor' })} required={true}>
                            <Row gutter={1}><Col span={3}><Input/></Col><Col span={1}> <center>/</center> </Col><Col span={3}><Input/></Col></Row>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.available_from' })} required={true}>
                            <DatePicker defaultValue={today}/>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }

}

export default injectIntl(FirstStepContainer);