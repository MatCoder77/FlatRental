import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import { withRouter } from 'react-router-dom';
import {Icon, Button, Input, AutoComplete, Select, Row, Col, Form, InputNumber, Card} from 'antd';
import './SearchBox.css'
import ComboBox from "../commons/ComboBox";
import { Collapse } from 'antd';
import { Radio } from 'antd';
import {
    getApartmentAmenitiesTypes,
    getApartmentStateTypes,
    getBuildingMaterialTypes,
    getBuildingTypes, getCookerTypes, getFurnishing,
    getHeatingTypes, getKitchenTypes,
    getLocations, getNeighborhoodItems, getParkingTypes, getPreferences, getWindowTypes, searchAnnouncementsByCriteria
} from "../infrastructure/RestApiHandler";
import MultiSelect from "../commons/MultiSelect";
import { Tabs } from 'antd';

const { TabPane } = Tabs;
const { Option } = AutoComplete;
const FormItem = Form.Item;

class SearchBox extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dataSource: [],
            appData: {},
            formData: {},
            isExpanded: false,
            validationStatus: {},
            errorMessages: {},
            isLocalitySelected: false
        };

        this.searchResult = this.searchResult.bind(this);
        this.updateFormData = this.updateFormData.bind(this);
        this.renderOption = this.renderOption.bind(this);
        this.handleSearch = this.handleSearch.bind(this);
        this.createSearchResultLabel = this.createSearchResultLabel.bind(this);
        this.changeIsExpanded = this.changeIsExpanded.bind(this);
        this.loadData = this.loadData.bind(this);
        this.validatePositiveIntegerRange = this.validatePositiveIntegerRange.bind(this);
        this.getValidationStatus = this.getValidationStatus.bind(this);
        this.getErrorMessage = this.getErrorMessage.bind(this);
        this.updateValidation = this.updateValidation.bind(this);
        this.onPositiveRangeBeginChanged = this.onPositiveRangeBeginChanged.bind(this);
        this.onPositiveRangeEndChanged = this.onPositiveRangeEndChanged.bind(this);
        this.isPositiveInteger = this.isPositiveInteger.bind(this);
        this.onPositiveWithZeroRangeBeginChanged = this.onPositiveWithZeroRangeBeginChanged.bind(this);
        this.onPositiveWithZeroRangeEndChanged = this.onPositiveWithZeroRangeEndChanged.bind(this);
        this.validatePositiveOrZeroIntegerRange = this.validatePositiveOrZeroIntegerRange.bind(this);
        this.areSearchCriteriaValid = this.areSearchCriteriaValid.bind(this);
        this.performSearchByCriteria = this.performSearchByCriteria.bind(this);
        this.navigateToAnnouncementsList = this.navigateToAnnouncementsList.bind(this);
        this.onSelect = this.onSelect.bind(this);

        this.onlyPositiveInteger = this.props.intl.formatMessage({ id: 'text.only_positive_integer_msg' });
        this.onlyPositiveIntegerOrZero = this.props.intl.formatMessage({ id: 'text.only_positive_integer_or_zero_msg' });
        this.incorrectRange = this.props.intl.formatMessage({ id: 'text.incorrect_range_msg' });
        this.voivodeshipAbbreviation = this.props.intl.formatMessage({ id: 'labels.voivodeship_abbreviation' });
        this.districtAbbreviation = this.props.intl.formatMessage({ id: 'labels.district_abbreviation' });
        this.urbanCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.urban_commune_abbreviation' });
        this.ruralCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.rural_commune_abbreviation' });
        this.mixedCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.mixed_abbreviation' });
        this.capitalCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.capital_commune_abbreviation' });
        this.streetAbbreviation = this.props.intl.formatMessage({ id: 'STREET' });
        this.avenueAbbreviation = this.props.intl.formatMessage({ id: 'AVENUE' });
        this.boulevardAbbreviation = this.props.intl.formatMessage({ id: 'BOULEVARD' });
        this.sqareAbbreviation = this.props.intl.formatMessage({ id: 'SQUARE' });
        this.estateAbbreviation = this.props.intl.formatMessage({ id: 'ESTATE' });
        this.coastAbbreviation = this.props.intl.formatMessage({ id: 'COAST' });
        this.announcementTypes = ["FLAT", "ROOM", "PLACE_IN_ROOM", "LOOK_FOR_FLAT", "LOOK_FOR_ROOM", "LOOK_FOR_PLACE_IN_ROOM"];
        this.updateFormData('announcementType', "FLAT");

    }

    searchResult(searchText) {
        let promise = getLocations(searchText);

        if (!promise) {
            return;
        }

        // this.setState({
        //     isLoading: true
        // });

        promise
            .then(response => {
                console.log(response);
                let result = response.map(this.renderOption)
                this.setState({
                    dataSource: result
                    // isLoading: false
                });
                // if (onDataLoadedCallbackFunction) {
                //     onDataLoadedCallbackFunction(fieldName, callbackFunctionParam);
                // }
            }).catch(error => {
            // this.setState({
            //     isLoading: false
            // })
        });
    }

    loadData(supplierFunction, fieldName, param, callbackFunction) {
        let promise = supplierFunction(param);

        if (!promise) {
            return;
        }

        // this.setState({
        //     isLoading: true
        // });

        promise
            .then(response => {
                const {appData} = this.state;
                appData[fieldName] = response;
                this.setState({
                    appData
                    // isLoading: false
                });
                if (callbackFunction) {
                    callbackFunction(appData[fieldName]);
                }
            }).catch(error => {
            // this.setState({
            //     isLoading: false
            // })
        });
    }

    renderOption(item, index) {
        return (
            <Option key={index} value={JSON.stringify(item)} text={this.createSearchResultLabel(item)}>
                <div className="global-search-item">
        <span className="global-search-item-desc">
          {/*Found {item.query} on*/}
            {/*<a*/}
            {/*    href={`https://s.taobao.com/search?q=${item.query}`}*/}
            {/*    target="_blank"*/}
            {/*    rel="noopener noreferrer"*/}
            {/*>*/}
            {/*  {item.category}*/}
            {/*</a>*/}
            {this.createSearchResultLabel(item)}
        </span>
                    {/*<span className="global-search-item-count">{item.count} results</span>*/}
                </div>
            </Option>
        );
    }

    createSearchResultLabel(searchResult) {
        if (typeof searchResult === 'string')
            searchResult = JSON.parse(searchResult);
        let voivodeship = searchResult.voivodeship ? this.voivodeshipAbbreviation + " " + searchResult.voivodeship.name : undefined;
        let district = searchResult.district ? this.districtAbbreviation + " " + searchResult.district.name : undefined;
        //let hasCommuneSameNameAsDistrict = searchResult.commune && searchResult.district && (searchResult.commune.name != searchResult.district.name);
        let commune = (searchResult.commune) ? this.getCommuneAbbreviation(searchResult.commune) + " " + searchResult.commune.name : undefined;
        let locality = (searchResult.locality ? searchResult.locality.name : undefined);
        let localityDistrict = (searchResult.localityDistrict ? searchResult.localityDistrict.name : undefined);
        let localityPart = (searchResult.localityPart ? searchResult.localityPart.name : undefined);
        let street = this.getStreetLabel(searchResult.street);

        return [voivodeship, district, commune, locality, localityDistrict, localityPart, street].filter(Boolean).join(", ");
    }

    getStreetLabel(street) {
        if (street) {
            return this.props.intl.formatMessage({id: street.type}) + " " + (street.leading_name ? street.leading_name + " " : "") + street.main_name;
        }
        return undefined;
    }

    getCommuneAbbreviation(commune) {
        if (commune.type == 'URBAN_COMMUNE')
            return this.urbanCommuneAbbreviation;
        if (commune.type == 'RURAL_COMMUNE')
            return this.ruralCommuneAbbreviation;
        if (commune.type == 'MIXED_COMMUNE')
            return this.mixedCommuneAbbreviation;
        if (commune.type == 'CAPITAL_COMMUNE')
            return this.capitalCommuneAbbreviation;
        return "";
    }

    handleSearch = value => {
        value = value.replace(this.voivodeshipAbbreviation, "")
            .replace(this.districtAbbreviation, "")
            .replace(this.mixedCommuneAbbreviation, "")
            .replace(this.ruralCommuneAbbreviation, "")
            .replace(this.urbanCommuneAbbreviation, "")
            .replace(this.streetAbbreviation, "")
            .replace(this.avenueAbbreviation, "")
            .replace(this.boulevardAbbreviation, "")
            .replace(this.sqareAbbreviation, "")
            .replace(this.estateAbbreviation, "")
            .replace(this.coastAbbreviation, "");
        this.searchResult(value);
        this.setState({isLocalitySelected: false});
    };

    onPositiveRangeBeginChanged(value, beginName, endName, rangeName) {
        this.updateFormData(beginName, value);
        let validationResult = this.validatePositiveIntegerRange(value, this.state.formData[endName]);
        this.updateValidation(rangeName, validationResult);
    }

    onPositiveRangeEndChanged(value, beginName, endName, rangeName) {
        this.updateFormData(endName, value);
        let validationResult = this.validatePositiveIntegerRange(this.state.formData[beginName], value);
        this.updateValidation(rangeName, validationResult);
    }

    onPositiveWithZeroRangeBeginChanged(value, beginName, endName, rangeName) {
        this.updateFormData(beginName, value);
        let validationResult = this.validatePositiveOrZeroIntegerRange(value, this.state.formData[endName]);
        this.updateValidation(rangeName, validationResult);
    }

    onPositiveWithZeroRangeEndChanged(value, beginName, endName, rangeName) {
        this.updateFormData(endName, value);
        let validationResult = this.validatePositiveOrZeroIntegerRange(this.state.formData[beginName], value);
        this.updateValidation(rangeName, validationResult);
    }
    updateFormData(fieldName, fieldValue) {
        const {formData} = this.state;
        formData[fieldName] = fieldValue;
        this.setState({formData});
        console.log(this.state.formData);
    }

    updateValidation(validationName, validationResult) {
        if (validationResult) {
            const {validationStatus} = this.state;
            const {errorMessages} = this.state;
            validationStatus[validationName] = validationResult.validateStatus;
            errorMessages[validationName] = validationResult.errorMsg;
            this.setState({validationStatus});
            this.setState({errorMessages});
        }
        console.log(this.state.validationStatus);
    }

    getErrorMessage(validationName) {
        const {errorMessages} = this.state;
        return errorMessages[validationName];
    }

    getValidationStatus(validationName) {
        const {validationStatus} = this.state;
        return validationStatus[validationName];
    }

    changeIsExpanded() {
        let currentIsExpandedState = this.state.isExpanded;
        this.setState({isExpanded: !currentIsExpandedState});
    }

    componentDidMount() {
        this.loadData(getBuildingTypes, 'buildingTypes');
        this.loadData(getBuildingMaterialTypes, 'buildingMaterials');
        this.loadData(getHeatingTypes, 'heatingTypes');
        this.loadData(getWindowTypes, 'windowTypes');
        this.loadData(getParkingTypes, 'parkingTypes');
        this.loadData(getApartmentStateTypes, 'apartmentStates');
        this.loadData(getKitchenTypes, 'kitchenTypes');
        this.loadData(getCookerTypes, 'cookerTypes');
        this.loadData(getApartmentAmenitiesTypes, 'apartmentAmenities');
        this.loadData(getFurnishing, 'kitchenFurnishing', 'KITCHEN');
        this.loadData(getFurnishing, 'bathroomFurnishing', 'BATHROOM');
        this.loadData(getPreferences, 'preferences');
        this.loadData(getNeighborhoodItems, 'neighborhood');
    }

    validatePositiveIntegerRange(begin, end) {
        if (!this.validateIfOptionalPositiveInteger(begin) || !this.validateIfOptionalPositiveInteger(end)) {
            return {
                validateStatus: 'error',
                errorMsg: this.onlyPositiveInteger
            };
        }
        if (begin && end) {
            let intBeg = parseInt(begin, 10);
            let intEnd = parseInt(end, 10);
            if (intBeg > intEnd) {
                return {
                    validateStatus: 'error',
                    errorMsg: this.incorrectRange
                };
            }
        }
        return {
            validateStatus: 'success',
            errorMsg: null,
        };
    }

    validateIfOptionalPositiveInteger = (input) => {
        if (!input) {
            return true;
        }
        if(!this.isPositiveInteger(input)) {
            return false;
        }
        return true;
    };

    isPositiveInteger(str) {
        return /^[1-9]\d*$/.test(str);
    }

    validatePositiveOrZeroIntegerRange(begin, end) {
        if (!this.validateIfOptionalPositiveIntegerOrZero(begin) || !this.validateIfOptionalPositiveIntegerOrZero(end)) {
            return {
                validateStatus: 'error',
                errorMsg: this.onlyPositiveIntegerOrZero
            };
        }
        if (begin && end) {
            let intBeg = parseInt(begin, 10);
            let intEnd = parseInt(end, 10);
            if (intBeg > intEnd) {
                return {
                    validateStatus: 'error',
                    errorMsg: this.incorrectRange
                };
            }
        }
        return {
            validateStatus: 'success',
            errorMsg: null,
        };
    }

    validateIfOptionalPositiveIntegerOrZero = (input) => {
        if (!input) {
            return true;
        }
        if(!this.isPositiveIntegerOrZero(input)) {
            return false;
        }
        return true;
    };

    isPositiveIntegerOrZero(str) {
        return /^(0|[1-9]\d*)$/.test(str);
    }

    areSearchCriteriaValid() {
        let validationStatus = this.state.validationStatus;
        for (let validation in validationStatus) {
            if (Object.prototype.hasOwnProperty.call(validationStatus, validation)) {
                if (validationStatus[validation] !== 'success') {
                    return false;
                }
            }
        }
        return true;
    }

    performSearchByCriteria() {
        this.loadData(searchAnnouncementsByCriteria, 'foundAnnouncements', this.state.formData, this.navigateToAnnouncementsList);
        console.log(this.state.appData.foundAnnouncements);

    }

    navigateToAnnouncementsList(data) {
        this.props.history.push({
            pathname: '/announcement/list',
            // search: '?query=abc',
            state: { announcementsList: data }
        });
    }

    onSelect(value, option) {
        console.log(option);
        let address = JSON.parse(value);
        this.updateFormData('voivodeshipId', address.voivodeship ? address.voivodeship.id : undefined);
        this.updateFormData('districtId', address.district ? address.district.id : undefined);
        this.updateFormData('communeId', address.commune ? address.commune.id : undefined);
        this.updateFormData('localityId', address.locality ? address.locality.id : undefined);
        this.updateFormData('localityDistrictId', address.localityDistrict ? address.localityDistrict.id : undefined);
        this.updateFormData('localityPartId', address.localityPart ? address.localityPart.id : undefined);
        this.updateFormData('streetId', address.street ? address.street.id : undefined);
        this.setState({isLocalitySelected: true});
    }

    render() {
        const {intl} = this.props;
        const buildingTypes = this.state.appData.buildingTypes ? this.state.appData.buildingTypes : [];
        return (
            <div className="global-search-wrapper" style={{ width: '100%' }}>
                <Form className="search-box-form">
                    <Row className="upper-row-container">
                        <Col span={5}>
                            <FormItem>
                                <Select
                                    style={{width: '100%', fontSize: '1em'}}
                                    size="large"
                                    name="announcementType"
                                    itemList={this.announcementTypes}
                                    onSelect={value => this.updateFormData("announcementType", value)}
                                    value={this.state.formData.announcementType}
                                    placeholder={intl.formatMessage({ id: 'placeholders.announcement_type' })}>
                                    {this.announcementTypes ? this.announcementTypes.map((type, index) => (
                                        <Option key={index} value={type}><FormattedMessage id={"labels.SB_" + type}/></Option>)) : ""}

                                </Select>
                            </FormItem>
                        </Col>
                        <Col span={19}>
                            <FormItem>
                                <AutoComplete
                                    backfill={true}
                                    className="global-search"
                                    size="large"
                                    dataSource={this.state.dataSource}
                                    onSelect={this.onSelect}
                                    onSearch={this.handleSearch}
                                    placeholder={intl.formatMessage({ id: 'labels.sb_placeholder' })}
                                    optionLabelProp="text"
                                >
                                    <Input
                                        suffix={
                                            <Button
                                                className="search-btn"
                                                style={{ marginRight: -12 }}
                                                size="large"
                                                type="primary"
                                                disabled={!this.areSearchCriteriaValid() || !this.state.isLocalitySelected}
                                                onClick={this.performSearchByCriteria}
                                            >
                                                <Icon type="search" />
                                            </Button>
                                        }
                                    />
                                </AutoComplete>
                            </FormItem>
                        </Col>
                    </Row>
                    <Row gutter={12} className="global-search-lower-container">
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.area'})}
                                validateStatus={this.getValidationStatus("totalArea")}
                                help={this.getErrorMessage("totalArea")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minTotalArea"
                                            //addonAfter="PLN"
                                            value={this.state.formData.minTotalArea}
                                            onChange={event => this.onPositiveRangeBeginChanged(event.target.value, 'minTotalArea', 'maxTotalArea', 'totalArea')}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxTotalArea"
                                            //addonAfter="PLN"
                                            value={this.state.formData.maxTotalArea}
                                            onChange={event => this.onPositiveRangeEndChanged(event.target.value, 'minTotalArea', 'maxTotalArea', 'totalArea')}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.number_of_rooms'})}
                                validateStatus={this.getValidationStatus("numberOfRooms")}
                                help={this.getErrorMessage("numberOfRooms")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minNumberOfRooms"
                                            //addonAfter="PLN"
                                            value={this.state.formData.minNumberOfRooms}
                                            onChange={event => this.onPositiveRangeBeginChanged(event.target.value, 'minNumberOfRooms', 'maxNumberOfRooms', 'numberOfRooms')}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxNumberOfRooms"
                                            //addonAfter="PLN"
                                            value={this.state.formData.maxNumberOfRooms}
                                            onChange={event => this.onPositiveRangeEndChanged(event.target.value, 'minNumberOfRooms', 'maxNumberOfRooms', 'numberOfRooms')}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.price_per_month'})}
                                validateStatus={this.getValidationStatus("pricePerMonth")}
                                help={this.getErrorMessage("pricePerMonth")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minPricePerMonth"
                                            //addonAfter="PLN"
                                            value={this.state.formData.minPricePerMonth}
                                            onChange={event => this.onPositiveRangeBeginChanged(event.target.value, 'minPricePerMonth', 'maxPricePerMonth', 'pricePerMonth')}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxPricePerMonth"
                                            //addonAfter="PLN"
                                            value={this.state.formData.maxPricePerMonth}
                                            onChange={event => this.onPositiveRangeEndChanged(event.target.value, 'minPricePerMonth', 'maxPricePerMonth', 'pricePerMonth')}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.additional_criteria'})} >
                                <Button className="expand-criteria-button" type="default" style={{width: '100%'}} onClick={this.changeIsExpanded}>
                                    <FormattedMessage id={this.state.isExpanded ? "labels.collapse" : "labels.expand"}/>
                                    <Icon type={this.state.isExpanded ? "up" : "down"}/>
                                </Button>
                            </FormItem>
                        </Col>
                    </Row>
                    {this.state.isExpanded &&
                    <Row gutter={12} type="flex" justify="space-between">
                        <Tabs>
                            <TabPane tab="Ogólne" key="1">
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.estimated_additional_costs'})}
                                validateStatus={this.getValidationStatus("additionalCostsPerMonth")}
                                help={this.getErrorMessage("additionalCostsPerMonth")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minAdditionalCostsPerMonth"
                                            //addonAfter="PLN"
                                            value={this.state.formData.minAdditionalCostsPerMonth}
                                            onChange={event => this.onPositiveWithZeroRangeBeginChanged(event.target.value, 'minAdditionalCostsPerMonth', 'maxAdditionalCostsPerMonth', 'additionalCostsPerMonth')}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxAdditionalCostsPerMonth"
                                            //addonAfter="PLN"
                                            value={this.state.formData.maxAdditionalCostsPerMonth}
                                            onChange={event => this.onPositiveWithZeroRangeEndChanged(event.target.value, 'minAdditionalCostsPerMonth', 'maxAdditionalCostsPerMonth', 'additionalCostsPerMonth')}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.deposit'})}
                                validateStatus={this.getValidationStatus("securityDeposit")}
                                help={this.getErrorMessage("securityDeposit")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minSecurityDeposit"
                                            //addonAfter="PLN"
                                            value={this.state.formData.minSecurityDeposit}
                                            onChange={event => this.onPositiveWithZeroRangeBeginChanged(event.target.value, 'minSecurityDeposit', 'maxSecurityDeposit', 'securityDeposit')}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxSecurityDeposit"
                                            //addonAfter="PLN"
                                            value={this.state.formData.maxSecurityDeposit}
                                            onChange={event => this.onPositiveWithZeroRangeEndChanged(event.target.value, 'minSecurityDeposit', 'maxSecurityDeposit', 'securityDeposit')}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.floor'})}
                                validateStatus={this.getValidationStatus("floor")}
                                help={this.getErrorMessage("floor")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minFloor"
                                            //addonAfter="PLN"
                                            value={this.state.formData.minFloor}
                                            onChange={event => this.onPositiveWithZeroRangeBeginChanged(event.target.value, 'minFloor', 'maxFloor', 'floor')}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxFloor"
                                            //addonAfter="PLN"
                                            value={this.state.formData.maxFloor}
                                            onChange={event => this.onPositiveWithZeroRangeEndChanged(event.target.value, 'minFloor', 'maxFloor', 'floor')}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.max_floor'})}
                                validateStatus={this.getValidationStatus("max_floor")}
                                help={this.getErrorMessage("max_floor")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minMaxFloorInBuilding"
                                            //addonAfter="PLN"
                                            value={this.state.formData.minMaxFloorInBuilding}
                                            onChange={event => this.onPositiveWithZeroRangeBeginChanged(event.target.value, 'minMaxFloorInBuilding', 'maxMaxFloorInBuilding', 'max_floor')}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxMaxFloorInBuilding"
                                            //addonAfter="PLN"
                                            value={this.state.formData.maxMaxFloorInBuilding}
                                            onChange={event => this.onPositiveWithZeroRangeEndChanged(event.target.value, 'minMaxFloorInBuilding', 'maxMaxFloorInBuilding', 'max_floor')}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                            </TabPane>
                            <TabPane tab="Mieszkanie" key="2">
                            <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.building_type'})}>
                                <MultiSelect
                                    itemList={this.state.appData.buildingTypes}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.allowedBuildingTypes}
                                    name='allowedBuildingTypes'
                                />
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.building_material'})}>
                                <MultiSelect
                                    itemList={this.state.appData.buildingMaterials}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.allowedBuildingMaterials}
                                    name='allowedBuildingMaterials'
                                />
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.heating_type'})}>
                                <MultiSelect
                                    itemList={this.state.appData.heatingTypes}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.allowedHeatingTypes}
                                    name='allowedHeatingTypes'
                                />
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.windows_type'})}>
                                <MultiSelect
                                    itemList={this.state.appData.windowTypes}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.allowedWindowTypes}
                                    name='allowedWindowTypes'
                                />
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.parking_type'})}>
                                <MultiSelect
                                    itemList={this.state.appData.parkingTypes}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.allowedParkingTypes}
                                    name='allowedParkingTypes'
                                />
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.apartment_state'})}>
                                <MultiSelect
                                    itemList={this.state.appData.apartmentStates}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.allowedApartmentStates}
                                    name='allowedApartmentStates'
                                />
                            </FormItem>
                        </Col>
                                <Col span={6}>
                                    <FormItem
                                        className="global-search-criteria-input"
                                        label={intl.formatMessage({id: 'labels.year_built'})}
                                        validateStatus={this.getValidationStatus("yearBuilt")}
                                        help={this.getErrorMessage("yearBuilt")}
                                    >
                                        <Row gutter={6}>
                                            <Col span={12}>
                                                <Input
                                                    name="minYearBuilt"
                                                    //addonAfter="PLN"
                                                    value={this.state.formData.minYearBuilt}
                                                    onChange={event => this.onPositiveRangeBeginChanged(event.target.value, 'minYearBuilt', 'maxYearBuilt', 'yearBuilt')}
                                                    placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                                />
                                            </Col>
                                            <Col span={12}>
                                                <Input
                                                    name="maxYearBuilt"
                                                    //addonAfter="PLN"
                                                    value={this.state.formData.maxYearBuilt}
                                                    onChange={event => this.onPositiveRangeEndChanged(event.target.value, 'minYearBuilt', 'maxYearBuilt', 'yearBuilt')}
                                                    placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                                />
                                            </Col>
                                        </Row>
                                    </FormItem>
                                </Col>
                                <Col span={6}>
                                    <FormItem label={intl.formatMessage({id: 'labels.well_planned'})} className="global-search-criteria-input">
                                        <Radio.Group buttonStyle="solid" style={{width: '100%'}} onChange={event => this.updateFormData('isWellPlanned', event.target.value)}>
                                            <Radio.Button style={{width: '50%'}} value="true"><FormattedMessage id={"labels.yes"}/></Radio.Button>
                                            <Radio.Button style={{width: '50%'}} value="false"><FormattedMessage id={"labels.no"}/></Radio.Button>
                                        </Radio.Group>
                                    </FormItem>
                                </Col>
                                <Col span={6}>
                                    <FormItem label={intl.formatMessage({id: 'labels.amenities'})}>
                                        <MultiSelect
                                            itemList={this.state.appData.apartmentAmenities}
                                            onUpdate={this.updateFormData}
                                            selectedItems={this.state.formData.requiredApartmentAmenities}
                                            name='requiredApartmentAmenities'
                                        />
                                    </FormItem>
                                </Col>
                            </TabPane>
                            <TabPane tab={intl.formatMessage({ id: 'labels.kitchen' })} key="3">
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.kitchen_type'})}>
                                <MultiSelect
                                    itemList={this.state.appData.kitchenTypes}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.allowedKitchenTypes}
                                    name='allowedKitchenTypes'
                                />
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.cooker_type'})}>
                                <MultiSelect
                                    itemList={this.state.appData.cookerTypes}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.allowedCookerTypes}
                                    name='allowedCookerTypes'
                                />
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                className="global-search-criteria-input"
                                label={intl.formatMessage({id: 'labels.area'})}
                                validateStatus={this.getValidationStatus("kitchenArea")}
                                help={this.getErrorMessage("kitchenArea")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minKitchenArea"
                                            //addonAfter="PLN"
                                            value={this.state.formData.minKitchenArea}
                                            onChange={event => this.onPositiveRangeBeginChanged(event.target.value, 'minKitchenArea', 'maxKitchenArea', 'kitchenArea')}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxKitchenArea"
                                            //addonAfter="PLN"
                                            value={this.state.formData.maxKitchenArea}
                                            onChange={event => this.onPositiveRangeEndChanged(event.target.value, 'minKitchenArea', 'maxKitchenArea', 'kitchenArea')}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                                <Col span={6}>
                                    <FormItem label={intl.formatMessage({id: 'labels.accessories'})}>
                                        <MultiSelect
                                            itemList={this.state.appData.kitchenFurnishing}
                                            onUpdate={this.updateFormData}
                                            selectedItems={this.state.formData.requiredKitchenFurnishing}
                                            name='requiredKitchenFurnishing'
                                        />
                                    </FormItem>
                                </Col>
                            </TabPane>
                        <TabPane tab="Łazienka" key="4">
                            <Col span={6}>
                                <FormItem
                                    className="global-search-criteria-input"
                                    label={intl.formatMessage({id: 'labels.number_of_bathrooms'})}
                                    validateStatus={this.getValidationStatus("numberOfBathrooms")}
                                    help={this.getErrorMessage("numberOfBathrooms")}
                                >
                                    <Row gutter={6}>
                                        <Col span={12}>
                                            <Input
                                                name="minNumberOfBathrooms"
                                                //addonAfter="PLN"
                                                value={this.state.formData.minNumberOfBathrooms}
                                                onChange={event => this.onPositiveWithZeroRangeBeginChanged(event.target.value, 'minNumberOfBathrooms', 'maxNumberOfBathrooms', 'numberOfBathrooms')}
                                                placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                            />
                                        </Col>
                                        <Col span={12}>
                                            <Input
                                                name="maxNumberOfBathrooms"
                                                //addonAfter="PLN"
                                                value={this.state.formData.maxNumberOfBathrooms}
                                                onChange={event => this.onPositiveWithZeroRangeEndChanged(event.target.value, 'minNumberOfBathrooms', 'maxNumberOfBathrooms', 'numberOfBathrooms')}
                                                placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                            />
                                        </Col>
                                    </Row>
                                </FormItem>
                            </Col>
                            <Col span={6}>
                                <FormItem label={intl.formatMessage({id: 'labels.separate_wc'})} className="global-search-criteria-input">
                                    <Radio.Group buttonStyle="solid" style={{width: '100%'}} onChange={event => this.updateFormData('hasSeparatedWC', event.target.value)}>
                                        <Radio.Button style={{width: '50%'}} value="true"><FormattedMessage id={"labels.yes"}/></Radio.Button>
                                        <Radio.Button style={{width: '50%'}} value="false"><FormattedMessage id={"labels.no"}/></Radio.Button>
                                    </Radio.Group>
                                </FormItem>
                            </Col>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.accessories'})}>
                                <MultiSelect
                                    itemList={this.state.appData.bathroomFurnishing}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.requiredBathroomFurnishing}
                                    name='requiredBathroomFurnishing'
                                />
                            </FormItem>
                        </Col>
                        </TabPane>
                        <TabPane tab={"Preferencje i okolica"} key="5">
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.preferences'})}>
                                <MultiSelect
                                    itemList={this.state.appData.preferences}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.requiredPreferences}
                                    name='requiredPreferences'
                                />
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.neighbourhood'})}>
                                <MultiSelect
                                    itemList={this.state.appData.neighborhood}
                                    onUpdate={this.updateFormData}
                                    selectedItems={this.state.formData.requiredNeighbourhoodItems}
                                    name='requiredNeighbourhoodItems'
                                />
                            </FormItem>
                        </Col>
                        </TabPane>
                        </Tabs>
                    </Row>}
                </Form>
            </div>
        );
    }
}

export default injectIntl(withRouter(SearchBox));