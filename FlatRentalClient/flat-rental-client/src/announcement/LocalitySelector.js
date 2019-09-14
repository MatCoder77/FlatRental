import {Form, Select} from 'antd';
import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import {
    getBuildingTypes,
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

class LocalitySelector extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedValue: null,
            voivodeships: [],
            districts: [],
            communes: [],
            localities: [],
            localityDistricts: [],
            localityParts: [],
            streets: [],
        }
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
    }


    loadData(supplierFunction, filedName, param) {
        let promise = supplierFunction(param);

        if(!promise) {
            return;
        }

        // this.setState({
        //     isLoading: true
        // });

        promise
            .then(response => {
                this.setState({
                    [filedName]: response
                    // isLoading: false
                })
            }).catch(error => {
            // this.setState({
            //     isLoading: false
            // })
        });
    }

    autoselectIfOnlyOneElement(data) {
        if(data.length == 1) {
            return data[0].name;
        }
        return;
    }

    loadVoivodeships() {
        this.loadData(getVoivodeships, 'voivodeships');
    }

    loadDistrictForVoivodeship(voivodeshipId) {
        this.loadData(getDistricts, 'districts', voivodeshipId);
    }

    loadCommunesForDistrict(districtId) {
        this.loadData(getCommunes, 'communes', districtId);
    }

    loadLocalitiesForCommune(communeId) {
        this.loadData(getLocalities, 'localities', communeId);
    }

    loadLocalityDistrictsForLocality(localityId) {
        this.loadData(getLocalityDistricts, 'localityDistricts', localityId);
    }

    loadLocalityPartsForParentLocality(parentLocalityId) {
        this.loadData(getLocalityPartsForParentLocality, 'localityParts', parentLocalityId);
    }

    loadLocalityPartsForParentLocalityDistrict(parentLocalityDistrictId) {
        this.loadData(getLocalityPartsForParentLocalityDistrict, 'localityParts', parentLocalityDistrictId);
    }

    loadStreetsForParentLocality(parentLocalityId) {
        this.loadData(getStreets, 'streets', parentLocalityId);
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

    render() {
        const { intl } = this.props;
        return (
            <Form className="step-form" layout="horizontal" {...this.props}>
                <FormItem label={intl.formatMessage({ id: 'labels.voivodeship' })}>
                <Select
                    showSearch
                    placeholder={intl.formatMessage({ id: 'placeholders.voivodeship' })}
                    optionFilterProp="children"
                    onChange={this.loadDistrictForVoivodeship}
                    //value={this.autoselectIfOnlyOneElement(this.state.voivodeships)}
                    // onFocus={onFocus}
                    // onBlur={onBlur}
                    // onSearch={onSearch}8888888
                     filterOption={(input, option) =>
                         option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                     }
                >
                    {this.state.voivodeships.map(voivodeship => (
                        <Option key={voivodeship.id} value={voivodeship.id}>{voivodeship.name}</Option>
                    ))}
                </Select>
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.district' })}>
                <Select
                    showSearch
                    placeholder={intl.formatMessage({ id: 'placeholders.district' })}
                    optionFilterProp="children"
                    onChange={this.loadCommunesForDistrict}
                    //value={this.autoselectIfOnlyOneElement(this.state.districts)}
                    // onFocus={onFocus}
                    // onBlur={onBlur}
                    // onSearch={onSearch}8888888
                    filterOption={(input, option) =>
                        option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                    }
                >
                    {this.state.districts.map(district => (
                        <Option key={district.id} value={district.id}>{district.name}</Option>
                    ))}
                </Select>
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.commune' })}>
                    <Select
                        showSearch
                        placeholder={intl.formatMessage({ id: 'placeholders.commune' })}
                        optionFilterProp="children"
                        onChange={this.loadLocalitiesForCommune}
                        //value={this.autoselectIfOnlyOneElement(this.state.communes)}
                        // onFocus={onFocus}
                        // onBlur={onBlur}
                        // onSearch={onSearch}8888888
                        filterOption={(input, option) =>
                            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                    >
                        {this.state.communes.map(commune => (
                            <Option key={commune.id} value={commune.id}>{commune.name}</Option>
                        ))}
                    </Select>
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.locality' })}>
                    <Select
                        showSearch
                        placeholder={intl.formatMessage({ id: 'placeholders.locality' })}
                        optionFilterProp="children"
                        onChange={this.onLocalityChange}
                        //value={this.autoselectIfOnlyOneElement(this.state.localities)}
                        // onFocus={onFocus}
                        // onBlur={onBlur}
                        // onSearch={onSearch}8888888
                        filterOption={(input, option) =>
                            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                    >
                        {this.state.localities.map(locality => (
                            <Option key={locality.id} value={locality.id}>{locality.name}</Option>
                        ))}
                    </Select>
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.localityDistrict' })}>
                    <Select
                        showSearch
                        placeholder={intl.formatMessage({ id: 'placeholders.localityDistrict' })}
                        optionFilterProp="children"
                        onChange={this.onLocalityDistrictChange}
                        //value={this.autoselectIfOnlyOneElement(this.state.localityDistricts)}
                        // onFocus={onFocus}
                        // onBlur={onBlur}
                        // onSearch={onSearch}8888888
                        filterOption={(input, option) =>
                            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                    >
                        {this.state.localityDistricts.map(localityDistrict => (
                            <Option key={localityDistrict.id} value={localityDistrict.id}>{localityDistrict.name}</Option>
                        ))}
                    </Select>
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.localityPart' })}>
                    <Select
                        showSearch
                        placeholder={intl.formatMessage({ id: 'placeholders.localityPart' })}
                        optionFilterProp="children"
                        //value={this.autoselectIfOnlyOneElement(this.state.localityParts)}
                        //onChange={this.loadCommunesForDistrict}
                        // onFocus={onFocus}
                        // onBlur={onBlur}
                        // onSearch={onSearch}8888888
                        filterOption={(input, option) =>
                            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                    >
                        {this.state.localityParts.map(localityPart => (
                            <Option key={localityPart.id} value={localityPart.id}>{localityPart.name}</Option>
                        ))}
                    </Select>
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.street' })}>
                    <Select
                        showSearch
                        placeholder={intl.formatMessage({ id: 'placeholders.street' })}
                        optionFilterProp="children"
                        //value={this.autoselectIfOnlyOneElement(this.state.communes)}
                        //onChange={this.loadCommunesForDistrict}
                        // onFocus={onFocus}
                        // onBlur={onBlur}
                        // onSearch={onSearch}8888888
                        filterOption={(input, option) =>
                            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                    >
                        {this.state.streets.map(street => (
                            <Option key={street.id} value={street.id}>{this.getStreetLabel(street)}</Option>
                        ))}
                    </Select>
                </FormItem>
            </Form>

        );
    }
}

export default injectIntl(LocalitySelector);
