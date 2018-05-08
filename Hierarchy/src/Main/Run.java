package Main;

import Cars.*;

public class Run {
    public static void main(String[] args) {
        Bus myBus = new Bus(100, 100, 15, "Black", "Russia", 12);
        myBus.printSpecifications();
        RacingCar thisCar = new RacingCar(300, 200, 10, "Red", "USA", "RedBull");
        thisCar.printSpecifications();

        System.out.println("\n>>>>> Warning!!! Sponsor is changed\n");

        thisCar.setSponsor("Adrenaline");
        thisCar.printSpecifications();
    }
}
