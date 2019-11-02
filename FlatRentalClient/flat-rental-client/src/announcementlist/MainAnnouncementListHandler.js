import React, {Component} from "react";
import {
    getEncodedQueryParam,
    searchAnnouncementsByCriteria
} from "../infrastructure/RestApiHandler";
import { withRouter } from 'react-router-dom';
import LoadingIcon from "../commons/LoadingIcon";
import AnnouncementList from "./AnnouncementList";
import queryString from 'query-string'
import {Col, Divider, Row, Select, Icon, Tooltip} from "antd";
import {FormattedMessage, injectIntl} from "react-intl";
import * as CONS from "../infrastructure/Constants";
import SearchBox from "../searchbox/SearchBox";

const { Option } = Select;

class MainAnnouncementListHandler extends Component{
    constructor(props) {
        super(props);
        let criteria = queryString.parse(this.props.location.search).searchCriteria;
        let pageSize = queryString.parse(this.props.location.search).size;
        let pageNumber = queryString.parse(this.props.location.search).page;
        let sorting = this.getSortingFromQueryParams(queryString.parse(this.props.location.search).sort);
        this.state = {
            announcements: {},
            searchCriteria: {},
            searchCriteriaParam: criteria ? criteria : "",
            pageSize:  pageSize ? pageSize : 15,
            pageNumber: pageNumber ? pageNumber : 0,
            totalSize: undefined,
            sortBy: sorting.sortBy,
            sortingOrder: sorting.sortingOrder,
            isLoading: true
        };
        this.loadAnnouncements = this.loadAnnouncements.bind(this);
        this.performSearchByCriteria = this.performSearchByCriteria.bind(this);
        this.onPageChange = this.onPageChange.bind(this);
        this.changeSortingOrder = this.changeSortingOrder.bind(this);
        this.onSortBySelected = this.onSortBySelected.bind(this);
        this.getSortingFromQueryParams = this.getSortingFromQueryParams.bind(this);
        this.getSortByAndOrder = this.getSortByAndOrder.bind(this);
    }

    componentDidMount() {
        this.loadAnnouncements();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        let criteria = queryString.parse(this.props.location.search).searchCriteria;
        if (criteria && criteria != prevState.searchCriteriaParam) {
            let criteria = queryString.parse(this.props.location.search).searchCriteria;
            this.setState({
                searchCriteriaParam: criteria ? criteria : "",
                searchCriteria: criteria
            }, () => {this.loadAnnouncements()})
        }
    }

    loadAnnouncements(noLoading) {
        let promise = searchAnnouncementsByCriteria(this.state.searchCriteriaParam, this.state.pageNumber, this.state.pageSize, this.state.sortBy + "," + this.state.sortingOrder);

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

    performSearchByCriteria(event) {
        let criteria = this.state.searchCriteria ? getEncodedQueryParam(this.state.searchCriteria) : this.state.searchCriteriaParam;
        this.loadAnnouncements(searchAnnouncementsByCriteria, 'announcementSearchResult', criteria);
    }

    onPageChange(number) {
        this.setState({
            pageNumber: number - 1
        },() => {
            this.loadAnnouncements(true);
        });
        let queryParams = queryString.parse(this.props.history.location.search);
        this.props.history.push({
            search:
                "?page=" + (number - 1) +
                "&size=" + queryParams["size"] +
                "&sort=" + queryParams["sort"] +
                "&searchCriteria=" + queryParams["searchCriteria"] +
                "&q=" + queryParams["q"]
        });
    }

    changeSortingOrder() {
        let currentOrder = this.state.sortingOrder;
        let newSortingOrder = currentOrder === 'asc' ? 'desc' : 'asc';
        this.setState({
            sortingOrder: newSortingOrder
        }, () => {this.loadAnnouncements(true)})
        let queryParams = queryString.parse(this.props.history.location.search);
        this.props.history.push({
            search:
                "?page=" + queryParams["page"] +
                "&size=" + queryParams["size"] +
                "&sort=" + this.state.sortBy + "," + newSortingOrder +
                "&searchCriteria=" + queryParams["searchCriteria"] +
                "&q=" + queryParams["q"]
        });
    }

    onSortBySelected(value) {
        this.setState({
            sortBy: value
        }, () => {this.loadAnnouncements(true);});
        let queryParams = queryString.parse(this.props.history.location.search);
        this.props.history.push({
            search:
                "?page=" + queryParams["page"] +
                "&size=" + queryParams["size"] +
                "&sort=" + value + "," + this.state.sortingOrder +
                "&searchCriteria=" + queryParams["searchCriteria"] +
                "&q=" + queryParams["q"]
        });
    }

    getSortingFromQueryParams(sortingParams) {
        if (!sortingParams) {
            return {sortBy: 'createdAt', sortingOrder: 'desc'};
        }
        if (Array.isArray(sortingParams)) {
            let params = sortingParams[0];
            let sortByAndOrder = params.split(",");
            return this.getSortByAndOrder(sortByAndOrder);
        } else {
            let sortByAndOrder = sortingParams.split(",");
            return this.getSortByAndOrder(sortByAndOrder);
        }
    }

    getSortByAndOrder(sortByAndOrder) {
        if (sortByAndOrder[0] && sortByAndOrder[0] !== "" && sortByAndOrder[1] && sortByAndOrder[1] !== "") {
            return {sortBy: sortByAndOrder[0], sortingOrder: sortByAndOrder[1]};
        } else if (sortByAndOrder[0] && sortByAndOrder[0] !== "") {
            return {sortBy: sortByAndOrder[0], sortingOrder: 'asc'};
        } else {
            return {sortBy: 'createdAt', sortingOrder: 'desc'};
        }
    }

    render() {
        if (this.state.isLoading) {
            return (<LoadingIcon/>);
        }

        const sortCombobox = (
            <div>
                <Row gutter={5} type="flex" justify="end">
                    <Col span={20}>
                <Select style={{width: '100%'}} size={"default"} value={this.state.sortBy} onSelect={this.onSortBySelected}>
                    <Option value="createdAt">{this.props.intl.formatMessage({id: 'labels.sort_by_createdAt'})}</Option>
                    <Option value="pricePerMonth">{this.props.intl.formatMessage({id: 'labels.sort_by_pricePerMonth'})}</Option>
                    <Option value="quality">{this.props.intl.formatMessage({id: 'labels.sort_by_announcementRate'})}</Option>
                </Select>
                    </Col>
                    <Col span={3}>
                        {this.state.sortingOrder === 'asc' ? <Tooltip title={this.props.intl.formatMessage({id: 'labels.sort_order_asc_' + this.state.sortBy})}><Icon type="sort-ascending" style={{ fontSize: '32px' }} onClick={this.changeSortingOrder}/></Tooltip> : <Tooltip title={this.props.intl.formatMessage({id: 'labels.sort_order_desc_' + this.state.sortBy})}><Icon type="sort-descending" style={{ fontSize: '32px' }} onClick={this.changeSortingOrder}/></Tooltip>}
                    </Col>
                </Row>
            </div>
        );

        return (
            <div>
                <SearchBox searchCriteria={this.state.searchCriteria} sortBy={this.state.sortBy} sortingOrder={this.state.sortingOrder}/>
                <div style={{marginBottom: '-15px'}}><Row type="flex" justify="space-between"><Col span={16}><FormattedMessage id={"labels.found_announcements"} values={{number : this.state.totalSize}}/></Col><Col span={7}>{sortCombobox}</Col></Row></div>
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

export default withRouter(injectIntl(MainAnnouncementListHandler));