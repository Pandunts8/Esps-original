package com.example.esps;

import android.os.Parcel;
import android.os.Parcelable;

public class Company implements Parcelable {
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
    private double energyPrice;
    private double solarPower;
    private double production;
    private double unitCost;
    private double maintenanceCost;

    // Empty constructor needed for Firebase
    public Company() {
    }

    public Company(String id, String name, String address, double power, double productivity, int licensePeriod,
                   double powerPurchaseTariff, String technicalSpecification, String insurance, String imageUrl,
                   double energyPrice, double solarPower, double production, double unitCost, double maintenanceCost) {
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
        this.energyPrice = energyPrice;
        this.solarPower = solarPower;
        this.production = production;
        this.unitCost = unitCost;
        this.maintenanceCost = maintenanceCost;
    }

    protected Company(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        power = in.readDouble();
        productivity = in.readDouble();
        licensePeriod = in.readInt();
        powerPurchaseTariff = in.readDouble();
        technicalSpecification = in.readString();
        insurance = in.readString();
        imageUrl = in.readString();
        energyPrice = in.readDouble(); // Read the new field
        production = in.readDouble(); // Read the new field
        unitCost = in.readDouble(); // Read the new field
        maintenanceCost = in.readDouble(); // Read the new field
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(power);
        dest.writeDouble(productivity);
        dest.writeInt(licensePeriod);
        dest.writeDouble(powerPurchaseTariff);
        dest.writeString(technicalSpecification);
        dest.writeString(insurance);
        dest.writeString(imageUrl);
        dest.writeDouble(energyPrice); // Write the new field
        dest.writeDouble(production); // Write the new field
        dest.writeDouble(unitCost); // Write the new field
        dest.writeDouble(maintenanceCost); // Write the new field
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    // Getters and setters for the new fields
    public double getEnergyPrice() { return energyPrice; }
    public void setEnergyPrice(double energyPrice) { this.energyPrice = energyPrice; }

    public double getProduction() { return production; }
    public void setProduction(double production) { this.production = production; }

    public double getUnitCost() { return unitCost; }
    public void setUnitCost(double unitCost) { this.unitCost = unitCost; }

    public double getMaintenanceCost() { return maintenanceCost; }
    public void setMaintenanceCost(double maintenanceCost) { this.maintenanceCost = maintenanceCost; }
    public double getSolarPower() { return solarPower; }
    public void setSolarPower(double solarPower) { this.solarPower = solarPower; }

    // Getters for the existing fields
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

    // Setters for the existing fields
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
