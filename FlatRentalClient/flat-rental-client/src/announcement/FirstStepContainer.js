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

        this.updateOnChange = this.updateOnChange.bind(this);
        this.updateOnChangeInputNumber = this.updateOnChangeInputNumber.bind(this);
        this.updateOnChangeDatePicker = this.updateOnChangeDatePicker.bind(this);
    }



    updateOnChange(event) {
        this.props.onUpdate(event.target.name, event.target.value);
    }

    updateOnChangeInputNumber = name => value => {
        this.props.onUpdate(name, value);
    };

    updateOnChangeDatePicker = name => (timeMoment, timeString) => {
        this.props.onUpdate(name, timeMoment);
    };

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
                            <Input
                                name="title"
                                autoComplete="off"
                                value={this.props.formData.title}
                                onChange={this.updateOnChange}
                                placeholder={intl.formatMessage({ id: 'placeholders.title' })}
                            />
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.area' })}>
                            <Input
                                addonAfter="m2"
                                name="totalArea"
                                autoComplete="off"
                                value={this.props.formData.totalArea}
                                onChange={this.updateOnChange}
                                placeholder={intl.formatMessage({ id: 'placeholders.total_area' })}
                            />
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.number_of_rooms' })} required={true}>
                            <InputNumber
                                name="numberOfRooms"
                                min={1} max={10}
                                value={this.props.formData.numberOfRooms}
                                onChange={this.updateOnChangeInputNumber('numberOfRooms')}
                            />
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.price_per_month' })} required={true}>
                            <Input
                                name="pricePerMonth"
                                addonAfter="PLN"
                                value={this.props.formData.pricePerMonth}
                                onChange={this.updateOnChange}
                                placeholder={intl.formatMessage({ id: 'placeholders.price_per_month' })}
                            />
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.estimated_additional_costs' })} required={true}>
                            <Input
                                name="additionalCostsPerMonth"
                                addonAfter="PLN"
                                value={this.props.formData.additionalCostsPerMonth}
                                onChange={this.updateOnChange}
                                placeholder={intl.formatMessage({ id: 'placeholders.estimated_additional_costs' })}
                            />
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.deposit' })} required={true}>
                            <Input
                                name="securityDeposit"
                                addonAfter="PLN"
                                value={this.props.formData.securityDeposit}
                                onChange={this.updateOnChange}
                                placeholder={intl.formatMessage({ id: 'placeholders.deposit' })}
                            />
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.floor_max_floor' })} required={true}>
                            <Row gutter={1}>
                                <Col span={3}>
                                    <Input
                                        name="floor"
                                        value={this.props.formData.floor}
                                        onChange={this.updateOnChange}
                                    />
                                </Col>
                                <Col span={1}> <center>/</center> </Col>
                                <Col span={3}>
                                    <Input
                                        name="maxFloorInBuilding"
                                        value={this.props.formData.maxFloorInBuilding}
                                        onChange={this.updateOnChange}
                                    />
                                </Col>
                            </Row>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.available_from' })} required={true}>
                            <DatePicker
                                name="availableFrom"
                                disabledTime={true}
                                value={this.props.formData.availableFrom}
                                onChange={this.updateOnChangeDatePicker('availableFrom')}
                            />
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }

}

export default injectIntl(FirstStepContainer);