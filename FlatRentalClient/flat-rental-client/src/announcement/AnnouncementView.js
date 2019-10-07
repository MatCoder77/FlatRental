import React, { Component } from 'react';
import { PageHeader, Tabs, Button, Statistic, Descriptions } from 'antd';
import {FormattedMessage, injectIntl} from "react-intl";
import ImagesGallery2 from "./ImagesGallery";
import {API_BASE_URL} from "../infrastructure/Constants";

const { TabPane } = Tabs;

class AnnouncementView extends Component {

    constructor(props) {
        super(props);
        this.getReadableValueForEnumeration = this.getReadableValueForEnumeration.bind(this);
        this.getTextValueOptionalDescriptionItem = this.getTextValueOptionalDescriptionItem.bind(this);
        this.getEnumValueOptionalDescriptionItem = this.getEnumValueOptionalDescriptionItem.bind(this);
        this.getBooleanValueOptionalDescriptionItem = this.getBooleanValueOptionalDescriptionItem.bind(this);
        this.getDescriptionListForCheckedItems = this.getDescriptionListForCheckedItems.bind(this);
        this.getImages = this.getImages.bind(this);
    }

    getTextValueOptionalDescriptionItem(labelKey, value, suffix) {
        if (value) {
            return <Descriptions.Item label={this.props.intl.formatMessage({id: labelKey})}>{value} {suffix ? suffix : ""}</Descriptions.Item>
        }
        return "";
    }

    getEnumValueOptionalDescriptionItem(labelKey, enumValue, suffix) {
        let readableValue = this.getReadableValueForEnumeration(enumValue);
        if (readableValue) {
            return <Descriptions.Item label={this.props.intl.formatMessage({id: labelKey})}>{readableValue} {suffix ? suffix : ""}</Descriptions.Item>
        }
        return "";
    }

    getBooleanValueOptionalDescriptionItem(labelKey, value) {
        if (typeof value !== 'undefined' && value !== null) {
            let readableValue = this.props.intl.formatMessage({id: (value ? 'labels.yes' : 'labels.no')});
            return <Descriptions.Item label={this.props.intl.formatMessage({id: labelKey})}>{readableValue}</Descriptions.Item>;
        }
        return "";
    }

    getReadableValueForEnumeration(enumValue) {
        if (enumValue) {
            return this.props.intl.formatMessage({id: enumValue})
        }
        return "";
    }

    getDescriptionListForCheckedItems(items) {
        return items ? items.map(item => item.value ? (<Descriptions.Item>&#8226; {this.props.intl.formatMessage({id: item.value})}</Descriptions.Item>) : "") : "";
    }

    getLocalization() {
        let voivodeship = this.props.data.voivodeship;
        let district = this.props.data.district;
        let commune = this.props.data.commune;
        let locality = this.props.data.locality;
        let localityDistrict = this.props.data.localityDistrict;
        let localityPart = this.props.data.localityPart;
        let street = this.props.data.street;


    }

    getImages() {
        let images = this.props.data.announcementImages ? this.props.data.announcementImages : [];
        return images.map(image => API_BASE_URL + "/file/download/" + image.filename);
    }

    render() {
        const {intl} = this.props;
        const renderContent = (column = 2) => (
            <Descriptions size="small" column={column}>
                <Descriptions.Item label="Created">Lili Qu</Descriptions.Item>
                <Descriptions.Item label="Association">
                    <a>421421</a>
                </Descriptions.Item>
                <Descriptions.Item label="Creation Time">2017-01-10</Descriptions.Item>
                <Descriptions.Item label="Effective Time">2017-10-10</Descriptions.Item>
                <Descriptions.Item label="Remarks">
                    Gonghu Road, Xihu District, Hangzhou, Zhejiang, China
                </Descriptions.Item>
            </Descriptions>
        );

        const extraContent = (
            <div
                style={{
                    display: 'flex',
                    width: 'max-content',
                    justifyContent: 'flex-end',
                }}
            >
                <Statistic
                    title="Status"
                    value="Pending"
                    style={{
                        marginRight: 32,
                    }}
                />
                <Statistic title="Price" prefix="$" value={568.08} />
            </div>
        );

        const Content = ({ children, extra }) => {
            return (
                <div className="content">
                    <div className="main">{children}</div>
                    <div className="extra">{extra}</div>
                </div>
            );
        };
        const squareMeterSuffix = (<span>m<sup>2</sup></span>);
        return (
            <div>
                <PageHeader
                    onBack={() => window.history.back()}
                    title={this.props.data.title}
                    subTitle="This is a subtitle"
                    extra={[
                        <Button key="3">Operation</Button>,
                        <Button key="2">Operation</Button>,
                        <Button key="1" type="primary">
                            Primary
                        </Button>,
                    ]}
                    footer={
                        <Tabs defaultActiveKey="1">
                            <TabPane tab="Details" key="1" />
                            <TabPane tab="Rule" key="2" />
                        </Tabs>
                    }
                >
                    <Content extra={extraContent}>{renderContent()}</Content>
                </PageHeader>

                <div>
                    <ImagesGallery2 imagesList={this.getImages()}/>
                </div>
                <Descriptions size={"default"} title={intl.formatMessage({id: "labels.general_info"})} column={3}>
                    {this.getTextValueOptionalDescriptionItem('labels.area', this.props.data.totalArea, squareMeterSuffix)};
                    {this.getTextValueOptionalDescriptionItem('labels.number_of_rooms', this.props.data.numberOfRooms)};
                    {this.getTextValueOptionalDescriptionItem('labels.floor_max_floor', (this.props.data.floor && this.props.data.maxFloorInBuilding) ? this.props.data.floor + "/" + this.props.data.maxFloorInBuilding : undefined)};
                    {this.getTextValueOptionalDescriptionItem('labels.price_per_month', this.props.data.pricePerMonth, 'PLN')};
                    {this.getTextValueOptionalDescriptionItem('labels.estimated_additional_costs', this.props.data.additionalCostsPerMonth, 'PLN')};
                    {this.getTextValueOptionalDescriptionItem('labels.deposit', this.props.data.securityDeposit, 'PLN')};
                    {this.getTextValueOptionalDescriptionItem('labels.available_from', this.props.data.availableFrom.format('YYYY-MM-DD'))};
                </Descriptions>
                <Descriptions size={"default"} title={intl.formatMessage({id: "labels.amenities"})} column={3}>
                    {this.getDescriptionListForCheckedItems(this.props.data.apartmentAmenities)}
                </Descriptions>
                <Descriptions size={"default"} title={intl.formatMessage({id: "labels.detail_info"})} column={3}>
                    {this.getEnumValueOptionalDescriptionItem('labels.building_type', this.props.data["buildingType.value"])};
                    {this.getEnumValueOptionalDescriptionItem('labels.building_material', this.props.data["buildingMaterial.value"])};
                    {this.getEnumValueOptionalDescriptionItem('labels.heating_type', this.props.data["heatingType.value"])};
                    {this.getEnumValueOptionalDescriptionItem('labels.windows_type', this.props.data["windowType.value"])};
                    {this.getEnumValueOptionalDescriptionItem('labels.parking_type', this.props.data["parkingType.value"])};
                    {this.getEnumValueOptionalDescriptionItem('labels.apartment_state', this.props.data["apartmentState.value"])};
                    {this.getTextValueOptionalDescriptionItem('labels.year_built', this.props.data["yearBuilt"])};
                    {this.getBooleanValueOptionalDescriptionItem('labels.well_planned', this.props.data["wellPlanned"])};

                    {this.getEnumValueOptionalDescriptionItem('labels.kitchen_type', this.props.data["kitchen.kitchenType.value"])};
                    {this.getTextValueOptionalDescriptionItem('labels.area', this.props.data["kitchen.kitchenArea"], squareMeterSuffix)};
                    {this.getEnumValueOptionalDescriptionItem('labels.cooker_type', this.props.data["kitchen.cookerType.value"])};
                    {this.getTextValueOptionalDescriptionItem('labels.number_of_bathrooms', this.props.data["bathroom.numberOfBathrooms"])};
                    {this.getBooleanValueOptionalDescriptionItem('labels.separate_wc', this.props.data["bathroom.separateWC"])};
                </Descriptions>
                <Descriptions size={"default"} title={intl.formatMessage({id: "labels.accessories"})} column={3}>
                    {this.getDescriptionListForCheckedItems(this.props.data["kitchen.furnishing"])}
                </Descriptions>
            </div>
        );
    }

}

export default injectIntl(AnnouncementView);