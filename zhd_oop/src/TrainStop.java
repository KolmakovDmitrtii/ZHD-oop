class TrainStop {
    private Station station;
    private int arrivalTime;
    private int departureTime;

    public TrainStop(Station station, int arrivalTime, int departureTime) {
        this.station = station;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public Station getStation() {
        return station;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getDepartureTime() {
        return departureTime;
    }
}