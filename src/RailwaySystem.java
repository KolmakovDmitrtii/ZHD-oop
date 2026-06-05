import java.util.ArrayList;

class RailwaySystem {

    public static String formatTime(int minutes) {
        int h = minutes / 60;
        int m = minutes % 60;

        String hs;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        String ms;
        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        return hs + ":" + ms;
    }

    private ArrayList<Station> stations;
    private ArrayList<Train> trains;
    private ArrayList<Ticket> tickets;
    private ArrayList<RejectedRequest> rejected;
    private java.util.Random random;
    private int currentTime;
    private int currentDate;

    public RailwaySystem() {
        this.stations = new ArrayList<Station>();
        this.trains = new ArrayList<Train>();
        this.tickets = new ArrayList<Ticket>();
        this.rejected = new ArrayList<RejectedRequest>();
        this.random = new java.util.Random();
        this.currentTime = 0;
        this.currentDate = 1;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public ArrayList<Train> getTrains() {
        return trains;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public ArrayList<RejectedRequest> getRejected() {
        return rejected;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public int getCurrentDate() {
        return currentDate;
    }

    public void setCurrentTime(int time) {
        this.currentTime = time;
    }

    public void setCurrentDate(int date) {
        this.currentDate = date;
    }

    public java.util.Random getRandom() {
        return random;
    }

    public void init() {
        Station minsk       = new Station("Минск",       0,    0);
        Station brest       = new Station("Брест",      350,  20);
        Station gomel       = new Station("Гомель",     310, -180);
        Station vitebsk     = new Station("Витебск",    260,  210);
        Station grodno      = new Station("Гродно",    -250,   90);
        Station baranovichi = new Station("Барановичи", 140,   30);
        Station orsha       = new Station("Орша",       170,  130);

        stations.add(minsk);
        stations.add(brest);
        stations.add(gomel);
        stations.add(vitebsk);
        stations.add(grodno);
        stations.add(baranovichi);
        stations.add(orsha);

        Train t1 = new Train(101, 1);
        t1.addStop(minsk,       0,  40);
        t1.addStop(baranovichi, 95, 100);
        t1.addStop(brest,      190, 190);
        t1.addWagon(new SittingWagon(1,  40, 0.20, false, false));
        t1.addWagon(new PlatzkartWagon(2, 54, 0.35, true,  false));
        t1.addWagon(new CoupeWagon(3,    36, 0.55, true,  true,  20));
        t1.addWagon(new RestaurantWagon(4));
        t1.addWagon(new MailWagon(5));

        Train t2 = new Train(202, 1);
        t2.addStop(brest,       0,  60);
        t2.addStop(baranovichi,145, 150);
        t2.addStop(minsk,      210, 215);
        t2.addStop(orsha,      285, 290);
        t2.addStop(vitebsk,    360, 360);
        t2.addWagon(new SittingWagon(1,  45, 0.18, false, true));
        t2.addWagon(new PlatzkartWagon(2, 54, 0.32, false, false));
        t2.addWagon(new CoupeWagon(3,    32, 0.50, true,  false, 18));
        t2.addWagon(new BuffetWagon(4));

        Train t3 = new Train(303, 1);
        t3.addStop(grodno,      0,  70);
        t3.addStop(baranovichi,160, 165);
        t3.addStop(minsk,      235, 240);
        t3.addStop(gomel,      390, 390);
        t3.addWagon(new SittingWagon(1,  42, 0.19, true,  false));
        t3.addWagon(new PlatzkartWagon(2, 52, 0.34, true,  true));
        t3.addWagon(new CoupeWagon(3,    30, 0.60, true,  true,  25));
        t3.addWagon(new RestaurantWagon(4));
        t3.addWagon(new BuffetWagon(5));

        Train t4 = new Train(404, 2);
        t4.addStop(vitebsk,     0,  50);
        t4.addStop(orsha,      120, 125);
        t4.addStop(minsk,      205, 210);
        t4.addStop(baranovichi,280, 285);
        t4.addStop(grodno,     420, 420);
        t4.addWagon(new SittingWagon(1,  38, 0.17, false, false));
        t4.addWagon(new PlatzkartWagon(2, 50, 0.30, false, true));
        t4.addWagon(new CoupeWagon(3,    28, 0.48, true,  false, 15));
        t4.addWagon(new MailWagon(4));

        Train t5 = new Train(505, 2);
        t5.addStop(gomel,       0,  80);
        t5.addStop(minsk,      230, 235);
        t5.addStop(baranovichi,305, 310);
        t5.addStop(brest,      430, 430);
        t5.addWagon(new SittingWagon(1,  40, 0.19, true,  false));
        t5.addWagon(new PlatzkartWagon(2, 54, 0.33, false, true));
        t5.addWagon(new CoupeWagon(3,    34, 0.52, true,  true,  22));
        t5.addWagon(new RestaurantWagon(4));

        trains.add(t1);
        trains.add(t2);
        trains.add(t3);
        trains.add(t4);
        trains.add(t5);
    }

    public Train findTrain(int number) {
        for (int i = 0; i < trains.size(); i++) {
            if (trains.get(i).getNumber() == number) {
                return trains.get(i);
            }
        }
        return null;
    }

    public Ticket sellTicket(TicketRequest request) {
        Train train = findTrain(request.getTrainNumber());
        if (train == null) {
            rejected.add(new RejectedRequest(request, "Нет поезда с таким номером", request.getFrom()));
            return null;
        }
        if (request.getDate() != train.getDate()) {
            rejected.add(new RejectedRequest(request, "Поезд идет в другую дату", request.getFrom()));
            return null;
        }
        if (!train.canCarry(request.getFrom(), request.getDestination())) {
            rejected.add(new RejectedRequest(request, "Поезд не идет по указанному направлению", request.getFrom()));
            return null;
        }
        int departure = train.departureFrom(request.getFrom());
        int arrival   = train.arrivalTo(request.getDestination());
        if (request.getDate() < currentDate || (request.getDate() == currentDate && currentTime > departure)) {
            rejected.add(new RejectedRequest(request, "Поезд уже ушел с этой станции", request.getFrom()));
            return null;
        }
        if (request.getDate() == currentDate && currentTime > arrival) {
            rejected.add(new RejectedRequest(request, "Поезд уже прибыл на станцию назначения", request.getFrom()));
            return null;
        }
        if (request.isNeedRestaurant() && !train.hasRestaurant()) {
            rejected.add(new RejectedRequest(request, "В поезде нет ресторана", request.getFrom()));
            return null;
        }
        if (request.isNeedBuffet() && !train.hasBuffet()) {
            rejected.add(new RejectedRequest(request, "В поезде нет буфета", request.getFrom()));
            return null;
        }
        PassengerWagon wagon = train.findSuitableWagon(request);
        if (wagon == null) {
            rejected.add(new RejectedRequest(request, "Нет подходящего вагона или свободных мест", request.getFrom()));
            return null;
        }
        double distance = train.distanceBetween(request.getFrom(), request.getDestination());
        wagon.takeSeat();
        double price = wagon.calculatePrice(distance);
        train.addRevenue(price);
        Ticket ticket = new Ticket(request.getPassenger(), train, wagon, price,
                request.getFrom(), request.getDestination(), request.getDate(), distance);
        tickets.add(ticket);
        return ticket;
    }

    public void makeRandomPassenger() {
        Train train = trains.get(random.nextInt(trains.size()));
        int fromIndex = random.nextInt(train.getStops().size() - 1);
        int toIndex   = fromIndex + 1 + random.nextInt(train.getStops().size() - fromIndex - 1);
        Station from        = train.getStops().get(fromIndex).getStation();
        Station destination = train.getStops().get(toIndex).getStation();

        String[] types = {"Сидячий", "Плацкартный", "Купейный"};
        String wagonType     = types[random.nextInt(types.length)];
        boolean needRestaurant = random.nextDouble() < 0.25;
        boolean needBuffet     = random.nextDouble() < 0.25;
        boolean needTv         = random.nextDouble() < 0.30;
        boolean needPhone      = random.nextDouble() < 0.20;
        boolean needLiePlace   = random.nextDouble() < 0.35;

        Passenger p = new Passenger("Пассажир_" + (tickets.size() + rejected.size() + 1));
        TicketRequest request = new TicketRequest(p, train.getNumber(), train.getDate(),
                from, destination, wagonType,
                needRestaurant, needBuffet, needTv, needPhone, needLiePlace);

        Ticket ticket = sellTicket(request);
        if (ticket != null) {
            System.out.println("[день " + currentDate + ", " + formatTime(currentTime) + "] "
                    + p.getName() + " купил билет "
                    + from.getName() + " -> " + destination.getName()
                    + " поезд " + train.getNumber()
                    + " вагон №" + ticket.getWagon().getNumber()
                    + " цена " + Math.round(ticket.getPrice()) + " руб.");
        } else {
            RejectedRequest last = rejected.get(rejected.size() - 1);
            System.out.println("[день " + currentDate + ", " + formatTime(currentTime) + "] "
                    + p.getName() + " ОТКАЗ: " + last.getReason());
        }
    }
}