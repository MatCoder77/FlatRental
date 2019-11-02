import React, {Component} from "react";
import {
    getEncodedQueryParam,
    searchAnnouncementsByCriteria
} from "../infrastructure/RestApiHandler";
import { withRouter } from 'react-router-dom';
import LoadingIcon from "../commons/LoadingIcon";
import {Col, Divider, Row, Select, Icon, Tooltip} from "antd";
import {FormattedMessage, injectIntl} from "react-intl";
import AnnouncementList from "../announcementlist/AnnouncementList";

const { Option } = Select;

class UserAnnouncementListHandler extends Component{
    constructor(props) {
        super(props);
        this.state = {
            announcements: {},
            searchCriteria: {allowedManagedObjectStates: ['ACTIVE'], author: this.props.currentUser.id},
            pageSize: 15,
            pageNumber: 0,
            sortBy: 'createdAt',
            sortingOrder: 'desc',
            totalSize: undefined,
            isLoading: true
        };
        this.loadAnnouncements = this.loadAnnouncements.bind(this);
        this.onPageChange = this.onPageChange.bind(this);
        this.onAnnouncementStateSelected = this.onAnnouncementStateSelected.bind(this);
        this.fromSelectValueToObjectState = this.fromSelectValueToObjectState.bind(this);
        this.fromObjectStateToSelectValue = this.fromObjectStateToSelectValue.bind(this);
    }

    componentDidMount() {
        this.loadAnnouncements();
    }
    loadAnnouncements(noLoading) {
        console.log("ŁADUJE");
        let promise = searchAnnouncementsByCriteria(getEncodedQueryParam(this.state.searchCriteria), this.state.pageNumber, this.state.pageSize, this.state.sortBy + "," + this.state.sortingOrder);

        if (!promise) {
            return;
        }

        if(!noLoading) {
            this.setState({
                isLoading: true
            });
        }

        promise.then(response => {
            let announcementSearchResult = response;
            console.log(response);
            this.setState({
                announcements: announcementSearchResult.announcements,
                searchCriteria: announcementSearchResult.criteria,
                totalSize: announcementSearchResult.totalSize,
                pageSize: announcementSearchResult.pageSize,
                isLoading: false
            });
        }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

    onPageChange(number) {
        this.setState({
            pageNumber: number - 1
        },() => {
            this.loadAnnouncements(true);
        });
    }

    onAnnouncementStateSelected(value) {
        const {searchCriteria} = this.state;
        searchCriteria['allowedManagedObjectStates'] = this.fromSelectValueToObjectState(value);
        this.setState({
            searchCriteria
        }, () => {this.loadAnnouncements()});
    }

    fromSelectValueToObjectState(value) {
        if (value === "ACTIVE") {
            return ["ACTIVE"];
        } else if (value == "INACTIVE") {
            return ["INACTIVE"];
        } else if (value === "ALL") {
            return ["ACTIVE", "INACTIVE"];
        } else {
            return undefined;
        }
    }

    fromObjectStateToSelectValue(value) {
        if (value.includes("ACTIVE") && value.includes("INACTIVE")) {
            return "ALL";
        } else if (value.includes("ACTIVE")) {
            return "ACTIVE";
        } else if (value.includes("INACTIVE")) {
            return "INACTIVE";
        } else {
            return undefined;
        }
    }

    render() {
        if (this.state.isLoading) {
            return (<LoadingIcon/>);
        }

        const announcementStateCombobox = (
            <div>
                <Row gutter={5} type="flex" justify="end">
                    <Col span={20}>
                        <Select style={{width: '100%'}} size={"default"} value={this.fromObjectStateToSelectValue(this.state.searchCriteria.allowedManagedObjectStates)} onSelect={this.onAnnouncementStateSelected}>
                            <Option value="ACTIVE">{this.props.intl.formatMessage({id: 'labels.show_active'})}</Option>
                            <Option value="INACTIVE">{this.props.intl.formatMessage({id: 'labels.show_inactive'})}</Option>
                            <Option value={"ALL"}>{this.props.intl.formatMessage({id: 'labels.show_all'})}</Option>
                        </Select>
                    </Col>
                </Row>
            </div>
        );

        return (
            <div>
                <div style={{marginBottom: '-15px'}}><Row type="flex" justify="space-between"><Col span={16}><FormattedMessage id={"labels.found_announcements"} values={{number : this.state.totalSize}}/></Col><Col span={7}>{announcementStateCombobox}</Col></Row></div>
                <Divider/>
                <AnnouncementList
                    key={Math.random()}
                    currentUser={this.props.currentUser}
                    announcementsList={this.state.announcements}
                    customPaginationHandling={true}
                    paginationTotalSize={this.state.totalSize}
                    paginationPageSize={this.state.pageSize}
                    paginationCurrentPage={parseInt(this.state.pageNumber) + 1}
                    onPageChange={this.onPageChange}
                />
            </div>
        );
    }
}

export default withRouter(injectIntl(UserAnnouncementListHandler));