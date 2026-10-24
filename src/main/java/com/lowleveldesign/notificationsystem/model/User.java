package com.lowleveldesign.notificationsystem.model;

public class User {
    private final String id;
    private final String name;
    private final String email;       // may be null
    private final String phone;       // may be null
    private final String deviceToken; // may be null
    private final UserPreference preference = new UserPreference();

    public User(String id, String name, String email, String phone, String deviceToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deviceToken = deviceToken;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDeviceToken() { return deviceToken; }
    public UserPreference getPreference() { return preference; }
}
