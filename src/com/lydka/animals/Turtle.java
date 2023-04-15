package com.lydka.animals;

import com.lydka.Animal;
import com.lydka.Organism;
import com.lydka.World;
import com.lydka.Commentator;
import com.lydka.Position;
import java.awt.*;

public class Turtle extends Animal {
    private static final int RANGE = 1;
    private static final double TURTLE_MOVE_CHANCE = 0.25;
    private static final int TURTLE_STRENGTH = 2;
    private static final int TURTLE_INITIATIVE = 1;

    public Turtle(World world, Position position, int birth) {
        super(OrganismType.TURTLE, world, position, birth, TURTLE_STRENGTH, TURTLE_INITIATIVE);
        this.setRange(RANGE);
        this.setMoveChance(TURTLE_MOVE_CHANCE);
        setColour(new Color(7, 85, 7));
    }

    @Override
    public String getSymbol(){ return "Z"; }

    @Override
    public String toString() {
        return "Zolw";
    }

    @Override
    public boolean attackAction(Organism attacker, Organism victim) {
        if (this == victim) {
            if (attacker.getStrength() < 5 && attacker.isAnimal()) {
                Commentator.addComment(organismInformation() + " odpiera atak " + attacker.organismInformation());
                return true;
            } else return false;
        } else {
            if (attacker.getStrength() >= victim.getStrength()) return false;
            else {
                if (victim.getStrength() < 5 && victim.isAnimal()) {
                    Commentator.addComment(organismInformation() + " odpiera atak " + victim.organismInformation());
                    return true;
                } else return false;
            }
        }
    }
}