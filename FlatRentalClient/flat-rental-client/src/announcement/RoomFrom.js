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
import * as CONS from "../Constants";

const FormItem = Form.Item;

class RoomFrom extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const { intl } = this.props;
        return (
            <Card title={intl.formatMessage({ id: 'labels.room_name' }, { num: this.props.num })} bordered={false}>
                <FormItem label={intl.formatMessage({ id: 'labels.area' })}>
                    <Input addonAfter="m2" name="area" autoComplete="off" placeholder={intl.formatMessage({ id: 'placeholders.room_area' })}/>
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.number_of_persons' })} required={true}>
                    <InputNumber min={1} max={10}/>
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.price_per_month' })} required={true}>
                    <Input  addonAfter="PLN" placeholder={intl.formatMessage({ id: 'placeholders.price_per_month' })}/>
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.accessories' })} layout="horizontal" help="">
                    <CheckBoxGrid itemList={this.props.roomFurnishing} span={8}/>
                </FormItem>
            </Card>
        );
    }
}

export default injectIntl(RoomFrom);
