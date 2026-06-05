class SittingWagon extends PassengerWagon {
    public SittingWagon(int number, int seats, double pricePerKm, boolean tv, boolean phone) {
        super(number, "Сидячий", seats, pricePerKm, tv, phone, false);
    }
}