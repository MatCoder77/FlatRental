import React, { Component } from "react";
import {FormattedMessage} from "react-intl";

class MainPage extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
          <h1><FormattedMessage id="browser"/></h1>
        );
    }
}

export default MainPage;