package com.lydka.animals;

import com.lydka.Animal;
import com.lydka.Commentator;
import com.lydka.Organism;
import com.lydka.World;
import com.lydka.Position;

import java.awt.*;
import java.util.Random;

public class Antelope extends Animal {
    private static final int ANTELOPE_RANGE = 2;
    private static final int MOVE_CHANCE = 1;
    private static final int ANTELOPE_STRENGTH = 4;
    private static final int ANTELOPE_INITIATIVE = 4;


    public Antelope(World world, Position position, int birth) {
        super(OrganismType.ANTELOPE, world, position, birth, ANTELOPE_STRENGTH, ANTELOPE_INITIATIVE);
        this.setRange(ANTELOPE_RANGE);
        this.setMoveChance(MOVE_CHANCE);
        setColour(new Color(160, 86, 62, 255));
    }

    @Override
    public String getSymbol(){ return "A"; }

    @Override
    public String toString() {
        return "Antylopa";
    }

    @Override
    public boolean attackAction(Organism attacker, Organism victim) {
        Random rand = new Random();
        int random = rand.nextInt(100);
        if (random < 50) {
            if (this == attacker) {
                Commentator.addComment(organismInformation() + " ucieka od " + victim.organismInformation());
                Position pos = randomEmptyPosition(victim.getPosition());
                if (!pos.equals(victim.getPosition()))
                    move(pos);
            } else if (this == victim) {
                Commentator.addComment(organismInformation() + " ucieka od " + attacker.organismInformation());
                Position pos = this.getPosition();
                move(randomEmptyPosition(this.getPosition()));
                if (getPosition().equals(pos)) {
                    getWorld().deleteOrganism(this);
                    Commentator.addComment(attacker.organismInformation() + " zabija " + organismInformation());
                }
                attacker.move(pos);
            }
            return true;
        } else return false;
    }
}
