import React, { Component } from "react";
import { Checkbox, Row, Col } from 'antd';
import {FormattedMessage} from "react-intl";

class CheckBoxGrid extends Component {
    constructor(props) {
        super(props);
        this.updateOnChange = this.updateOnChange.bind(this);
    }

    updateOnChange(event) {
        let checkedValues = this.props.checkedValues ? this.props.checkedValues : [];
        let currentValue = event.target.value;

        if (event.target.checked) {
            checkedValues.push({id:currentValue});
        } else {
            let index = checkedValues.map(value => value.id).indexOf(currentValue);
            if (index !== -1) checkedValues.splice(index, 1);
        }
        this.props.onUpdate(this.props.name, checkedValues);
    }

    render() {
        let items = this.props.itemList;
        let checkedValues = this.props.checkedValues ? this.props.checkedValues.map(value => value.id) : [];
        return (
            <Checkbox.Group style={{ width: '100%' }} value={checkedValues}>
                <Row type="flex">
                    { items ? (items.map(item => (<Col span={this.props.span}><Checkbox onChange={this.updateOnChange} key={item.id} value={item.id}><FormattedMessage id={item.value}/></Checkbox></Col>))) : ""}
                </Row>
            </Checkbox.Group>
        );
    }

}

export default CheckBoxGrid;