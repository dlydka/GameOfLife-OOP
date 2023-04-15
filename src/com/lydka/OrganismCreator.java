package com.lydka;

import com.lydka.animals.*;
import com.lydka.plants.*;

public class OrganismCreator {
    public static Organism createOrganism(Organism.OrganismType type, World world, Position position) {
        return switch (type) {
            case WOLF -> new Wolf(world, position, world.getTurn());
            case SHEEP -> new Sheep(world, position, world.getTurn());
            case FOX -> new Fox(world, position, world.getTurn());
            case TURTLE -> new Turtle(world, position, world.getTurn());
            case ANTELOPE -> new Antelope(world, position, world.getTurn());
            case HUMAN -> new Human(world, position, world.getTurn());
            case GRASS -> new Grass(world, position, world.getTurn());
            case DANDELION -> new Dandelion(world, position, world.getTurn());
            case GUARANA -> new Guarana(world, position, world.getTurn());
            case GOJI -> new Goji(world, position, world.getTurn());
            case PINEBROSCHT -> new PineBroscht(world, position, world.getTurn());
            case CYBERSHEEP -> new CyberSheep(world, position, world.getTurn());
        };
    }
}
