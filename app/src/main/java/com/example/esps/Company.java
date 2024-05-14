package com.example.esps;

public class Company {
    private String id;
    private String name;
    private String address;
    private double power;
    private double productivity;
    private int licensePeriod;
    private double powerPurchaseTariff;
    private String technicalSpecification;
    private String insurance;
    private String imageUrl;

    // No-argument constructor required for Firebase
    public Company() {
        // Default constructor required for calls to DataSnapshot.getValue(Company.class)
    }

    // Constructor for initializing a new Company instance
    public Company(String id, String name, String address, double power, double productivity,
                   int licensePeriod, double powerPurchaseTariff, String technicalSpecification,
                   String insurance, String imageUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.power = power;
        this.productivity = productivity;
        this.licensePeriod = licensePeriod;
        this.powerPurchaseTariff = powerPurchaseTariff;
        this.technicalSpecification = technicalSpecification;
        this.insurance = insurance;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public double getPower() { return power; }
    public double getProductivity() { return productivity; }
    public int getLicensePeriod() { return licensePeriod; }
    public double getPowerPurchaseTariff() { return powerPurchaseTariff; }
    public String getTechnicalSpecification() { return technicalSpecification; }
    public String getInsurance() { return insurance; }
    public String getImageUrl() { return imageUrl; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPower(double power) { this.power = power; }
    public void setProductivity(double productivity) { this.productivity = productivity; }
    public void setLicensePeriod(int licensePeriod) { this.licensePeriod = licensePeriod; }
    public void setPowerPurchaseTariff(double powerPurchaseTariff) { this.powerPurchaseTariff = powerPurchaseTariff; }
    public void setTechnicalSpecification(String technicalSpecification) { this.technicalSpecification = technicalSpecification; }
    public void setInsurance(String insurance) { this.insurance = insurance; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
