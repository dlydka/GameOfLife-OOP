package com.lydka.plants;

import com.lydka.Plant;
import com.lydka.Organism;
import com.lydka.Commentator;
import com.lydka.World;
import com.lydka.Position;

import java.awt.*;

public class Guarana extends Plant {
    private static final int GUARANA_STRENGTH_BOOST = 3;
    private static final int GUARANA_STRENGTH = 0;
    private static final int PLANT_INITIATIVE = 0;

    public Guarana(World world, Position position, int birth) {
        super(OrganismType.GUARANA, world, position,
                birth, GUARANA_STRENGTH, PLANT_INITIATIVE);
        setColour(new Color(48, 16, 80));
    }

    @Override
    public String getSymbol(){ return "g"; }

    @Override
    public String toString() {
        return "Guarana";
    }

    @Override
    public boolean attackAction(Organism attacker, Organism victim) {
        Position pos = this.getPosition();
        getWorld().deleteOrganism(this);
        attacker.move(pos);
        Commentator.addComment(attacker.organismInformation() + " zjada " + this.organismInformation()
                               + "  i zwieksza swoja sile na " + (GUARANA_STRENGTH_BOOST + attacker.getStrength()));
        attacker.setStrength(attacker.getStrength() + GUARANA_STRENGTH_BOOST);
        return true;
    }
}
