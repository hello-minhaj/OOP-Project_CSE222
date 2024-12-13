//@minhaj copyright
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
class StarCinema {
    static List<Hall> hallList = new ArrayList<>();

    public void entryHall(Hall hall) {
        hallList.add(hall);
    }

    public static List<Hall> viewHall() {
        return hallList;
    }
}

class Hall extends StarCinema {
    private int rows;
    private int cols;
    private int hallNo;
    private List<Show> showList = new ArrayList<>();
    private java.util.Map<Integer, int[][]> seats = new java.util.HashMap<>();

    public Hall(int rows, int cols, int hallNo) {
        this.rows = rows;
        this.cols = cols;
        this.hallNo = hallNo;
        super.entryHall(this);
    }

    public String viewShowList() {
        StringBuilder sb = new StringBuilder("Shows in Hall " + hallNo + ":\n");
        for (Show show : showList) {
            sb.append("Movie: ").append(show.movieName)
              .append(", Show ID: ").append(show.id)
              .append(", Time: ").append(show.time).append("\n");
        }
        return sb.toString();
    }

    public void entryShow(int id, String movieName, String time) {
        showList.add(new Show(id, movieName, time));
        int[][] matrix = new int[rows][cols];
        seats.put(id, matrix);
    }

    public String viewAvailableSeats(int showID) {
        if (!seats.containsKey(showID)) return "Show ID not found!";
        int[][] seatMatrix = seats.get(showID);
        StringBuilder sb = new StringBuilder("Available seats for Show ID " + showID + ":\n");
        for (int[] row : seatMatrix) {
            for (int seat : row) {
                sb.append(seat == 0 ? "[ ]" : "[X]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String bookSeat(int showID, int row, int col) {
        if (!seats.containsKey(showID)) return "Show ID not found!";
        if (row < 0 || row >= rows || col < 0 || col >= cols) return "Invalid seat!";
        int[][] seatMatrix = seats.get(showID);
        if (seatMatrix[row][col] == 0) {
            seatMatrix[row][col] = 1;
            return "Seat (" + row + ", " + col + ") booked successfully!";
        }
        return "Seat already booked!";
    }
}

class Show {
    int id;
    String movieName;
    String time;

    public Show(int id, String movieName, String time) {
        this.id = id;
        this.movieName = movieName;
        this.time = time;
    }
}

public class CinemaBookingSystemGUI {
    public static void main(String[] args) {
        Hall hall1 = new Hall(5, 5, 1);
        Hall hall2 = new Hall(5, 8, 2);

        hall1.entryShow(111, "Jhaka Naka", "11 am");
        hall1.entryShow(221, "Matha Nosto", "2 pm");

        hall2.entryShow(222, "Jhaka Naka 2", "11 am");
        hall2.entryShow(333, "Matha Nosto 2", "2 pm");

        JFrame frame = new JFrame("Star Cinema Hall");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.BLACK, getWidth(), getHeight(), new Color(85, 85, 85));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        JLabel headerLabel = new JLabel("Welcome to Star Cinema Hall!");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(255, 215, 0));
        headerPanel.add(headerLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        displayArea.setBackground(Color.BLACK);
        displayArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBackground(new Color(50, 50, 50));
        JButton viewShowsButton = new JButton("View Shows");
        JButton viewSeatsButton = new JButton("View Seats");
        JButton bookSeatsButton = new JButton("Book Seats");
        JButton exitButton = new JButton("Exit");

        JButton[] buttons = {viewShowsButton, viewSeatsButton, bookSeatsButton, exitButton};
        for (JButton button : buttons) {
            button.setFont(new Font("Serif", Font.BOLD, 18));
            button.setBackground(new Color(30, 30, 30));
            button.setForeground(new Color(255, 215, 0));
        }

        buttonPanel.add(viewShowsButton);
        buttonPanel.add(viewSeatsButton);
        buttonPanel.add(bookSeatsButton);
        buttonPanel.add(exitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        viewShowsButton.addActionListener(e -> {
            displayArea.setText("");
            for (Hall hall : StarCinema.viewHall()) {
                displayArea.append(hall.viewShowList());
            }
        });

        viewSeatsButton.addActionListener(e -> {
            String hallInput = JOptionPane.showInputDialog("Enter Hall Number:");
            try {
                int hallNo = Integer.parseInt(hallInput);
                Hall selectedHall = StarCinema.viewHall().get(hallNo - 1);

                String showInput = JOptionPane.showInputDialog("Enter Show ID:");
                int showID = Integer.parseInt(showInput);
                displayArea.setText(selectedHall.viewAvailableSeats(showID));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input or hall number!");
            }
        });

        bookSeatsButton.addActionListener(e -> {
            String hallInput = JOptionPane.showInputDialog("Enter Hall Number:");
            try {
                int hallNo = Integer.parseInt(hallInput);
                Hall selectedHall = StarCinema.viewHall().get(hallNo - 1);

                String showInput = JOptionPane.showInputDialog("Enter Show ID:");
                int showID = Integer.parseInt(showInput);

                String rowInput = JOptionPane.showInputDialog("Enter Row:");
                int row = Integer.parseInt(rowInput);

                String colInput = JOptionPane.showInputDialog("Enter Column:");
                int col = Integer.parseInt(colInput);

                displayArea.setText(selectedHall.bookSeat(showID, row, col));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input or hall number!");
            }
        });

        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Thank you for visiting Star Cinema Hall!");
            frame.dispose();
        });

        frame.setVisible(true);
    }
}
