import React, { Component } from 'react';
import {
    Form,
    Input,
    InputNumber,
    Card,
    Switch,
} from 'antd';
import * as CONS from "../infrastructure/Constants";
import './Step.css';
import ComboBox from "../commons/ComboBox";
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
import RoomList from "./RoomList";

const { Paragraph } = Typography;

const FormItem = Form.Item;
const { TextArea } = Input;

const today = moment(new Date());

class ThirdStepContainer extends Component {
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

    updateOnChange(event) {
        this.props.onUpdate(event.target.name, event.target.value);
    }

    updateOnChangeWithName = name => value => {
        this.props.onUpdate(name, value);
    };

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
                                <ComboBox name="buildingType.id"
                                          itemList={this.props.appData.buildingTypes}
                                          onUpdate={this.props.onUpdate}
                                          value={this.props.formData["buildingType.id"]}
                                          placeholder={intl.formatMessage({ id: 'placeholders.building_type' })}
                                />
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.building_material' })}
                                help="">
                                <ComboBox
                                    name="buildingMaterial.id"
                                    itemList={this.props.appData.buildingMaterialTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["buildingMaterial.id"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.building_material' })}
                                />
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.heating_type' })}
                                help="">
                                <ComboBox
                                    name="heatingType.id"
                                    itemList={this.props.appData.heatingTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["heatingType.id"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.heating_type' })}
                                />
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.windows_type' })}
                                help="">
                                <ComboBox
                                    name="windowType.id"
                                    itemList={this.props.appData.windowTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["windowType.id"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.windows_type' })}
                                />
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.parking_type' })}
                                help="">
                                <ComboBox
                                    name="parkingType.id"
                                    itemList={this.props.appData.parkingTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["parkingType.id"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.parking_type' })}/>
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.apartment_state' })}
                                help="">
                                <ComboBox
                                    name="apartmentState.id"
                                    itemList={this.props.appData.apartmentStateTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["apartmentState.id"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.apartment_state' })}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.year_built' })}>
                                <InputNumber
                                    min={1800}
                                    max={today.year()}
                                    onChange={this.updateOnChangeWithName('yearBuilt')}
                                    value={this.props.formData.yearBuilt}
                                />
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.well_planned' })}>
                                <Switch
                                    name="wellPlanned"
                                    checkedChildren={intl.formatMessage({ id: 'labels.yes' })}
                                    unCheckedChildren={intl.formatMessage({ id: 'labels.no' })}
                                    title={intl.formatMessage({ id: 'labels.well_planned' })}
                                    onChange={this.updateOnChangeWithName('wellPlanned')}
                                    checked={this.props.formData.wellPlanned}
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
                        <Card title={intl.formatMessage({ id: 'labels.rooms' })} bordered={false}>
                            <RoomList name={"rooms"} onUpdate={this.props.onUpdate} appData={this.props.appData} formData={this.props.formData}/>
                        </Card>
                        <Card title={intl.formatMessage({ id: 'labels.kitchen' })} bordered={false}>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.kitchen_type' })}
                                help="">
                                <ComboBox
                                    name="kitchen.kitchenType"
                                    itemList={this.props.appData.kitchenTypes}
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["kitchen.kitchenType"]}
                                    placeholder={intl.formatMessage({ id: 'placeholders.kitchen_type' })}/>
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.area' })}>
                                <Input
                                    name="kitchen.kitchenArea"
                                    onChange={this.updateOnChange}
                                    value={this.props.formData["kitchen.kitchenArea"]}
                                    addonAfter="m2"
                                    autoComplete="off"
                                    placeholder={intl.formatMessage({ id: 'placeholders.kitchen_area' })}/>
                            </FormItem>
                            <FormItem
                                label={intl.formatMessage({ id: 'labels.cooker_type' })}
                                help="">
                                <ComboBox
                                    name="kitchen.cookerType"
                                    onUpdate={this.props.onUpdate}
                                    value={this.props.formData["kitchen.cookerType"]}
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
                            <FormItem label={intl.formatMessage({ id: 'labels.number_of_bathrooms' })}>
                                <InputNumber
                                    min={1}
                                    max={10}
                                    onChange={this.updateOnChangeWithName('bathroom.numberOfBathrooms')}
                                    value={this.props.formData["bathroom.numberOfBathrooms"]}
                                />
                            </FormItem>
                            <FormItem label={intl.formatMessage({ id: 'labels.separate_wc' })}>
                                <Switch
                                    checkedChildren={intl.formatMessage({ id: 'labels.yes' })}
                                    unCheckedChildren={intl.formatMessage({ id: 'labels.no' })}
                                    title={intl.formatMessage({ id: 'labels.separate_wc' })}
                                    onChange={this.updateOnChangeWithName('bathroom.separateWC')}
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

}

export default injectIntl(ThirdStepContainer);