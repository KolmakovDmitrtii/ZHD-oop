class CoupeWagon extends PassengerWagon {
    private double bedPrice;

    public CoupeWagon(int number, int seats, double pricePerKm,
                      boolean tv, boolean phone, double bedPrice) {
        super(number, "Купейный", seats, pricePerKm, tv, phone, true);
        this.bedPrice = bedPrice;
    }

    public double getBedPrice() {
        return bedPrice;
    }

    public double calculatePrice(double distance) {
        return super.calculatePrice(distance) + bedPrice;
    }
}