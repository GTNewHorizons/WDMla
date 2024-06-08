package mcp.mobius.waila.api.impl;

import mcp.mobius.waila.api.BackwardCompatibility;

import java.util.HashMap;

@BackwardCompatibility
public class ConfigModule {

    String modName;
    HashMap<String, String> options;

    public ConfigModule(String _modName) {
        this.modName = _modName;
        this.options = new HashMap<>();
    }

    public ConfigModule(String _modName, HashMap<String, String> _options) {
        this.modName = _modName;
        this.options = _options;
    }

    public void addOption(String key, String name) {
        this.options.put(key, name);
    }

}
