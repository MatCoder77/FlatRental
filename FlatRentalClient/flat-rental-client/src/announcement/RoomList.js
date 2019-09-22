import React, { Component } from 'react';
import {
    Steps,
    Form,
    Input,
    Button,
    notification,
    InputNumber,
    Row,
    Col,
    DatePicker,
    Checkbox,
    Card,
    Switch,
} from 'antd';
import {FormattedMessage, injectIntl} from 'react-intl';
import CheckBoxGrid from "../commons/CheckBoxGrid";
import * as CONS from "../infrastructure/Constants";
import RoomFrom from "./RoomFrom";

const FormItem = Form.Item;

class RoomList extends Component {
    constructor(props) {
        super(props);
        this.updateOnChange = this.updateOnChange.bind(this);
        let numberOfRooms = this.props.formData.numberOfRooms;
        let roomsList = this.props.formData[this.props.name];
        if(!roomsList || roomsList.length != numberOfRooms) {
            let roomNumbers = Array.from({length: numberOfRooms}, (v, k) => k+1);
            roomsList = roomNumbers.map(number => new Object({"roomNumber": number}))
            this.props.onUpdate(this.props.name, roomsList);
        }
    }

    updateOnChange(fieldName, fieldValue, roomNumber) {
        const roomsData = this.props.formData[this.props.name];
        roomsData[roomNumber - 1][fieldName] = fieldValue;
        this.setState({ roomsData });
        this.props.onUpdate(this.props.name, roomsData)
    }

    render() {
        let listName = this.props.name;
        let roomList = this.props.formData[listName];
        return (
            <div className="room-list">
                {roomList.map(room => (<RoomFrom onUpdate={this.updateOnChange} formData={this.props.formData} roomList={this.props.formData[listName]} num={room["roomNumber"]} appData={this.props.appData} />))}
            </div>
        );
    }

}

export default RoomList;