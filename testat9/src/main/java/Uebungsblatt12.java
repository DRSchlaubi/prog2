import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Uebungsblatt12 extends JFrame {
    private final List<Eintrag> datenbank = new ArrayList<>(1000);
    private final JButton backButton = new JButton("<--");
    private final JButton newButton = new JButton("Neu");
    private final JButton nextButton = new JButton("-->");
    private final JTextField searchBox = new JTextField("", 10);
    private final JButton searchButton = new JButton("Search");
    private final JTextField namenFeld = new JTextField("loading...", 15);
    private final JTextField adressenFeld = new JTextField("loading...", 15);
    private final JButton startButton = new JButton("Start");
    private int index = 0;

    private void save() {
        datenbank.set(index, new Eintrag(namenFeld.getText(), adressenFeld.getText()));
    }

    private void update() {
        nextButton.setEnabled(index < datenbank.size() - 1);
        backButton.setEnabled(index > 0);
        var eintrag = datenbank.get(index);
        namenFeld.setText(eintrag.name());
        adressenFeld.setText(eintrag.adresse());
    }

    public Uebungsblatt12() {

        for (int i = 0; i < 1000; i++)
            datenbank.add(i, new Eintrag((i % 2 == 0 ? "Meier" : "Müller") + i, "Teststraße 1"));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);

        var menuBar = new JMenuBar();
        var fileMenu = new JMenu("Datei");
        var helpMenu = new JMenu("Help");
        var end = new JMenuItem("Ende");

        end.addActionListener(_ -> System.exit(0));

        fileMenu.add(end);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // -- Inner Panel --

        var innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(3, 1, 0, 5));

        // -- Top Row --

        var topRow = new JPanel();
        topRow.setLayout(new FlowLayout());
        topRow.add(new JLabel("Name"));
        topRow.add(namenFeld);
        innerPanel.add(topRow);

        // -- Middle Row --

        var middleRow = new JPanel();
        middleRow.setLayout(new FlowLayout());
        middleRow.add(new JLabel("Adresse"));
        middleRow.add(adressenFeld);
        innerPanel.add(middleRow);

        // -- Lower Row --

        var lowerRow = new JPanel();
        lowerRow.setLayout(new FlowLayout());
        lowerRow.add(backButton);
        lowerRow.add(newButton);
        lowerRow.add(nextButton);
        lowerRow.add(searchBox);
        lowerRow.add(searchButton);
        lowerRow.add(startButton);
        innerPanel.add(lowerRow);

        backButton.addActionListener(e -> {
            save();
            index--;
            update();
        });

        nextButton.addActionListener(e -> {
            save();
            index++;
            update();
        });

        newButton.addActionListener(e -> {
            index = datenbank.size();
            datenbank.add(new Eintrag("",""));
            update();
        });

        searchButton.addActionListener(e -> {
            var searchQuery = searchBox.getText();
            for (int i = 0; i < datenbank.size(); i++) {
                if (Objects.equals(datenbank.get(i).name(), searchQuery)) {
                    index = i;
                    update();
                    break;
                }
            }
        });

        startButton.addActionListener(e -> {
            index = 0;
            update();
        });

        var contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentPane.add(innerPanel, BorderLayout.NORTH);

        update();
        setVisible(true);
    }

    static void main() {
        new Uebungsblatt12();
    }
}
