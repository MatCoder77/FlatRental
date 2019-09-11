import React, { Component } from 'react';
import {Steps, Form, Input, Button, notification, InputNumber, Row, Col, DatePicker, Checkbox } from 'antd';
import * as CONS from "../Constants";
import {Link} from "react-router-dom";
import './Step.css';
import ComboBox from "../commons/ComboBox";
import Text from "antd/lib/typography/Text";
import moment from "moment";
import {
    getApartmentStateTypes,
    getBuildingMaterialTypes,
    getBuildingTypes,
    getHeatingTypes, getParkingTypes,
    getWindowTypes
} from "../infrastructure/RestApiHandler";

const FormItem = Form.Item;

const today = moment(new Date())

class ThirdStepContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            buildingTypes: [],
            buildingMaterialTypes: [],
            heatingTypes: [],
            parkingTypes: [],
            windowTypes: [],
            apartmentStateTypes: []
        }
        this.loadTypes = this.loadTypes.bind(this);
        this.loadBuildingTypes = this.loadBuildingTypes.bind(this);
        this.loadBuildingMaterialTypes = this.loadBuildingMaterialTypes.bind(this);
        this.loadHeatingTypes = this.loadHeatingTypes.bind(this);
        this.loadWindowTypes = this.loadWindowTypes.bind(this);
        this.loadApartmentStateTypes = this.loadApartmentStateTypes.bind(this);
    }

    loadTypes(supplierFunction, filedName) {
        let promise = supplierFunction();

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

    loadBuildingTypes() {
        this.loadTypes(getBuildingTypes, 'buildingTypes');
    }

    loadBuildingMaterialTypes() {
        this.loadTypes(getBuildingMaterialTypes, 'buildingMaterialTypes');
    }

    loadHeatingTypes() {
        this.loadTypes(getHeatingTypes, 'heatingTypes');
    }

    loadWindowTypes() {
        this.loadTypes(getWindowTypes, 'windowTypes')
    }

    loadParkingTypes() {
        this.loadTypes(getParkingTypes, 'parkingTypes')
    }

    loadApartmentStateTypes() {
        this.loadTypes(getApartmentStateTypes, 'apartmentStateTypes')
    }

    componentDidMount() {
        this.loadBuildingTypes();
        this.loadBuildingMaterialTypes();
        this.loadHeatingTypes();
        this.loadWindowTypes();
        this.loadParkingTypes();
        this.loadApartmentStateTypes();
    }

    render() {
        return (
            <div className="step-container">
                <h1 className="page-title">Detail Information</h1>
                <div className="step-container-content">
                    <Form className="step-form" layout="horizontal">
                        <FormItem label="Built year">
                            <InputNumber min={1800} max={today.year()}/>
                        </FormItem>
                        <FormItem
                            label="Building type"
                            help="">
                            <ComboBox itemList={this.state.buildingTypes} placeholder="Select building type"/>
                        </FormItem>
                        <FormItem
                            label="Building material"
                            help="">
                            <ComboBox itemList={this.state.buildingMaterialTypes} placeholder="Select building material"/>
                        </FormItem>
                        <FormItem
                            label="Heating type"
                            help="">
                            <ComboBox itemList={this.state.heatingTypes} placeholder="Select building material"/>
                        </FormItem>
                        <FormItem
                            label="Windows type"
                            help="">
                            <ComboBox itemList={this.state.windowTypes} placeholder="Select building material"/>
                        </FormItem>
                        <FormItem
                            label="Parking type"
                            help="">
                            <ComboBox itemList={this.state.parkingTypes} placeholder="Select building material"/>
                        </FormItem>
                        <FormItem
                            label="Apartment State"
                            help="">
                            <ComboBox itemList={this.state.apartmentStateTypes} placeholder="Select building material"/>
                        </FormItem>
                        <FormItem label="Amenities" layout="horizontal" hasFeedback required={true} help="">
                            <Checkbox.Group style={{ width: '100%' }} >
                                <Row>
                                    <Col span={8}>
                                        <Checkbox value="Balkon">Balkon</Checkbox>
                                    </Col>
                                    <Col span={8}>
                                        <Checkbox value="Klimatyzacja">Klimatyzacja</Checkbox>
                                    </Col>
                                    <Col span={8}>
                                        <Checkbox value="Ogrzewane podłogi">Ogrzewane podłogi</Checkbox>
                                    </Col>
                                    <Col span={8}>
                                        <Checkbox value="Taras">Taras</Checkbox>
                                    </Col>
                                    <Col span={8}>
                                        <Checkbox value="Blblblr">Blblblr</Checkbox>
                                    </Col>
                                </Row>
                            </Checkbox.Group>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }

}

export default ThirdStepContainer;