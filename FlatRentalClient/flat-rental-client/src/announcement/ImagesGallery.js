import BannerAnim from 'rc-banner-anim';
import TweenOne, { TweenOneGroup } from 'rc-tween-one';
import 'rc-banner-anim/assets/index.css';
import './ImagesGallery.css';
import React from "react";
const { Element, Arrow, Thumb } = BannerAnim;
const BgElement = Element.BgElement;
class ImageGallery extends React.Component {
    constructor() {
        super(...arguments);
        this.imgArray = [
            'https://apollo-ireland.akamaized.net/v1/files/eyJmbiI6ImY4NWduODhwaTZmMTEtQVBMIiwidyI6W3siZm4iOiJqMWozbzEzbTZiZ24xLUFQTCIsInMiOiIxNCIsInAiOiIxMCwtMTAiLCJhIjoiMCJ9XX0.HWgV0FPvYSqGPfX9Y3ajrZz7GJuxvuyy4nvJFMEVjZE/image;s=1280x1024;q=80',
            'https://apollo-ireland.akamaized.net/v1/files/eyJmbiI6Im9qbXM1ejZxbHptNzMtQVBMIiwidyI6W3siZm4iOiJqMWozbzEzbTZiZ24xLUFQTCIsInMiOiIxNCIsInAiOiIxMCwtMTAiLCJhIjoiMCJ9XX0.g1-wcn868jiqlQKx2JDgvdrBwdqCdsrIT6Mp-F80EzU/image;s=1280x1024;q=80',
        ];
        this.state = {
            intShow: 0,
            prevEnter: false,
            nextEnter: false,
            thumbEnter: false,
        };
        [
            'onChange',
            'prevEnter',
            'prevLeave',
            'nextEnter',
            'nextLeave',
            'onMouseEnter',
            'onMouseLeave',
        ].forEach((method) => this[method] = this[method].bind(this));
    }

    onChange(type, int) {
        if (type === 'before') {
            this.setState({
                intShow: int,
            });
        }
    }

    getNextPrevNumber() {
        let nextInt = this.state.intShow + 1;
        let prevInt = this.state.intShow - 1;
        if (nextInt >= this.imgArray.length) {
            nextInt = 0;
        }
        if (prevInt < 0) {
            prevInt = this.imgArray.length - 1;
        }

        return [prevInt, nextInt];
    }

    prevEnter() {
        this.setState({
            prevEnter: true,
        });
    }

    prevLeave() {
        this.setState({
            prevEnter: false,
        });
    }

    nextEnter() {
        this.setState({
            nextEnter: true,
        });
    }

    nextLeave() {
        this.setState({
            nextEnter: false,
        });
    }

    onMouseEnter() {
        this.setState({
            thumbEnter: true,
        });
    }

    onMouseLeave() {
        this.setState({
            thumbEnter: false,
        });
    }

    render() {
        const intArray = this.getNextPrevNumber();
        const thumbChildren = this.imgArray.map((img, i) =>
            <span key={i}><i style={{ backgroundImage: `url(${img})` }} /></span>
        );
        return (
            <BannerAnim
                type="acrossOverlay"
                onChange={this.onChange}
                onMouseEnter={this.onMouseEnter}
                onMouseLeave={this.onMouseLeave}
                prefixCls="custom-arrow-thumb"
            >
                <Element key="aaa"
                         prefixCls="banner-user-elem"
                >
                    <BgElement
                        key="bg"
                        className="bg"
                        style={{
                            backgroundImage: `url(${this.imgArray[0]})`,
                            backgroundSize: 'cover',
                            backgroundPosition: 'center',
                        }}
                    />
                </Element>
                <Element key="bbb"
                         prefixCls="banner-user-elem"
                >
                    <BgElement
                        key="bg"
                        className="bg"
                        style={{
                            backgroundImage: `url(${this.imgArray[1]})`,
                            backgroundSize: 'cover',
                            backgroundPosition: 'center',
                        }}
                    />
                </Element>
                <Arrow arrowType="prev" key="prev" prefixCls="user-arrow" component={TweenOne}
                       onMouseEnter={this.prevEnter}
                       onMouseLeave={this.prevLeave}
                       animation={{ left: this.state.prevEnter ? 0 : -120 }}
                >
                    <div className="arrow"></div>
                    <TweenOneGroup
                        enter={{ opacity: 0, type: 'from' }}
                        leave={{ opacity: 0 }}
                        appear={false}
                        className="img-wrapper" component="ul"
                    >
                        <li style={{ backgroundImage: `url(${this.imgArray[intArray[0]]})`}} key={intArray[0]} />
                    </TweenOneGroup>
                </Arrow>
                <Arrow arrowType="next" key="next" prefixCls="user-arrow" component={TweenOne}
                       onMouseEnter={this.nextEnter}
                       onMouseLeave={this.nextLeave}
                       animation={{ right: this.state.nextEnter ? 0 : -120 }}
                >
                    <div className="arrow"></div>
                    <TweenOneGroup
                        enter={{ opacity: 0, type: 'from' }}
                        leave={{ opacity: 0 }}
                        appear={false}
                        className="img-wrapper"
                        component="ul"
                    >
                        <li style={{ backgroundImage: `url(${this.imgArray[intArray[1]]})`}} key={intArray[1]} />
                    </TweenOneGroup>
                </Arrow>
                <Thumb prefixCls="user-thumb" key="thumb" component={TweenOne}
                       animation={{ bottom: this.state.thumbEnter ? 0 : -70 }}
                >
                    {thumbChildren}
                </Thumb>
            </BannerAnim>
        );
    }
}

export default ImageGallery;