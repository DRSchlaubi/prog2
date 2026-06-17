import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

import static java.lang.IO.println;

void main() {
    final var printerJobs = Executors.newVirtualThreadPerTaskExecutor();
    final List<Integer> neinClix = new ArrayList<>();

    JFrame gui = new JFrame("Layout");
    JPanel contentPain = new JPanel();
    contentPain.setLayout(new BoxLayout(contentPain, BoxLayout.Y_AXIS));
    gui.setContentPane(contentPain);
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gui.setSize(300, 250);

    //----------------------------------------

    JMenuBar menuBar = new JMenuBar();
    JMenu file = new JMenu("File");

    JMenuItem reset = new JMenuItem("Reset queue");
    reset.addActionListener(_ -> neinClix.clear());
    file.add(reset);

    JMenuItem print = new JMenuItem("Print queue");
    print.addActionListener(_ -> println(neinClix));
    file.add(print);

    JMenuItem actuallyPrint = new JMenuItem("Actually Print queue");
    actuallyPrint.addActionListener(_ -> {
        printerJobs.execute(() -> {
            var job = PrinterJob.getPrinterJob();
            var pageFormat = job.defaultPage();
            var paper = pageFormat.getPaper();
            // A4 size in 1/72 inch units
            paper.setSize(595, 842);
            paper.setImageableArea(72, 72, 451, 698);
            pageFormat.setPaper(paper);

            job.setPrintable((graphics, pf, page) -> {
                if (page > 0) return Printable.NO_SUCH_PAGE;

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pf.getImageableX(), pf.getImageableY());
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 24));
                g2d.drawString(neinClix.toString(), 10, 30);

                return Printable.PAGE_EXISTS;
            }, pageFormat);


            HashPrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(DialogTypeSelection.NATIVE);
            if (job.printDialog(attributes)) {
                try {
                    job.print(attributes);
                } catch (PrinterException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    });
    file.add(actuallyPrint);

    JMenuItem exit = new JMenuItem("Exit");
    exit.addActionListener(_ -> System.exit(0));
    file.add(exit);

    menuBar.add(file);
    gui.setJMenuBar(menuBar);

    //----------------------------------------

    JLabel label = new JLabel("Left1+Left2+Left3+Right1 klicken!");
    label.setAlignmentX(JComponent.CENTER_ALIGNMENT);

    JPanel gridPanel = new JPanel(new GridLayout(3, 2));
    var buttonNames = List.of(
            Map.entry("Left1", 1), Map.entry("Right1", 11),
            Map.entry("Left2", 2), Map.entry("Right2", 12),
            Map.entry("Left3", 3), Map.entry("Right3", 13)
    );
    buttonNames.forEach(entry -> {
        JButton button = new JButton(entry.getKey());
        gridPanel.add(button);
        button.addActionListener(_ -> {
            neinClix.add(entry.getValue());
            if (neinClix.equals(List.of(1, 2, 3, 11))) {
                System.exit(0);
            }
        });
    });
    gridPanel.setMaximumSize(gridPanel.getPreferredSize());
    gridPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

    JButton jumper = new JButton("JUMP");
    jumper.addActionListener(_ -> contentPain.setBackground(Color.RED));
    jumper.setAlignmentX(JComponent.CENTER_ALIGNMENT);
    jumper.addMouseListener(new MouseListener() {
        enum MovingDirection {
            LEFT {
                @Override
                public MovingDirection unaryMinus() {
                    return MovingDirection.RIGHT;
                }

                @Override
                public int moveTo(MovingDirection direction) {
                    return direction == MovingDirection.CENTER ? 100 : -100;
                }
            }, RIGHT {
                @Override
                public MovingDirection unaryMinus() {
                    return MovingDirection.LEFT;
                }

                @Override
                public int moveTo(MovingDirection direction) {
                    return direction == MovingDirection.CENTER ? -100 : 100;
                }
            }, CENTER {
                private MovingDirection previous;

                @Override
                public MovingDirection unaryMinus() {
                    throw new UnsupportedOperationException("Unary minus not supported for CENTER direction");
                }

                @Override
                public int moveTo(MovingDirection direction) {
                    return direction == MovingDirection.LEFT ? -100 : 100;
                }

                @Override
                public MovingDirection next() {
                    MovingDirection next;
                    if (previous == null) {
                        var random = ThreadLocalRandom.current().nextBoolean();
                        next = random ? MovingDirection.LEFT : MovingDirection.RIGHT;
                    } else {
                        next = previous.unaryMinus();
                    }
                    previous = next;
                    return next;
                }
            };

            public MovingDirection next() {
                return MovingDirection.CENTER;
            }

            public abstract MovingDirection unaryMinus();

            public abstract int moveTo(MovingDirection direction);
        }

        private MovingDirection direction = MovingDirection.CENTER;


        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            var nextDirection = direction.next();
            int jx = jumper.getX();
            int jy = jumper.getY();
            int dx = direction.moveTo(nextDirection);
            jumper.setLocation(jx + dx, jy);
            direction = nextDirection;
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    });

    contentPain.add(Box.createVerticalStrut(20));
    contentPain.add(gridPanel);
    contentPain.add(Box.createVerticalStrut(10));
    contentPain.add(label);
    contentPain.add(Box.createVerticalStrut(10));
    contentPain.add(jumper);


    gui.setVisible(true);
}
