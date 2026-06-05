class Ticket {
    private Passenger passenger;
    private Train train;
    private PassengerWagon wagon;
    private double price;
    private Station stationFrom;
    private Station stationTo;
    private int date;
    private double distance;

    public Ticket(Passenger passenger, Train train, PassengerWagon wagon,
                  double price, Station stationFrom, Station stationTo,
                  int date, double distance) {
        this.passenger = passenger;
        this.train = train;
        this.wagon = wagon;
        this.price = price;
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.date = date;
        this.distance = distance;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Train getTrain() {
        return train;
    }

    public PassengerWagon getWagon() {
        return wagon;
    }

    public double getPrice() {
        return price;
    }

    public Station getStationFrom() {
        return stationFrom;
    }

    public Station getStationTo() {
        return stationTo;
    }

    public int getDate() {
        return date;
    }

    public double getDistance() {
        return distance;
    }
}