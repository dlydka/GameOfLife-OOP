package com.lydka.plants;

import com.lydka.Plant;
import com.lydka.World;
import com.lydka.Position;
import java.awt.*;

public class Grass extends Plant {

    private static final int GRASS_STRENGTH = 0;
    private static final int PLANT_INITIATIVE = 0;

    public Grass(World world, Position position, int birth) {
        super(OrganismType.GRASS, world, position, birth, GRASS_STRENGTH, PLANT_INITIATIVE);
        setColour(Color.GREEN);
    }

    @Override
    public String getSymbol(){ return "t"; }

    @Override
    public String toString() {
        return "Trawa";
    }
}