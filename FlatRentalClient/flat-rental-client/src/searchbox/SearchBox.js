import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import { withRouter } from 'react-router-dom';
import {Icon, Button, Input, AutoComplete, Select, Row, Col, Form, InputNumber} from 'antd';
import './SearchBox.css'
import ComboBox from "../commons/ComboBox";
import { Collapse } from 'antd';
import { Radio } from 'antd';
import {
    getApartmentStateTypes,
    getBuildingMaterialTypes,
    getBuildingTypes, getCookerTypes,
    getHeatingTypes, getKitchenTypes,
    getLocations, getParkingTypes, getWindowTypes
} from "../infrastructure/RestApiHandler";
import MultiSelect from "../commons/MultiSelect";

const { Option } = AutoComplete;
const FormItem = Form.Item;

const text = `
  A dog is a type of domesticated animal.
  Known for its loyalty and faithfulness,
  it can be found as a welcome guest in many households across the world.
`;

class SearchBox extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dataSource: [],
            appData: {},
            formData: {},
            isExpanded: false
        };

        this.getRandomInt = this.getRandomInt.bind(this);
        this.searchResult = this.searchResult.bind(this);
        this.updateFormData = this.updateFormData.bind(this);
        this.renderOption = this.renderOption.bind(this);
        this.handleSearch = this.handleSearch.bind(this);
        this.createSearchResultLabel = this.createSearchResultLabel.bind(this);
        this.changeIsExpanded = this.changeIsExpanded.bind(this);
        this.loadData = this.loadData.bind(this);

        this.announcementTypes = ["FLAT", "ROOM", "PLACE_IN_ROOM", "LOOK_FOR_FLAT", "LOOK_FOR_ROOM", "LOOK_FOR_PLACE_IN_ROOM"]
    }

    getRandomInt(max, min = 0) {
        return Math.floor(Math.random() * (max - min + 1)) + min; // eslint-disable-line no-mixed-operators
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

    loadData(supplierFunction, fieldName, param) {
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
            }).catch(error => {
            // this.setState({
            //     isLoading: false
            // })
        });
    }

    renderOption(item, index) {
        return (
            <Option key={index}>
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
        let voivodeship = searchResult.voivodeship ? searchResult.voivodeship.name : "";
        let district = searchResult.district ? searchResult.district.name : "";
        //let hasCommuneSameNameAsDistrict = searchResult.commune && searchResult.district && (searchResult.commune.name != searchResult.district.name);
        let commune = (searchResult.commune) ? searchResult.commune.name : "";
        let locality = (searchResult.locality ? searchResult.locality.name : "");
        let localityDistrict = (searchResult.localityDistrict ? searchResult.localityDistrict.name : "");
        let localityPart = (searchResult.localityPart ? searchResult.localityPart.name : "");
        let street = (searchResult.street ? (searchResult.street.leading_name ? searchResult.street.leading_name : "") + searchResult.street.main_name : "");

        return [voivodeship, district, commune, locality, localityDistrict, localityPart, street].join(", ");
    }

    handleSearch = value => {
        this.searchResult(value);
    };

    updateFormData(fieldName, fieldValue) {
        const {formData} = this.state;
        formData[fieldName] = fieldValue;
        this.setState({formData});
        console.log(this.state.formData);
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
                                            >
                                                <Icon type="search" />
                                            </Button>
                                        }
                                    />
                                </AutoComplete>
                            </FormItem>
                        </Col>
                    </Row>
                    <Row gutter={12}>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.area'})}
                                //validateStatus={this.props.getValidationStatus("totalArea")}
                                //help={this.props.getErrorMessage("totalArea")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minTotalArea"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxTotalArea"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.number_of_rooms'})}
                                //validateStatus={this.props.getValidationStatus("numberOfRooms")}
                                //help={this.props.getErrorMessage("numberOfRooms")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minNumberOfRooms"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxNumberOfRooms"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.price_per_month'})}
                                //validateStatus={this.props.getValidationStatus("pricePerMonth")}
                                //help={this.props.getErrorMessage("pricePerMonth")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minPricePerMonth"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxPricePerMonth"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
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
                    <Row gutter={12}>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.estimated_additional_costs'})}
                                //validateStatus={this.props.getValidationStatus("pricePerMonth")}
                                //help={this.props.getErrorMessage("pricePerMonth")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minAdditionalCostsPerMonth"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxAdditionalCostsPerMonth"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.deposit'})}
                                //validateStatus={this.props.getValidationStatus("pricePerMonth")}
                                //help={this.props.getErrorMessage("pricePerMonth")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minSecurityDeposit"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="minSecurityDeposit"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.floor'})}
                                //validateStatus={this.props.getValidationStatus("pricePerMonth")}
                                //help={this.props.getErrorMessage("pricePerMonth")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minFloor"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxFloor"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.max_floor'})}
                                //validateStatus={this.props.getValidationStatus("pricePerMonth")}
                                //help={this.props.getErrorMessage("pricePerMonth")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minMaxFloorInBuilding"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxMaxFloorInBuilding"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
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
                                label={intl.formatMessage({id: 'labels.area'})}
                                //validateStatus={this.props.getValidationStatus("pricePerMonth")}
                                //help={this.props.getErrorMessage("pricePerMonth")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minKitchenArea"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxKitchenArea"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.year_built'})}
                                //validateStatus={this.props.getValidationStatus("pricePerMonth")}
                                //help={this.props.getErrorMessage("pricePerMonth")}
                            >
                                <Row gutter={6}>
                                    <Col span={12}>
                                        <Input
                                            name="minYearBuilt"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.min_value'})}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Input
                                            name="maxYearBuilt"
                                            //addonAfter="PLN"
                                            //value={this.props.formData.pricePerMonth}
                                            //onChange={event => this.updateOnChange(event, this.validateIfPositiveInteger)}
                                            placeholder={intl.formatMessage({id: 'placeholders.max_value'})}
                                        />
                                    </Col>
                                </Row>
                            </FormItem>
                        </Col>
                        <Col span={6}>
                            <FormItem label={intl.formatMessage({id: 'labels.well_planned'})}>
                                <Radio.Group buttonStyle="solid" style={{width: '100%'}}>
                                    <Radio.Button style={{width: '50%'}} value="true"><FormattedMessage id={"labels.yes"}/></Radio.Button>
                                    <Radio.Button style={{width: '50%'}} value="false"><FormattedMessage id={"labels.no"}/></Radio.Button>
                                </Radio.Group>
                            </FormItem>
                        </Col>
                    </Row>}
                </Form>
            </div>
        );
    }
}

export default injectIntl(withRouter(SearchBox));