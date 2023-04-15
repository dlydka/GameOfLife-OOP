package com.lydka.animals;

import com.lydka.Animal;
import com.lydka.World;
import com.lydka.Position;
import java.awt.*;

public class Sheep extends Animal{
    private static final int RANGE = 1;
    private static final int MOVE_CHANCE = 1;
    private static final int SHEEP_STRENGTH = 4;
    private static final int SHEEP_INITIATIVE = 4;

    public Sheep(World world, Position position, int birth) {
        super(OrganismType.SHEEP, world, position, birth, SHEEP_STRENGTH, SHEEP_INITIATIVE);
        this.setRange(RANGE);
        this.setMoveChance(MOVE_CHANCE);
        setColour(new Color(255, 255, 255));
    }

    @Override
    public String getSymbol(){ return "O"; }

    @Override
    public String toString() {
        return "Owca";
    }
}
