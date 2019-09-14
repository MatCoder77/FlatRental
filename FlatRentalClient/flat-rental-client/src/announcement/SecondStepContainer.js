import React, { Component } from 'react';
import {Steps, Form, Input, Button, notification, InputNumber, Row, Col, DatePicker, Checkbox } from 'antd';
import * as CONS from "../Constants";
import {Link} from "react-router-dom";
import './Step.css';
import ComboBox from "../commons/ComboBox";
import Text from "antd/lib/typography/Text";
import moment from "moment";
import {
    getApartmentAmenitiesTypes,
    getApartmentStateTypes,
    getBuildingMaterialTypes,
    getBuildingTypes,
    getHeatingTypes, getParkingTypes, getVoivodeships,
    getWindowTypes
} from "../infrastructure/RestApiHandler";
import CheckBoxGrid from "../commons/CheckBoxGrid";
import LocalitySelector from "./LocalitySelector";
import {FormattedMessage} from "react-intl";

const FormItem = Form.Item;

const today = moment(new Date())

class SecondStepContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            voivodeships: [],
            districts: [],
            communes: [],
            localities: [],
            localityParts: [],
            streets: [],
        }
    }

    render() {
        return (
            <div className="step-container">
                <h1 className="page-title"><FormattedMessage id="labels.localization"/></h1>
                <div className="step-container-content">
                        {/*<FormItem*/}
                        {/*    help="">*/}
                            <LocalitySelector {...this.props}/>
                        {/*</FormItem>*/}

                </div>
            </div>
        );
    }

}

export default SecondStepContainer;