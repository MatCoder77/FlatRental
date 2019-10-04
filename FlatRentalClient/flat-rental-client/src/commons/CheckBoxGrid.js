import React, { Component } from "react";
import { Checkbox, Row, Col } from 'antd';
import {FormattedMessage} from "react-intl";

class CheckBoxGrid extends Component {
    constructor(props) {
        super(props);
        this.updateOnChange = this.updateOnChange.bind(this);
    }

    updateOnChange(event, item) {
        console.log(item);
        let checkedItems = this.props.checkedValues ? this.props.checkedValues : [];
        let currentItemId = event.target.value;

        if (event.target.checked) {
            checkedItems.push({id:currentItemId, value: item.value});
        } else {
            let index = checkedItems.map(value => value.id).indexOf(currentItemId);
            if (index !== -1) checkedItems.splice(index, 1);
        }
        this.props.onUpdate(this.props.name, checkedItems);
    }

    render() {
        let items = this.props.itemList;
        let checkedValues = this.props.checkedValues ? this.props.checkedValues.map(value => value.id) : [];
        return (
            <Checkbox.Group style={{ width: '100%' }} value={checkedValues}>
                <Row type="flex">
                    { items ? (items.map(item => (<Col span={this.props.span}><Checkbox onChange={event => this.updateOnChange(event, item)} key={item.id} value={item.id}><FormattedMessage id={item.value}/></Checkbox></Col>))) : ""}
                </Row>
            </Checkbox.Group>
        );
    }

}

export default CheckBoxGrid;