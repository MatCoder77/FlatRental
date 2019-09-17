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
import * as CONS from "../Constants";
import {Link} from "react-router-dom";
import './Step.css';
import ComboBox from "../commons/ComboBox";
import Text from "antd/lib/typography/Text";
import {FormattedMessage, injectIntl} from 'react-intl';
import moment from "moment";
import {
    getApartmentAmenitiesTypes,
    getApartmentStateTypes,
    getBuildingMaterialTypes,
    getBuildingTypes, getCookerTypes, getFurnishing,
    getHeatingTypes, getKitchenTypes, getNeighborhoodItems, getParkingTypes, getPreferences,
    getWindowTypes
} from "../infrastructure/RestApiHandler";
import CheckBoxGrid from "../commons/CheckBoxGrid";
import ImageGalleryUploader from "./ImageGalleryUploader";
import { Typography } from 'antd';
import RoomFrom from "./RoomFrom";

const { Paragraph } = Typography;

const FormItem = Form.Item;
const { TextArea } = Input;

const today = moment(new Date());

const rooms = [1, 2, 3];

class ThirdStepContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            buildingTypes: [],
            buildingMaterialTypes: [],
            heatingTypes: [],
            parkingTypes: [],
            windowTypes: [],
            apartmentStateTypes: [],
            apartmentAmenitiesTypes: [],
            roomFurnishing: [],
            kitchenTypes: [],
            cookerTypes: [],
            kitchenFurnishing: [],

            bathroomFurnishing: [],
            media: [],
            neighbourhoodItems: [],
            preferences: [],
        }
        this.loadData = this.loadData.bind(this);
        this.loadBuildingTypes = this.loadBuildingTypes.bind(this);
        this.loadBuildingMaterialTypes = this.loadBuildingMaterialTypes.bind(this);
        this.loadHeatingTypes = this.loadHeatingTypes.bind(this);
        this.loadWindowTypes = this.loadWindowTypes.bind(this);
        this.loadApartmentStateTypes = this.loadApartmentStateTypes.bind(this);
        this.loadApartmentAmenitiesTypes = this.loadApartmentAmenitiesTypes.bind(this);
        this.loadKitchenTypes = this.loadKitchenTypes.bind(this);
        this.loadCookerTypes = this.loadCookerTypes.bind(this);
        this.loadKitchenFurnishing = this.loadKitchenFurnishing.bind(this);
        this.loadBathroomFurnishing = this.loadBathroomFurnishing.bind(this);
        //this.loadMedia = this.loadMedia.bind(this);
        this.loadNeighbourhoodItems = this.loadNeighbourhoodItems.bind(this);
        this.loadPreferences = this.loadPreferences.bind(this);
        this.loadRoomFurnishing = this.loadRoomFurnishing.bind(this);
    }

    loadData(supplierFunction, filedName, arg) {
        let promise = supplierFunction(arg);

        if(!promise) {
            return;
        }

        // this.setState({
        //     isLoading: true
        // });

        promise
            .then(response => {
                this.setState({
                    [filedName]: response
                    // isLoading: false
                })
            }).catch(error => {
            // this.setState({
            //     isLoading: false
            // })
        });
    }

    loadBuildingTypes() {
        this.loadData(getBuildingTypes, 'buildingTypes');
    }

    loadBuildingMaterialTypes() {
        this.loadData(getBuildingMaterialTypes, 'buildingMaterialTypes');
    }

    loadHeatingTypes() {
        this.loadData(getHeatingTypes, 'heatingTypes');
    }

    loadWindowTypes() {
        this.loadData(getWindowTypes, 'windowTypes')
    }

    loadParkingTypes() {
        this.loadData(getParkingTypes, 'parkingTypes')
    }

    loadApartmentStateTypes() {
        this.loadData(getApartmentStateTypes, 'apartmentStateTypes')
    }

    loadApartmentAmenitiesTypes() {
        this.loadData(getApartmentAmenitiesTypes, 'apartmentAmenitiesTypes');
    }

    loadKitchenTypes() {
        this.loadData(getKitchenTypes, 'kitchenTypes');
    }

    loadCookerTypes() {
        this.loadData(getCookerTypes, 'cookerTypes')
    }

    loadKitchenFurnishing() {
        this.loadData(getFurnishing, 'kitchenFurnishing', 'KITCHEN');
    }

    loadBathroomFurnishing() {
        this.loadData(getFurnishing, 'bathroomFurnishing', 'BATHROOM');
    }

    // loadMedia() {
    //     this.loadData(get)
    // }

    loadNeighbourhoodItems() {
        this.loadData(getNeighborhoodItems, 'neighbourhoodItems');
    }

    loadPreferences() {
        this.loadData(getPreferences, 'preferences')
    }

    loadRoomFurnishing() {
        this.loadData(getFurnishing, 'roomFurnishing', 'ROOM')
    }

    componentDidMount() {
        this.loadBuildingTypes();
        this.loadBuildingMaterialTypes();
        this.loadHeatingTypes();
        this.loadWindowTypes();
        this.loadParkingTypes();
        this.loadApartmentStateTypes();
        this.loadApartmentAmenitiesTypes();
        this.loadKitchenTypes();
        this.loadCookerTypes();
        this.loadKitchenFurnishing();
        this.loadBathroomFurnishing();
        this.loadNeighbourhoodItems();
        this.loadPreferences();
        this.loadRoomFurnishing();
    }

    render() {
        const { intl } = this.props;
        return (
            <div className="step-container">
                <h1 className="page-title"><FormattedMessage id="labels.detail_info"/></h1>
                <div className="step-container-content">
                    <Form className="step-form" layout="horizontal" {...this.props}>
                        <Card title={intl.formatMessage({ id: 'labels.apartment' })} bordered={false}>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.building_type' })}
                                help="">
                                <ComboBox itemList={this.state.buildingTypes} placeholder={intl.formatMessage({ id: 'placeholders.building_type' })}/>
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.building_material' })}
                                help="">
                                <ComboBox itemList={this.state.buildingMaterialTypes} placeholder={intl.formatMessage({ id: 'placeholders.building_material' })}/>
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.heating_type' })}
                                help="">
                                <ComboBox itemList={this.state.heatingTypes} placeholder={intl.formatMessage({ id: 'placeholders.heating_type' })}/>
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.windows_type' })}
                                help="">
                                <ComboBox itemList={this.state.windowTypes} placeholder={intl.formatMessage({ id: 'placeholders.windows_type' })}/>
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.parking_type' })}
                                help="">
                                <ComboBox itemList={this.state.parkingTypes} placeholder={intl.formatMessage({ id: 'placeholders.parking_type' })}/>
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.apartment_state' })}
                                help="">
                                <ComboBox itemList={this.state.apartmentStateTypes} placeholder={intl.formatMessage({ id: 'placeholders.apartment_state' })}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.year_built' })}>
                                <InputNumber min={1800} max={today.year()}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.well_planned' })}>
                                <Switch checkedChildren={intl.formatMessage({ id: 'labels.yes' })} unCheckedChildren={intl.formatMessage({ id: 'labels.no' })} title={intl.formatMessage({ id: 'labels.separate_wc' })}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.amenities' })} layout="horizontal" help="">
                                <CheckBoxGrid itemList={this.state.apartmentAmenitiesTypes} span={8}/>
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.rooms' })} bordered={false}>
                            {rooms.map(room => (
                                <RoomFrom roomFurnishing={this.state.roomFurnishing} num={room} {...this.props}/>
                            ))}
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.kitchen' })} bordered={false}>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.kitchen_type' })}
                                help="">
                                <ComboBox itemList={this.state.kitchenTypes} placeholder={intl.formatMessage({ id: 'placeholders.kitchen_type' })}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.area' })}>
                                <Input addonAfter="m2" name="large" autoComplete="off" placeholder={intl.formatMessage({ id: 'placeholders.kitchen_area' })}/>
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.cooker_type' })}
                                help="">
                                <ComboBox itemList={this.state.cookerTypes} placeholder={intl.formatMessage({ id: 'placeholders.cooker_type' })}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.accessories' })} layout="horizontal" help="">
                                <CheckBoxGrid itemList={this.state.kitchenFurnishing} span={8}/>
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.bathroom' })} bordered={false}>
                            <FormItem label={intl.formatMessage({ id: 'labels.number_of_bathrooms' })}>
                                <InputNumber min={1} max={10} defaultValue={1}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.separate_wc' })}>
                                <Switch checkedChildren={intl.formatMessage({ id: 'labels.yes' })} unCheckedChildren={intl.formatMessage({ id: 'labels.no' })} title={intl.formatMessage({ id: 'labels.separate_wc' })}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.accessories' })} layout="horizontal" help="">
                                <CheckBoxGrid itemList={this.state.bathroomFurnishing} span={8}/>
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.media' })} bordered={false}>
                            <FormItem layout="horizontal" help="">
                                <CheckBoxGrid itemList={this.state.media} span={8}/>
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.preferences' })} bordered={false}>
                            <FormItem layout="horizontal" labelCol={0} wrapperCol={24} help="">
                                <CheckBoxGrid itemList={this.state.preferences} span={8}/>
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.neighbourhood' })} bordered={false}>
                            <FormItem layout="horizontal" labelCol={0} wrapperCol={24} help="">
                                <CheckBoxGrid itemList={this.state.neighbourhoodItems} span={8}/>
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.flat_description' })} bordered={false}>
                            <TextArea rows={15}
                                placeholder={intl.formatMessage({ id: 'placeholders.flat_description' })}
                            />
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.photos' })} bordered={false}>
                            <Paragraph>Załącz zdjęcia mieszkania. Pierwsza fotografia będzie wyświetlana jako zdjęcie główne ogłosznia. Możesz załączyć do 20 zdjęć. Pamiętaj, że dobrze wykonane zjęcia znacząco zwiększają atrakcyjność oferty</Paragraph>
                            <ImageGalleryUploader/>
                        </Card>
                    </Form>
                </div>
            </div>
        );
    }

}

export default injectIntl(ThirdStepContainer);