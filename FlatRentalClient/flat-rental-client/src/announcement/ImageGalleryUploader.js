import React, { Component } from "react";
import { Upload, Icon, Modal } from 'antd';
import './ImageGalleryUploader.css';
import {uploadFile} from "../infrastructure/RestApiHandler";
import {API_BASE_URL} from "../Constants";

function getBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });
}

const dummyRequest = ({ file, onSuccess }) => {
    let promise = uploadFile(file);
    setTimeout(() => {
        onSuccess(promise);
    }, 0);
};

class ImageGalleryUploader extends Component {
    state = {
        previewVisible: false,
        previewImage: '',
    };

    handleCancel = () => this.setState({ previewVisible: false });

    handlePreview = async file => {
        if (!file.url && !file.preview) {
            file.preview = await getBase64(file.originFileObj);
        }

        this.setState({
            previewImage: file.url || file.preview,
            previewVisible: true,
        });
    };

    handleChange = (change) => {
        this.props.onUpdate(this.props.transientDataName, change.fileList);
        this.updateFilenameList(change.fileList);
    }

    updateFilenameList(fileList) {
        let promiseList = fileList.map(file => file.response);
        if(!promiseList) {
            return;
        }
        Promise.all(promiseList)
            .then(values => {
                let filenameList = values.map(value => value.fileName)
                this.props.onUpdate(this.props.name, filenameList);
            }).catch(error => {});
    }

    render() {
        const { previewVisible, previewImage } = this.state;
        const uploadButton = (
            <div>
                <Icon type="plus" />
                <div className="ant-upload-text">Upload</div>
            </div>
        );
        return (
            <div className="clearfix">
                <Upload
                    customRequest={dummyRequest}
                    listType="picture-card"
                    fileList={this.props.fileList ? this.props.fileList : []}
                    onPreview={this.handlePreview}
                    onChange={this.handleChange}
                >
                    {(this.props.fileList ? this.props.fileList.length : 0) >= 10 ? null : uploadButton}
                </Upload>
                <Modal visible={previewVisible} footer={null} onCancel={this.handleCancel}>
                    <img alt="Image" style={{ width: '100%' }} src={previewImage} />
                </Modal>
            </div>
        );
    }
}

export default ImageGalleryUploader;