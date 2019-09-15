import React, { Component } from "react";
import { Upload, Icon, Modal } from 'antd';
import './ImageGalleryUploader.css';
import {getUploadFilePromise, uploadFile} from "../infrastructure/RestApiHandler";
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
    let response = uploadFile(file);
    setTimeout(() => {
        onSuccess(response);
    }, 0);
};

class ImageGalleryUploader extends Component {
    state = {
        previewVisible: false,
        previewImage: '',
        fileList: [],
        uidToNameMap: []
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
        console.log(change);
        this.setState({ fileList: change.fileList });
    }

    render() {
        const { previewVisible, previewImage, fileList } = this.state;
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
                    fileList={fileList}
                    onPreview={this.handlePreview}
                    onChange={this.handleChange}
                >
                    {fileList.length >= 10 ? null : uploadButton}
                </Upload>
                <Modal visible={previewVisible} footer={null} onCancel={this.handleCancel}>
                    <img alt="example" style={{ width: '100%' }} src={previewImage} />
                </Modal>
            </div>
        );
    }
}

export default ImageGalleryUploader;