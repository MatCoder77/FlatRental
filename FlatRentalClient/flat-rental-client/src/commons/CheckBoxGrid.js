import React, { Component } from "react";
import { Checkbox, Row, Col } from 'antd';
import {FormattedMessage} from "react-intl";

class CheckBoxGrid extends Component {
    constructor(props) {
        super(props);

    }

    render() {
        let items = this.props.itemList;
        return (
            <Checkbox.Group style={{ width: '100%' }}>
                <Row type="flex">
                    {items.map(item => (
                        <Col span={this.props.span}> <Checkbox key={item.id} value={item.value}><FormattedMessage id={item.value}/></Checkbox></Col>
                    ))}
                </Row>
            </Checkbox.Group>
        );
    }

}

export default CheckBoxGrid;