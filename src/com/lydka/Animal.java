package com.lydka;

import java.util.Random;

public abstract class Animal extends Organism {
    private int range;
    private double moveChance;

    public Animal(OrganismType type, World world,
                   Position position, int birth, int strength, int initiative) {
        super(type, world, position, birth, strength, initiative);
        setReproduced(false);
        setReproduceChance(0.5);
    }

    @Override
    public void action() {
        for (int i = 0; i < range; i++) {
            Position nextPosition = planMove();

            if (getWorld().isPositionTaken(nextPosition)
                    && getWorld().whatOnPosition(nextPosition) != this) {
                collision(getWorld().whatOnPosition(nextPosition));
                break;
            } else if (getWorld().whatOnPosition(nextPosition) != this)
                move(nextPosition);
        }
    }

    @Override
    public void collision(Organism other) {
        if (this.getImortal() || other.getImortal()) {
            if (getStrength() > other.getStrength()) {
                getWorld().deleteOrganism(other);
                move(other.getPosition());
                Commentator.addComment(organismInformation() + " zabija " + other.organismInformation());
            }
            return;
        }

        if (getType() == other.getType()) {
            Random rand = new Random();
            int random = rand.nextInt(100);
            if (random < getReproduceChance() * 100)
                reproduce(other);
        } else {
            if (other.attackAction(this, other)) return;
            if (attackAction(this, other)) return;

            if (getStrength() >= other.getStrength()) {
                getWorld().deleteOrganism(other);
                move(other.getPosition());
                Commentator.addComment(organismInformation() + " zabija " + other.organismInformation());
            } else {
                getWorld().deleteOrganism(this);
                Commentator.addComment(other.organismInformation() + " zabija " + organismInformation());
            }
        }
    }

    @Override
    public boolean isAnimal() {
        return true;
    }

    protected Position planMove() {
        Random rand = new Random();
        int random = rand.nextInt(100);
        if (random >= (int) (moveChance * 100)) return getPosition();
        else return randomPosition(getPosition());
    }

    private void reproduce(Organism other) {
        if (this.getReproduced() || other.getReproduced()) return;
        Position pos1 = this.randomEmptyPosition(getPosition());
        if (pos1.equals(getPosition())) {
            Position pos2 = other.randomEmptyPosition(other.getPosition());
            if (pos2.equals(other.getPosition())) return;
            else {
                Organism organism = OrganismCreator.createOrganism(getType(), this.getWorld(), pos2);
                Commentator.addComment("Urodzil sie " + organism.organismInformation());
                getWorld().addOrganism(organism);
                setReproduced(true);
                other.setReproduced(true);
            }
        } else {
            Organism organism = OrganismCreator.createOrganism(getType(), this.getWorld(), pos1);
            Commentator.addComment("Urodzil sie " + organism.organismInformation());
            getWorld().addOrganism(organism);
            setReproduced(true);
            other.setReproduced(true);
        }
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setMoveChance(double moveChance) {
        this.moveChance = moveChance;
    }

}