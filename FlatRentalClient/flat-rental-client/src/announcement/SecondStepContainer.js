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

const { Option } = Select;
const FormItem = Form.Item;

class SecondStepContainer extends Component {
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
    }

    autoselectIfOnlyOneElement(data) {
        if(data.length == 1) {
            return data[0].name;
        }
        return;
    }

    loadVoivodeships() {
        this.props.loadData(getVoivodeships, 'voivodeships');
    }

    loadDistrictForVoivodeship(voivodeshipId) {
        this.props.loadData(getDistricts, 'districts', voivodeshipId);
    }

    loadCommunesForDistrict(districtId) {
        this.props.loadData(getCommunes, 'communes', districtId);
    }

    loadLocalitiesForCommune(communeId) {
        this.props.loadData(getLocalities, 'localities', communeId);
    }

    loadLocalityDistrictsForLocality(localityId) {
        this.props.loadData(getLocalityDistricts, 'localityDistricts', localityId);
    }

    loadLocalityPartsForParentLocality(parentLocalityId) {
        this.props.loadData(getLocalityPartsForParentLocality, 'localityParts', parentLocalityId);
    }

    loadLocalityPartsForParentLocalityDistrict(parentLocalityDistrictId) {
        this.props.loadData(getLocalityPartsForParentLocalityDistrict, 'localityParts', parentLocalityDistrictId);
    }

    loadStreetsForParentLocality(parentLocalityId) {
        this.props.loadData(getStreets, 'streets', parentLocalityId);
    }

    onLocalityChange(localityId) {
        this.loadLocalityDistrictsForLocality(localityId);
        this.loadLocalityPartsForParentLocality(localityId);
        this.loadStreetsForParentLocality(localityId);
    }

    onLocalityDistrictChange(localityDistrictId) {
        this.loadLocalityPartsForParentLocalityDistrict(localityDistrictId);
        this.loadStreetsForParentLocality(localityDistrictId);
    }

    componentDidMount() {
        this.loadVoivodeships();
    }

    getStreetLabel(street) {
        return street.streetType.readableValue + " " + (street.leadingName ? street.leadingName + " " : "") + street.mainName;
    }

    updateOnSelect = name => (value, option) => {
        this.props.onUpdate(name, value);
    }

    render() {
        const { intl } = this.props;
        return (
            <div className="step-container">
                <h1 className="page-title"><FormattedMessage id="labels.localization"/></h1>
                <div className="step-container-content">
                    <Form className="step-form" layout="horizontal" {...this.props}>
                        <FormItem label={intl.formatMessage({ id: 'labels.voivodeship' })}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({ id: 'placeholders.voivodeship' })}
                                optionFilterProp="children"
                                onChange={this.loadDistrictForVoivodeship}
                                onSelect={this.updateOnSelect('address.voivodeship.id')}
                                value={this.props.formData["address.voivodeship.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                { this.props.appData.voivodeships ? this.props.appData.voivodeships.map(voivodeship => (<Option key={voivodeship.id} value={voivodeship.id}>{voivodeship.name}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.district' })}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({ id: 'placeholders.district' })}
                                optionFilterProp="children"
                                onChange={this.loadCommunesForDistrict}
                                onSelect={this.updateOnSelect('address.district.id')}
                                value={this.props.formData["address.district.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                { this.props.appData.districts ? this.props.appData.districts.map(district => (<Option key={district.id} value={district.id}>{district.name}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.commune' })}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({ id: 'placeholders.commune' })}
                                optionFilterProp="children"
                                onChange={this.loadLocalitiesForCommune}
                                onSelect={this.updateOnSelect('address.commune.id')}
                                value={this.props.formData["address.commune.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                { this.props.appData.communes ? this.props.appData.communes.map(commune => (<Option key={commune.id} value={commune.id}>{commune.name}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.locality' })}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({ id: 'placeholders.locality' })}
                                optionFilterProp="children"
                                onChange={this.onLocalityChange}
                                onSelect={this.updateOnSelect('address.locality.id')}
                                value={this.props.formData["address.locality.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {  this.props.appData.localities ? this.props.appData.localities.map(locality => (<Option key={locality.id} value={locality.id}>{locality.name}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.localityDistrict' })}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({ id: 'placeholders.localityDistrict' })}
                                optionFilterProp="children"
                                onChange={this.onLocalityDistrictChange}
                                onSelect={this.updateOnSelect('address.localityDistrict.id')}
                                value={this.props.formData["address.localityDistrict.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                { this.props.appData.localityDistricts ? this.props.appData.localityDistricts.map(localityDistrict => (<Option key={localityDistrict.id} value={localityDistrict.id}>{localityDistrict.name}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.localityPart' })}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({ id: 'placeholders.localityPart' })}
                                optionFilterProp="children"
                                onSelect={this.updateOnSelect('address.localityPart.id')}
                                value={this.props.formData["address.localityPart.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                { this.props.appData.localityParts ? this.props.appData.localityParts.map(localityPart => (<Option key={localityPart.id} value={localityPart.id}>{localityPart.name}</Option>)) : ""}
                            </Select>
                        </FormItem>
                        <FormItem label={intl.formatMessage({ id: 'labels.street' })}>
                            <Select
                                showSearch
                                placeholder={intl.formatMessage({ id: 'placeholders.street' })}
                                optionFilterProp="children"
                                onSelect={this.updateOnSelect('address.street.id')}
                                value={this.props.formData["address.street.id"]}
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                { this.props.appData.streets ? this.props.appData.streets.map(street => (<Option key={street.id} value={street.id}>{this.getStreetLabel(street)}</Option>)) : ""}
                            </Select>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }

}

export default injectIntl(SecondStepContainer);