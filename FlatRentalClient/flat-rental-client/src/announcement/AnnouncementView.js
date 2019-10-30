import React, { Component } from 'react';
import {PageHeader, Tabs, Button, Statistic, Descriptions, Input, Card, Icon, Row, Col} from 'antd';
import {FormattedMessage, injectIntl} from "react-intl";
import ImagesGallery2 from "./ImagesGallery";
import {API_BASE_URL} from "../infrastructure/Constants";
import moment from "moment";
import { Typography, Divider } from 'antd';
import './AnnouncementView.css'
import CommentsSection from "../comment/CommentsSection";
import {addToFavourites, removeFromFavourites} from "../infrastructure/RestApiHandler";
import * as DTOUtils from "../infrastructure/DTOUtils";
import {Link} from "react-router-dom";

const { Title, Paragraph, Text } = Typography;

const { TabPane } = Tabs;
const { TextArea } = Input;

class AnnouncementView extends Component {

    constructor(props) {
        super(props);
        this.getReadableValueForEnumeration = this.getReadableValueForEnumeration.bind(this);
        this.getTextValueOptionalDescriptionItem = this.getTextValueOptionalDescriptionItem.bind(this);
        this.getEnumValueOptionalDescriptionItem = this.getEnumValueOptionalDescriptionItem.bind(this);
        this.getBooleanValueOptionalDescriptionItem = this.getBooleanValueOptionalDescriptionItem.bind(this);
        this.getDescriptionListForCheckedItems = this.getDescriptionListForCheckedItems.bind(this);
        this.getImages = this.getImages.bind(this);
        this.attachLocalityData = this.attachLocalityData.bind(this);
        this.createSearchResultLabel = this.createSearchResultLabel.bind(this);
        this.onCommentAdded = this.onCommentAdded.bind(this);
        this.onCommentRemoved = this.onCommentRemoved.bind(this);
        this.onFavouriteClicked = this.onFavouriteClicked.bind(this);
        this.showNumber = this.showNumber.bind(this);


        this.attachLocalityData();

        this.state = {
            commentsCounter: this.props.data['statistics.commentsCounter'],
            favouritesCounter: this.props.data['statistics.favouritesCounter'],
            isMarkedAsFavourite: this.props.data['userSpecificInfo.isMarkedAsFavourite'],
            viewsCounter: this.props.data['statistics.viewsCounter'],
            isNumberVisible:false
        }

        this.voivodeshipAbbreviation = this.props.intl.formatMessage({ id: 'labels.voivodeship_abbreviation' });
        this.districtAbbreviation = this.props.intl.formatMessage({ id: 'labels.district_abbreviation' });
        this.urbanCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.urban_commune_abbreviation' });
        this.ruralCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.rural_commune_abbreviation' });
        this.mixedCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.mixed_abbreviation' });
        this.capitalCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.capital_commune_abbreviation' });
    }

    showNumber() {
        this.setState({
            isNumberVisible: true
        })
    }

    attachLocalityData() {
        if (this.props.setLocalityAttribute) {
            this.props.setLocalityAttribute('voivodeship', 'voivodeships', ['name']);
            this.props.setLocalityAttribute('district', 'districts', ['name', 'type']);
            this.props.setLocalityAttribute('commune', 'communes', ['name', 'type']);
            this.props.setLocalityAttribute('locality', 'localities', ['name', 'type.id', 'type.typeCode', 'type.typeName']);
            this.props.setLocalityAttribute('localityDistrict', 'localityDistricts', ['name']);
            this.props.setLocalityAttribute('localityPart', 'localityParts', ['name', 'type.id', 'type.typeCode', 'type.typeName']);
            this.props.setLocalityAttribute('street', 'streets', ['name', 'type', 'mainName', 'leadingName']);
        }
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
        return items ? items.map((item, index) => item.value ? (<Descriptions.Item key={index}>&#8226; {this.props.intl.formatMessage({id: item.value})}</Descriptions.Item>) : "") : "";
    }

    getImages() {
        let images = this.props.data.announcementImages ? this.props.data.announcementImages : [];
        return images.map(image => API_BASE_URL + "/file/download/" + image.filename);
    }

    createSearchResultLabel(item) {
        let voivodeship = item['address.voivodeship.name'] ? this.voivodeshipAbbreviation + " " + item['address.voivodeship.name'] : undefined;
        let district = item['address.district.name'] ? (item['address.district.type'] != 'DISTRICT_CITY' && item['address.district.type'] != 'DISTRICT_CAPITAL') ? this.districtAbbreviation + " " + item['address.district.name'] : undefined : undefined;
        //let hasCommuneSameNameAsDistrict = searchResult.commune && searchResult.district && (searchResult.commune.name != searchResult.district.name);
        let commune = (item['address.commune.name']) ? (item['address.district.type'] != 'DISTRICT_CITY' && item['address.district.type'] != 'DISTRICT_CAPITAL') ? this.getCommuneAbbreviation(item['address.commune.type']) + " " + item['address.commune.name'] : undefined : undefined;
        let locality = (item['address.locality.name'] ? item['address.locality.name'] : undefined);
        let localityDistrict = (item['address.localityDistrict.name'] ? item['address.localityDistrict.name'] : undefined);
        let localityPart = (item['address.localityPart.name'] ? item['address.localityPart.name'] : undefined);
        let street = this.getStreetLabel(item['address.street.id'], item['address.street.type'], item['address.street.mainName'], item['address.street.leadingName']);

        return [voivodeship, district, commune, locality, localityDistrict, localityPart, street].filter(Boolean).join(", ");
    }

    getStreetLabel(id, type, mainName, leadingName) {
        if (id && type && mainName) {
            return this.props.intl.formatMessage({id: type}) + " " + (leadingName ? leadingName + " " : "") + mainName;
        }
        return undefined;
    }

    getCommuneAbbreviation(type) {
        if (type == 'URBAN_COMMUNE')
            return this.urbanCommuneAbbreviation;
        if (type == 'RURAL_COMMUNE')
            return this.ruralCommuneAbbreviation;
        if (type == 'MIXED_COMMUNE')
            return this.mixedCommuneAbbreviation;
        if (type == 'CAPITAL_COMMUNE')
            return this.capitalCommuneAbbreviation;
        return "";
    }

    onCommentAdded() {
        const currentCommentsCounter = this.state.commentsCounter
        this.setState({
            commentsCounter: currentCommentsCounter + 1
        });
    }

    onCommentRemoved(numberOfDeletedComments) {
        const currentCommentsCounter = this.state.commentsCounter
        this.setState({
            commentsCounter: currentCommentsCounter - numberOfDeletedComments
        });
    }

    onFavouriteClicked() {
        let promise;
        if (!this.state.isMarkedAsFavourite) {
            promise = addToFavourites(this.props.data.id);
        } else {
            promise = removeFromFavourites(this.props.data.id);
        }
        if (!promise) {
            return;
        }
        promise
            .then(response => {
                let updatedIsMarkedAsFavourite = !this.state.isMarkedAsFavourite;
                let updatedFavouritesCounter = updatedIsMarkedAsFavourite ? this.state.favouritesCounter + 1 : this.state.favouritesCounter - 1;
                this.setState({
                    isMarkedAsFavourite: updatedIsMarkedAsFavourite,
                    favouritesCounter: updatedFavouritesCounter
                });
            }).catch(error => {
        });
    }

    render() {
        const {intl} = this.props;
        const squareMeterSuffix = (<span>m<sup>2</sup></span>);
        const totalArea = this.getTextValueOptionalDescriptionItem('labels.area', this.props.data.totalArea, squareMeterSuffix);
        const numberOfRooms = this.getTextValueOptionalDescriptionItem('labels.number_of_rooms', this.props.data.numberOfRooms);
        const floorAndMaxFloor = this.getTextValueOptionalDescriptionItem('labels.floor_max_floor', (this.props.data.floor && this.props.data.maxFloorInBuilding) ? this.props.data.floor + "/" + this.props.data.maxFloorInBuilding : undefined);
        const pricePerMonth = this.getTextValueOptionalDescriptionItem('labels.price_per_month', this.props.data.pricePerMonth, 'PLN');
        const additionalCosts = this.getTextValueOptionalDescriptionItem('labels.estimated_additional_costs', this.props.data.additionalCostsPerMonth, 'PLN');
        const deposit = this.getTextValueOptionalDescriptionItem('labels.deposit', this.props.data.securityDeposit, 'PLN');
        const availableFrom = this.getTextValueOptionalDescriptionItem('labels.available_from', moment(this.props.data.availableFrom).format('YYYY-MM-DD'));
        const apartmentAmenities = this.getDescriptionListForCheckedItems(this.props.data.apartmentAmenities);
        const buildingType = this.getEnumValueOptionalDescriptionItem('labels.building_type', this.props.data["buildingType.value"]);
        const buildingMaterial = this.getEnumValueOptionalDescriptionItem('labels.building_material', this.props.data["buildingMaterial.value"]);
        const heatingType = this.getEnumValueOptionalDescriptionItem('labels.heating_type', this.props.data["heatingType.value"]);
        const windowType = this.getEnumValueOptionalDescriptionItem('labels.windows_type', this.props.data["windowType.value"]);
        const parkingType = this.getEnumValueOptionalDescriptionItem('labels.parking_type', this.props.data["parkingType.value"]);
        const apartmentState = this.getEnumValueOptionalDescriptionItem('labels.apartment_state', this.props.data["apartmentState.value"]);
        const yearBuilt = this.getTextValueOptionalDescriptionItem('labels.year_built', this.props.data["yearBuilt"]);
        const wellPlanned = this.getBooleanValueOptionalDescriptionItem('labels.well_planned', this.props.data["wellPlanned"]);
        const kitchenType = this.getEnumValueOptionalDescriptionItem('labels.kitchen_type', this.props.data["kitchen.kitchenType.value"]);
        const kitchenArea = this.getTextValueOptionalDescriptionItem('labels.area', this.props.data["kitchen.kitchenArea"], squareMeterSuffix);
        const cookerType = this.getEnumValueOptionalDescriptionItem('labels.cooker_type', this.props.data["kitchen.cookerType.value"]);
        const numberOfBathrooms = this.getTextValueOptionalDescriptionItem('labels.number_of_bathrooms', this.props.data["bathroom.numberOfBathrooms"]);
        const separateWc = this.getBooleanValueOptionalDescriptionItem('labels.separate_wc', this.props.data["bathroom.separateWC"]);
        const kitchenFurnishing = this.getDescriptionListForCheckedItems(this.props.data["kitchen.furnishing"]);
        const bathroomFurnishing = this.getDescriptionListForCheckedItems(this.props.data["bathroom.furnishing"]);
        const preferences = this.getDescriptionListForCheckedItems(this.props.data["preferences"]);
        const neighbourhood = this.getDescriptionListForCheckedItems(this.props.data["neighbourhood"]);
        const sidePricePerMonth = this.props.data.pricePerMonth ? <span className="ant-descriptions-title" style={{fontSize: '1.3em'}}>{this.props.data.pricePerMonth} {intl.formatMessage({id: "labels.price_per_month_2"})}</span> : "";
        const roomArea = room => {
            return this.getTextValueOptionalDescriptionItem('labels.area', room.area, squareMeterSuffix);
        };
        const numberOfPersons = room => {
            return this.getTextValueOptionalDescriptionItem('labels.number_of_persons', room.numberOfPersons);
        };
        const roomFurnishing = room => {
            return this.getDescriptionListForCheckedItems(room.furnishing);
        };
        const roomDescription = (room, roomNumber) => {
            return (
                <Card title={intl.formatMessage({ id: 'labels.room_name' }, { num: roomNumber + 1})} bordered={false} style={{width: '90%'}}>
                    <Descriptions size={"small"} column={3}>
                        {roomArea(room)}
                        {numberOfPersons(room)}
                    </Descriptions>
                    <Descriptions>
                        <Descriptions.Item label={intl.formatMessage({id: "labels.accessories"})}><br/>
                            <Descriptions column={3}>
                                {roomFurnishing(room)}
                            </Descriptions>
                        </Descriptions.Item>
                    </Descriptions>
                </Card>
            );
        };

        const numberOfFlatmates = this.getTextValueOptionalDescriptionItem('labels.flatmates_number', this.props.data.numberOfFlatmates);
        const createdBy = this.props.data.id ? this.getTextValueOptionalDescriptionItem('labels.announcement_author', this.props.data['info.createdBy.name'] + " " + this.props.data['info.createdBy.surname']) : undefined;
        const createdAt = this.props.data.id ? this.getTextValueOptionalDescriptionItem('labels.created_at', moment(this.props.data['info.createdAt']).format('YYYY-MM-DD')) : undefined;
        const updatedAt = this.props.data.id ? this.getTextValueOptionalDescriptionItem('labels.updated_at', moment(this.props.data['info.updatedAt']).format('YYYY-MM-DD')) : undefined;
        const phoneNumber = this.props.data.id ? this.getTextValueOptionalDescriptionItem('labels.phoneNumber', this.state.isNumberVisible ? this.props.data['info.createdBy.phoneNumber'].match(/.{1,3}/g).join(' ') : this.props.data['info.createdBy.phoneNumber'].substr(0, 3) + " XXX XXX") : undefined;

        const flatAnnouncement = (
            <div>
                <Descriptions size={"default"} title={intl.formatMessage({id: "labels.general_info"})} column={3}>
                    {totalArea}
                    {numberOfRooms}
                    {floorAndMaxFloor}
                    {pricePerMonth}
                    {additionalCosts}
                    {deposit}
                    {availableFrom}
                </Descriptions>
                <div className="ant-descriptions-title">{intl.formatMessage({ id: "labels.detail_info" })}</div>
                <Tabs defaultActiveKey="1">
                    <TabPane tab={intl.formatMessage({id: "labels.apartment"})} key="1">
                        <Descriptions size={"default"} column={3}>
                            {buildingType}
                            {buildingMaterial}
                            {heatingType}
                            {windowType}
                            {parkingType}
                            {apartmentState}
                            {yearBuilt}
                            {wellPlanned}
                        </Descriptions>
                        <Descriptions>
                            <Descriptions.Item label={intl.formatMessage({id: "labels.amenities"})}><br/>
                                <Descriptions column={3}>
                                    {apartmentAmenities}
                                </Descriptions>
                            </Descriptions.Item>
                        </Descriptions>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.rooms"})} key="2">
                        <div className="rooms-description-container">
                            {this.props.data.rooms ? this.props.data.rooms.map((room, index) => (roomDescription(room, index))) : ""}
                        </div>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.kitchen"})} key="3">
                        <Descriptions size={"default"} column={3}>
                            {kitchenType}
                            {kitchenArea}
                            {cookerType}
                        </Descriptions>
                        <Descriptions>
                            <Descriptions.Item label={intl.formatMessage({id: "labels.accessories"})}><br/>
                                <Descriptions column={3}>
                                    {kitchenFurnishing}
                                </Descriptions>
                            </Descriptions.Item>
                        </Descriptions>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.bathroom"})} key="4">
                        <Descriptions size={"default"} column={3}>
                            {numberOfBathrooms}
                            {separateWc}
                        </Descriptions>
                        <Descriptions>
                            <Descriptions.Item label={intl.formatMessage({id: "labels.accessories"})}><br/>
                                <Descriptions column={3}>
                                    {bathroomFurnishing}
                                </Descriptions>
                            </Descriptions.Item>
                        </Descriptions>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.preferences"})} key="5">
                        <Descriptions column={3}>
                            {preferences}
                        </Descriptions>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.neighbourhood"})} key="6">
                        <Descriptions column={3}>
                            {neighbourhood}
                        </Descriptions>
                    </TabPane>
                </Tabs>
                <div className="ant-descriptions-title">{intl.formatMessage({ id: 'labels.flat_description' })}</div>
                <Typography>
                    <Paragraph>
                        {this.props.data.description}
                    </Paragraph>
                </Typography>
            </div>
        );

        const roomAnnouncement = (
            <div>
                <Descriptions size={"default"} title={intl.formatMessage({id: "labels.general_info"})} column={3}>
                    {roomArea(this.props.data.rooms[0])}
                    {numberOfPersons(this.props.data.rooms[0])}
                    {floorAndMaxFloor}
                    {pricePerMonth}
                    {additionalCosts}
                    {deposit}
                    {availableFrom}
                </Descriptions>
                <div className="ant-descriptions-title">{intl.formatMessage({ id: "labels.detail_info" })}</div>
                <Tabs defaultActiveKey="1">
                    <TabPane tab={intl.formatMessage({id: "labels.apartment"})} key="1">
                        <Descriptions size={"default"} column={3}>
                            {totalArea}
                            {numberOfRooms}
                            {buildingType}
                            {buildingMaterial}
                            {heatingType}
                            {windowType}
                            {parkingType}
                            {apartmentState}
                            {yearBuilt}
                            {wellPlanned}
                        </Descriptions>
                        <Descriptions>
                            <Descriptions.Item label={intl.formatMessage({id: "labels.amenities"})}><br/>
                                <Descriptions column={3}>
                                    {apartmentAmenities}
                                </Descriptions>
                            </Descriptions.Item>
                        </Descriptions>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.room"})} key="2">
                        <Descriptions>
                            <Descriptions.Item label={intl.formatMessage({id: "labels.accessories"})}><br/>
                                <Descriptions column={3}>
                                    {roomFurnishing(this.props.data.rooms[0])}
                                </Descriptions>
                            </Descriptions.Item>
                        </Descriptions>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.kitchen"})} key="3">
                        <Descriptions size={"default"} column={3}>
                            {kitchenType}
                            {kitchenArea}
                            {cookerType}
                        </Descriptions>
                        <Descriptions>
                            <Descriptions.Item label={intl.formatMessage({id: "labels.accessories"})}><br/>
                                <Descriptions column={3}>
                                    {kitchenFurnishing}
                                </Descriptions>
                            </Descriptions.Item>
                        </Descriptions>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.bathroom"})} key="4">
                        <Descriptions size={"default"} column={3}>
                            {numberOfBathrooms}
                            {separateWc}
                        </Descriptions>
                        <Descriptions>
                            <Descriptions.Item label={intl.formatMessage({id: "labels.accessories"})}><br/>
                                <Descriptions column={3}>
                                    {bathroomFurnishing}
                                </Descriptions>
                            </Descriptions.Item>
                        </Descriptions>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.flatmates"})} key="5">
                        <Descriptions size={"default"} column={1}>
                            {numberOfFlatmates}
                            <Typography>
                                <Paragraph>
                                    {this.props.data.aboutFlatmates}
                                </Paragraph>
                            </Typography>
                        </Descriptions>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.preferences"})} key="6">
                        <Descriptions column={3}>
                            {preferences}
                        </Descriptions>
                    </TabPane>
                    <TabPane tab={intl.formatMessage({id: "labels.neighbourhood"})} key="7">
                        <Descriptions column={3}>
                            {neighbourhood}
                        </Descriptions>
                    </TabPane>
                </Tabs>
                <div className="ant-descriptions-title">{intl.formatMessage({ id: 'labels.flat_description' })}</div>
                <Typography>
                    <Paragraph>
                        {this.props.data.description}
                    </Paragraph>
                </Typography>
            </div>
        );

        const announcementByType = new Map([['flat', flatAnnouncement], ['room', roomAnnouncement], ['place_in_room', roomAnnouncement]]);

        const IconText = ({ type, text, theme, onClick }) => (
            <span><Icon type={type} theme={theme} style={{ marginRight: 8, fontSize: '24px' }} onClick={onClick}/>{text}</span>
        );

        const statsPanel = (
            <Row style={{maxWidth: '350px'}}>
                <Col span={6}>
                    <IconText type="heart" theme={this.state.isMarkedAsFavourite ? "filled" : "outlined"} text={this.state.favouritesCounter}  onClick={this.onFavouriteClicked} key="list-vertical-star-o" />
                </Col>
                <Col span={6}>
                    <IconText type="eye" text={this.state.viewsCounter} key="list-vertical-eye-o" />
                </Col>
                <Col span={6}>
                    <IconText type="message" text={this.state.commentsCounter} key="list-vertical-message" />
                </Col>
            </Row>
        );
        return (
            <div>
                <PageHeader
                    style={{marginBottom: '12px'}}
                    title={this.props.data.title}
                    subTitle={intl.formatMessage({id: "labels.announcement_type_" + this.props.data.type})}
                    extra={sidePricePerMonth}
                    footer={<div style={{marginTop: '-10px'}}><Icon type="environment" /> {this.createSearchResultLabel(this.props.data)}</div>}
                >
                </PageHeader>

                <div>
                    <ImagesGallery2 imagesList={this.getImages()}/>
                </div>
                <br/>
                {announcementByType.get(this.props.data.type)}
                {this.props.data.id &&
                <div>
                    <Divider />
                    <Descriptions title={intl.formatMessage({id: "labels.about_offer"})} column={3}>
                        {createdBy}
                        <Descriptions.Item>{<Link to={"/profile/" + this.props.data['info.createdBy.id']}><FormattedMessage id={"labels.show_profile"}/></Link>}</Descriptions.Item>
                        <Descriptions.Item/>
                        {phoneNumber}
                        <Descriptions.Item><Button type={"link"} onClick={this.showNumber}><FormattedMessage id="labels.show"/></Button></Descriptions.Item>
                        <Descriptions.Item/>
                        {createdAt}
                        <Descriptions.Item/>
                        <Descriptions.Item/>
                        {updatedAt}
                    </Descriptions>
                    {statsPanel}
                    <Divider/>
                    <Card title={intl.formatMessage({id: "labels.comments"})}>
                        <CommentsSection announcementId={this.props.data.id}
                                         placeholder={intl.formatMessage({id: "labels.no_comment"})}
                                         currentUser={this.props.currentUser}
                                         onCommentAdded={this.onCommentAdded}
                                         onCommentRemoved={this.onCommentRemoved}
                                         displayRate={false}
                        />
                    </Card>

                </div>}
            </div>
        );
    }

}

export default injectIntl(AnnouncementView);