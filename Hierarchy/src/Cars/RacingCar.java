package Cars;

public class RacingCar extends Car {
    private String sponsor;

    public RacingCar(double maxSpeed, double power, double fuelConsumption, String color, String manufacturerCountry, String sponsor) {
        super(maxSpeed, power, fuelConsumption, color, manufacturerCountry);
        this.sponsor = sponsor;
    }

    public RacingCar() {

    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    @Override
    public void printSpecifications() {
        System.out.println("\ud83d\ude97 " + getClass());
        super.printSpecifications();
        System.out.println("Sponsor: " + sponsor);
    }
}
