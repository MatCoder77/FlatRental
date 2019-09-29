import {Form, Select} from 'antd';
import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import {
    getCommunes,
    getDistricts,
    getLocalities,
    getLocalityDistricts,
    getLocalityPartsForParentLocality,
    getLocalityPartsForParentLocalityDistrict,
    getStreets,
    getVoivodeships
} from "../infrastructure/RestApiHandler";
import './Step.css';

const {Option} = Select;
const FormItem = Form.Item;

class LocalityStep extends Component {
    constructor(props) {
        super(props);
        this.loadVoivodeships = this.loadVoivodeships.bind(this);
        this.loadDistrictForVoivodeship = this.loadDistrictForVoivodeship.bind(this);
        this.loadCommunesForDistrict = this.loadCommunesForDistrict.bind(this);
        this.loadLocalitiesForCommune = this.loadLocalitiesForCommune.bind(this);
        this.loadLocalityDistrictsForLocality = this.loadLocalityDistrictsForLocality.bind(this);
        this.loadLocalityPartsForParentLocality = this.loadLocalityPartsForParentLocality.bind(this);
        this.loadLocalityPartsForParentLocalityDistrict = this.loadLocalityPartsForParentLocalityDistrict.bind(this);
        this.loadStreetsForParentLocality = this.loadStreetsForParentLocality.bind(this);
        this.onLocalityChange = this.onLocalityChange.bind(this);
        this.onLocalityDistrictChange = this.onLocalityDistrictChange.bind(this);
        this.getStreetLabel = this.getStreetLabel.bind(this);
        this.autoselectIfOnlyOneElement = this.autoselectIfOnlyOneElement.bind(this);
        this.updateOnSelect = this.updateOnSelect.bind(this);
        this.onVoivodeshipChange = this.onVoivodeshipChange.bind(this);
        this.onDistrictChange = this.onDistrictChange.bind(this);
        this.onCommuneChange = this.onCommuneChange.bind(this);
        this.hasAnyLoadedData = this.hasAnyLoadedData.bind(this);
        this.autoselectAndMakeOptionalIfNecessary = this.autoselectAndMakeOptionalIfNecessary.bind(this);

        this.props.registerRequiredFields(['address.voivodeship.id', 'address.district.id', 'address.commune.id', 'address.locality.id', 'address.localityDistrict.id', 'address.street.id']);
    }

    autoselectIfOnlyOneElement(loadedData, callbackParam) {
        if (this.props.appData[loadedData].length == 1) {
            let id = this.props.appData[loadedData][0].id;
            this.updateOnSelect(callbackParam, id);
            if (callbackParam === 'address.voivodeship.id') {
                this.loadDistrictForVoivodeship(id);
            } else if (callbackParam === 'address.district.id') {
                this.loadCommunesForDistrict(id);
            } else if (callbackParam === 'address.commune.id') {
                this.loadLocalitiesForCommune(id);
            } else if (callbackParam === 'address.locality.id') {
                this.onLocalityChange(id);
            } else if (callbackParam === 'address.localityDistrict.id') {
                this.onLocalityDistrictChange(id);
            }
        }
    }

    loadVoivodeships() {
        this.props.loadData(getVoivodeships, 'voivodeships');
    }

    loadDistrictForVoivodeship(voivodeshipId) {
        this.props.loadData(getDistricts, 'districts', voivodeshipId, this.autoselectIfOnlyOneElement, 'address.district.id');
    }

    loadCommunesForDistrict(districtId) {
        this.props.loadData(getCommunes, 'communes', districtId, this.autoselectIfOnlyOneElement, 'address.commune.id');
    }

    loadLocalitiesForCommune(communeId) {
        this.props.loadData(getLocalities, 'localities', communeId, this.autoselectIfOnlyOneElement, 'address.locality.id');
    }

    loadLocalityDistrictsForLocality(localityId) {
        this.props.loadData(getLocalityDistricts, 'localityDistricts', localityId, this.autoselectAndMakeOptionalIfNecessary, 'address.localityDistrict.id');
    }

    loadLocalityPartsForParentLocality(parentLocalityId) {
        this.props.loadData(getLocalityPartsForParentLocality, 'localityParts', parentLocalityId);
    }

    loadLocalityPartsForParentLocalityDistrict(parentLocalityDistrictId) {
        this.props.loadData(getLocalityPartsForParentLocalityDistrict, 'localityParts', parentLocalityDistrictId);
    }

    loadStreetsForParentLocality(parentLocalityId) {
        this.props.loadData(getStreets, 'streets', parentLocalityId, this.autoselectAndMakeOptionalIfNecessary, 'address.street.id');
    }

    componentDidMount() {
        this.loadVoivodeships();
    }

    getStreetLabel(street) {
        return street.streetType.readableValue + " " + (street.leadingName ? street.leadingName + " " : "") + street.mainName;
    }

    autoselectAndMakeOptionalIfNecessary(loadedData, callbackParam) {
        if(this.props.appData[loadedData].length === 0) {
            this.props.unregisterRequiredFields([callbackParam]);
            this.props.onUpdate(callbackParam, undefined, {validateStatus: 'success', errorMsg: null});
        }
        this.autoselectIfOnlyOneElement(loadedData, callbackParam);
    }

    onVoivodeshipChange(voivodeshipId) {
        this.props.formData["address.district.id"] = undefined;
        this.props.appData.districts = [];
        this.props.formData["address.commune.id"] = undefined;
        this.props.appData.communes = [];
        this.props.formData["address.locality.id"] = undefined;
        this.props.appData.localities = [];
        this.props.formData["address.localityDistrict.id"] = undefined;
        this.props.appData.localityDistricts = [];
        this.props.formData["address.localityPart.id"] = undefined;
        this.props.appData.localityParts = [];
        this.props.formData["address.street.id"] = undefined;
        this.props.appData.streets = [];
        this.props.registerRequiredFields(['address.localityDistrict.id', 'address.street.id'], true);
        this.loadDistrictForVoivodeship(voivodeshipId);
    }

    onDistrictChange(districtId) {
        this.props.formData["address.commune.id"] = undefined;
        this.props.appData.communes = [];
        this.props.formData["address.locality.id"] = undefined;
        this.props.appData.localities = [];
        this.props.formData["address.localityDistrict.id"] = undefined;
        this.props.appData.localityDistricts = [];
        this.props.formData["address.localityPart.id"] = undefined;
        this.props.appData.localityParts = [];
        this.props.formData["address.street.id"] = undefined;
        this.props.appData.streets = [];
        this.props.registerRequiredFields(['address.localityDistrict.id', 'address.street.id'], true);
        this.loadCommunesForDistrict(districtId);
    }

    onCommuneChange(communeId) {
        this.props.formData["address.locality.id"] = undefined;
        this.props.appData.localities = [];
        this.props.formData["address.localityDistrict.id"] = undefined;
        this.props.appData.localityDistricts = [];
        this.props.formData["address.localityPart.id"] = undefined;
        this.props.appData.localityParts = [];
        this.props.formData["address.street.id"] = undefined;
        this.props.appData.streets = [];
        this.props.registerRequiredFields(['address.localityDistrict.id', 'address.street.id'], true);
        this.loadLocalitiesForCommune(communeId);
    }

    onLocalityChange(localityId) {
        this.props.formData["address.localityDistrict.id"] = undefined;
        this.props.appData.localityDistricts = [];
        this.props.formData["address.localityPart.id"] = undefined;
        this.props.appData.localityParts = [];
        this.props.formData["address.street.id"] = undefined;
        this.props.appData.streets = [];
        this.props.registerRequiredFields(['address.localityDistrict.id', 'address.street.id'], true);
        this.loadLocalityDistrictsForLocality(localityId);
        this.loadLocalityPartsForParentLocality(localityId);
        this.loadStreetsForParentLocality(localityId);
    }

    onLocalityDistrictChange(localityDistrictId) {
        this.props.formData["address.localityPart.id"] = undefined;
        this.props.appData.localityParts = [];
        this.props.formData["address.street.id"] = undefined;
        this.props.appData.streets = [];
        this.props.registerRequiredFields(['address.street.id'], true);
        this.loadLocalityPartsForParentLocalityDistrict(localityDistrictId);
        this.loadStreetsForParentLocality(localityDistrictId);
    }

    hasAnyLoadedData(data) {
        return data && data.length > 0;
    }

    updateOnSelect(name, value, validationFunction) {
        let validationResult = validationFunction ? validationFunction(value) : {validateStatus: 'success', errorMsg: null};
        this.props.onUpdate(name, value, validationResult);
    }

    render() {
        const {intl} = this.props;
        return (
            <div className="step-container">
                <h1 className="page-title"><FormattedMessage id="labels.localization"/></h1>
                <div className="step-container-content">
                    <Form className="step-form" layout="horizontal" {...this.props}>
                        <FormItem label={intl.formatMessage({id: 'labels.voivodeship'})} required={true}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({id: 'placeholders.voivodeship'})}
                                optionFilterProp="children"
                                onChange={this.onVoivodeshipChange}
                                onSelect={value => this.updateOnSelect('address.voivodeship.id', value)}
                                value={this.props.formData["address.voivodeship.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {this.props.appData.voivodeships ? this.props.appData.voivodeships.map(voivodeship => (
                                    <Option key={voivodeship.id}
                                            value={voivodeship.id}>{voivodeship.name}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        {this.hasAnyLoadedData(this.props.appData.districts) &&
                        <FormItem label={intl.formatMessage({id: 'labels.district'})} required={true}>

                            <Select
                                showSearch
                                placeholder={intl.formatMessage({id: 'placeholders.district'})}
                                optionFilterProp="children"
                                onChange={this.onDistrictChange}
                                onSelect={value => this.updateOnSelect('address.district.id', value)}
                                value={this.props.formData["address.district.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toString().toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {this.props.appData.districts ? this.props.appData.districts.map(district => (
                                    <Option key={district.id} value={district.id}>{district.name} <FormattedMessage id={district.districtType}>{txt => <font color="#808080"><i>({txt})</i></font>}</FormattedMessage></Option>)) : ""}
                            </Select>

                        </FormItem>
                        }
                        {this.hasAnyLoadedData(this.props.appData.communes) &&
                        <FormItem label={intl.formatMessage({id: 'labels.commune'})} required={true}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({id: 'placeholders.commune'})}
                                optionFilterProp="children"
                                onChange={this.onCommuneChange}
                                onSelect={value => this.updateOnSelect('address.commune.id', value)}
                                value={this.props.formData["address.commune.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {this.props.appData.communes ? this.props.appData.communes.map(commune => (
                                    <Option key={commune.id} value={commune.id}>{commune.name} <FormattedMessage id={commune.communeType}>{txt => <font color="#808080"><i>({txt})</i></font>}</FormattedMessage></Option>)) : ""}
                            </Select>
                        </FormItem>
                        }
                        {this.hasAnyLoadedData(this.props.appData.localities) &&
                        <FormItem label={intl.formatMessage({id: 'labels.locality'})} required={true}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({id: 'placeholders.locality'})}
                                optionFilterProp="children"
                                onChange={this.onLocalityChange}
                                onSelect={value => this.updateOnSelect('address.locality.id', value)}
                                value={this.props.formData["address.locality.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {this.props.appData.localities ? this.props.appData.localities.map(locality => (
                                    <Option key={locality.id} value={locality.id}>{locality.name}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        }
                        {this.hasAnyLoadedData(this.props.appData.localityDistricts) &&
                        <FormItem label={intl.formatMessage({id: 'labels.localityDistrict'})} required={true}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({id: 'placeholders.localityDistrict'})}
                                optionFilterProp="children"
                                onChange={this.onLocalityDistrictChange}
                                onSelect={value => this.updateOnSelect('address.localityDistrict.id', value)}
                                value={this.props.formData["address.localityDistrict.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {this.props.appData.localityDistricts ? this.props.appData.localityDistricts.map(localityDistrict => (
                                    <Option key={localityDistrict.id}
                                            value={localityDistrict.id}>{localityDistrict.name}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        }
                        {this.hasAnyLoadedData(this.props.appData.localityParts) && (this.props.formData["address.localityDistrict.id"] || !this.hasAnyLoadedData(this.props.appData.localityDistricts)) &&
                        <FormItem label={intl.formatMessage({id: 'labels.localityPart'})}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({id: 'placeholders.localityPart'})}
                                optionFilterProp="children"
                                onSelect={value => this.updateOnSelect('address.localityPart.id', value)}
                                value={this.props.formData["address.localityPart.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {this.props.appData.localityParts ? this.props.appData.localityParts.map(localityPart => (
                                    <Option key={localityPart.id}
                                            value={localityPart.id}>{localityPart.name}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        }
                        {this.hasAnyLoadedData(this.props.appData.streets) &&
                        <FormItem label={intl.formatMessage({id: 'labels.street'})} required={true}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({id: 'placeholders.street'})}
                                optionFilterProp="children"
                                onSelect={value => this.updateOnSelect('address.street.id', value)}
                                value={this.props.formData["address.street.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {this.props.appData.streets ? this.props.appData.streets.map(street => (
                                    <Option key={street.id}
                                            value={street.id}>{this.getStreetLabel(street)}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        }
                    </Form>
                </div>
            </div>
        );
    }

}

export default injectIntl(LocalityStep);