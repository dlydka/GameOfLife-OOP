package com.lydka.animals;

import com.lydka.Organism;
import com.lydka.World;
import com.lydka.plants.PineBroscht;
import com.lydka.Position;
import java.awt.*;

public class CyberSheep extends Sheep {
    private static final int RANGE = 1;
    private static final int MOVE_CHANCE = 1;
    private static final int CYBER_SHEEP_STRENGTH = 11;
    private static final int CYBER_SHEEP_INITIATIVE = 4;

    public CyberSheep(World world, Position position, int birth) {
        super(world, position, birth);
        setType(OrganismType.CYBERSHEEP);
        setStrength(CYBER_SHEEP_STRENGTH);
        setInitiative(CYBER_SHEEP_INITIATIVE);
        setReproduceChance(0.1);

        this.setRange(RANGE);
        this.setMoveChance(MOVE_CHANCE);
        setColour(Color.BLACK);
    }

    @Override
    public String getSymbol(){ return "C"; }

    @Override
    public Position randomPosition(Position position) {
        if (getWorld().existPineBroscht()) {

            Position pineBroscht = findPineBroscht().getPosition();
            int dx = Math.abs(position.getX() - pineBroscht.getX());
            int dy = Math.abs(position.getY() - pineBroscht.getY());
            if (dx >= dy) {
                if (position.getX() > pineBroscht.getX()) {
                    return new Position(position.getX() - 1, position.getY());
                } else {
                    return new Position(position.getX() + 1, position.getY());
                }
            } else {
                if (position.getY() > pineBroscht.getY()) {
                    return new Position(position.getX(), position.getY() - 1);
                } else {
                    return new Position(position.getX(), position.getY() + 1);
                }
            }
        } else return super.randomPosition(position);
    }

    private PineBroscht findPineBroscht() {
        PineBroscht pb = null;
        int distance = getWorld().getWidth() + getWorld().getHeight() + 1;
        for (int i = 0; i < getWorld().getHeight(); i++) {
            for (int j = 0; j < getWorld().getWidth(); j++) {
                Organism organism = getWorld().getArea()[i][j];
                if (organism != null &&
                        organism.getType() == OrganismType.PINEBROSCHT) {
                    int tmpDistance = findDistance(organism.getPosition());
                    if (distance > tmpDistance) {
                        distance = tmpDistance;
                        pb = (PineBroscht) organism;
                    }
                }
            }
        }
        return pb;
    }

    private int findDistance(Position pos) {
        int dx = Math.abs(getPosition().getX() - pos.getX());
        int dy = Math.abs(getPosition().getY() - pos.getY());
        return dx + dy;
    }

    @Override
    public String toString() {
        return "Cyber owca";
    }
}
