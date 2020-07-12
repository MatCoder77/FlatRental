package com.flatrental.domain.locations.teryt.terc;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String, Date> {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date unmarshal(String xml) throws Exception {
        return dateFormat.parse(xml);
    }

    @Override
    public String marshal(Date date) {
        return dateFormat.format(date);
    }

}
