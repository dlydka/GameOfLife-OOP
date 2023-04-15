package com.lydka.plants;

import com.lydka.Plant;
import com.lydka.Organism;
import com.lydka.Commentator;
import com.lydka.World;
import com.lydka.Position;
import java.util.Random;
import java.awt.*;

public class PineBroscht extends Plant {
    private static final int PINEBROSCHT_STRENGTH = 10;
    private static final int PLANT_INITIATIVE = 0;

    public PineBroscht(World world, Position position, int turaUrodzenia) {
        super(OrganismType.PINEBROSCHT, world, position,
                turaUrodzenia, PINEBROSCHT_STRENGTH, PLANT_INITIATIVE);
        setColour(new Color(153, 243, 137));
        setReproduceChance(0.05);
    }

    @Override
    public String getSymbol(){ return "b"; }

    @Override
    public void action() {
        int posX = getPosition().getX();
        int posY = getPosition().getY();
        randomPosition(getPosition());
        for (int i = 0; i < 4; i++) {
            Organism organism = null;
            if (i == 0 && !isDirectionLocked(Direction.DOWN))
                organism = getWorld().whatOnPosition(new Position(posX, posY + 1));
            else if (i == 1 && !isDirectionLocked(Direction.UP))
                organism = getWorld().whatOnPosition(new Position(posX, posY - 1));
            else if (i == 2 && !isDirectionLocked(Direction.LEFT))
                organism = getWorld().whatOnPosition(new Position(posX - 1, posY));
            else if (i == 3 && !isDirectionLocked(Direction.RIGHT))
                organism = getWorld().whatOnPosition(new Position(posX + 1, posY));

            if (organism != null && organism.isAnimal()
                    && organism.getType() != OrganismType.CYBERSHEEP) {
                if(organism.getImortal() != true) {
                    getWorld().deleteOrganism(organism);
                    Commentator.addComment(organismInformation() + " zabija " + organism.organismInformation());
                }
            }
        }
        Random rand = new Random();
        int random = rand.nextInt(100);
        if (random < getReproduceChance() * 100)
            overgrowth();
    }

    @Override
    public String toString() {
        return "Barszcz Sosnowskiego";
    }

    @Override
    public boolean attackAction(Organism attacker, Organism victim) {
        if (attacker.getStrength() >= 10) {
            getWorld().deleteOrganism(this);
            Commentator.addComment(attacker.organismInformation() + " zjada " + this.organismInformation());
            attacker.move(victim.getPosition());
        }
        if ((attacker.isAnimal() && attacker.getType() != OrganismType.CYBERSHEEP) || attacker.getStrength() < 10) {
            getWorld().deleteOrganism(attacker);
            Commentator.addComment(this.organismInformation() + " zabija " + attacker.organismInformation());
        }
        return true;
    }
}
