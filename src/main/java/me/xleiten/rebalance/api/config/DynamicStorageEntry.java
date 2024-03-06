package me.xleiten.rebalance.api.config;

public abstract class DynamicStorageEntry
{
    public final DynamicStorage config;
    private boolean validated = false;

    DynamicStorageEntry(DynamicStorage config) {
        this.config = config;
    }

    public void validate() {
        validated = true;
    }

    public boolean isValidated() {
        return validated;
    }

    abstract void copyFrom(DynamicStorageEntry source);

}
