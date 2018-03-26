package com.atg.openssp.dataprovider.provider.dto;

public enum MaintenanceCommand {
    ADD(1), REMOVE(2), UPDATE(3);

    private final int value;

    MaintenanceCommand(int value) {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static MaintenanceCommand fromValue(int value) {
        for (final MaintenanceCommand v : values()) {
            if (v.getValue() == value) {
                return v;
            }
        }
        return null;
    }

}
