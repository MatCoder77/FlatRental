import React, {Component} from 'react';
import {Steps, Button, Row, Col, Icon} from 'antd';
import { withRouter } from 'react-router-dom';
import './Step.css';
import moment from "moment";
import {createAnnouncement, downloadFile, getAnnouncement, updateAnnouncement} from "../infrastructure/RestApiHandler";
import FirstStepContainer from "./flat/FlatAnnouncementGeneralInfoStep";
import ThirdStepContainer from "./flat/FlatAnnouncementDetailInfoStep";
import SecondStepContainer from "./LocalityStep";
import {FormattedMessage, injectIntl} from "react-intl";
import * as DTOUtils from "../infrastructure/DTOUtils";
import "./AnnouncementStepWizard.css"
import RoomAnnouncementGeneralInfoStep from "./room/RoomAnnouncementGeneralInfoStep";
import RoomAnnouncementDetailInfoStep from "./room/RoomAnnouncementDetailInfoStep";
import PlaceInRoomAnnouncementGeneralInfoStep from "./placeinroom/PlaceInRoomAnnouncementGeneralInfoStep";
import PlaceInRoomAnnouncementDetailInfoStep from "./placeinroom/PlaceInRoomAnnouncementDetailInfoStep";
import AnnouncementView from "./AnnouncementView";
import * as CONS from "../infrastructure/Constants";

const Step = Steps.Step;

const formItemLayout = {
    labelCol: {
        xs: {span: 12},
        sm: {span: 4},
    },
    wrapperCol: {
        xs: {span: 28},
        sm: {span: 18},
    },
};

class AnnouncementStepWizard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            current: 0,
            formData: this.props.formData ? this.props.formData : {type: this.props.announcementType},
            appData: {},
            validationStatus: {},
            errorMessages: {}
        };
        this.updateFormData = this.updateFormData.bind(this);
        this.setAvailableFrom = this.setAvailableFrom.bind(this);

        this.updateFormData('bathroom.numberOfBathrooms', 1);
        this.updateFormData('wellPlanned', this.props.formData['wellPlanned'] ? this.props.formData['wellPlanned'] : false);
        this.updateFormData('bathroom.separateWC', this.props.formData['bathroom.separateWC'] ? this.props.formData['bathroom.separateWC'] : false);

        this.loadData = this.loadData.bind(this);
        this.submitAnnouncement = this.submitAnnouncement.bind(this);
        this.updateValidation = this.updateValidation.bind(this);
        this.getValidationStatus = this.getValidationStatus.bind(this);
        this.getErrorMessage = this.getErrorMessage.bind(this);
        this.registerRequiredFields = this.registerRequiredFields.bind(this);
        this.unregisterRequiredFields = this.unregisterRequiredFields.bind(this);
        // this.setFormWithSuppliedData = this.setFormWithSuppliedData.bind(this);
        // this.setFormWithSuppliedData();
        this.setAvailableFrom();
    }

    setAvailableFrom() {
        let suppliedAvailableFrom = this.props.formData ? this.props.formData.availableFrom : undefined;
        let availableFrom = suppliedAvailableFrom ? moment(suppliedAvailableFrom) : moment(new Date());
        this.updateFormData('availableFrom', availableFrom, {validateStatus: 'success', errorMsg: null});
    }

    next() {
        const current = this.state.current + 1;
        console.log(this.state.formData);
        this.setState({current});
    }

    prev() {
        const current = this.state.current - 1;
        this.setState({current});
    }

    updateFormData(fieldName, fieldValue, validationResult) {
        const {formData} = this.state;
        formData[fieldName] = fieldValue;
        this.setState({formData});
        console.log(formData);
        this.updateValidation(fieldName, validationResult)
    }

    updateValidation(validationName, validationResult) {
        if (validationResult) {
            const {validationStatus} = this.state;
            const {errorMessages} = this.state;
            const currentStep = this.state.current.toString();
            validationStatus[currentStep + validationName] = validationResult.validateStatus;
            errorMessages[currentStep + validationName] = validationResult.errorMsg;
            this.setState({validationStatus});
            this.setState({errorMessages});
        }
        console.log(this.state.validationStatus);
    }

    getErrorMessage(filedName) {
        const {errorMessages} = this.state;
        const currentStep = this.state.current.toString();
        return errorMessages[currentStep + filedName];
    }

    getValidationStatus(fieldName) {
        const {validationStatus} = this.state;
        const currentStep = this.state.current.toString();
        return validationStatus[currentStep + fieldName];
    }

    registerRequiredFields(requiredFields, force) {
        for (let field of requiredFields) {
            if (this.getValidationStatus(field) !== 'success' || force) {
                this.updateValidation(field, {validateStatus: undefined, errorMsg: undefined});
            }
        }
    }

    unregisterRequiredFields(fields) {
        const {validationStatus} = this.state;
        const currentStep = this.state.current.toString();
        for (let field of fields) {
            delete validationStatus[currentStep + field];
        }
        this.setState({validationStatus});
    }

    loadData(supplierFunction, fieldName, param, onDataLoadedCallbackFunction, callbackFunctionParam) {
        let promise = supplierFunction(param);

        if (!promise) {
            return;
        }

        // this.setState({
        //     isLoading: true
        // });

        promise
            .then(response => {
                const {appData} = this.state;
                appData[fieldName] = response;
                this.setState({
                    appData
                    // isLoading: false
                });
                if (onDataLoadedCallbackFunction) {
                    onDataLoadedCallbackFunction(fieldName, callbackFunctionParam);
                }
            }).catch(error => {
            // this.setState({
            //     isLoading: false
            // })
        });
    }

    // setFormWithSuppliedData() {
    //     if(this.props.formData) {
    //         for (let property in this.props.formData) {
    //             if (Object.prototype.hasOwnProperty.call(this.props.formData, property)) {
    //                this.updateFormData(property, this.props.formData[property], {validateStatus: 'success', errorMsg: null});
    //             }
    //         }
    //     }
    // }

    submitAnnouncement() {
        let filteredFormData = DTOUtils.excludeTransientProperties(this.state.formData);
        let announcementDTO = DTOUtils.unflatten(filteredFormData);
        let promise = createAnnouncement(announcementDTO);
    }

    render() {
        const {intl} = this.props;
        const flatSteps = [{
            title: intl.formatMessage({id: "labels.general_info"}),
            content: (
                <FirstStepContainer
                    formData={this.state.formData}
                    onUpdate={this.updateFormData} {...formItemLayout}
                    getValidationStatus={this.getValidationStatus}
                    getErrorMessage={this.getErrorMessage}
                    registerRequiredFields={this.registerRequiredFields}
                    updateValidation={this.updateValidation}
                />
            ),
        }, {
            title: intl.formatMessage({id: "labels.localization"}),
            content: (
                <SecondStepContainer formData={this.state.formData}
                                     onUpdate={this.updateFormData}
                                     loadData={this.loadData}
                                     appData={this.state.appData} {...formItemLayout}
                                     getValidationStatus={this.getValidationStatus}
                                     getErrorMessage={this.getErrorMessage}
                                     registerRequiredFields={this.registerRequiredFields}
                                     unregisterRequiredFields={this.unregisterRequiredFields}
                                     updateValidation={this.updateValidation}/>
            ),
        }, {
            title: intl.formatMessage({id: "labels.detail_info"}),
            content: (
                <ThirdStepContainer formData={this.state.formData} onUpdate={this.updateFormData}
                                    loadData={this.loadData} appData={this.state.appData} {...formItemLayout}
                                    getValidationStatus={this.getValidationStatus}
                                    getErrorMessage={this.getErrorMessage}
                                    updateValidation={this.updateValidation}/>
            ),
        }, {
            title: intl.formatMessage({id: "labels.summary"}),
            content: (
                <AnnouncementView data={this.state.formData} loadData={this.loadData}/>
            ),
        }];

        const roomSteps = [{
            title: intl.formatMessage({id: "labels.general_info"}),
            content: (
                <RoomAnnouncementGeneralInfoStep
                    formData={this.state.formData}
                    onUpdate={this.updateFormData} {...formItemLayout}
                    getValidationStatus={this.getValidationStatus}
                    getErrorMessage={this.getErrorMessage}
                    registerRequiredFields={this.registerRequiredFields}
                    loadData={this.loadData}
                    appData={this.state.appData} {...formItemLayout}
                />
            ),
        }, {
            title: intl.formatMessage({id: "labels.localization"}),
            content: (
                <SecondStepContainer formData={this.state.formData}
                                     onUpdate={this.updateFormData}
                                     loadData={this.loadData}
                                     appData={this.state.appData} {...formItemLayout}
                                     getValidationStatus={this.getValidationStatus}
                                     getErrorMessage={this.getErrorMessage}
                                     registerRequiredFields={this.registerRequiredFields}
                                     unregisterRequiredFields={this.unregisterRequiredFields}/>
            ),
        }, {
            title: intl.formatMessage({id: "labels.detail_info"}),
            content: (
                <RoomAnnouncementDetailInfoStep formData={this.state.formData} onUpdate={this.updateFormData}
                                    loadData={this.loadData} appData={this.state.appData} {...formItemLayout}
                                    getValidationStatus={this.getValidationStatus}
                                    getErrorMessage={this.getErrorMessage}/>
            ),
        }, {
            title: intl.formatMessage({id: "labels.summary"}),
            content: 'Last-content',
        }];
        const placeInRoomSteps =  [{
            title: intl.formatMessage({id: "labels.general_info"}),
            content: (
                <PlaceInRoomAnnouncementGeneralInfoStep
                    formData={this.state.formData}
                    onUpdate={this.updateFormData} {...formItemLayout}
                    getValidationStatus={this.getValidationStatus}
                    getErrorMessage={this.getErrorMessage}
                    registerRequiredFields={this.registerRequiredFields}
                    loadData={this.loadData}
                    appData={this.state.appData} {...formItemLayout}
                />
            ),
        }, {
            title: intl.formatMessage({id: "labels.localization"}),
            content: (
                <SecondStepContainer formData={this.state.formData}
                                     onUpdate={this.updateFormData}
                                     loadData={this.loadData}
                                     appData={this.state.appData} {...formItemLayout}
                                     getValidationStatus={this.getValidationStatus}
                                     getErrorMessage={this.getErrorMessage}
                                     registerRequiredFields={this.registerRequiredFields}
                                     unregisterRequiredFields={this.unregisterRequiredFields}/>
            ),
        }, {
            title: intl.formatMessage({id: "labels.detail_info"}),
            content: (
                <PlaceInRoomAnnouncementDetailInfoStep formData={this.state.formData} onUpdate={this.updateFormData}
                                                loadData={this.loadData} appData={this.state.appData} {...formItemLayout}
                                                getValidationStatus={this.getValidationStatus}
                                                getErrorMessage={this.getErrorMessage}/>
            ),
        }, {
            title: intl.formatMessage({id: "labels.summary"}),
            content: 'Last-content',
        }];
        const stepsByAnnouncementType = new Map([['flat', flatSteps], ['room', roomSteps], ['place_in_room', placeInRoomSteps]]);
        const steps = stepsByAnnouncementType.get(this.state.formData.type);

        const {current} = this.state;
        return (
            <div className="step-wizard-container">
                <Steps current={current}>
                    {steps.map(item => <Step key={item.title} title={item.title}/>)}
                </Steps>
                <div className="steps-content">{steps[current].content}</div>
                <div className="steps-action">
                    <Row type="flex" justify="space-around">

                        {current > 0 && (
                            <Col span={10}>
                                <Button className="step-wizard-button" onClick={() => this.prev()} size="large">
                                    <Icon type="left"/>
                                    <FormattedMessage id="labels.previous"/>
                                </Button>
                            </Col>
                        )}
                        {current < steps.length - 1 && (
                            <Col span={10}>
                                <Button className="step-wizard-button" type="primary" onClick={() => this.next()}
                                        size="large" disabled={!this.isStepValid()}>
                                    <FormattedMessage id="labels.next"/>
                                    <Icon type="right"/>
                                </Button>
                            </Col>
                        )}
                        {current === steps.length - 1 && (
                            <Col span={10}>
                                <Button className="step-wizard-button" type="primary" onClick={this.submitAnnouncement}
                                        size="large">
                                    <FormattedMessage id="labels.add_announcement"/>
                                </Button>
                            </Col>
                        )}
                    </Row>
                </div>
            </div>
        );
    }

    isStepValid() {
        let validationStatus = this.state.validationStatus;
        for (let validation in validationStatus) {
            if (Object.prototype.hasOwnProperty.call(validationStatus, validation) && validation.startsWith(this.state.current)) {
                if (validationStatus[validation] !== 'success') {
                    return false;
                }
            }
        }
        return true;
    }
}

export default withRouter(injectIntl(AnnouncementStepWizard));