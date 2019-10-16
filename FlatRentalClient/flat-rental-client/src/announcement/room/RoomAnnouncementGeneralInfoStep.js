import React, {Component} from 'react';
import {Form, Input, InputNumber, Row, Col, DatePicker} from 'antd';
import '../Step.css';
import {FormattedMessage, injectIntl} from "react-intl";
import * as CONS from "../../infrastructure/Constants";

const FormItem = Form.Item;

class RoomAnnouncementGeneralInfoStep extends Component {
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
        this.validateSuppliedValues = this.validateSuppliedValues.bind(this);
        this.validateIfSupplied = this.validateIfSupplied.bind(this);
        this.getRoomAttribute = this.getRoomAttribute.bind(this);
        this.onRoomChange = this.onRoomChange.bind(this);
        this.validateRoomAttribute = this.validateRoomAttribute.bind(this);

        this.titleIsTooShortMessage = this.props.intl.formatMessage({ id: 'text.title_too_short_msg' }, { min: CONS.TITLE_MIN_LENGTH });
        this.titleIsTooLongMessage = this.props.intl.formatMessage({ id: 'text.title_too_long_msg' }, { max: CONS.TITLE_MAX_LENGTH });
        this.onlyPositiveInteger = this.props.intl.formatMessage({ id: 'text.only_positive_integer_msg' });
        this.onlyPositiveIntegerOrZero = this.props.intl.formatMessage({ id: 'text.only_positive_integer_or_zero_msg' });
        this.onlyInteger = this.props.intl.formatMessage({ id: 'text.only_integer' });
        this.smallerThanMinusOne = this.props.intl.formatMessage({ id: 'text.smaller_than_minus_one' });
        this.floorNumberGreaterThanMaxFloor = this.props.intl.formatMessage({ id: 'text.floor_number_grater_than_max_floor' });

        this.props.registerRequiredFields(['title', 'rooms.0.area', 'rooms.0.numberOfPersons', 'pricePerMonth', 'additionalCostsPerMonth', 'securityDeposit', 'floor', 'maxFloorInBuilding', 'availableFrom']);        this.validateSuppliedValues();
        this.onRoomChange('rooms', 1, undefined, 'roomNumber');
    }

    validateSuppliedValues() {
        this.validateIfSupplied('title', this.validateTitle);
        this.validateIfSuppliedRooms('rooms',this.validateIfPositiveInteger,  'area');
        this.validateIfSuppliedRooms('rooms',this.validateIfPositiveInteger, 'numberOfPersons');
        this.validateIfSupplied('pricePerMonth', this.validateIfPositiveInteger);
        this.validateIfSupplied('additionalCostsPerMonth', this.validateIfNaturalNumber);
        this.validateIfSupplied('securityDeposit', this.validateIfNaturalNumber);
        this.validateIfSupplied('floor', this.validateFloor);
        this.validateIfSupplied('maxFloorInBuilding', this.validateMaxFloor);
        this.validateIfSupplied('availableFrom', this.validateIfNotEmpty);
    }

    validateIfSuppliedRooms(fieldName, validatorFunction, attribute) {
        if (this.props.formData[fieldName] && this.props.formData[fieldName][0] && this.props.formData[fieldName][0][attribute]) {
            this.props.updateValidation(fieldName + ".0." + attribute, validatorFunction(this.props.formData[fieldName][0][attribute]));
        }
    }

    validateIfSupplied(fieldName, validatorFunction) {
        if (this.props.formData[fieldName]) {
            this.props.updateValidation(fieldName, validatorFunction(this.props.formData[fieldName]));
        }
    }

    validateRoomAttribute(attribute, validationName, validatorFunction) {
        if (this.props.formData.rooms && this.props.formData.rooms[0] && this.props.formData.rooms[0][attribute]) {
            this.props.updateValidation(validationName, validatorFunction(this.props.formData.rooms[0][attribute]));
        }
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

    onRoomChange(name, value, validationFunction, roomAttribute) {
        const validationResult = validationFunction ? validationFunction(value) : undefined;
        let rooms = this.props.formData.rooms;
        if (rooms && rooms[0]) {
            rooms[0][roomAttribute] = value;
        } else {
            rooms = new Array(new Object({[roomAttribute]: value}));
        }
        this.props.onUpdate("rooms", rooms, validationResult, "rooms.0." + roomAttribute);
    }


    getRoomAttribute(attribute) {
        if(this.props.formData && this.props.formData.rooms && this.props.formData.rooms[0]) {
            return this.props.formData.rooms[0][attribute];
        }
        return undefined;
    }

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
                        <FormItem label={intl.formatMessage({id: 'labels.room_area'})}
                                  validateStatus={this.props.getValidationStatus("rooms.0.area")}
                                  help={this.props.getErrorMessage("rooms.0.area")}
                                  required={true}>
                            <Input
                                addonAfter={<span>m<sup>2</sup></span>}
                                name="rooms"
                                autoComplete="off"
                                value={this.getRoomAttribute('area')}
                                onChange={event => this.onRoomChange(event.target.name, event.target.value, this.validateIfPositiveInteger, 'area')}
                                placeholder={intl.formatMessage({id: 'placeholders.room_area'})}
                            />
                        </FormItem>
                        <FormItem
                            label={intl.formatMessage({ id: 'labels.number_of_persons' })}
                            validateStatus={this.props.getValidationStatus("rooms.0.numberOfPersons")}
                            help={this.props.getErrorMessage("rooms.0.numberOfPersons")}
                            required={true}>
                            <InputNumber
                                min={1}
                                max={10}
                                onChange={value => this.onRoomChange('rooms', value, this.validateIfPositiveInteger, 'numberOfPersons')}
                                value={this.getRoomAttribute('numberOfPersons')}
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

export default injectIntl(RoomAnnouncementGeneralInfoStep);