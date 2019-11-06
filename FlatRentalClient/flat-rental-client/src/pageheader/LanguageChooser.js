import React from "react";
import { IntlContext } from '../infrastructure/LanguageHandler'
import {Dropdown, Icon, Menu} from "antd";
import {FormattedMessage} from "react-intl";
import './PageHeader.css'

const LanguageChooser = () => {
    const onLanguageChange = React.useContext(IntlContext);

    function handleMenuClick({key}) {
        onLanguageChange(key);
    }

    const languageMenu = (
        <Menu onClick={handleMenuClick} className="profile-dropdown-menu">
            <Menu.Item key="user-info" className="dropdown-item" disabled>
                <div className="username-info" style={{fontSize: '14px', color: 'rgba(0,0,0,0.65)'}}>
                    <FormattedMessage id="labels.choose_language"/>
                </div>
            </Menu.Item>
            <Menu.Divider />
            <Menu.Item key="pl" className="dropdown-item">
                <FormattedMessage id="labels.language_pl"/>
            </Menu.Item>
            <Menu.Item key="en" className="dropdown-item">
                <FormattedMessage id="labels.language_en"/>
            </Menu.Item>
        </Menu>
    );
    return (
        <Dropdown
            overlay={languageMenu}
            trigger={['click']}
            getPopupContainer = { () => document.getElementsByClassName('language-menu')[0]}>
            <a className="ant-dropdown-link">
                <Icon type="global" className="nav-icon" style={{fontSize: '20px'}}/>
            </a>
        </Dropdown>
    );
};

export default LanguageChooser;