import java.util.ArrayList;

class Statistics {
    private RailwaySystem system;

    public Statistics(RailwaySystem system) {
        this.system = system;
    }

    public void show() {
        System.out.println("\n===== СТАТИСТИКА =====\n");
        showTrains();
        showEquipmentDemand();
        showWagonLoad();
        showRouteLoad();
        showRevenue();
        showRejected();
    }

    private void showTrains() {
        System.out.println("Состояние поездов");
        ArrayList<Train> list = system.getTrains();
        for (int i = 0; i < list.size(); i++) {
            Train t = list.get(i);
            System.out.println(t.info());
            System.out.println("  Пассажиров всего: " + t.passengerCount());
            ArrayList<Wagon> wgList = t.getWagons();
            for (int j = 0; j < wgList.size(); j++) {
                System.out.println("  " + wgList.get(j).info());
            }
        }
        System.out.println();
    }

    private void showEquipmentDemand() {
        System.out.println("Пассажиры, требовавшие доп. оборудование по станциям");
        ArrayList<Station> stList = system.getStations();
        ArrayList<Ticket> tkList = system.getTickets();
        ArrayList<RejectedRequest> rjList = system.getRejected();
        int totalTv = 0;
        int totalPhone = 0;

        for (int i = 0; i < stList.size(); i++) {
            Station st = stList.get(i);
            int tv = 0;
            int phone = 0;
            for (int j = 0; j < tkList.size(); j++) {
                Ticket ticket = tkList.get(j);
                if (ticket.getStationFrom().getName().equals(st.getName())) {
                    if (ticket.getWagon().isTv()) tv++;
                    if (ticket.getWagon().isPhone()) phone++;
                }
            }
            for (int j = 0; j < rjList.size(); j++) {
                RejectedRequest r = rjList.get(j);
                if (r.getStation().getName().equals(st.getName())) {
                    if (r.getRequest().isNeedTv()) tv++;
                    if (r.getRequest().isNeedPhone()) phone++;
                }
            }
            if (tv > 0 || phone > 0) {
                System.out.println("  " + st.getName() + ": TV=" + tv + ", Телефон=" + phone);
                totalTv += tv;
                totalPhone += phone;
            }
        }
        System.out.println("  Итого: TV=" + totalTv + ", Телефон=" + totalPhone);
        System.out.println();
    }

    private void showWagonLoad() {
        System.out.println("Загруженность вагонов по типам");
        int sittingSeats = 0, sittingBusy = 0;
        int platzSeats = 0, platzBusy = 0;
        int coupeSeats = 0, coupeBusy = 0;
        ArrayList<Train> trList = system.getTrains();

        for (int i = 0; i < trList.size(); i++) {
            Train t = trList.get(i);
            ArrayList<Wagon> wgList = t.getWagons();
            for (int j = 0; j < wgList.size(); j++) {
                Wagon w = wgList.get(j);
                if (w instanceof PassengerWagon) {
                    PassengerWagon pw = (PassengerWagon) w;
                    if (pw.getType().equals("Сидячий")) {
                        sittingSeats += pw.getSeats();
                        sittingBusy += pw.getOccupied();
                    } else if (pw.getType().equals("Плацкартный")) {
                        platzSeats += pw.getSeats();
                        platzBusy += pw.getOccupied();
                    } else if (pw.getType().equals("Купейный")) {
                        coupeSeats += pw.getSeats();
                        coupeBusy += pw.getOccupied();
                    }
                }
            }
        }
        printLoad("Сидячие",     sittingBusy, sittingSeats);
        printLoad("Плацкартные", platzBusy,   platzSeats);
        printLoad("Купейные",    coupeBusy,   coupeSeats);
        System.out.println();
    }

    private void printLoad(String name, int busy, int seats) {
        double percent = 0;
        if (seats > 0) percent = busy * 100.0 / seats;
        System.out.println("  " + name + ": " + busy + "/" + seats + " (" + Math.round(percent) + "%)");
    }

    private void showRouteLoad() {
        System.out.println("Загруженность маршрутов (пассажиро-километры)");
        ArrayList<Train> trList = system.getTrains();
        ArrayList<Ticket> tkList = system.getTickets();

        for (int i = 0; i < trList.size(); i++) {
            Train t = trList.get(i);
            double passengerKm = 0;
            for (int j = 0; j < tkList.size(); j++) {
                Ticket ticket = tkList.get(j);
                if (ticket.getTrain() == t) {
                    passengerKm += ticket.getDistance();
                }
            }
            double maxPassengerKm = t.totalSeats() * t.fullRouteDistance();
            double percent = 0;
            if (maxPassengerKm > 0) percent = passengerKm * 100.0 / maxPassengerKm;
            System.out.println("  Поезд " + t.getNumber() + " " + t.routeInfo() + ": " +
                    Math.round(passengerKm) + "/" + Math.round(maxPassengerKm) +
                    " пасс-км (" + Math.round(percent) + "%)");
        }
        System.out.println();
    }

    private void showRevenue() {
        System.out.println("Выручка по поездам");
        double total = 0;
        ArrayList<Train> trList = system.getTrains();
        ArrayList<Station> stList = system.getStations();
        ArrayList<Ticket> tkList = system.getTickets();

        for (int i = 0; i < trList.size(); i++) {
            Train t = trList.get(i);
            System.out.println("  Поезд " + t.getNumber() + ": " + Math.round(t.getRevenue()) + " руб.");
            total += t.getRevenue();
        }

        System.out.println("\nВыручка по станциям продажи");
        for (int i = 0; i < stList.size(); i++) {
            Station st = stList.get(i);
            double stationRevenue = 0;
            for (int j = 0; j < tkList.size(); j++) {
                Ticket ticket = tkList.get(j);
                if (ticket.getStationFrom().getName().equals(st.getName())) {
                    stationRevenue += ticket.getPrice();
                }
            }
            if (stationRevenue > 0) {
                System.out.println("  " + st.getName() + ": " + Math.round(stationRevenue) + " руб.");
            }
        }

        System.out.println("\nВыручка по типам вагонов");
        double sitting = 0, platz = 0, coupe = 0;
        for (int i = 0; i < tkList.size(); i++) {
            Ticket ticket = tkList.get(i);
            if (ticket.getWagon().getType().equals("Сидячий")) {
                sitting += ticket.getPrice();
            } else if (ticket.getWagon().getType().equals("Плацкартный")) {
                platz += ticket.getPrice();
            } else if (ticket.getWagon().getType().equals("Купейный")) {
                coupe += ticket.getPrice();
            }
        }
        System.out.println("  Сидячий:     " + Math.round(sitting) + " руб.");
        System.out.println("  Плацкартный: " + Math.round(platz) + " руб.");
        System.out.println("  Купейный:    " + Math.round(coupe) + " руб.");
        System.out.println("  Всего:       " + Math.round(total) + " руб.");
        System.out.println();
    }

    private void showRejected() {
        ArrayList<RejectedRequest> rjList = system.getRejected();
        System.out.println("Отказы в продаже билетов: " + rjList.size());

        ArrayList<String> reasons = new ArrayList<String>();
        for (int i = 0; i < rjList.size(); i++) {
            String r = rjList.get(i).getReason();
            boolean found = false;
            for (int k = 0; k < reasons.size(); k++) {
                if (reasons.get(k).equals(r)) {
                    found = true;
                    break;
                }
            }
            if (!found) reasons.add(r);
        }

        for (int i = 0; i < reasons.size(); i++) {
            String reason = reasons.get(i);
            int count = 0;
            for (int j = 0; j < rjList.size(); j++) {
                if (rjList.get(j).getReason().equals(reason)) count++;
            }
            System.out.println("  " + reason + ": " + count);
        }
        System.out.println();
    }
}