import { Select } from 'antd';
import React, {Component} from "react";
import {FormattedMessage} from "react-intl";
import './MultiSelect.css'

const { Option } = Select;

class MultiSelect extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedValue: null
        }
    }

    updateOnSelect = name => (value) => {
        let selectedItems = this.props.selectedItems ? this.props.selectedItems : [];
        selectedItems.push(value);
        this.props.onUpdate(name, selectedItems);
    };

    updateOnDeselect = name => (value) => {
        let selectedItems = this.props.selectedItems ? this.props.selectedItems : [];
        let index = selectedItems.indexOf(value);
        if (index !== -1) selectedItems.splice(index, 1);
        this.props.onUpdate(name, selectedItems);
    };


    render() {
        let items = this.props.itemList;
        return (
            <Select
                //showSearch
                mode="multiple"
                style={{ width: '100%', fontSize: '0.85em'}}
                placeholder={this.props.placeholder}
                onSelect={this.updateOnSelect(this.props.name)}
                onDeselect={this.updateOnDeselect(this.props.name)}
                maxTagCount={1}
                // maxTagTextLength="10"
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

export default MultiSelect;
