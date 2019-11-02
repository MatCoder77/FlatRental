import React, {Component} from "react";
import {FormattedMessage, injectIntl} from "react-intl";
import { withRouter } from 'react-router-dom';
import {List, Avatar, Icon, Descriptions, Button, Row, Col, notification, Divider} from 'antd';
import {API_BASE_URL} from "../infrastructure/Constants";
import './AnnouncementList.css'
import moment from "moment";
import {getSurrogateAvatar} from "../profile/ProfileUtils";
import {changeAnnouncementState} from "../infrastructure/RestApiHandler";
import {hasRole, MODERATOR, userEquals} from "../infrastructure/PermissionsUtils";


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
            formData: this.props.announcementsList ? {announcementsList: this.props.announcementsList} : {}
        };
        this.updateFormData = this.updateFormData.bind(this);
        if (!this.state.formData.announcementsList) {
            this.updateFormData('announcementsList', this.props.location.state.announcementSearchResult.announcements);
        }
        this.navigateToAnnouncement = this.navigateToAnnouncement.bind(this);
        this.createSearchResultLabel = this.createSearchResultLabel.bind(this);
        this.voivodeshipAbbreviation = this.props.intl.formatMessage({ id: 'labels.voivodeship_abbreviation' });
        this.districtAbbreviation = this.props.intl.formatMessage({ id: 'labels.district_abbreviation' });
        this.urbanCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.urban_commune_abbreviation' });
        this.ruralCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.rural_commune_abbreviation' });
        this.mixedCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.mixed_abbreviation' });
        this.capitalCommuneAbbreviation = this.props.intl.formatMessage({ id: 'labels.capital_commune_abbreviation' });
        this.handleScroll = this.handleScroll.bind(this);
        this.handleEditClicked = this.handleEditClicked.bind(this);
        this.handleDeactivateClicked = this.handleDeactivateClicked.bind(this);
        this.handleDeleteClicked = this.handleDeleteClicked.bind(this);
        this.isCurrentUserCreatorOrModerator = this.isCurrentUserCreatorOrModerator.bind(this);
        this.isCurrentUserModerator = this.isCurrentUserModerator.bind(this);
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

    handleScroll = () => {
        const { index, selected } = this.props
        if (index === selected) {
            setTimeout(() => {
                window.scroll({top: 0, left: 0, behavior: 'smooth' })
            }, 200)
        }
    };

    handleEditClicked(e, id) {
        e.stopPropagation();
        this.props.history.push({
            pathname: '/announcement/edit/' + id,
            // search: '?query=abc',
            //state: { announcementsList: data }
        });
    }

    handleDeactivateClicked(e, item) {
        e.stopPropagation();
        if (item.info.objectState === 'ACTIVE') {
            let promise = changeAnnouncementState(item.id, 'INACTIVE');
            if (!promise) {
                return;
            }
            promise.then(response => {
                notification.success({
                    message: 'Flat Rental',
                    description: this.props.intl.formatMessage({id: "labels.announcement_closed"}),
                    duration: 5
                });
                const announcementsList = this.state.formData.announcementsList;
                item.info.objectState = "INACTIVE";
                this.setState({
                    formData: {announcementsList: announcementsList}
                })
            }).catch(error => {});
        } else if (item.info.objectState === 'INACTIVE') {
            let promise = changeAnnouncementState(item.id, 'ACTIVE');
            if (!promise) {
                return;
            }
            promise.then(response => {
                notification.success({
                    message: 'Flat Rental',
                    description: this.props.intl.formatMessage({id: "labels.announcement_opened"}),
                    duration: 5
                });
                const announcementsList = this.state.formData.announcementsList;
                item.info.objectState = "ACTIVE";
                this.setState({
                    formData: {announcementsList: announcementsList}
                })
            }).catch(error => {});
        }
    }

    handleDeleteClicked(e, item) {
        e.stopPropagation();
        let promise = changeAnnouncementState(item.id, "REMOVED");
        if (!promise) {
            return;
        }
        promise.then(response => {
            notification.success({
                message: 'Flat Rental',
                description: this.props.intl.formatMessage({id: "labels.announcement_removed"}),
                duration: 5
            });
            const announcementsList = this.state.formData.announcementsList;
            let index = announcementsList.map(announcement => announcement.id).indexOf(item.id);
            if (index !== -1) announcementsList.splice(index, 1);
            this.setState({
                formData: {announcementsList: announcementsList}
            })
        }).catch(error => {});
    }

    isCurrentUserCreatorOrModerator(user) {
        return userEquals(user, this.props.currentUser) || this.isCurrentUserModerator();
    }

    isCurrentUserModerator() {
        return hasRole(MODERATOR, this.props.currentUser);
    }

    render() {
        const {intl} = this.props;
        return (
            <List
                key={this.props.paginationCurrentPage}
                className="announcements-list"
                itemLayout="vertical"
                size="large"
                pagination={ this.props.customPaginationHandling ? {
                    onChange: page => {
                        this.handleScroll();
                        this.props.onPageChange(page);
                    },
                    pageSize: this.props.paginationPageSize,
                    total: this.props.paginationTotalSize,
                    current: this.props.paginationCurrentPage
                } : {pageSize: this.props.paginationPageSize}}
                dataSource={this.state.formData.announcementsList}
                footer={""}
                renderItem={item => (
                    <List.Item
                        style={item.info.objectState === "INACTIVE" ? {opacity: '0.58', filter: 'grayscale(11%)'} : {}}
                        onClick={() => {this.navigateToAnnouncement(item.id)}}
                        key={item.id}
                        actions={[
                            <IconText type="heart-o" text={item.statistics.favouritesCounter} key="list-vertical-heart-o" />,
                            <IconText type="eye-o" text={item.statistics.viewsCounter} key="list-vertical-eye-o" />,
                            <IconText type="message" text={item.statistics.commentsCounter} key="list-vertical-message" />,
                            <span>{this.props.intl.formatMessage({id: "labels.created_at"})}: {moment(item.info.createdAt).format('YYYY-MM-DD')}</span>
                        ]}
                        extra={
                            <Row>
                                <Col>
                                    <div>
                                        {item.announcementImages && item.announcementImages[0] &&
                                        <img
                                            width={272}
                                            alt=" "
                                            src={API_BASE_URL + "/file/download/" + item.announcementImages[0].filename}
                                        />}
                                    </div>
                                </Col>
                                <Col>
                                    <div style={{marginTop: '24px'}}>
                                        <Row gutter={5} type="flex" justify="end">
                                            <Col span={8}>
                                                {this.isCurrentUserCreatorOrModerator(item.info.createdBy) && <Button style={{width: '100%'}} onClick={(event) => {this.handleEditClicked(event, item.id)}}><FormattedMessage id={"labels.edit2"}/></Button>}
                                            </Col>
                                            <Col span={8}>
                                                {this.isCurrentUserCreatorOrModerator(item.info.createdBy) && <Button style={{width: '100%'}} onClick={(event) => {this.handleDeactivateClicked(event, item)}}>{item.info.objectState === "ACTIVE" ? intl.formatMessage({id: 'labels.close_announcement'}) : intl.formatMessage({id: 'labels.open_announcement'})}</Button>}
                                            </Col>
                                            <Col span={8}>
                                                {this.isCurrentUserModerator() && <Button style={{width: '100%'}} onClick={(event) => {this.handleDeleteClicked(event, item)}}><FormattedMessage id={"labels.delete"}/></Button>}
                                            </Col>
                                        </Row>
                                    </div>
                                </Col>
                            </Row>
                        }
                    >
                        <List.Item.Meta
                            avatar={item.info.createdBy.avatarUrl ? <Avatar src={item.info.createdBy.avatarUrl}/> : getSurrogateAvatar(item.info.createdBy.name)}
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