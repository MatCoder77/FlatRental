import * as CONS from "./Constants"


export function unflatten(data) {
    "use strict";
    if (Object(data) !== data || Array.isArray(data))
        return data;
    let result = {}, cur, prop, idx, last, temp;
    for(let p in data) {
        cur = result;
        prop = "";
        last = 0;
        do {
            idx = p.indexOf(".", last);
            temp = p.substring(last, idx !== -1 ? idx : undefined);
            cur = cur[prop] || (cur[prop] = (!isNaN(parseInt(temp)) ? [] : {}));
            prop = temp;
            last = idx + 1;
        } while(idx >= 0);
        cur[prop] = data[p];
    }
    return result[""];
}

export function flatten(data) {
    let result = {};
    function recurse (cur, prop) {
        if (Object(cur) !== cur) {
            if (prop === "") {
                result = cur;
            } else {
                result[prop] = cur;
            }
        } else if (Array.isArray(cur)) {
            if(prop === "") {
                result = cur.map(element => flatten(element));
            } else {
                result[prop] = cur.map(element => flatten(element));
            }
            // for(var i=0, l=cur.length; i<l; i++)
            //     recurse(cur[i], "");
            // if (l == 0)
            //     result[prop] = [];
        } else {
            let isEmpty = true;
            for (let p in cur) {
                isEmpty = false;
                recurse(cur[p], prop ? prop+"."+p : p);
            }
            if (isEmpty)
                result[prop] = {};
        }
    }
    recurse(data, "");
    return result;
}

export function excludeTransientProperties (data) {
    let copy = {};
    for (let property in data) {
        if (!property.startsWith(CONS.TRANSIENT_MARKER)) {
            copy[property] = data[property];
        }
    }
    return copy;
}
