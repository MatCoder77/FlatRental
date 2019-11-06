import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './infrastructure/ServiceWorkerRegistration';
import { BrowserRouter as Router } from 'react-router-dom';
import {LanguageHandler} from "./infrastructure/LanguageHandler";
// import {IntlProvider} from "react-intl";
// import { addLocaleData } from "react-intl";
// import locale_en from 'react-intl/locale-data/en';
// import locale_pl from 'react-intl/locale-data/pl';
// import messages_pl from "./translations/pl.json";
// import messages_en from "./translations/en.json";
// import moment from "moment";
// import 'moment/locale/pl'
//
// addLocaleData([...locale_en, ...locale_pl]);
// const messages = {
//     'pl': messages_pl,
//     'en': messages_en
// };
//
// moment.locale('pl');
// //const language = navigator.language.split(/[-_]/)[0];  // language without region code

ReactDOM.render(
    <LanguageHandler>
        <Router>
            <App />
        </Router>
    </LanguageHandler>,
    document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
registerServiceWorker();
