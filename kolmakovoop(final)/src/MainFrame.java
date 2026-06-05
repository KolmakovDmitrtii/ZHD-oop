import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class MainFrame extends JFrame {
    private RailwaySystem railway;
    private JTextArea logTextArea;
    private JButton startButton;
    private JButton statsButton;
    private JButton resetButton;
    private JLabel statusLabel;

    private final Color COLOR_BG = new Color(30, 30, 30);          // Глубокий тёмный фон
    private final Color COLOR_PANEL = new Color(43, 43, 43);       // Фон панелей управления
    private final Color COLOR_TEXT_AREA = new Color(18, 18, 18);   // Фон для логов (почти чёрный)
    private final Color COLOR_TEXT = new Color(220, 220, 220);     // Светло-серый текст
    private final Color COLOR_ACCENT = new Color(0, 150, 255);     // Неоново-синий акцент для кнопок
    private final Color COLOR_STATUS = new Color(80, 220, 100);    // Зеленый цвет для статуса

    public MainFrame() {
        this.railway = new RailwaySystem();
        this.railway.init();

        setTitle("Симуляция железной дороги");
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Главная панель с отступами
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Настройка шрифтов
        Font interfaceFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 14);

        // --- ЛЕВАЯ ПАНЕЛЬ (ЛОГИ) ---
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBackground(COLOR_PANEL);

        // Красивая рамка с подсветкой
        TitledBorder logBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_ACCENT, 1), "Ход симуляции (Продажа билетов)");
        logBorder.setTitleFont(titleFont);
        logBorder.setTitleColor(COLOR_ACCENT);
        logPanel.setBorder(logBorder);

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setBackground(COLOR_TEXT_AREA);
        logTextArea.setForeground(new Color(139, 233, 253)); // Стильный бирюзовый цвет для текста логов
        logTextArea.setFont(new Font("Consolas", Font.PLAIN, 13)); // Шрифт как в хакерских фильмах
        logTextArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        logPanel.add(scrollPane, BorderLayout.CENTER);

        // --- ПРАВАЯ ПАНЕЛЬ (УПРАВЛЕНИЕ) ---
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(COLOR_PANEL);
        controlPanel.setPreferredSize(new Dimension(240, 0));

        TitledBorder controlBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_ACCENT, 1), "Управление");
        controlBorder.setTitleFont(titleFont);
        controlBorder.setTitleColor(COLOR_ACCENT);
        controlPanel.setBorder(controlBorder);

        // Создаем и кастомизируем кнопки
        startButton = createStyledButton("Запустить симуляцию", interfaceFont);
        statsButton = createStyledButton("Показать статистику", interfaceFont);
        resetButton = createStyledButton("Сбросить данные", interfaceFont);

        statsButton.setEnabled(false);

        // Текстовый статус
        statusLabel = new JLabel("Статус: Готов к запуску");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(COLOR_STATUS);

        // Собираем правую панель воедино с красивыми отступами
        controlPanel.add(Box.createVerticalStrut(25));
        controlPanel.add(startButton);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(statsButton);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(resetButton);
        controlPanel.add(Box.createVerticalStrut(40));
        controlPanel.add(statusLabel);

        mainPanel.add(logPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.EAST);

        add(mainPanel);

        // СЛУШАТЕЛИ КНОПОК
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runSimulation();
            }
        });

        statsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showStatisticsDialog();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetSimulation();
            }
        });
    }

    // Вспомогательный метод для создания красивых кнопок в едином стиле
    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setMaximumSize(new Dimension(210, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(COLOR_ACCENT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    private void runSimulation() {
        logTextArea.setText("");

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream() {
            @Override
            public void flush() throws java.io.IOException {
                super.flush();
                String text = this.toString("UTF-8");
                logTextArea.append(text);
                logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
                this.reset();
            }
        };

        try {
            java.io.PrintStream printStream = new java.io.PrintStream(baos, true, "UTF-8");
            System.setOut(printStream);
        } catch (java.io.UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        statusLabel.setText("Статус: Выполнение...");
        statusLabel.setForeground(Color.ORANGE);
        startButton.setEnabled(false);

        for (int day = 1; day <= 2; day++) {
            railway.setCurrentDate(day);
            System.out.println("--- День " + day + " ---");
            for (int time = 0; time <= 480; time++) {
                railway.setCurrentTime(time);
                if (railway.getRandom().nextDouble() < 0.18) {
                    railway.makeRandomPassenger();
                }
            }
        }

        System.setOut(new java.io.PrintStream(new java.io.FileOutputStream(java.io.FileDescriptor.out)));

        statusLabel.setText("Статус: Завершено");
        statusLabel.setForeground(COLOR_STATUS);
        statsButton.setEnabled(true);
    }

    private void resetSimulation() {
        railway = new RailwaySystem();
        railway.init();
        logTextArea.setText("");
        statusLabel.setText("Статус: Сброшено");
        statusLabel.setForeground(COLOR_STATUS);
        startButton.setEnabled(true);
        statsButton.setEnabled(false);
    }

    private void showStatisticsDialog() {
        JDialog dialog = new JDialog(this, "Итоговые параметры объектов", true);
        dialog.setSize(850, 600);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(COLOR_BG);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_PANEL);
        tabbedPane.setForeground(COLOR_TEXT);

        ArrayList<Train> trains = railway.getTrains();
        ArrayList<Station> stations = railway.getStations();
        ArrayList<Ticket> tickets = railway.getTickets();
        ArrayList<RejectedRequest> rejected = railway.getRejected();

        // --- ВКЛАДКА 1: СОСТОЯНИЕ ПОЕЗДОВ ---
        StringBuilder sbTrains = new StringBuilder();
        sbTrains.append("МОНИТОРИНГ ТЕКУЩЕГО СОСТОЯНИЯ СОСТАВОВ\n");
        sbTrains.append("========================================================\n\n");
        for (int i = 0; i < trains.size(); i++) {
            Train t = trains.get(i);
            sbTrains.append("Поезд №").append(t.getNumber()).append(" | ").append(t.routeInfo()).append("\n");
            sbTrains.append("  Общее число пассажиров в пути: ").append(t.passengerCount()).append(" чел.\n");
            sbTrains.append("  Состав поезда по вагонам:\n");
            ArrayList<Wagon> wagons = t.getWagons();
            for (int j = 0; j < wagons.size(); j++) {
                sbTrains.append("    [Вагон] ").append(wagons.get(j).info()).append("\n");
            }
            sbTrains.append("\n------------------------------------------------------------------------\n\n");
        }
        tabbedPane.addTab("Поезда и вагоны", createTabScrollPane(sbTrains.toString()));

        // --- ВКЛАДКА 2: АНАЛИЗ ЗАГРУЖЕННОСТИ ---
        StringBuilder sbLoad = new StringBuilder();
        sbLoad.append("АНАЛИТИКА ЗАГРУЖЕННОСТИ ПАССАЖИРСКИХ МЕСТ\n");
        sbLoad.append("========================================================\n\n");
        int sittingSeats = 0, sittingBusy = 0;
        int platzSeats = 0, platzBusy = 0;
        int coupeSeats = 0, coupeBusy = 0;

        for (int i = 0; i < trains.size(); i++) {
            ArrayList<Wagon> wagons = trains.get(i).getWagons();
            for (int j = 0; j < wagons.size(); j++) {
                Wagon w = wagons.get(j);
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
        sbLoad.append(formatLoadString("Сидячая категория", sittingBusy, sittingSeats));
        sbLoad.append(formatLoadString("Плацкартная категория", platzBusy, platzSeats));
        sbLoad.append(formatLoadString("Купейная категория", coupeBusy, coupeSeats));

        sbLoad.append("\nЭФФЕКТИВНОСТЬ МАРШРУТОВ (ПАССАЖИРО-КИЛОМЕТРЫ)\n");
        sbLoad.append("------------------------------------------------------------------------\n\n");
        for (int i = 0; i < trains.size(); i++) {
            Train t = trains.get(i);
            double passengerKm = 0;
            for (int j = 0; j < tickets.size(); j++) {
                Ticket ticket = tickets.get(j);
                if (ticket.getTrain() == t) {
                    passengerKm += ticket.getDistance();
                }
            }
            double maxPassengerKm = t.totalSeats() * t.fullRouteDistance();
            double percent = maxPassengerKm > 0 ? (passengerKm * 100.0 / maxPassengerKm) : 0;
            sbLoad.append(" Направление поезда №").append(t.getNumber()).append(":\n")
                    .append("   Выполнено работы: ").append(Math.round(passengerKm)).append(" пасс-км из ").append(Math.round(maxPassengerKm)).append("\n")
                    .append("   Коэффициент полезного использования: ").append(Math.round(percent)).append("%\n\n");
        }
        tabbedPane.addTab("Загруженность", createTabScrollPane(sbLoad.toString()));

        // --- ВКЛАДКА 3: ФИНАНСОВЫЙ ОТЧЕТ ---
        StringBuilder sbFinance = new StringBuilder();
        double totalRevenue = 0;
        sbFinance.append("ФИНАНСОВЫЙ ОТЧЕТ О ВЫРУЧКЕ СЕТИ\n");
        sbFinance.append("========================================================\n\n");

        sbFinance.append("1. Распределение доходов по составам:\n");
        for (int i = 0; i < trains.size(); i++) {
            Train t = trains.get(i);
            sbFinance.append("   • Поезд №").append(t.getNumber()).append(": ").append(Math.round(t.getRevenue())).append(" руб.\n");
            totalRevenue += t.getRevenue();
        }

        sbFinance.append("\n2. Продажи билетов по станциям отправления:\n");
        for (int i = 0; i < stations.size(); i++) {
            Station st = stations.get(i);
            double stationRevenue = 0;
            for (int j = 0; j < tickets.size(); j++) {
                if (tickets.get(j).getStationFrom().getName().equals(st.getName())) {
                    stationRevenue += tickets.get(j).getPrice();
                }
            }
            if (stationRevenue > 0) {
                sbFinance.append("   • Станция ").append(st.getName()).append(": ").append(Math.round(stationRevenue)).append(" руб.\n");
            }
        }

        sbFinance.append("\n3. Доходы по классам обслуживания:\n");
        double sitting = 0, platz = 0, coupe = 0;
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            if (ticket.getWagon().getType().equals("Сидячий")) { sitting += ticket.getPrice(); }
            else if (ticket.getWagon().getType().equals("Плацкартный")) { platz += ticket.getPrice(); }
            else if (ticket.getWagon().getType().equals("Купейный")) { coupe += ticket.getPrice(); }
        }
        sbFinance.append("   • Сидячие места:   ").append(Math.round(sitting)).append(" руб.\n");
        sbFinance.append("   • Плацкартные места:  ").append(Math.round(platz)).append(" руб.\n");
        sbFinance.append("   • Купейные места:      ").append(Math.round(coupe)).append(" руб.\n\n");
        sbFinance.append("========================================================\n");
        sbFinance.append("ИТОГОВАЯ ВЫРУЧКА ЖЕЛЕЗНОДОРОЖНОЙ СЕТИ: ").append(Math.round(totalRevenue)).append(" РУБ.\n");
        tabbedPane.addTab("Финансы", createTabScrollPane(sbFinance.toString()));

        // --- ВКЛАДКА 4: АНАЛИЗ ОТКАЗОВ ---
        StringBuilder sbRejected = new StringBuilder();
        sbRejected.append("ОТЧЕТ ПО НЕУДОВЛЕТВОРЕННОМУ СПРОСУ\n");
        sbRejected.append("========================================================\n\n");
        sbRejected.append(" Зафиксировано отказов в приобретении билетов: ").append(rejected.size()).append(" шт.\n\n");
        sbRejected.append("Анализ ключевых причин отказа:\n");

        ArrayList<String> reasons = new ArrayList<String>();
        for (int i = 0; i < rejected.size(); i++) {
            String r = rejected.get(i).getReason();
            if (!reasons.contains(r)) { reasons.add(r); }
        }
        for (int i = 0; i < reasons.size(); i++) {
            String reason = reasons.get(i);
            int count = 0;
            for (int j = 0; j < rejected.size(); j++) {
                if (rejected.get(j).getReason().equals(reason)) { count++; }
            }
            sbRejected.append("   [!] ").append(reason).append(": ").append(count).append(" пасс.\n");
        }

        sbRejected.append("\nСпрос на дополнительные услуги по станциям (всего запросов):\n");
        int totalTv = 0, totalPhone = 0;
        for (int i = 0; i < stations.size(); i++) {
            Station st = stations.get(i);
            int tv = 0, phone = 0;
            for (int j = 0; j < tickets.size(); j++) {
                Ticket ticket = tickets.get(j);
                if (ticket.getStationFrom().getName().equals(st.getName())) {
                    if (ticket.getWagon().isTv()) tv++;
                    if (ticket.getWagon().isPhone()) phone++;
                }
            }
            for (int j = 0; j < rejected.size(); j++) {
                RejectedRequest r = rejected.get(j);
                if (r.getStation().getName().equals(st.getName())) {
                    if (r.getRequest().isNeedTv()) tv++;
                    if (r.getRequest().isNeedPhone()) phone++;
                }
            }
            if (tv > 0 || phone > 0) {
                sbRejected.append("   • Вокзал ").append(st.getName()).append(": Мультимедиа (TV)=").append(tv).append(", Средства связи=").append(phone).append("\n");
                totalTv += tv; totalPhone += phone;
            }
        }
        sbRejected.append("   Итоговые требования по сети: TV=").append(totalTv).append(", Телефон=").append(totalPhone).append("\n");
        tabbedPane.addTab("Отказы", createTabScrollPane(sbRejected.toString()));

        dialog.add(tabbedPane);
        dialog.setVisible(true);
    }

    private JScrollPane createTabScrollPane(String text) {
        JTextArea statsArea = new JTextArea(text);
        statsArea.setEditable(false);
        statsArea.setBackground(COLOR_TEXT_AREA);
        statsArea.setForeground(COLOR_TEXT);

        statsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        statsArea.setLineWrap(true);
        statsArea.setWrapStyleWord(true);
        statsArea.setMargin(new Insets(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(statsArea);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }
    private String formatLoadString(String name, int busy, int seats) {
        double percent = 0;
        if (seats > 0) {
            percent = busy * 100.0 / seats;
        }
        return "  " + name + ": " + busy + "/" + seats + " (" + Math.round(percent) + "%)\n";
    }
}