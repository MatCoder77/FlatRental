import React, { Component } from 'react';
import {
    Link,
    withRouter
} from 'react-router-dom';
import './PageHeader.css';
import {Layout, Menu, Dropdown, Icon, Button} from 'antd';
import {FormattedMessage, injectIntl} from 'react-intl';
import LanguageChooser from "./LanguageChooser";

const Header = Layout.Header;

class AppHeader extends Component {
    constructor(props) {
        super(props);
        this.handleMenuClick = this.handleMenuClick.bind(this);
    }

    handleMenuClick({ key }) {
        if(key === "logout") {
            this.props.onLogout();
        }
    }

    render() {
        let menuItems;
        if(this.props.currentUser) {
            menuItems = [
                <Menu.Item key="/">
                    <Link to="/">
                        <Icon type="home" className="nav-icon"/>
                    </Link>
                </Menu.Item>,
                <Menu.Item key="create-announcement">
                        <CreateAnnouncementDropDownMenu
                            currentUser={this.props.currentUser}/>
                </Menu.Item>,
                <Menu.Item key="profile" className="profile-menu">
                    <ProfileDropdownMenu
                        currentUser={this.props.currentUser}
                        handleMenuClick={this.handleMenuClick}/>
                </Menu.Item>,
                <Menu.Item key="language" className="language-menu">
                    <LanguageChooser/>
                </Menu.Item>
            ];
        } else {
            menuItems = [
                <Menu.Item key="/login">
                    <Link to="/login" style={{fontSize: '17px'}}><FormattedMessage id="labels.login_button"/></Link>
                </Menu.Item>,
                <Menu.Item key="/signup">
                    <Link to="/signup" style={{fontSize: '17px'}}><FormattedMessage id="buttons.signup"/></Link>
                </Menu.Item>,
                <Menu.Item key="language" className="language-menu">
                    <LanguageChooser/>
                </Menu.Item>
            ];
        }

        return (
            <Header className="app-header">
                <div className="container">
                    <div className="app-title" >
                        <Link to="/">Flat Rental</Link>
                    </div>
                    <Menu
                        className="app-menu"
                        mode="horizontal"
                        selectedKeys={[this.props.location.pathname]}
                        style={{ lineHeight: '62px' }} >
                        {menuItems}
                    </Menu>
                </div>
            </Header>
        );
    }
}

function ProfileDropdownMenu(props) {
    const dropdownMenu = (
        <Menu onClick={props.handleMenuClick} className="profile-dropdown-menu">
            <Menu.Item key="user-info" className="dropdown-item" disabled>
                <div className="user-full-name-info">
                    {props.currentUser.name}
                </div>
                <div className="username-info">
                    @{props.currentUser.username}
                </div>
            </Menu.Item>
            <Menu.Divider />
            <Menu.Item key="account" className="dropdown-item">
                <Link to={"/account"}><FormattedMessage id="labels.account"/></Link>
            </Menu.Item>
            <Menu.Item key="logout" className="dropdown-item">
                <FormattedMessage id="labels.logout"/>
            </Menu.Item>
        </Menu>
    );

    return (
        <Dropdown
            overlay={dropdownMenu}
            trigger={['click']}
            getPopupContainer = { () => document.getElementsByClassName('profile-menu')[0]}>
            <a className="ant-dropdown-link">
                <Icon type="user" className="nav-icon" style={{marginRight: 0}} /> <Icon type="down"/>
            </a>
        </Dropdown>
    );
}

function CreateAnnouncementDropDownMenu(props) {
    const dropdownMenu = (
        <Menu className="profile-dropdown-menu">
            <Menu.Item key="user-info" className="dropdown-item" disabled>
                <div className="username-info">
                    <FormattedMessage id="labels.choose_announcement_type"/>
                </div>
            </Menu.Item>
            <Menu.Divider />
            <Menu.Item key="flat" className="dropdown-item">
                <Link to={"/announcement/create/flat"}><FormattedMessage id="labels.announcement_type_flat"/></Link>
            </Menu.Item>
            <Menu.Item key="room" className="dropdown-item">
                <Link to={"/announcement/create/room"}><FormattedMessage id="labels.announcement_type_room"/></Link>
            </Menu.Item>
            <Menu.Item key="place_in_room" className="dropdown-item">
                <Link to={"/announcement/create/place_in_room"}><FormattedMessage id="labels.announcement_type_place_in_room"/></Link>
            </Menu.Item>
            <Menu.Divider />
            <Menu.Item key="looking_for_flat" className="dropdown-item">
                <Link to={"/announcement/create/look_for_flat"}><FormattedMessage id="labels.announcement_type_looking_for_flat"/></Link>
            </Menu.Item>
            <Menu.Item key="room" className="dropdown-item">
                <Link to={"/announcement/create/look_for_room"}><FormattedMessage id="labels.announcement_type_looking_for_room"/></Link>
            </Menu.Item>
            <Menu.Item key="room" className="dropdown-item">
                <Link to={"/announcement/create/look_for_place_in_room"}><FormattedMessage id="labels.announcement_type_looking_for_place_in_room"/></Link>
            </Menu.Item>
        </Menu>
    );

    return (
        <Dropdown
            overlay={dropdownMenu}
            trigger={['click']}
            getPopupContainer = { () => document.getElementsByClassName('profile-menu')[0]}>
            <a className="ant-dropdown-link">
                <span><Icon type="plus-circle" style={{marginRight: 0, fontSize: '18px'}} /><span style={{fontSize: '17px'}}> <FormattedMessage id="labels.add_announcement"/></span></span>
            </a>
        </Dropdown>
    );
}


export default withRouter(AppHeader);