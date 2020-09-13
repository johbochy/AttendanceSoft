package com.propscout.data.dao;

import com.propscout.data.models.Settings;

import java.util.Optional;

public interface SettingsDao {

    Settings save(Settings settings);

    Settings update(Settings settings);

    Optional<Settings> find(int id);

}
