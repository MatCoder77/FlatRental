import React from "react";
import { IntlProvider, addLocaleData } from "react-intl";
import locale_en from 'react-intl/locale-data/en';
import locale_pl from 'react-intl/locale-data/pl';
import messages_pl from "../translations/pl.json";
import messages_en from "../translations/en.json";
import moment from "moment";
import 'moment/locale/pl'

addLocaleData([...locale_en, ...locale_pl]);

const defaultLocale = 'pl';

const messages = {
    'pl': messages_pl,
    'en': messages_en
};

moment.locale(defaultLocale);

const Context = React.createContext(undefined);

class LanguageHandler extends React.Component {
    constructor(...props) {
        super(...props);

        this.onLanguageChange = this.onLanguageChange.bind(this);

        this.state = {
            locale: defaultLocale,
            messages: messages[defaultLocale]
        };
    }

    onLanguageChange(language) {
        this.setState({
            locale: language,
            messages: messages[language]
        });
        moment.locale(language);
    }

    render() {
        const { children } = this.props;
        const { locale, messages } = this.state;
        return (
            <Context.Provider value={this.onLanguageChange}>
                <IntlProvider
                    key={locale}
                    locale={locale}
                    messages={messages}
                    defaultLocale={defaultLocale}
                >
                    {children}
                </IntlProvider>
            </Context.Provider>
        );
    }
}

export { LanguageHandler, Context as IntlContext };
