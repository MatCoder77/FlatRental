import React, { Component } from 'react';
import {Steps, Form, Input, Button, notification, InputNumber, Row, Col, DatePicker, Checkbox } from 'antd';
import * as CONS from "../Constants";
import {Link} from "react-router-dom";
import './Step.css';
import ComboBox from "../commons/ComboBox";
import Text from "antd/lib/typography/Text";
import moment from "moment";
import {getBuildingTypes} from "../infrastructure/RestApiHandler";
import FirstStepContainer from "./FirstStepContainer";
import ThirdStepContainer from "./ThirdStepContainer";
import SecondStepContainer from "./SecondStepContainer";
import {FormattedMessage, injectIntl} from "react-intl";

const FormItem = Form.Item;
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

const today = moment(new Date())



class CreateAnnouncementStepWizard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            current: 0,
            formData: {},
            appData: {},
            // announcementTitle: null,
            // totalArea: null,
            // numberOfRooms: null,
            // pricePerMonth: null,
            // estimatedAdditionalCosts: null,
            // deposit: null,
            // floorNumber: null,
            // maxFloorNumber: null,
            // availableFrom: null,
        };
        this.updateFormData = this.updateFormData.bind(this);
        this.updateFormData('availableFrom', today);
        this.updateFormData('bathroom.numberOfBathrooms', 1);

        this.loadData = this.loadData.bind(this);
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

    updateFormData(fieldName, fieldValue) {
        const { formData } = this.state;
        formData[fieldName] = fieldValue;
        this.setState({ formData });
    }

    // updateAppData(fieldName, fieldValue) {
    //     const { appData } = this.state;
    //     appData[fieldName] = fieldValue;
    //     this.setState( { appData });
    // }

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

    render() {
        const steps = [{
            title: 'General Information',
            content: (
                <FirstStepContainer formData={this.state.formData} onUpdate={this.updateFormData} {...formItemLayout}/>
            ),
        }, {
            title: 'Localization',
            content: (
                <SecondStepContainer formData={this.state.formData} onUpdate={this.updateFormData} loadData={this.loadData} appData={this.state.appData} {...formItemLayout}/>
            ),
        }, {
            title: 'Detail Information',
            content: (
                <ThirdStepContainer formData={this.state.formData} onUpdate={this.updateFormData} loadData={this.loadData} appData={this.state.appData} {...formItemLayout}/>
            ),
        },{
            title: 'Summary',
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
                    {current > 0 && (
                        <Button style={{ marginLeft: 8 }} onClick={() => this.prev()}>
                            Previous
                        </Button>
                    )}
                    {current < steps.length - 1 && (
                        <Button type="primary" onClick={() => this.next()}>
                            Next
                        </Button>
                    )}
                    {current === steps.length - 1 && (
                        <Button type="primary">
                            Done
                        </Button>
                    )}

                </div>
            </div>
        );
    }
}

export default injectIntl(CreateAnnouncementStepWizard);