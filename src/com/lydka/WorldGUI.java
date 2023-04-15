package com.lydka;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WorldGUI implements ActionListener, KeyListener {
    private Toolkit toolkit;
    private Dimension dimension;
    private JFrame jFrame;
    private JMenu menu;
    private JMenuItem newGame, load, save, exit;
    private AreaGUI areaGUI = null;
    private CommentatorGUI commentatorGUI = null;
    private JPanel mainPanel;
    private final int SPACE;
    private World world;

    public WorldGUI() {
        toolkit = Toolkit.getDefaultToolkit();
        dimension = toolkit.getScreenSize();
        SPACE = dimension.height / 100;
        jFrame = new JFrame("Virtual World");
        jFrame.setBounds((dimension.width - 800) / 2, (dimension.height - 600) / 2, 800, 600);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        menu = new JMenu("Start");
        newGame = new JMenuItem("New Game");
        load = new JMenuItem("Load");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");
        newGame.addActionListener(this);
        load.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);
        menu.add(newGame);
        menu.add(load);
        menu.add(save);
        menu.add(exit);
        menuBar.add(menu);
        jFrame.setJMenuBar(menuBar);
        jFrame.setLayout(new CardLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        jFrame.addKeyListener(this);
        jFrame.add(mainPanel);
        jFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGame) {
            Commentator.clearComment();
            int width = Integer.parseInt(JOptionPane.showInputDialog(jFrame, "Podaj szerokosc swiata",
                    "Virtual World", JOptionPane.INFORMATION_MESSAGE));
            int height = Integer.parseInt(JOptionPane.showInputDialog(jFrame, "Podaj wysokosc swiata",
                    "Virtual World", JOptionPane.INFORMATION_MESSAGE));
            world = new World(width, height, this);
            world.generateWorld();
            if (areaGUI != null)
                mainPanel.remove(areaGUI);
            if (commentatorGUI != null)
                mainPanel.remove(commentatorGUI);
            startGame();
        }
        if (e.getSource() == load) {
            Commentator.clearComment();
            String nameOfFile = JOptionPane.showInputDialog(jFrame, "Podaj nazwe pliku");
            world = World.loadWorld(nameOfFile);
            world.setWorldGUI(this);
            areaGUI = new AreaGUI(world);
            commentatorGUI = new CommentatorGUI();
            if (areaGUI != null)
                mainPanel.remove(areaGUI);
            if (commentatorGUI != null)
                mainPanel.remove(commentatorGUI);
            startGame();
        }
        if (e.getSource() == save) {
            String nameOfFile = JOptionPane.showInputDialog(jFrame, "Podaj nazwe pliku");
            world.saveWorld(nameOfFile);
            Commentator.addComment("Swiat zostal zapisany");
            commentatorGUI.refreshComments();
        }
        if (e.getSource() == exit) {
            jFrame.dispose();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (world != null && world.isPause()) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ENTER) {

            } else if (world.getIsHumanAlive()) {
                if (keyCode == KeyEvent.VK_UP) {
                    world.getHuman().setDirection(Organism.Direction.UP);
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    world.getHuman().setDirection(Organism.Direction.DOWN);
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    world.getHuman().setDirection(Organism.Direction.LEFT);
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    world.getHuman().setDirection(Organism.Direction.RIGHT);
                } else if (keyCode == KeyEvent.VK_A) {

                    if (world.getHuman().getIsAvaiable()) {
                        world.getHuman().activate();
                        Commentator.addComment("Umiejetnosc 'Niesmiertelnosc' zostala wlaczona. Pozostaly" +
                                " czas trwania wynosi " + world.getHuman().getAbilityTime() + " tur");

                    } else if (world.getHuman().getIsActive()) {
                        Commentator.addComment("Umiejetnosc juz zostala aktywowana " + "Pozostaly" +
                                " czas trwania wynosi " + world.getHuman().getAbilityTime() + " tur");
                        commentatorGUI.refreshComments();
                        return;
                    } else {
                        Commentator.addComment("Umiejetnosc mozna wlaczyc tylko po "
                                + world.getHuman().getCooldown() + " turach");
                        commentatorGUI.refreshComments();
                        return;
                    }
                } else {
                    Commentator.addComment("\nNieoznaczony symbol, sprobuj ponownie");
                    commentatorGUI.refreshComments();
                    return;
                }
            } else if (!world.getIsHumanAlive() && (keyCode == KeyEvent.VK_UP ||
                    keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT ||
                    keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_P)) {
                Commentator.addComment("Czlowiek nie zyje. Nie mozesz wiecej nim sterowac");
                commentatorGUI.refreshComments();
                return;
            } else {
                Commentator.addComment("\nNieoznaczony symbol, sprobuj ponownie");
                commentatorGUI.refreshComments();
                return;
            }
            Commentator.clearComment();
            world.setPause(false);
            world.doTurn();
            refreshWorld();
            world.setPause(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

    private class AreaGUI extends JPanel {
        private final int width;
        private final int height;
        private AreaElement[][] areaElements;
        private World WORLD;

        public AreaGUI(World world) {
            super();
            setBounds(mainPanel.getX() + SPACE, mainPanel.getY() + 4 * SPACE,
                    mainPanel.getHeight() * 9 / 10 - SPACE, mainPanel.getHeight() * 9 / 10 - SPACE);
            WORLD = world;
            this.width = world.getWidth();
            this.height = world.getHeight();

            areaElements = new AreaElement[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    areaElements[i][j] = new AreaElement();
                }
            }

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    this.add(areaElements[i][j]);
                }
            }
            this.setLayout(new GridLayout(height, width));
        }

        private class AreaElement extends JButton {
            private Color colour;

            public AreaElement() {
                super();
                colour = Color.WHITE;
                setBackground(colour);
            }

            public void setColour(Color colour) {
                this.colour = colour;
                setBackground(colour);
            }
        }

        public void refreshArea() {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Organism organism = world.getArea()[i][j];
                    if (organism != null) {
                        areaElements[i][j].setColour(organism.getColour());
                        areaElements[i][j].setText(organism.getSymbol());
                    } else {
                        areaElements[i][j].setColour(Color.LIGHT_GRAY);
                        areaElements[i][j].setText("");
                    }
                    areaElements[i][j].setEnabled(false);
                    areaElements[i][j].setMargin(new Insets(0, 0, 0, 0));
                }
            }
        }
    }

    private class CommentatorGUI extends JPanel {
        private String text;
        private final JTextArea textArea;

        public CommentatorGUI() {
            super();
            setBounds(areaGUI.getX() + areaGUI.getWidth() + SPACE, mainPanel.getY() + SPACE,
                    mainPanel.getWidth() - areaGUI.getWidth() - SPACE * 3, mainPanel.getHeight() - SPACE * 2);
            text = Commentator.getText();
            textArea = new JTextArea(text);
            textArea.setEditable(false);
            setLayout(new CardLayout());

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setMargin(new Insets(5, 5, 5, 5));
            JScrollPane sp = new JScrollPane(textArea);
            add(sp);
        }

        public void refreshComments() {
            String instruction = """
                    Autor: Dawid Lydka 184440
                    Strzalki - kierowanie Czlowiekiem
                    * strzalka w gore - ruch o jedno pole w gore
                    * strzalka w dol - ruch o jedno pole w dol
                    * strzalka w lewo - ruch o jedno pole w lewo
                    * strzalka w prawo - ruch o jedno pole w prawo
                    A - aktywacja umiejetnosci
                    Enter - przejscie do nastepnej tury
                    """;
            text = instruction + Commentator.getText();
            textArea.setText(text);
        }
    }

    private void startGame() {
        areaGUI = new AreaGUI(world);
        mainPanel.add(areaGUI);

        commentatorGUI = new CommentatorGUI();
        mainPanel.add(commentatorGUI);

        refreshWorld();
    }

    public void refreshWorld() {
        areaGUI.refreshArea();
        commentatorGUI.refreshComments();
        SwingUtilities.updateComponentTreeUI(jFrame);
        jFrame.requestFocusInWindow();
    }

    public World getWorld() {
        return world;
    }
}