class PassengerWagon extends Wagon {
    private int seats;
    private int occupied;
    private double pricePerKm;
    private boolean canLie;

    public PassengerWagon(int number, String type, int seats,
                          double pricePerKm, boolean tv, boolean phone, boolean canLie) {
        super(number, type, tv, phone);
        this.seats = seats;
        this.occupied = 0;
        this.pricePerKm = pricePerKm;
        this.canLie = canLie;
    }

    public int getSeats() {
        return seats;
    }

    public int getOccupied() {
        return occupied;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }

    public boolean isCanLie() {
        return canLie;
    }

    public boolean hasFreeSeat() {
        return occupied < seats;
    }

    public void takeSeat() {
        if (occupied < seats) {
            occupied++;
        }
    }

    public double loadPercent() {
        if (seats == 0) {
            return 0;
        }
        return occupied * 100.0 / seats;
    }

    public double calculatePrice(double distance) {
        double price = distance * pricePerKm;
        if (isTv()) {
            price += 80;
        }
        if (isPhone()) {
            price += 50;
        }
        return price;
    }

    public String info() {
        return super.info() + " мест: " + occupied + "/" + seats;
    }
}