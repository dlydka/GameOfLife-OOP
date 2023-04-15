package com.lydka.animals;

import com.lydka.Animal;
import com.lydka.World;
import com.lydka.Position;
import java.awt.*;

public class Wolf extends Animal {
    private static final int RANGE = 1;
    private static final int MOVE_CHANCE = 1;
    private static final int WOLF_STRENGTH = 9;
    private static final int WOLF_INITIATIVE = 5;

    public Wolf(World world, Position position, int birth) {
        super(OrganismType.WOLF, world, position, birth, WOLF_STRENGTH, WOLF_INITIATIVE);
        this.setRange(RANGE);
        this.setMoveChance(MOVE_CHANCE);
        setColour(new Color(76, 73, 73));
    }

    @Override
    public String getSymbol(){ return "W"; }

    @Override
    public String toString() {
        return "Wilk";
    }
}
