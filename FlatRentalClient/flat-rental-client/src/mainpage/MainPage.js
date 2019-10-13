import React, { Component } from "react";
import {FormattedMessage} from "react-intl";
import SearchBox from "../searchbox/SearchBox";

class MainPage extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
          <SearchBox/>
        );
    }
}

export default MainPage;