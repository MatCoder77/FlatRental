import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import { withRouter } from 'react-router-dom';
import {List, Avatar, Icon, Descriptions} from 'antd';
import {API_BASE_URL} from "../infrastructure/Constants";
import './AnnouncementList.css'

const IconText = ({ type, text }) => (
    <span>
    <Icon type={type} style={{ marginRight: 8 }} />
        {text}
  </span>
);


class AnnouncementList extends Component{
    constructor(props) {
        super(props);
        this.state = {
            formData: {}
        };
        this.updateFormData = this.updateFormData.bind(this);
        this.updateFormData('announcementsList', this.props.location.state.announcementsList);
        this.navigateToAnnouncement = this.navigateToAnnouncement.bind(this);
        this.createSearchResultLabel = this.createSearchResultLabel.bind(this);
        this.voivodeshipAbbreviation = this.props.intl.formatMessage({ id: 'labels.voivodeship_abbreviation' });
        this.districtAbbreviation = this.props.intl.formatMessage({ id: 'labels.district_abbreviation' });
        this.urbanCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.urban_commune_abbreviation' });
        this.ruralCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.rural_commune_abbreviation' });
        this.mixedCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.mixed_abbreviation' });
        this.capitalCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.capital_commune_abbreviation' });
    }

    updateFormData(fieldName, fieldValue) {
        const {formData} = this.state;
        formData[fieldName] = fieldValue;
        this.setState({formData});
        console.log(this.state.formData);
    }

    navigateToAnnouncement(id) {
        this.props.history.push({
            pathname: '/announcement/view/' + id,
            // search: '?query=abc',
            //state: { announcementsList: data }
        });
    }

    createSearchResultLabel(item) {
        let voivodeship = item.address.voivodeship ? this.voivodeshipAbbreviation + " " + item.address.voivodeship.name : undefined;
        let district = item.address.district ? (item.address.district.type != 'DISTRICT_CITY' && item.address.district.type != 'DISTRICT_CAPITAL') ? this.districtAbbreviation + " " + item.address.district.name : undefined : undefined;
        //let hasCommuneSameNameAsDistrict = searchResult.commune && searchResult.district && (searchResult.commune.name != searchResult.district.name);
        let commune = (item.address.commune) ? (item.address.district.type != 'DISTRICT_CITY' && item.address.district.type != 'DISTRICT_CAPITAL') ? this.getCommuneAbbreviation(item.address.commune) + " " + item.address.commune.name : undefined : undefined;
        let locality = (item.address.locality ? item.address.locality.name : undefined);
        let localityDistrict = (item.address.localityDistrict ? item.address.localityDistrict.name : undefined);
        let localityPart = (item.address.localityPart ? item.address.localityPart.name : undefined);
        let street = this.getStreetLabel(item.address.street);

        return [voivodeship, district, commune, locality, localityDistrict, localityPart, street].filter(Boolean).join(", ");
    }

    getStreetLabel(street) {
        if (street) {
            return this.props.intl.formatMessage({id: street.type}) + " " + (street.leadingName ? street.leadingName + " " : "") + street.mainName;
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

    getFlatDescription(item) {
        return (<Descriptions style={{marginTop: '10px'}} column={3}>
            <Descriptions.Item label={this.props.intl.formatMessage({id: 'labels.price_per_month'})}>{item.pricePerMonth} PLN</Descriptions.Item>
            <Descriptions.Item label={this.props.intl.formatMessage({id: 'labels.area'})}>{item.totalArea} <span>m<sup>2</sup></span></Descriptions.Item>
            <Descriptions.Item label={this.props.intl.formatMessage({id: 'labels.number_of_rooms'})}>{item.numberOfRooms}</Descriptions.Item></Descriptions>);
    };

    getRoomDescription(item) {
        return (<Descriptions style={{marginTop: '10px'}} column={3}>
            <Descriptions.Item label={this.props.intl.formatMessage({id: 'labels.price_per_month'})}>{item.pricePerMonth} PLN</Descriptions.Item>
            <Descriptions.Item label={this.props.intl.formatMessage({id: 'labels.area'})}>{item.rooms[0].area} <span>m<sup>2</sup></span></Descriptions.Item>
            <Descriptions.Item label={this.props.intl.formatMessage({id: 'labels.number_of_persons'})}>{item.rooms[0].numberOfPersons}</Descriptions.Item></Descriptions>);
    }

    getPlaceInRoomDescription(item) {
        return (<Descriptions style={{marginTop: '10px'}} column={3}>
            <Descriptions.Item label={this.props.intl.formatMessage({id: 'labels.price_per_month'})}>{item.pricePerMonth} PLN</Descriptions.Item>
            <Descriptions.Item label={this.props.intl.formatMessage({id: 'labels.area'})}>{item.rooms[0].area} <span>m<sup>2</sup></span></Descriptions.Item>
            <Descriptions.Item label={this.props.intl.formatMessage({id: 'labels.number_of_persons'})}>{item.rooms[0].numberOfPersons}</Descriptions.Item></Descriptions>);
    }

    getDescription(item) {
        if (item.type.toLowerCase() == 'FLAT'.toLowerCase()) {
            return this.getFlatDescription(item);
        }
        if (item.type.toLowerCase()  == 'ROOM'.toLowerCase() ) {
            return this.getRoomDescription(item);
        }
        if (item.type.toLowerCase()  == 'PLACE_IN_ROOM'.toLowerCase() ) {
            return this.getPlaceInRoomDescription(item);
        }
        return "";
    }

    render() {
        console.log("INSIDE");
        console.log(this.props);
        const {intl} = this.props;
        return (
            <List
                itemLayout="vertical"
                size="large"
                pagination={{
                    onChange: page => {
                        console.log(page);
                    },
                    pageSize: 3,
                }}
                dataSource={this.state.formData.announcementsList}
                footer={
                    <div>
                        <b>ant design</b> footer part
                    </div>
                }
                renderItem={item => (
                    <List.Item
                        onClick={() => {this.navigateToAnnouncement(item.id)}}
                        key={item.id}
                        actions={[
                            <IconText type="star-o" text="156" key="list-vertical-star-o" />,
                            <IconText type="like-o" text="156" key="list-vertical-like-o" />,
                            <IconText type="message" text="2" key="list-vertical-message" />,
                        ]}
                        extra={
                            <div>
                                {item.announcementImages && item.announcementImages[0] &&
                            <img
                                width={272}
                                alt=" "
                                src={API_BASE_URL + "/file/download/" + item.announcementImages[0].filename}
                            />}
                            </div>
                        }
                    >
                        <List.Item.Meta
                            avatar={<Avatar src={item.avatar} />}
                            title={<div>{item.title}<span className="ant-descriptions-item-content" style={{fontSize: '0.8em'}}>   {intl.formatMessage({id: 'labels.announcement_type_' + item.type.toLowerCase()})}</span></div> }
                            description={
                                <div><span>{this.createSearchResultLabel(item)}</span>
                                    {this.getDescription(item)}
                                </div>
                            }
                        />
                    </List.Item>
                )}
            />
        );
    }
}

export default injectIntl(withRouter(AnnouncementList));