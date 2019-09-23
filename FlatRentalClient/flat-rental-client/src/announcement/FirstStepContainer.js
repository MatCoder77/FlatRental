import React, {Component} from 'react';
import {Form, Input, InputNumber, Row, Col, DatePicker} from 'antd';
import './Step.css';
import {FormattedMessage, injectIntl} from "react-intl";
import * as CONS from "../infrastructure/Constants";

const FormItem = Form.Item;

class FirstStepContainer extends Component {
    constructor(props) {
        super(props);

        this.updateOnChange = this.updateOnChange.bind(this);
        this.updateOnChangeInputNumber = this.updateOnChangeInputNumber.bind(this);
        this.updateOnChangeDatePicker = this.updateOnChangeDatePicker.bind(this);

        this.validateTitle = this.validateTitle.bind(this);
        this.validateArea = this.validateArea.bind(this);

        this.titleIsTooShortMessage = this.props.intl.formatMessage({ id: 'text.title_too_short_msg' }, { min: CONS.TITLE_MIN_LENGTH });
        this.titleIsTooLongMessage = this.props.intl.formatMessage({ id: 'text.title_too_long_msg' }, { max: CONS.TITLE_MAX_LENGTH });
        this.onlyPositiveInteger = this.props.intl.formatMessage({ id: 'text.only_positive_integer_msg' }, { max: CONS.TITLE_MAX_LENGTH });
    }

    updateOnChange(event, validationFunction) {
        const validationResult = validationFunction(event.target.value);
        this.props.onUpdate(event.target.name, event.target.value, validationResult);
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
        const {intl} = this.props;
        return (
            <div className="step-container">
                <h1 className="page-title"><FormattedMessage id="labels.general_info"/></h1>
                <div className="step-container-content">
                    <Form className="step-form" layout="horizontal" {...this.props}>
                        <FormItem label={intl.formatMessage({id: 'labels.title'})}
                                  hasFeedback
                                  validateStatus={this.props.getValidationStatus("title")}
                                  help={this.props.getErrorMessage("title")}
                                  required={true}>
                            <Input
                                name="title"
                                autoComplete="off"
                                value={this.props.formData.title}
                                onChange={event => this.updateOnChange(event, this.validateTitle)}
                                placeholder={intl.formatMessage({id: 'placeholders.title'})}
                            />
                        </FormItem>
                        <FormItem label={intl.formatMessage({id: 'labels.area'})}
                                  validateStatus={this.props.getValidationStatus("totalArea")}
                                  help={this.props.getErrorMessage("totalArea")}
                                  required={true}>
                            <Input
                                addonAfter="m2"
                                name="totalArea"
                                autoComplete="off"
                                value={this.props.formData.totalArea}
                                onChange={event => this.updateOnChange(event, this.validateArea)}
                                placeholder={intl.formatMessage({id: 'placeholders.total_area'})}
                            />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({id: 'labels.number_of_rooms'})}
                            required={true}>
                            <InputNumber
                                name="numberOfRooms"
                                min={1} max={10}
                                value={this.props.formData.numberOfRooms}
                                onChange={this.updateOnChangeInputNumber('numberOfRooms')}
                            />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({id: 'labels.price_per_month'})}
                            required={true}>
                            <Input
                                name="pricePerMonth"
                                addonAfter="PLN"
                                value={this.props.formData.pricePerMonth}
                                onChange={this.updateOnChange}
                                placeholder={intl.formatMessage({id: 'placeholders.price_per_month'})}
                            />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({id: 'labels.estimated_additional_costs'})}
                            required={true}>
                            <Input
                                name="additionalCostsPerMonth"
                                addonAfter="PLN"
                                value={this.props.formData.additionalCostsPerMonth}
                                onChange={this.updateOnChange}
                                placeholder={intl.formatMessage({id: 'placeholders.estimated_additional_costs'})}
                            />
                        </FormItem>
                        <FormItem label={intl.formatMessage({id: 'labels.deposit'})} required={true}>
                            <Input
                                name="securityDeposit"
                                addonAfter="PLN"
                                value={this.props.formData.securityDeposit}
                                onChange={this.updateOnChange}
                                placeholder={intl.formatMessage({id: 'placeholders.deposit'})}
                            />
                        </FormItem>
                        <FormItem label={intl.formatMessage({id: 'labels.floor_max_floor'})} required={true}>
                            <Row gutter={1}>
                                <Col span={3}>
                                    <Input
                                        name="floor"
                                        value={this.props.formData.floor}
                                        onChange={this.updateOnChange}
                                    />
                                </Col>
                                <Col span={1}>
                                    <center>/</center>
                                </Col>
                                <Col span={3}>
                                    <Input
                                        name="maxFloorInBuilding"
                                        value={this.props.formData.maxFloorInBuilding}
                                        onChange={this.updateOnChange}
                                    />
                                </Col>
                            </Row>
                        </FormItem>
                        <FormItem label={intl.formatMessage({id: 'labels.available_from'})} required={true}>
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


    validateTitle = (title) => {
        if(title.length < CONS.TITLE_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.titleIsTooShortMessage
            }
        } else if (title.length > CONS.TITLE_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: this.titleIsTooLongMessage
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    }

    validateArea = (area) => {
        if(!this.isPositiveInteger(area)) {
            return {
                validateStatus: 'error',
                errorMsg: this.onlyPositiveInteger
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    }

    isPositiveInteger(str) {
        return /^\+?[1-9]\d*$/.test(str);
    }

}

export default injectIntl(FirstStepContainer);