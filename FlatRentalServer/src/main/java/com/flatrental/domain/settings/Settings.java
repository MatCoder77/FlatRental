package com.flatrental.domain.settings;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Settings {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "PROPERTY", nullable = false)
    SettingName settingName;

    String value;


    public Settings(SettingName settingName, String value) {
        this.settingName = settingName;
        this.value = value;
    }

    public SettingName getSettingName() {
        return settingName;
    }

    public String getValue() {
        return value;
    }

    public static Settings createSetting(SettingName settingName, String value) {
        return new Settings(settingName, value);
    }
}
