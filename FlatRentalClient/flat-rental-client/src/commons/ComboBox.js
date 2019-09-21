import { Select } from 'antd';
import React, {Component} from "react";
import {FormattedMessage} from "react-intl";

const { Option } = Select;

class ComboBox extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedValue: null
        }
    }

    updateOnSelect = name => (value, option) => {
        this.props.onUpdate(name, value);
    }


    render() {
        let items = this.props.itemList;
        return (
            <Select
                //showSearch
                placeholder={this.props.placeholder}
                onSelect={this.updateOnSelect(this.props.name)}
                value={this.props.value}
                //optionFilterProp="children"
                // onChange={onChange}
                // onFocus={onFocus}
                // onBlur={onBlur}
                // onSearch={onSearch}8888888
                // filterOption={(input, option) =>
                //     option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                // }
            >
                { items ? items.map(item => (<Option key={item.id} value={item.id}><FormattedMessage id={item.value}/></Option>)) : ""}
            </Select>
        );
    }
}

export default ComboBox;
