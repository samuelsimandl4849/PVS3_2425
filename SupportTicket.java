package exams;


import gui.Flow;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SupportTicket {
    private int ticketId;
    private String employeeId;
    private String department;
    private String category;
    private String priority;
    private int createdHrsAgo;
    private String status;

    public static List<SupportTicket> tickets = new ArrayList<>();

    public SupportTicket(int ticketId, String employeeId, String department,
                         String category, String priority, int createdHrsAgo, String status) {
        this.ticketId = ticketId;
        this.employeeId = employeeId;
        this.department = department;
        this.category = category;
        this.priority = priority;
        this.createdHrsAgo = createdHrsAgo;
        this.status = status;
    }

    public static void main(String[] args) throws IOException {
        getTickets();
        Scanner sc = new Scanner(System.in);
        List<SupportTicket> recent = tickets.stream()
                .filter(e ->
                    e.getCreatedHrsAgo() <= 72
                )
                .toList();

        Map<String, Long> result = tickets.stream()
                .collect(Collectors.groupingBy(
                        SupportTicket::getCategory,
                        Collectors.counting()
                ));

        result.forEach((k, v) -> System.out.println(k + ": " + v));


        int choice;

        while (true) {
            System.out.println("vyber si jednu z moznosti:");
            System.out.println("1. print all tickets");
            System.out.println("2. GUI");
            System.out.println("3. show statistics??");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice){
                case 1: printTickets(); break;
                case 2:
                        createGUI(); break;
                case 3:

                default:
                    System.out.println("neplatna volba");
            }
        }
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public String getCategory() {
        return category;
    }

    public String getPriority() {
        return priority;
    }

    public int getCreatedHrsAgo() {
        return createdHrsAgo;
    }

    public String getStatus() {
        return status;
    }

    public static void printTickets(){
        for (SupportTicket t : tickets) {
            System.out.println(t);
        }
    }


    public static void getTickets() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data/tickets.csv"));
        String line;
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            SupportTicket ticket = new SupportTicket(
                    Integer.parseInt(data[0]), data[1], data[2], data[3], data[4], Integer.parseInt(data[5]), (data[6])
            );

            tickets.add(ticket);
        }
    }

    public static void createGUI() {

        JFrame frame = new JFrame("Create Support Ticket");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        JTextField txtEmployee = new JTextField();
        JTextField txtDepartment = new JTextField();
        JTextField txtCategory = new JTextField();
        
        mainPanel.add(new JLabel("Employee ID:"));
        mainPanel.add(txtEmployee);

        mainPanel.add(new JLabel("Department:"));
        mainPanel.add(txtDepartment);

        mainPanel.add(new JLabel("Category:"));
        mainPanel.add(txtCategory);
        
        JRadioButton low = new JRadioButton("Low");
        JRadioButton medium = new JRadioButton("Medium");
        JRadioButton high = new JRadioButton("High");
        JRadioButton critical = new JRadioButton("Critical");

        ButtonGroup group = new ButtonGroup();
        group.add(low);
        group.add(medium);
        group.add(high);
        group.add(critical);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(low);
        buttonPanel.add(medium);
        buttonPanel.add(high);
        buttonPanel.add(critical);

        mainPanel.add(new JLabel("Priority:"));
        mainPanel.add(buttonPanel);
        
        frame.add(mainPanel, BorderLayout.CENTER);
        
        JButton btnCreate = new JButton("Create Ticket");
        frame.add(btnCreate, BorderLayout.SOUTH);
        
        btnCreate.addActionListener(e -> {

            String emp = txtEmployee.getText();
            String dep = txtDepartment.getText();
            String cat = txtCategory.getText();

            String priority = "";

            if (low.isSelected()) priority = "Low";
            else if (medium.isSelected()) priority = "Medium";
            else if (high.isSelected()) priority = "High";
            else if (critical.isSelected()) priority = "Critical";
            
            if (emp.isEmpty() || dep.isEmpty() || cat.isEmpty() || priority.equals("")) {
                JOptionPane.showMessageDialog(frame, "Vyplň všechna pole!");
                return;
            }

            if (!emp.matches("E00[1-5]")) {
                JOptionPane.showMessageDialog(frame, "Employee ID musí být E001–E005");
                return;
            }
            
            SupportTicket t = new SupportTicket(
                    (999 + tickets.size()), emp, dep, cat, priority, 0, "Open"
            );

            tickets.add(t);

            JOptionPane.showMessageDialog(frame, "Ticket vytvořen!");
        });

        frame.setVisible(true);
    }

    @Override
    public String toString() {
        return String.format(
                "Ticket{ID=%d, Emp=%s, Dept=%s, Cat=%s, Priority=%s, Created=%dh ago, Status=%s}",
                ticketId, employeeId, department, category, priority, createdHrsAgo, status
        );
    }
}
