import React, { Component } from 'react';
import {Steps, Button, Row, Col, Icon} from 'antd';
import './Step.css';
import moment from "moment";
import {createAnnouncement} from "../infrastructure/RestApiHandler";
import FirstStepContainer from "./FirstStepContainer";
import ThirdStepContainer from "./ThirdStepContainer";
import SecondStepContainer from "./SecondStepContainer";
import {FormattedMessage, injectIntl} from "react-intl";
import * as DTOUtils from "../infrastructure/DTOUtils";
import "./AnnouncementStepWizard.css"

const Step = Steps.Step;

const formItemLayout = {
    labelCol: {
        xs: { span: 12 },
        sm: { span: 4 },
    },
    wrapperCol: {
        xs: { span: 28 },
        sm: { span: 18 },
    },
};

class AnnouncementStepWizard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            current: 0,
            formData: {},
            appData: {},
            validationStatus: {},
            errorMessages: {}
        };
        this.updateFormData = this.updateFormData.bind(this);
        this.updateFormData('availableFrom', moment(new Date()));
        this.updateFormData('bathroom.numberOfBathrooms', 1);

        this.loadData = this.loadData.bind(this);
        this.submitAnnouncement = this.submitAnnouncement.bind(this);
        this.updateValidation = this.updateValidation.bind(this);
        this.getValidationStatus = this.getValidationStatus.bind(this);
        this.getErrorMessage = this.getErrorMessage.bind(this);
    }

    next() {
        const current = this.state.current + 1;
        console.log(this.state.formData);
        this.setState({ current });
    }

    prev() {
        const current = this.state.current - 1;
        this.setState({ current });
    }

    updateFormData(fieldName, fieldValue, validationResult) {
        const { formData } = this.state;
        formData[fieldName] = fieldValue;
        this.setState({ formData });

        this.updateValidation(fieldName, validationResult)
    }

    updateValidation(validationName, validationResult) {
        const {validationStatus} = this.state;
        const {errorMessages} = this.state;
        const currentStep = this.state.current.toString();
        validationStatus[currentStep + validationName] = validationResult ? validationResult.validateStatus : undefined;
        errorMessages[currentStep + validationName] = validationResult ? validationResult.errorMsg : undefined;
        this.setState({validationStatus});
        this.setState({errorMessages});
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

    loadData(supplierFunction, fieldName, param) {
        let promise = supplierFunction(param);

        if(!promise) {
            return;
        }

        // this.setState({
        //     isLoading: true
        // });

        promise
            .then(response => {
                const { appData } = this.state;
                appData[fieldName] = response;
                this.setState({
                    appData
                    // isLoading: false
                })
            }).catch(error => {
            // this.setState({
            //     isLoading: false
            // })
        });
    }

    submitAnnouncement() {
        let filteredFormData = DTOUtils.excludeTransientProperties(this.state.formData);
        let announcementDTO = DTOUtils.unflatten(filteredFormData);
        let promise = createAnnouncement(announcementDTO);
    }

    render() {
        const { intl } = this.props;
        const steps = [{
            title: intl.formatMessage({ id: "labels.general_info" }),
            content: (
                <FirstStepContainer formData={this.state.formData} onUpdate={this.updateFormData} {...formItemLayout} getValidationStatus={this.getValidationStatus} getErrorMessage={this.getErrorMessage}/>
            ),
        }, {
            title: intl.formatMessage({ id: "labels.localization" }),
            content: (
                <SecondStepContainer formData={this.state.formData} onUpdate={this.updateFormData} loadData={this.loadData} appData={this.state.appData} {...formItemLayout} getValidationStatus={this.getValidationStatus} getErrorMessage={this.getErrorMessage}/>
            ),
        }, {
            title: intl.formatMessage({ id: "labels.detail_info" }),
            content: (
                <ThirdStepContainer formData={this.state.formData} onUpdate={this.updateFormData} loadData={this.loadData} appData={this.state.appData} {...formItemLayout} getValidationStatus={this.getValidationStatus} getErrorMessage={this.getErrorMessage}/>
            ),
        },{
            title: intl.formatMessage({ id: "labels.summary" }),
            content: 'Last-content',
        }];

        const { current } = this.state;
        return (
            <div className="step-wizard-container">
            <Steps current={current}>
                {steps.map(item => <Step key={item.title} title={item.title} />)}
            </Steps>
            <div className="steps-content">{steps[current].content}</div>
                <div className="steps-action">
                    <Row type="flex" justify="space-around">

                    {current > 0 && (
                        <Col span={10}>
                        <Button className="step-wizard-button" onClick={() => this.prev()} size="large">
                            <Icon type="left" />
                            <FormattedMessage id="labels.previous"/>
                        </Button>
                        </Col>
                    )}
                    {current < steps.length - 1 && (
                        <Col span={10}>
                        <Button className="step-wizard-button" type="primary" onClick={() => this.next()} size="large" disabled={!this.isStepValid()}>
                            <FormattedMessage id="labels.next"/>
                            <Icon type="right" />
                        </Button>
                        </Col>
                    )}
                    {current === steps.length - 1 && (
                        <Col span={10}>
                        <Button className="step-wizard-button" type="primary" onClick={this.submitAnnouncement} size="large">
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
        for(let validation in validationStatus) {
            if(Object.prototype.hasOwnProperty.call(validationStatus, validation) && validation.startsWith(this.props.current)) {
                if(validationStatus[validation] !== 'success') {
                    return false;
                }
            }
        }
        return true;
    }
}

export default injectIntl(AnnouncementStepWizard);