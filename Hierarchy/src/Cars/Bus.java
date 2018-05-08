package Cars;

public class Bus extends Car {
    private int routeNumber;

    public Bus(double maxSpeed, double power, double fuelConsumption, String color, String manufacturerCountry, int routeNumber) {
        super(maxSpeed, power, fuelConsumption, color, manufacturerCountry);
        this.routeNumber = routeNumber;
    }

    public Bus() {

    }

    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }

    @Override
    public void printSpecifications() {
        System.out.println("\ud83d\ude8c " + getClass());
        super.printSpecifications();
        System.out.println("Route Number: " + routeNumber);
        System.out.println();
    }
}
