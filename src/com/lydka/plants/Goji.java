package com.lydka.plants;

import com.lydka.Plant;
import com.lydka.Organism;
import com.lydka.Commentator;
import com.lydka.World;
import com.lydka.Position;
import java.util.Random;
import java.awt.*;

public class Goji extends Plant {
    private static final int GOJI_STRENGTH = 99;
    private static final int PLANT_INITIATIVE = 0;

    public Goji(World world, Position position, int birth) {
        super(OrganismType.GOJI, world, position, birth, GOJI_STRENGTH, PLANT_INITIATIVE);
        setColour(new Color(255, 64, 64));
        setReproduceChance(0.05);
    }


    @Override
    public void action() {
        Random rand = new Random();
        int random = rand.nextInt(100);
        if (random < getReproduceChance() * 100)
            overgrowth();
    }

    @Override
    public String getSymbol(){ return "j"; }

    @Override
    public String toString() {
        return "Wilcze jagody";
    }

    @Override
    public boolean attackAction(Organism attacker, Organism victim) {
        Commentator.addComment(attacker.organismInformation() + " zjada " + this.organismInformation());
        if (attacker.getStrength() >= 99) {
            getWorld().deleteOrganism(this);
            Commentator.addComment(attacker.organismInformation() + " niszczy krzak wilczej jagody");
        }
        if (attacker.isAnimal()) {
            getWorld().deleteOrganism(attacker);
            Commentator.addComment(attacker.organismInformation() + " ginie od wilczej jagody");
        }
        return true;
    }
}
