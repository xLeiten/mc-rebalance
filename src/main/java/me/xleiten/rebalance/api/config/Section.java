package me.xleiten.rebalance.api.config;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class Section extends DynamicStorageEntry
{
    final ConcurrentHashMap<String, DynamicStorageEntry> entries = new ConcurrentHashMap<>();

    Section(DynamicStorage config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    public <T> Option<T> option(@NotNull String path, @NotNull T defaultValue) {
        var elements = getPath(path);
        var entryKey = elements[elements.length - 1];
        Option<T> option;
        if (elements.length > 1) {
            option = section(path.substring(0, path.lastIndexOf("."))).option(entryKey, defaultValue);
        } else {
            var entry = entries.get(entryKey);
            if (entry != null) {
                if (!(entry instanceof Option<?> option1))
                    throw new IllegalArgumentException("Attempt to create an option with key '" + entryKey + "', which is already used by non option entry");

                if (defaultValue.getClass() == option1.getValue().getClass()) {
                    option1.validate();
                    return (Option<T>) option1;
                } else {
                    if (config.isStrictMode())
                        throw new IllegalStateException("Attempt to create an already existing option but with with different value type.");
                }
            }
            option = new Option<>(entryKey, defaultValue, config);
            entries.put(entryKey, option);
        }
        option.validate();
        return option;
    }

    public Section section(@NotNull String path) {
        var elements = getPath(path);
        var entryName = elements[0];
        var entry = entries.get(entryName);

        Section section;
        if (entry != null) {
            if (!(entry instanceof Section sectionEntry))
                throw new IllegalArgumentException("Attempt to create a section with key '" + entryName + "', which is already used by non section entry");

            section = sectionEntry;
        } else {
            section = new Section(config);
            entries.put(entryName, section);
        }
        section.validate();
        return elements.length > 1 ? section.section(path.substring(path.indexOf(".") + 1)) : section;
    }

    @Override
    public void copyFrom(DynamicStorageEntry source) {
        if (source instanceof Section section) {
            for (Map.Entry<String, DynamicStorageEntry> entry : section.entries.entrySet()) {
                var currEntry = entries.get(entry.getKey());
                var newEntry = entry.getValue();
                if (currEntry != null)
                    currEntry.copyFrom(newEntry);
                else
                    entries.put(entry.getKey(), newEntry);
            }
        } else
            config.logger.warn("Attempt to copy data to section from non section config entry. Skipping...");
    }

    public int size() {
        return entries.size();
    }

    public ImmutableSet<Map.Entry<String, DynamicStorageEntry>> getEntries() {
        return ImmutableSet.copyOf(entries.entrySet());
    }

    public boolean isEmpty(boolean deepSearch) {
        if (size() > 0) {
            var result = false;
            for (Map.Entry<String, DynamicStorageEntry> entry : entries.entrySet()) {
                var ent = entry.getValue();
                if (ent instanceof Section section && deepSearch)
                    result = section.isEmpty(true);
                else {
                    if (ent instanceof Option<?>)
                        return false;
                }
            }
            return result;
        }
        return true;
    }

    public void clearUnusedEntries(boolean deepSearch) {
        entries.entrySet().removeIf(entry -> {
            var configEntry = entry.getValue();
            if (configEntry instanceof Section section) {
                if (deepSearch) section.clearUnusedEntries(true);
                return section.isEmpty(true);
            } else {
                if (configEntry instanceof Option<?> option) {
                    return !option.isValidated();
                } else
                    return true;
            }
        });
    }

    public void clearEmptySections() {
        entries.entrySet().removeIf(entry -> entry.getValue() instanceof Section section && section.isEmpty(true));
    }

    private String[] getPath(@NotNull String path) {
        if (!path.isEmpty()) {
            var elements = path.split("\\.");
            if (elements.length > 0)
                return elements;
            else
                throw new IllegalArgumentException("Path to config entry cannot be empty");
        } else
            throw new IllegalArgumentException("Config entry name cannot be empty");
    }
}
