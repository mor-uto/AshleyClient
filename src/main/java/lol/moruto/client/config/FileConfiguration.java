package lol.moruto.mod.config;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileConfiguration {
    private final Path path;
    private final JsonObject root;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public FileConfiguration(Path path) {
        this.path = path;
        this.root = loadOrCreate();
    }

    private JsonObject loadOrCreate() {
        try {
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path)) {
                    return JsonParser.parseReader(reader).getAsJsonObject();
                }
            }
        } catch (Exception ignored) {}
        return new JsonObject();
    }

    public boolean contains(String key) {
        return getElement(key) != null;
    }

    public Object get(String key) {
        JsonElement e = getElement(key);
        if (e == null) return null;
        if (e.isJsonPrimitive()) {
            JsonPrimitive p = e.getAsJsonPrimitive();
            if (p.isBoolean()) return p.getAsBoolean();
            if (p.isNumber()) return p.getAsDouble();
            if (p.isString()) return p.getAsString();
        }
        if (e.isJsonArray()) {
            List<String> list = new ArrayList<>();
            e.getAsJsonArray().forEach(v -> list.add(v.getAsString()));
            return list;
        }
        return e;
    }

    public String getString(String key) {
        Object val = get(key);
        return val == null ? null : val.toString();
    }

    public int getInt(String key) {
        Object val = get(key);
        return val instanceof Number ? ((Number) val).intValue() : 0;
    }

    public boolean getBoolean(String key) {
        Object val = get(key);
        return val instanceof Boolean ? (Boolean) val : false;
    }

    public double getDouble(String key) {
        Object val = get(key);
        return val instanceof Number ? ((Number) val).doubleValue() : 0.0;
    }

    public long getLong(String key) {
        Object val = get(key);
        return val instanceof Number ? ((Number) val).longValue() : 0L;
    }

    @SuppressWarnings("unchecked")
    public List<String> getList(String key) {
        Object val = get(key);
        return val instanceof List ? (List<String>) val : List.of();
    }

    public void set(String key, Object value) {
        String[] parts = key.split("\\.");
        JsonObject current = root;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];

            if (!current.has(part) || !current.get(part).isJsonObject()) {
                JsonObject newObj = new JsonObject();
                current.add(part, newObj);
                current = newObj;
            } else {
                current = current.getAsJsonObject(part);
            }
        }

        String lastPart = parts[parts.length - 1];

        switch (value) {
            case Number number -> current.addProperty(lastPart, number);
            case Boolean b -> current.addProperty(lastPart, b);
            case Character c -> current.addProperty(lastPart, c);
            case null, default -> current.addProperty(lastPart, String.valueOf(value));
        }
    }

    public void save() {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(root, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonElement getElement(String key) {
        String[] parts = key.split("\\.");
        JsonElement current = root;
        for (String part : parts) {
            if (!(current instanceof JsonObject obj) || !obj.has(part)) return null;
            current = obj.get(part);
        }
        return current;
    }
}
