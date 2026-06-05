import java.util.ArrayList;

class Train {
    private int number;
    private int date;
    private ArrayList<TrainStop> stops;
    private ArrayList<Wagon> wagons;
    private double revenue;

    public Train(int number, int date) {
        this.number = number;
        this.date = date;
        this.stops = new ArrayList<TrainStop>();
        this.wagons = new ArrayList<Wagon>();
        this.revenue = 0;
    }

    public int getNumber() {
        return number;
    }

    public int getDate() {
        return date;
    }

    public double getRevenue() {
        return revenue;
    }

    public ArrayList<TrainStop> getStops() {
        return stops;
    }

    public ArrayList<Wagon> getWagons() {
        return wagons;
    }

    public void addRevenue(double amount) {
        this.revenue += amount;
    }

    public void addStop(Station station, int arrivalTime, int departureTime) {
        stops.add(new TrainStop(station, arrivalTime, departureTime));
    }

    public void addWagon(Wagon wagon) {
        wagons.add(wagon);
    }

    public Station firstStation() {
        return stops.get(0).getStation();
    }

    public Station lastStation() {
        return stops.get(stops.size() - 1).getStation();
    }

    public int indexOfStation(Station station) {
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).getStation().getName().equals(station.getName())) {
                return i;
            }
        }
        return -1;
    }

    public boolean canCarry(Station from, Station to) {
        int fromIndex = indexOfStation(from);
        int toIndex = indexOfStation(to);
        return fromIndex >= 0 && toIndex >= 0 && fromIndex < toIndex;
    }

    public int departureFrom(Station station) {
        int index = indexOfStation(station);
        if (index == -1) {
            return -1;
        }
        return stops.get(index).getDepartureTime();
    }

    public int arrivalTo(Station station) {
        int index = indexOfStation(station);
        if (index == -1) {
            return -1;
        }
        return stops.get(index).getArrivalTime();
    }

    public double distanceBetween(Station from, Station to) {
        int fromIndex = indexOfStation(from);
        int toIndex = indexOfStation(to);
        if (fromIndex == -1 || toIndex == -1 || fromIndex >= toIndex) {
            return -1;
        }
        double result = 0;
        for (int i = fromIndex; i < toIndex; i++) {
            result += stops.get(i).getStation().distanceTo(stops.get(i + 1).getStation());
        }
        return result;
    }

    public boolean hasRestaurant() {
        for (int i = 0; i < wagons.size(); i++) {
            if (wagons.get(i) instanceof RestaurantWagon) {
                return true;
            }
        }
        return false;
    }

    public boolean hasBuffet() {
        for (int i = 0; i < wagons.size(); i++) {
            if (wagons.get(i) instanceof BuffetWagon) {
                return true;
            }
        }
        return false;
    }

    public int passengerCount() {
        int count = 0;
        for (int i = 0; i < wagons.size(); i++) {
            Wagon w = wagons.get(i);
            if (w instanceof PassengerWagon) {
                count += ((PassengerWagon) w).getOccupied();
            }
        }
        return count;
    }

    public int totalSeats() {
        int count = 0;
        for (int i = 0; i < wagons.size(); i++) {
            Wagon w = wagons.get(i);
            if (w instanceof PassengerWagon) {
                count += ((PassengerWagon) w).getSeats();
            }
        }
        return count;
    }

    public double fullRouteDistance() {
        return distanceBetween(firstStation(), lastStation());
    }

    public PassengerWagon findSuitableWagon(TicketRequest request) {
        for (int i = 0; i < wagons.size(); i++) {
            Wagon w = wagons.get(i);
            if (w instanceof PassengerWagon) {
                PassengerWagon pw = (PassengerWagon) w;
                if (!pw.getType().equals(request.getWagonType())) {
                    continue;
                }
                if (request.isNeedTv() && !pw.isTv()) {
                    continue;
                }
                if (request.isNeedPhone() && !pw.isPhone()) {
                    continue;
                }
                if (request.isNeedLiePlace() && !pw.isCanLie()) {
                    continue;
                }
                if (!pw.hasFreeSeat()) {
                    continue;
                }
                return pw;
            }
        }
        return null;
    }

    public String routeInfo() {
        String s = "";
        for (int i = 0; i < stops.size(); i++) {
            s += stops.get(i).getStation().getName();
            if (i < stops.size() - 1) {
                s += " -> ";
            }
        }
        return s;
    }

    public String info() {
        return "Поезд " + number + " (дата " + date + ") маршрут: " + routeInfo();
    }
}