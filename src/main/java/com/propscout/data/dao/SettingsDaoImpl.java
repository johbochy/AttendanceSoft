package com.propscout.data.dao;

import com.propscout.data.models.Settings;

import java.util.Optional;

public class SettingsDaoImpl implements SettingsDao {
    @Override
    public Settings save(Settings settings) {

        String sql = "INSERT INTO settings(commence_date, end_date, semester) VALUES(?, ?, ?)";

        return null;
    }

    @Override
    public Settings update(Settings settings) {
        return null;
    }

    @Override
    public Optional<Settings> find(int id) {
        return Optional.empty();
    }
}
