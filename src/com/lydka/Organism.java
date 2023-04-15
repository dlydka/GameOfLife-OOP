package com.lydka;

import java.awt.*;
import java.util.Random;

public abstract class Organism {
    public enum OrganismType {
        HUMAN,
        WOLF,
        SHEEP,
        FOX,
        TURTLE,
        ANTELOPE,
        CYBERSHEEP,
        GRASS,
        DANDELION,
        GUARANA,
        GOJI,
        PINEBROSCHT
    }

    public enum Direction {
        LEFT(0),
        RIGHT(1),
        UP(2),
        DOWN(3),
        NONE(4);

        private final int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private int strength;
    private int initiative;
    private int birth;
    private Color colour;
    private boolean isDead;
    public boolean isImortal;
    private boolean[] direction;
    private boolean reproduced;
    private World world;
    private Position position;
    private OrganismType type;
    private double reproduceChance;
    private static final int ORGANISM_AMOUNT = 12;

    public abstract String toString();

    public abstract void action();

    public abstract String getSymbol();

    public abstract void collision(Organism other);

    public abstract boolean isAnimal();

    public Organism(OrganismType type, World world, Position position, int birth, int strength, int initiative) {
        this.type = type;
        this.world = world;
        this.position = position;
        this.birth = birth;
        this.strength = strength;
        this.initiative = initiative;
        isDead = false;
        direction = new boolean[]{true, true, true, true};
    }

    public boolean attackAction(Organism attacker, Organism victim) {
        return false;
    }

    public String organismInformation() {
        return (toString() + " pozycja [" + position.getX() + "; "
                + position.getY() + "] sila: " + getStrength());
    }

    public void move(Position nextPosition) {
        int x = nextPosition.getX();
        int y = nextPosition.getY();
        world.getArea()[position.getY()][position.getX()] = null;
        world.getArea()[y][x] = this;
        position.setX(x);
        position.setY(y);
    }

    static OrganismType randomType() {
        Random rand = new Random();
        int tmp = rand.nextInt(ORGANISM_AMOUNT - 1); //BEZ CZLOWIEKA
        if (tmp == 0)
            return OrganismType.ANTELOPE;
        if (tmp == 1)
            return OrganismType.PINEBROSCHT;
        if (tmp == 2)
            return OrganismType.GUARANA;
        if (tmp == 3)
            return OrganismType.FOX;
        if (tmp == 4)
            return OrganismType.DANDELION;
        if (tmp == 5)
            return OrganismType.SHEEP;
        if (tmp == 6)
            return OrganismType.GRASS;
        if (tmp == 7)
            return OrganismType.GOJI;
        if (tmp == 8)
            return OrganismType.WOLF;
        if (tmp == 9)
            return OrganismType.CYBERSHEEP;
        else
            return OrganismType.TURTLE;
    }

    public Position randomPosition(Position position) {
        unlockDirections();
        int posX = position.getX();
        int posY = position.getY();
        int width = world.getWidth();
        int height = world.getHeight();
        int possibleDirections = 0;

        if (posX == 0) lockDirection(Direction.LEFT);
        else possibleDirections++;
        if (posX == width - 1) lockDirection(Direction.RIGHT);
        else possibleDirections++;
        if (posY == 0) lockDirection(Direction.UP);
        else possibleDirections++;
        if (posY == height - 1) lockDirection(Direction.DOWN);
        else possibleDirections++;

        if (possibleDirections == 0)
            return position;
        while (true) {
            Random rand = new Random();
            int random = rand.nextInt(100);
            // ruch w lewo
            if (random < 25 && !isDirectionLocked(Direction.LEFT))
                return new Position(posX - 1, posY);
                // ruch w prawo
            else if (random >= 25 && random < 50 && !isDirectionLocked(Direction.RIGHT))
                return new Position(posX + 1, posY);
                // ruch w gore
            else if (random >= 50 && random < 75 && !isDirectionLocked(Direction.UP))
                return new Position(posX, posY - 1);
                // ruch w dol
            else if (random >= 75 && !isDirectionLocked(Direction.DOWN))
                return new Position(posX, posY + 1);
        }
    }

    public Position randomEmptyPosition(Position position) {
        unlockDirections();
        int posX = position.getX();
        int posY = position.getY();
        int width = world.getWidth();
        int height = world.getHeight();
        int possibleDirections = 0;

        if (posX == 0) lockDirection(Direction.LEFT);
        else {
            if (!world.isPositionTaken(new Position(posX - 1, posY))) possibleDirections++;
            else lockDirection(Direction.LEFT);
        }

        if (posX == width - 1) lockDirection(Direction.RIGHT);
        else {
            if (!world.isPositionTaken(new Position(posX + 1, posY))) possibleDirections++;
            else lockDirection(Direction.RIGHT);
        }

        if (posY == 0) lockDirection(Direction.UP);
        else {
            if (!world.isPositionTaken(new Position(posX, posY - 1))) possibleDirections++;
            else lockDirection(Direction.UP);
        }

        if (posY == height - 1) lockDirection(Direction.DOWN);
        else {
            if (!world.isPositionTaken(new Position(posX, posY + 1))) possibleDirections++;
            else lockDirection(Direction.DOWN);
        }

        if (possibleDirections == 0) return new Position(posX, posY);
        while (true) {
            Random rand = new Random();
            int random = rand.nextInt(100);
            // ruch w lewo
            if (random < 25 && !isDirectionLocked(Direction.LEFT))
                return new Position(posX - 1, posY);
                // ruch w prawo
            else if (random >= 25 && random < 50 && !isDirectionLocked(Direction.RIGHT))
                return new Position(posX + 1, posY);
                // ruch w gore
            else if (random >= 50 && random < 75 && !isDirectionLocked(Direction.UP))
                return new Position(posX, posY - 1);
                // ruch w dol
            else if (random >= 75 && !isDirectionLocked(Direction.DOWN))
                return new Position(posX, posY + 1);
        }
    }

    protected void lockDirection(Direction direction) {
        this.direction[direction.getValue()] = false;
    }

    protected void unlockDirection(Direction direction) {
        this.direction[direction.getValue()] = true;
    }

    protected void unlockDirections() {
        unlockDirection(Direction.LEFT);
        unlockDirection(Direction.RIGHT);
        unlockDirection(Direction.UP);
        unlockDirection(Direction.DOWN);
    }

    protected boolean isDirectionLocked(Direction direction) {
        return !(this.direction[direction.getValue()]);
    }

    public int getStrength() {
        return strength;
    }

    public int getInitiative() {
        return initiative;
    }

    public int getBirth() {
        return birth;
    }

    public boolean getIsDead() {
        return isDead;
    }

    public boolean getReproduced() {
        return reproduced;
    }

    public boolean getImortal() { return isImortal; }

    public void setImortal(boolean isImortal) { this.isImortal = isImortal; }

    public World getWorld() {
        return world;
    }

    public Position getPosition() {
        return position;
    }

    public OrganismType getType() {
        return type;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public void setReproduced(boolean reproduced) {
        this.reproduced = reproduced;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setType(OrganismType type) {
        this.type = type;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public double getReproduceChance() {
        return reproduceChance;
    }

    public void setReproduceChance(double reproduceChance) {
        this.reproduceChance = reproduceChance;
    }
}
