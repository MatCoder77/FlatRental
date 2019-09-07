import React from 'react';
import { Spin, Icon } from 'antd';

export default function LoadingIcon(props) {
    const antIcon = <Icon type="loading-3-quarters" style={{ fontSize: 30 }} spin />;
    return (
        <Spin indicator={antIcon} style = {{display: 'block', textAlign: 'center', marginTop: 30}} />
    );
}