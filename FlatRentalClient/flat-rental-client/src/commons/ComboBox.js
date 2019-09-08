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



    render() {
        return (
            <Select
                //showSearch
                placeholder={this.props.placeholder}
                //optionFilterProp="children"
                // onChange={onChange}
                // onFocus={onFocus}
                // onBlur={onBlur}
                // onSearch={onSearch}
                // filterOption={(input, option) =>
                //     option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                // }
            >
                {this.props.itemList.map(item => (
                    <Option key={item.id} value={item.value}><FormattedMessage id={item.value}/></Option>
                ))}
            </Select>
        );
    }
}

export default ComboBox;
