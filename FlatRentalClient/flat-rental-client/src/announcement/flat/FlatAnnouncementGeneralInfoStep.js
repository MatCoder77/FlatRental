import React, {Component} from 'react';
import {Form, Input, InputNumber, Row, Col, DatePicker} from 'antd';
import '../Step.css';
import {FormattedMessage, injectIntl} from "react-intl";
import * as CONS from "../../infrastructure/Constants";

const FormItem = Form.Item;

class FlatAnnouncementGeneralInfoStep extends Component {
    constructor(props) {
        super(props);

        this.updateOnChange = this.updateOnChange.bind(this);
        this.updateOnChangeInputNumber = this.updateOnChangeInputNumber.bind(this);
        this.updateOnChangeDatePicker = this.updateOnChangeDatePicker.bind(this);

        this.validateTitle = this.validateTitle.bind(this);
        this.validateIfPositiveInteger = this.validateIfPositiveInteger.bind(this);
        this.validateIfNaturalNumber = this.validateIfNaturalNumber.bind(this);
        this.validateIfNotEmpty = this.validateIfNotEmpty.bind(this);
        this.validateFloor = this.validateFloor.bind(this);
        this.validateMaxFloor = this.validateMaxFloor.bind(this);

        this.titleIsTooShortMessage = this.props.intl.formatMessage({ id: 'text.title_too_short_msg' }, { min: CONS.TITLE_MIN_LENGTH });
        this.titleIsTooLongMessage = this.props.intl.formatMessage({ id: 'text.title_too_long_msg' }, { max: CONS.TITLE_MAX_LENGTH });
        this.onlyPositiveInteger = this.props.intl.formatMessage({ id: 'text.only_positive_integer_msg' });
        this.onlyPositiveIntegerOrZero = this.props.intl.formatMessage({ id: 'text.only_positive_integer_or_zero_msg' });
        this.onlyInteger = this.props.intl.formatMessage({ id: 'text.only_integer' });
        this.smallerThanMinusOne = this.props.intl.formatMessage({ id: 'text.smaller_than_minus_one' });
        this.floorNumberGreaterThanMaxFloor = this.props.intl.formatMessage({ id: 'text.floor_number_grater_than_max_floor' });

        this.props.registerRequiredFields(['title', 'totalArea', 'numberOfRooms', 'pricePerMonth', 'additionalCostsPerMonth', 'securityDeposit', 'floor', 'maxFloorInBuilding', 'availableFrom']);
    }

    updateOnChange(event, validationFunction) {
        const validationResult = validationFunction(event.target.value);
        this.props.onUpdate(event.target.name, event.target.value, validationResult);
    }

    updateOnChangeInputNumber(name, value, validationFunction) {
        const validationResult = validationFunction(value);
        this.props.onUpdate(name, value, validationResult);
    };

    updateOnChangeDatePicker(name, timeMoment, validationFunction) {
        const validationResult = validationFunction(timeMoment);
        this.props.onUpdate(name, timeMoment, validationResult);
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
                                onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                placeholder={intl.formatMessage({id: 'placeholders.total_area'})}
                            />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({id: 'labels.number_of_rooms'})}
                            validateStatus={this.props.getValidationStatus("numberOfRooms")}
                            help={this.props.getErrorMessage("numberOfRooms")}
                            required={true}>
                            <InputNumber
                                name="numberOfRooms"
                                min={1} max={10}
                                value={this.props.formData.numberOfRooms}
                                onChange={value => this.updateOnChangeInputNumber('numberOfRooms', value, this.validateIfPositiveInteger)}
                            />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({id: 'labels.price_per_month'})}
                            validateStatus={this.props.getValidationStatus("pricePerMonth")}
                            help={this.props.getErrorMessage("pricePerMonth")}
                            required={true}>
                            <Input
                                name="pricePerMonth"
                                addonAfter="PLN"
                                value={this.props.formData.pricePerMonth}
                                onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                placeholder={intl.formatMessage({id: 'placeholders.price_per_month'})}
                            />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({id: 'labels.estimated_additional_costs'})}
                            validateStatus={this.props.getValidationStatus("additionalCostsPerMonth")}
                            help={this.props.getErrorMessage("additionalCostsPerMonth")}
                            required={true}>
                            <Input
                                name="additionalCostsPerMonth"
                                addonAfter="PLN"
                                value={this.props.formData.additionalCostsPerMonth}
                                onChange={event => this.updateOnChange(event, this.validateIfNaturalNumber)}
                                placeholder={intl.formatMessage({id: 'placeholders.estimated_additional_costs'})}
                            />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({id: 'labels.deposit'})}
                            validateStatus={this.props.getValidationStatus("securityDeposit")}
                            help={this.props.getErrorMessage("securityDeposit")}
                            required={true}>
                            <Input
                                name="securityDeposit"
                                addonAfter="PLN"
                                value={this.props.formData.securityDeposit}
                                onChange={event => this.updateOnChange(event, this.validateIfNaturalNumber)}
                                placeholder={intl.formatMessage({id: 'placeholders.deposit'})}
                            />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({id: 'labels.floor_max_floor'})}
                            validateStatus={this.props.getValidationStatus("floor") === 'error' ? this.props.getValidationStatus("floor") : this.props.getValidationStatus("maxFloorInBuilding")}
                            help={this.props.getValidationStatus("floor") === 'error' ? this.props.getErrorMessage("floor") : this.props.getErrorMessage("maxFloorInBuilding")}
                            required={true}>
                            <Row>
                                <Col span={3}>
                                    <InputNumber
                                        min={-1}
                                        name="floor"
                                        value={this.props.formData.floor}
                                        onChange={value => this.updateOnChangeInputNumber('floor', value, this.validateFloor)}
                                    />
                                </Col>
                                <Col span={1}>
                                    <center>/</center>
                                </Col>
                                <Col span={3}>
                                    <InputNumber
                                        min={0}
                                        name="maxFloorInBuilding"
                                        value={this.props.formData.maxFloorInBuilding}
                                        onChange={value => this.updateOnChangeInputNumber('maxFloorInBuilding', value, this.validateMaxFloor)}
                                    />
                                </Col>
                            </Row>
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({id: 'labels.available_from'})}
                            validateStatus={this.props.getValidationStatus("availableFrom")}
                            help={this.props.getErrorMessage("availableFrom")}
                            required={true}>
                            <DatePicker style={{width: '100%'}}
                                name="availableFrom"
                                disabledTime={true}
                                value={this.props.formData.availableFrom}
                                onChange={value => this.updateOnChangeDatePicker('availableFrom', value, this.validateIfNotEmpty)}
                                placeholder={intl.formatMessage({id: 'placeholders.select_date'})}
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
    };

    validateIfPositiveInteger = (input) => {
        if(!this.isPositiveInteger(input)) {
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
    };

    isPositiveInteger(str) {
        return /^[1-9]\d*$/.test(str);
    }

    validateIfNaturalNumber = (input) => {
        if(!this.isPositiveIntegerOrZero(input)) {
            return {
                validateStatus: 'error',
                errorMsg: this.onlyPositiveIntegerOrZero
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    };

    isPositiveIntegerOrZero(str) {
        return /^(0|[1-9]\d*)$/.test(str);
    }

    validateFloor = (input) => {
        if(!this.isInteger(input)) {
            return {
                validateStatus: 'error',
                errorMsg: this.onlyInteger
            }
        } else {
            let floor = parseInt(input, 10);
            let maxFloor = parseInt(this.props.formData.maxFloorInBuilding, 10);

            if(floor < -1) {
                return {
                    validateStatus: 'error',
                    errorMsg: this.smallerThanMinusOne
                }
            }

            if (floor > maxFloor) {
                return {
                    validateStatus: 'error',
                    errorMsg: this.floorNumberGreaterThanMaxFloor,
                }
            }

            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    };

    isInteger(str) {
        return /^\-?(\d+)$/.test(str);
    }

    validateMaxFloor = (input) => {
        if(!this.isInteger(input)) {
            return {
                validateStatus: 'error',
                errorMsg: this.onlyInteger
            }
        } else {
            let floor = parseInt(this.props.formData.floor, 10);
            let maxFloor = parseInt(input, 10);

            if(maxFloor < 0) {
                return {
                    validateStatus: 'error',
                    errorMsg: this.onlyPositiveIntegerOrZero
                }
            }

            if (floor > maxFloor) {
                return {
                    validateStatus: 'error',
                    errorMsg: this.floorNumberGreaterThanMaxFloor,
                }
            }

            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    };

    validateIfNotEmpty = (input) => {
        if (input !== null) {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
        return {
            validateStatus: undefined,
            errorMsg: null,
        };
    };

}

export default injectIntl(FlatAnnouncementGeneralInfoStep);