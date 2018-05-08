package Cars;

abstract class Car {
    protected double maxSpeed;
    protected double power;
    protected double fuelConsumption;
    protected String color;
    protected String manufacturerCountry;

    public Car(double maxSpeed, double power, double fuelConsumption, String color, String manufacturerCountry) {
        this.maxSpeed = maxSpeed;
        this.power = power;
        this.fuelConsumption = fuelConsumption;
        this.color = color;
        this.manufacturerCountry = manufacturerCountry;
    }

    public Car() {
        
    }

    //Setters
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    public void setPower(double power) {
        this.power = power;
    }
    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setManufacturerCountry(String manufacturerCountry) {
        this.manufacturerCountry = manufacturerCountry;
    }

    public void printSpecifications() {
        System.out.println("Maximal Speed: " + maxSpeed + " km/h");
        System.out.println("Power: " + power + " h.p");
        System.out.println("Fuel Consumption: " + fuelConsumption + " L/100 km");
        System.out.println("Color: " + color + " ");
        System.out.println("Manufacturer Country: " + manufacturerCountry);
    }
}