package com.lydka;

import java.util.Random;

public abstract class Plant extends Organism {
    protected Plant(OrganismType type, World world,
                    Position position, int birth, int strength, int initiative) {
        super(type, world, position, birth, strength, initiative);
        setReproduceChance(0.3);
    }

    @Override
    public void action() {
        Random rand = new Random();
        int random = rand.nextInt(100);
        if (random < getReproduceChance() * 100)
            overgrowth();
    }

    @Override
    public boolean isAnimal() {
        return false;
    }


    protected void overgrowth() {
        Position pos = this.randomEmptyPosition(getPosition());
        if (pos.equals(getPosition()))
            return;
        else {
            Organism organism = OrganismCreator.createOrganism(getType(), this.getWorld(), pos);
            Commentator.addComment("Wzrasta nowa roslina " + organism.organismInformation());
            getWorld().addOrganism(organism);
        }
    }

    @Override
    public void collision(Organism other) {}

}