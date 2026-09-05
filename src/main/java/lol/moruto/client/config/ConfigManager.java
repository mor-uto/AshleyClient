package lol.moruto.client.config;

import java.lang.reflect.*;
import java.util.List;

public class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(FileConfiguration config) {
        this.config = config;
    }

    public void loadConfig(Object instance) {
        Class<?> clazz = instance.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigEntry.class)) {
                ConfigEntry entry = field.getAnnotation(ConfigEntry.class);
                String path = entry.value();

                try {
                    field.setAccessible(true);
                    boolean isStatic = Modifier.isStatic(field.getModifiers());
                    Object defaultValue = field.get(isStatic ? null : instance);

                    if (!config.contains(path)) {
                        config.set(path, defaultValue);
                        config.save();
                    }

                    Object value = getConfigValue(path, field.getType());
                    field.set(isStatic ? null : instance, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object getConfigValue(String path, Class<?> type) {
        if (type == String.class) return config.getString(path);
        if (type == int.class || type == Integer.class) return config.getInt(path);
        if (type == boolean.class || type == Boolean.class) return config.getBoolean(path);
        if (type == double.class || type == Double.class) return config.getDouble(path);
        if (type == long.class || type == Long.class) return config.getLong(path);
        if (type == List.class) return config.getList(path);
        return config.get(path);
    }
}
