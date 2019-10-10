import React, { Component } from 'react';
import {
    Form,
    Input,
    InputNumber,
    Card,
    Switch,
} from 'antd';
import * as CONS from "../../infrastructure/Constants";
import '../Step.css';
import ComboBox from "../../commons/ComboBox";
import {FormattedMessage, injectIntl} from 'react-intl';
import moment from "moment";
import {
    downloadFile,
    getApartmentAmenitiesTypes,
    getApartmentStateTypes,
    getBuildingMaterialTypes,
    getBuildingTypes, getCookerTypes, getFurnishing,
    getHeatingTypes, getKitchenTypes, getNeighborhoodItems, getParkingTypes, getPreferences,
    getWindowTypes
} from "../../infrastructure/RestApiHandler";
import CheckBoxGrid from "../../commons/CheckBoxGrid";
import ImageGalleryUploader from "../ImageGalleryUploader";
import { Typography } from 'antd';
import RoomList from "../RoomList";
import {API_BASE_URL} from "../../infrastructure/Constants";

const { Paragraph } = Typography;

const FormItem = Form.Item;
const { TextArea } = Input;

const today = moment(new Date());

class FlatAnnouncementDetailInfoStep extends Component {
    constructor(props) {
        super(props);

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
        this.updateOnChange = this.updateOnChange.bind(this);
        this.updateOnChangeWithName = this.updateOnChangeWithName.bind(this);
        this.onRoomChange = this.onRoomChange.bind(this);
        this.getRoomAttribute = this.getRoomAttribute.bind(this);
        this.onRoomChangeCheckboxFurnishing = this.onRoomChangeCheckboxFurnishing.bind(this);
        this.onlyPositiveInteger = this.props.intl.formatMessage({ id: 'text.only_positive_integer_msg' });
        this.setTransientAnnouncementImagesData = this.setTransientAnnouncementImagesData.bind(this);
        this.setTransientAnnouncementImagesData();
    }

    loadBuildingTypes() {
        this.props.loadData(getBuildingTypes, 'buildingTypes');
    }

    loadBuildingMaterialTypes() {
        this.props.loadData(getBuildingMaterialTypes, 'buildingMaterialTypes');
    }

    loadHeatingTypes() {
        this.props.loadData(getHeatingTypes, 'heatingTypes');
    }

    loadWindowTypes() {
        this.props.loadData(getWindowTypes, 'windowTypes')
    }

    loadParkingTypes() {
        this.props.loadData(getParkingTypes, 'parkingTypes')
    }

    loadApartmentStateTypes() {
        this.props.loadData(getApartmentStateTypes, 'apartmentStateTypes')
    }

    loadApartmentAmenitiesTypes() {
        this.props.loadData(getApartmentAmenitiesTypes, 'apartmentAmenitiesTypes');
    }

    loadKitchenTypes() {
        this.props.loadData(getKitchenTypes, 'kitchenTypes');
    }

    loadCookerTypes() {
        this.props.loadData(getCookerTypes, 'cookerTypes')
    }

    loadKitchenFurnishing() {
        this.props.loadData(getFurnishing, 'kitchenFurnishing', 'KITCHEN');
    }

    loadBathroomFurnishing() {
        this.props.loadData(getFurnishing, 'bathroomFurnishing', 'BATHROOM');
    }

    // loadMedia() {
    //     this.loadData(get)
    // }

    loadNeighbourhoodItems() {
        this.props.loadData(getNeighborhoodItems, 'neighbourhoodItems');
    }

    loadPreferences() {
        this.props.loadData(getPreferences, 'preferences')
    }

    loadRoomFurnishing() {
        this.props.loadData(getFurnishing, 'roomFurnishing', 'ROOM')
    }

    updateOnChange(event, validationFunction) {
        const validationResult = validationFunction ? validationFunction(event.target.value) : undefined;
        this.props.onUpdate(event.target.name, event.target.value, validationResult);
    }

    updateOnChangeWithName(name, value, validationFunction) {
        const validationResult = validationFunction(value);
        this.props.onUpdate(name, value, validationResult);
    };

    onRoomChange(name, value, validationFunction, roomAttribute) {
        const validationResult = validationFunction ? validationFunction(value) : {validateStatus: 'success', errorMsg: null};
        let rooms = this.props.formData.rooms;
        if (rooms && rooms[0]) {
            rooms[0][roomAttribute] = value;
        } else {
            rooms = new Array(new Object({[roomAttribute]: value}));
        }
        this.props.onUpdate("rooms", rooms, validationResult, "rooms.0." + roomAttribute);
    }

    onRoomChangeCheckboxFurnishing(name, value, validationFunction) {
        this.onRoomChange(name, value, validationFunction, 'furnishing');
    }

    getRoomAttribute(attribute) {
        if(this.props.formData && this.props.formData.rooms && this.props.formData.rooms[0]) {
            return this.props.formData.rooms[0][attribute];
        }
        return undefined;
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
        console.log(this.props.appData);
    }

    setTransientAnnouncementImagesData() {
        if(this.props.formData && this.props.formData.announcementImages && !this.props.formData.transient_announcementImages) {
            let fileList = this.props.formData.announcementImages.map((image, index) => new Object({uid: "-" + index, name: image.filename, status: 'done', url: API_BASE_URL + "/file/download/" + image.filename}));
            this.props.onUpdate('transient_announcementImages', fileList);
        }
    }

    render() {
        const { intl } = this.props;
        return (
            <div className="step-container">
                <h1 className="page-title"><FormattedMessage id="labels.detail_info"/></h1>
                <div className="step-container-content">
                    <Form className="step-form" layout="horizontal" {...this.props}>
                        <Card title={intl.formatMessage({ id: 'labels.room_accessories' })} bordered={false}>
                            <CheckBoxGrid name="rooms"
                                          itemList={this.props.appData.roomFurnishing}
                                          onUpdate={this.onRoomChangeCheckboxFurnishing}
                                          checkedValues={this.getRoomAttribute('furnishing')}
                                          span={8}
                            />
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.apartment' })} bordered={false}>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.building_type' })}
                                help="">
                                <ComboBox name="buildingType"
                                          itemList={this.props.appData.buildingTypes}
                                          onUpdate={this.props.onUpdate}
                                          value={this.props.formData["buildingType.value"]}
                                          placeholder={intl.formatMessage({ id: 'placeholders.building_type' })}
                                />
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.building_material' })}
                                help="">
                                <ComboBox
                                    name="buildingMaterial"
                                    itemList={this.props.appData.buildingMaterialTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["buildingMaterial.value"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.building_material' })}
                                />
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.heating_type' })}
                                help="">
                                <ComboBox
                                    name="heatingType"
                                    itemList={this.props.appData.heatingTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["heatingType.value"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.heating_type' })}
                                />
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.windows_type' })}
                                help="">
                                <ComboBox
                                    name="windowType"
                                    itemList={this.props.appData.windowTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["windowType.value"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.windows_type' })}
                                />
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.parking_type' })}
                                help="">
                                <ComboBox
                                    name="parkingType"
                                    itemList={this.props.appData.parkingTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["parkingType.value"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.parking_type' })}/>
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.apartment_state' })}
                                help="">
                                <ComboBox
                                    name="apartmentState"
                                    itemList={this.props.appData.apartmentStateTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["apartmentState.value"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.apartment_state' })}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({id: 'labels.area'})}
                                      validateStatus={this.props.getValidationStatus("totalArea")}
                                      help={this.props.getErrorMessage("totalArea")}>
                                <Input
                                    addonAfter={<span>m<sup>2</sup></span>}
                                    name="totalArea"
                                    autoComplete="off"
                                    value={this.props.formData.totalArea}
                                    onChange={event => this.updateOnChange(event, this.validateIfOptionalPositiveInteger)}
                                    placeholder={intl.formatMessage({id: 'placeholders.total_area'})}
                                />
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.year_built' })}
                                      validateStatus={this.props.getValidationStatus("yearBuilt")}
                                      help={this.props.getErrorMessage("yearBuilt")}>
                                <InputNumber
                                    min={1700}
                                    max={today.year()}
                                    onChange={value => this.updateOnChangeWithName('yearBuilt', value, this.validateIfOptionalPositiveInteger)}
                                    value={this.props.formData.yearBuilt}
                                />
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.well_planned' })}>
                                <Switch
                                    name="wellPlanned"
                                    checkedChildren={intl.formatMessage({ id: 'labels.yes' })}
                                    unCheckedChildren={intl.formatMessage({ id: 'labels.no' })}
                                    title={intl.formatMessage({ id: 'labels.well_planned' })}
                                    onChange={value => this.updateOnChangeWithName('wellPlanned', value)}
                                    checked={this.props.formData.wellPlanned}
                                />
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({id: 'labels.number_of_rooms'})}
                                validateStatus={this.props.getValidationStatus("numberOfRooms")}
                                help={this.props.getErrorMessage("numberOfRooms")}>
                                <InputNumber
                                    name="numberOfRooms"
                                    min={1}
                                    max={10}
                                    value={this.props.formData.numberOfRooms}
                                    onChange={value => this.updateOnChangeWithName('numberOfRooms', value, this.validateIfOptionalPositiveInteger)}
                                />
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.amenities' })} layout="horizontal" help="">
                                <CheckBoxGrid
                                    name="apartmentAmenities"
                                    itemList={this.props.appData.apartmentAmenitiesTypes}
                                    span={8}
                                    onUpdate={this.props.onUpdate}
                                    checkedValues={this.props.formData.apartmentAmenities}
                                />
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.photos' })} bordered={false}>
                            <Paragraph><FormattedMessage id={"text.attach_images_text"} values={{max_images : CONS.MAX_IMAGES}}/></Paragraph>
                            <ImageGalleryUploader
                                name="announcementImages"
                                transientDataName= {CONS.TRANSIENT_MARKER + "announcementImages"}
                                onUpdate={this.props.onUpdate}
                                fileList={this.props.formData.transient_announcementImages}/>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.kitchen' })} bordered={false}>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.kitchen_type' })}
                                help="">
                                <ComboBox
                                    name="kitchen.kitchenType"
                                    itemList={this.props.appData.kitchenTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["kitchen.kitchenType.value"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.kitchen_type' })}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.area' })}
                                      validateStatus={this.props.getValidationStatus("kitchen.kitchenArea")}
                                      help={this.props.getErrorMessage("kitchen.kitchenArea")}>
                                <Input
                                    name="kitchen.kitchenArea"
                                    onChange={event => this.updateOnChange(event, this.validateIfOptionalPositiveInteger)}
                                    value={this.props.formData["kitchen.kitchenArea"]}
                                    addonAfter={<span>m<sup>2</sup></span>}
                                    autoComplete="off"
                                    placeholder={intl.formatMessage({ id: 'placeholders.kitchen_area' })}/>
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.cooker_type' })}
                                help="">
                                <ComboBox
                                    name="kitchen.cookerType"
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["kitchen.cookerType.value"]}
                                    itemList={this.props.appData.cookerTypes}
                                    placeholder={intl.formatMessage({ id: 'placeholders.cooker_type' })}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.accessories' })} layout="horizontal" help="">
                                <CheckBoxGrid
                                    name="kitchen.furnishing"
                                    checkedValues={this.props.formData["kitchen.furnishing"]}
                                    itemList={this.props.appData.kitchenFurnishing}
                                    onUpdate={this.props.onUpdate}
                                    span={8}/>
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.bathroom' })} bordered={false}>
                            <FormItem label={intl.formatMessage({ id: 'labels.number_of_bathrooms' })}
                                      validateStatus={this.props.getValidationStatus("bathroom.numberOfBathrooms")}
                                      help={this.props.getErrorMessage("bathroom.numberOfBathrooms")}>
                                <InputNumber
                                    min={1}
                                    max={10}
                                    onChange={value => this.updateOnChangeWithName('bathroom.numberOfBathrooms', value, this.validateIfOptionalPositiveInteger)}
                                    value={this.props.formData["bathroom.numberOfBathrooms"]}
                                />
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.separate_wc' })}>
                                <Switch
                                    checkedChildren={intl.formatMessage({ id: 'labels.yes' })}
                                    unCheckedChildren={intl.formatMessage({ id: 'labels.no' })}
                                    title={intl.formatMessage({ id: 'labels.separate_wc' })}
                                    onChange={value => this.updateOnChangeWithName('bathroom.separateWC', value)}
                                    checked={this.props.formData["bathroom.separateWC"]}
                                />
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.accessories' })} layout="horizontal" help="">
                                <CheckBoxGrid
                                    name="bathroom.furnishing"
                                    itemList={this.props.appData.bathroomFurnishing}
                                    span={8}
                                    onUpdate={this.props.onUpdate}
                                    checkedValues={this.props.formData["bathroom.furnishing"]}
                                />
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.flatmates' })} bordered={false}>
                            <FormItem label={intl.formatMessage({ id: 'labels.flatmates_number' })}
                                      validateStatus={this.props.getValidationStatus("numberOfFlatmates")}
                                      help={this.props.getErrorMessage("numberOfFlatmates")}>
                                <InputNumber
                                    min={1}
                                    max={10}
                                    onChange={value => this.updateOnChangeWithName('numberOfFlatmates', value, this.validateIfOptionalPositiveInteger)}
                                    value={this.props.formData["numberOfFlatmates"]}
                                />
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.about_flatmates' })} layout="horizontal" help="">
                                <TextArea
                                    name="aboutFlatmates"
                                    rows={6}
                                    onChange={this.updateOnChange}
                                    value={this.props.formData.aboutFlatmates}
                                    placeholder={intl.formatMessage({ id: 'placeholders.about_flatmates' })}
                                />
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.media' })} bordered={false}>
                            <FormItem layout="horizontal" help="">
                                <CheckBoxGrid itemList={this.props.appData.media} span={8}/>
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.preferences' })} bordered={false}>
                            <FormItem layout="horizontal" labelCol={0} wrapperCol={24} help="">
                                <CheckBoxGrid
                                    name="preferences"
                                    itemList={this.props.appData.preferences}
                                    span={8}
                                    onUpdate={this.props.onUpdate}
                                    checkedValues={this.props.formData.preferences}
                                />
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.neighbourhood' })} bordered={false}>
                            <FormItem layout="horizontal" labelCol={0} wrapperCol={24} help="">
                                <CheckBoxGrid
                                    name="neighbourhood"
                                    itemList={this.props.appData.neighbourhoodItems}
                                    span={8}
                                    onUpdate={this.props.onUpdate}
                                    checkedValues={this.props.formData.neighbourhood}
                                />
                            </FormItem>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.flat_description' })} bordered={false}>
                            <TextArea
                                name="description"
                                rows={15}
                                onChange={this.updateOnChange}
                                value={this.props.formData.description}
                                placeholder={intl.formatMessage({ id: 'placeholders.flat_description' })}
                            />
                        </Card>
                    </Form>
                </div>
            </div>
        );
    }

    validateIfOptionalPositiveInteger = (input) => {
        if (!input) {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
        if(!this.isPositiveInteger(input)) {
            return {
                validateStatus: 'error',
                errorMsg: this.onlyPositiveInteger
            };
        }
        return {
            validateStatus: 'success',
            errorMsg: null,
        };
    };

    isPositiveInteger(str) {
        return /^[1-9]\d*$/.test(str);
    }

}

export default injectIntl(FlatAnnouncementDetailInfoStep);