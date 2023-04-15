package com.lydka.plants;

import com.lydka.Plant;
import com.lydka.World;
import com.lydka.Position;
import java.util.Random;
import java.awt.*;

public class Dandelion extends Plant {
    private static final int DANDELION_STRENGTH = 0;
    private static final int PLANT_INITIATIVE = 0;
    private static final int TRIES = 3;

    public Dandelion(World world, Position position, int birth) {
        super(OrganismType.DANDELION, world, position,
                birth, DANDELION_STRENGTH, PLANT_INITIATIVE);
        setColour(Color.YELLOW);
        setReproduceChance(0.3);
    }

    @Override
    public String getSymbol(){ return "m"; }

    @Override
    public void action() {
        Random rand = new Random();
        for (int i = 0; i < TRIES; i++) {
            int random = rand.nextInt(100);
            if (random < getReproduceChance() * 100)
                overgrowth();
        }
    }

    @Override
    public String toString() {
        return "Mlecz";
    }
}