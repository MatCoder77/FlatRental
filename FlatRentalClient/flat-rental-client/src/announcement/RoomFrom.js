import React, { Component } from 'react';
import {
    Form,
    Input,
    InputNumber,
    Card,
} from 'antd';
import {FormattedMessage, injectIntl} from 'react-intl';
import CheckBoxGrid from "../commons/CheckBoxGrid";

const FormItem = Form.Item;

class RoomForm extends Component {
    constructor(props) {
        super(props);
        this.updateOnChange = this.updateOnChange.bind(this);
        this.updateOnChangeWithName = this.updateOnChangeWithName.bind(this);
        this.updateOnSelect = this.updateOnSelect.bind(this);
    }

    updateOnChange(event) {
        this.props.onUpdate(event.target.name, event.target.value, this.props.num);
    }

    updateOnChangeWithName = name => value => {
        this.props.onUpdate(name, value, this.props.num);
    };

    updateOnSelect(name, value) {
        this.props.onUpdate(name, value, this.props.num);
    }

    render() {
        const { intl } = this.props;
        const roomNumber = this.props.num;
        return (
            <Card title={intl.formatMessage({ id: 'labels.room_name' }, { num: this.props.num })} bordered={false}>
                <FormItem label={intl.formatMessage({ id: 'labels.area' })}>
                    <Input
                        addonAfter="m2"
                        name="area"
                        onChange={this.updateOnChange}
                        value={this.props.roomList[roomNumber - 1]["area"]}
                        autoComplete="off"
                        placeholder={intl.formatMessage({ id: 'placeholders.room_area' })}/>
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.number_of_persons' })} required={true}>
                    <InputNumber
                        min={1}
                        max={10}
                        onChange={this.updateOnChangeWithName('numberOfPersons')}
                        value={this.props.roomList[roomNumber - 1]["numberOfPersons"]}
                    />
                </FormItem>
                <FormItem label={intl.formatMessage({ id: 'labels.accessories' })} layout="horizontal" help="">
                    <CheckBoxGrid name="furnishing"
                                  itemList={this.props.appData.roomFurnishing}
                                  onUpdate={this.updateOnSelect}
                                  checkedValues={this.props.roomList[roomNumber - 1]["furnishing"]}
                                  span={8}/>
                </FormItem>
            </Card>
        );
    }
}

export default injectIntl(RoomForm);
