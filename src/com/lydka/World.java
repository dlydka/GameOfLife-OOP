package com.lydka;

import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;

import com.lydka.animals.Human;

public class World {
    private final int width;
    private final int height;
    private int turn;
    private final Organism[][] area;
    private boolean isHumanAlive;
    private boolean isGameOver;
    private boolean pause;
    private final ArrayList<Organism> organisms;
    private Human human;
    private WorldGUI worldGUI;

    World(int width, int height, WorldGUI worldGUI) {
        this.width = width;
        this.height = height;
        turn = 0;
        isGameOver = false;
        isHumanAlive = true;
        pause = true;


        area = new Organism[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                area[i][j] = null;
            }
        }
        organisms = new ArrayList<>();
        this.worldGUI = worldGUI;
    }

    public void saveWorld(String fileName) {
        try {
            fileName += ".txt";
            File file = new File(fileName);
            file.createNewFile();

            PrintWriter pw = new PrintWriter(file);
            pw.print(width + " ");
            pw.print(height + " ");
            pw.print(turn + " ");
            pw.print(isHumanAlive + " ");
            pw.print(isGameOver + "\n");
            for (Organism organism : organisms) {
                pw.print(organism.getType() + " ");
                pw.print(organism.getPosition().getX() + " ");
                pw.print(organism.getPosition().getY() + " ");
                pw.print(organism.getStrength() + " ");
                pw.print(organism.getBirth() + " ");
                pw.print(organism.getIsDead());
                if (organism.getType() == Organism.OrganismType.HUMAN) {
                    pw.print(" " + human.getAbilityTime() + " ");
                    pw.print(human.getCooldown() + " ");
                    pw.print(human.getIsActive() + " ");
                    pw.print(human.getIsAvaiable());
                }
                pw.println();
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static World loadWorld(String fileName) {
        try {
            fileName += ".txt";
            File file = new File(fileName);

            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            String[] properties = line.split(" ");
            int width = Integer.parseInt(properties[0]);
            int height = Integer.parseInt(properties[1]);
            World world = new World(width, height, null);
            world.turn = Integer.parseInt(properties[2]);
            world.isHumanAlive = Boolean.parseBoolean(properties[3]);
            world.isGameOver = Boolean.parseBoolean(properties[4]);
            world.human = null;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                properties = line.split(" ");
                Organism.OrganismType type = Organism.OrganismType.valueOf(properties[0]);
                int x = Integer.parseInt(properties[1]);
                int y = Integer.parseInt(properties[2]);

                Organism organism = OrganismCreator.createOrganism(type, world, new Position(x, y));
                int strength = Integer.parseInt(properties[3]);
                organism.setStrength(strength);
                int birth = Integer.parseInt(properties[4]);
                organism.setBirth(birth);
                boolean isDead = Boolean.parseBoolean(properties[5]);
                organism.setIsDead(isDead);

                if (type == Organism.OrganismType.HUMAN) {
                    world.human = (Human) organism;
                    int abilityTime = Integer.parseInt(properties[6]);
                    world.human.setAbilityTime(abilityTime);
                    int cooldown = Integer.parseInt(properties[7]);
                    world.human.setCooldown(cooldown);
                    boolean isActive = Boolean.parseBoolean(properties[8]);
                    world.human.setIsActive(isActive);
                    boolean isAvaiable = Boolean.parseBoolean(properties[9]);
                    world.human.setIsAvaiable(isAvaiable);
                }
                world.addOrganism(organism);
            }
            scanner.close();
            return world;
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        return null;
    }

    public void generateWorld() {
        Random rand = new Random();
        int filling = rand.nextInt(40) + 11;
        int organismsAmount = (int) Math.floor(width * height * filling / 100);

        Position position = pickFreePosition();
        Organism organism = OrganismCreator.createOrganism(Organism.OrganismType.HUMAN, this, position);
        addOrganism(organism);
        human = (Human) organism;

        for (int i = 0; i < organismsAmount - 1; i++) {
            position = pickFreePosition();
            if (position != new Position(-1, -1))
                addOrganism(OrganismCreator.createOrganism(Organism.randomType(), this, position));
		else
            return;
        }
    }

    public int getTurn() {
        return turn;
    }

    public void doTurn() {
        if (isGameOver) return;
        turn++;
        Commentator.addComment("\nTURA " + turn);

        sortOrganisms();
        for (int i = 0; i < organisms.size(); i++) {
            if (organisms.get(i).getBirth() != turn && !organisms.get(i).getIsDead()) {
                organisms.get(i).action();
            }
        }
        for (int i = 0; i < organisms.size(); i++) {
            if (organisms.get(i).getIsDead()) {
                organisms.remove(i);
                i--;
            }
        }
        for (int i = 0; i < organisms.size(); i++) {
            organisms.get(i).setReproduced(false);
        }
    }

    private void sortOrganisms() {
        Collections.sort(organisms, new Comparator<Organism>() {
            @Override
            public int compare(Organism o1, Organism o2) {
                if (o1.getInitiative() != o2.getInitiative())
                    return Integer.valueOf(o2.getInitiative()).compareTo(o1.getInitiative());
                else
                    return Integer.valueOf(o1.getBirth()).compareTo(o2.getBirth());
            }
        });
    }

    public Position pickFreePosition() {
        Random rand = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (area[i][j] == null) {
                    while (true) {
                        int x = rand.nextInt(width);
                        int y = rand.nextInt(height);
                        if (area[y][x] == null) return new Position(x, y);
                    }
                }
            }
        }
        return new Position(-1, -1);
    }

    public boolean isPositionTaken(Position position) {
        if (area[position.getY()][position.getX()] == null) return false;
        else return true;
    }

    public Organism whatOnPosition(Position position) {
        return area[position.getY()][position.getX()];
    }

    public void addOrganism(Organism organism) {
        organisms.add(organism);
        area[organism.getPosition().getY()][organism.getPosition().getX()] = organism;
    }

    public void deleteOrganism(Organism organism) {
        area[organism.getPosition().getY()][organism.getPosition().getX()] = null;
        organism.setIsDead(true);
        if (organism.getType() == Organism.OrganismType.HUMAN) {
            isHumanAlive = false;
            human = null;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Organism[][] getArea() {
        return area;
    }

    public boolean getIsHumanAlive() {
        return isHumanAlive;
    }

    public Human getHuman() {
        return human;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }


    public void setWorldGUI(WorldGUI worldGUI) {
        this.worldGUI = worldGUI;
    }

    public boolean existPineBroscht() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (area[i][j] != null &&
                        area[i][j].getType() == Organism.OrganismType.PINEBROSCHT) {
                    return true;
                }
            }
        }
        return false;
    }
}