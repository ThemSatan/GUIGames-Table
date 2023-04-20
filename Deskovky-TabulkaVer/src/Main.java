import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends JFrame {
    private JPanel panelMain;
    private JButton loadButton;
    private JButton nextButton;
    private JButton prevButton;
    static String separator = "-";
    int actualGame;
    ArrayList<String> gameName = new ArrayList();
    ArrayList<String> owningGame = new ArrayList();
    ArrayList<Integer> favLevel = new ArrayList();

    public Main() {
        initComponents();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        horizontalBox.add(prevButton);
        horizontalBox.add(loadButton);
        horizontalBox.add(nextButton);
        panelMain.add(horizontalBox);
    }

    private void readFromFile() throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            loadFile(selectedFile);
            showActualGame();
        }
    }

    String line;

    private void loadFile(File selectedFile) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(selectedFile)))) {
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String parameters[] = line.split(separator);
                gameName.add(parameters[0]);
                owningGame.add(parameters[1]);
                favLevel.add(Integer.parseInt(parameters[2]));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void initComponents() {
        loadButton.addActionListener(e -> {
            try {
                readFromFile();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        nextButton.addActionListener(e -> {
            nextGame(1);
        });
        prevButton.addActionListener(e -> {
            nextGame(-1);
        });
    }

    private void nextGame(int next) {
        int numberOfGames = gameName.size();
        if (numberOfGames == 0) {
            actualGame = 0;
            return;
        }
        actualGame += next;

        if (actualGame >= numberOfGames) {
            actualGame = 0;
        }
        if (actualGame <= -1) {
            actualGame = numberOfGames - 1;
        }
        showActualGame();
    }

    Box horizontalBox = Box.createHorizontalBox();

    JTable table = new JTable(3, 2);
    ;
    JScrollPane scrollPane = new JScrollPane(table);

    private void showActualGame() {
        DefaultTableModel model = new DefaultTableModel(new Object[][]{
                {owningGame.get(actualGame), gameName.get(actualGame), favLevel.get(actualGame)}},
                new Object[]{"Game owned", "Name", "Favorite"});
        table.setModel(model);
        this.add(Box.createRigidArea(new Dimension(0, 35)));
        this.add(scrollPane);
        this.revalidate();
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.setContentPane(m.panelMain);
        m.setSize(500, 500);
        m.setTitle("Deskovky - Tabulka verze");
        m.setVisible(true);
        m.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
