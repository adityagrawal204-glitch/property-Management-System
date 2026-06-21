package com.example.propertymgmt.model;

/**
 * Tracks whether a Unit currently has a tenant.
 * Using an enum (instead of a raw String) means the database and Java code
 * can only ever hold one of these two valid values - no typos like "Vacnt".
 */
public enum UnitStatus {
    VACANT,
    OCCUPIED
}
