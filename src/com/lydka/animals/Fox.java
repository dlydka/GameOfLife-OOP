package com.lydka.animals;

import com.lydka.Animal;
import com.lydka.Organism;
import com.lydka.World;
import com.lydka.Position;
import java.awt.*;
import java.util.Random;

public class Fox extends Animal {
    private static final int RANGE = 1;
    private static final int MOVE_CHANCE = 1;
    private static final int FOX_STRENGTH = 3;
    private static final int FOX_INITIATIVE = 7;

    public Fox(World world, Position position, int birth) {
        super(OrganismType.FOX, world, position, birth, FOX_STRENGTH, FOX_INITIATIVE);
        this.setRange(RANGE);
        this.setMoveChance(MOVE_CHANCE);
        setColour(new Color(238, 80, 16));
    }

    @Override
    public String getSymbol() { return "L"; }

    @Override
    public String toString() {
        return "Lis";
    }

    @Override
    public Position randomPosition(Position position) {
        unlockDirections();
        int posX = position.getX();
        int posY = position.getY();
        int width = getWorld().getWidth();
        int height = getWorld().getHeight();
        int possibleDirections = 0;
        Organism organism;

        if (posX == 0) lockDirection(Direction.LEFT);
        else {
            organism = getWorld().getArea()[posY][posX - 1];
            if (organism != null && organism.getStrength() > this.getStrength()) {
                lockDirection(Direction.LEFT);
            } else possibleDirections++;
        }

        if (posX == width - 1) lockDirection(Direction.RIGHT);
        else {
            organism = getWorld().getArea()[posY][posX + 1];
            if (organism != null && organism.getStrength() > this.getStrength()) {
                lockDirection(Direction.RIGHT);
            } else possibleDirections++;
        }

        if (posY == 0) lockDirection(Direction.UP);
        else {
            organism = getWorld().getArea()[posY - 1][posX];
            if (organism != null && organism.getStrength() > this.getStrength()) {
                lockDirection(Direction.UP);
            } else possibleDirections++;
        }

        if (posY == height - 1) lockDirection(Direction.DOWN);
        else {
            organism = getWorld().getArea()[posY + 1][posX];
            if (organism != null && organism.getStrength() > this.getStrength()) {
                lockDirection(Direction.DOWN);
            } else possibleDirections++;
        }

        if (possibleDirections == 0) return new Position(posX, posY);
        while (true) {
            Random rand = new Random();
            int random = rand.nextInt(100);
            //RUCH W LEWO
            if (random < 25 && !isDirectionLocked(Direction.LEFT))
                return new Position(posX - 1, posY);
                //RUCH W PRAWO
            else if (random >= 25 && random < 50 && !isDirectionLocked(Direction.RIGHT))
                return new Position(posX + 1, posY);
                //RUCH W GORE
            else if (random >= 50 && random < 75 && !isDirectionLocked(Direction.UP))
                return new Position(posX, posY - 1);
                //RUCH W DOL
            else if (random >= 75 && !isDirectionLocked(Direction.DOWN))
                return new Position(posX, posY + 1);
        }
    }
}
