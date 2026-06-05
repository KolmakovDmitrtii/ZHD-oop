class PlatzkartWagon extends PassengerWagon {
    public PlatzkartWagon(int number, int seats, double pricePerKm, boolean tv, boolean phone) {
        super(number, "Плацкартный", seats, pricePerKm, tv, phone, true);
    }
}