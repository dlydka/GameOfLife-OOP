package com.lydka.animals;

import com.lydka.Animal;
import com.lydka.Organism;
import com.lydka.Commentator;
import com.lydka.World;
import com.lydka.Position;

import java.awt.*;

public class Human extends Animal {
    private static final int RANGE = 1;
    private static final int MOVE_CHANCE = 1;
    private static final int HUMAN_STRENGTH = 5;
    private static final int HUMAN_INITIATIVE = 4;
    private Direction direction;


    public Human(World world, Position position, int birth) {
        super(OrganismType.HUMAN, world, position, birth, HUMAN_STRENGTH, HUMAN_INITIATIVE);
        this.setRange(RANGE);
        this.setMoveChance(MOVE_CHANCE);
        direction = direction.NONE;
        setColour(Color.BLUE);
    }

    private void immortality() {
        randomPosition(getPosition());
        int x = getPosition().getX();
        int y = getPosition().getY();
        Organism organism = null;
        for (int i = 0; i < 4; i++) {
            if (i == 0 && !isDirectionLocked(Direction.DOWN))
                organism = getWorld().whatOnPosition(new Position(x, y + 1));
            else if (i == 1 && !isDirectionLocked(Direction.UP))
                organism = getWorld().whatOnPosition(new Position(x, y - 1));
            else if (i == 2 && !isDirectionLocked(Direction.LEFT))
                organism = getWorld().whatOnPosition(new Position(x - 1, y));
            else if (i == 3 && !isDirectionLocked(Direction.RIGHT))
                organism = getWorld().whatOnPosition(new Position(x + 1, y));

            if (organism != null) {
                this.setImortal(true);
                collision(organism);
                Commentator.addComment(organismInformation() + " umiejetnosc 'Niesmiertelnosc' uratowala czlowieka przed "
                        + organism.organismInformation());
            }
        }
    }

    @Override
    protected Position planMove() {
        int x = getPosition().getX();
        int y = getPosition().getY();
        randomPosition(getPosition());
        if (direction == Direction.NONE ||
                isDirectionLocked(direction)) return getPosition();
        else {
            if (direction == Direction.DOWN) return new Position(x, y + 1);
            if (direction == Direction.UP) return new Position(x, y - 1);
            if (direction == Direction.LEFT) return new Position(x - 1, y);
            if (direction == Direction.RIGHT) return new Position(x + 1, y);
            else return new Position(x, y);
        }
    }

    @Override
    public void action() {
        if (getIsActive()) {
            Commentator.addComment(toString() + " 'Niesmiertelnosc' jest aktywna. Pozostaly czas "
                    + (getAbilityTime() - 1) + " tur");
            immortality();
        }
        for (int i = 0; i < getRange(); i++) {
            Position nextPos = planMove();

            if (getWorld().isPositionTaken(nextPos)
                    && getWorld().whatOnPosition(nextPos) != this) {
                collision(getWorld().whatOnPosition(nextPos));
                break;
            } else if (getWorld().whatOnPosition(nextPos) != this)
                move(nextPos);
            if (getIsActive()) immortality();
        }
        direction = Direction.NONE;
        checkConditions();
    }

    @Override
    public String getSymbol(){ return ""; }

    @Override
    public String toString() {
        return "Czlowiek";
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }


    private final int ABILITY_TIME = 5;
    private final int COOLDOWN = 10;
    private boolean isAvaiable;
    private boolean isActive;
    private int abilityTime;
    private int cooldown;


    public void checkConditions() {
        if (getCooldown() > 0)
            setCooldown(getCooldown() - 1);
        if (getAbilityTime() > 0)
            setAbilityTime(getAbilityTime() - 1);
        if (getAbilityTime() == 0) {
            deactivate();
            this.setImortal(false);
        }
        if (getCooldown() == 0)
            setIsAvaiable(true);
    }

    public void activate() {
        if (cooldown == 0) {
            isActive = true;
            isAvaiable = false;
            cooldown = COOLDOWN;
            abilityTime = ABILITY_TIME;
        }
    }

    public void deactivate() {
        isActive = false;
    }

    public boolean getIsAvaiable() {
        return isAvaiable;
    }

    public void setIsAvaiable(boolean isAvaiable) {
        this.isAvaiable = isAvaiable;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getAbilityTime() {
        return abilityTime;
    }

    public void setAbilityTime(int abilityTime) {
        this.abilityTime = abilityTime;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
