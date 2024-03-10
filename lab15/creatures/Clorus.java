package creatures;

import huglife.*;

import java.awt.Color;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature {
    private int r;
    private int g;
    private int b;

    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    public Clorus() {
        this(1);
    }

    @Override
    //red = 34, green = 0, blue = 231
    public Color color() {
        return color(r, g, b);
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public void move() {
        energy -= 0.03;
    }

    @Override
    public void stay() {
        energy -= 0.01;
    }

    @Override
    public Clorus replicate() {
        energy /= 2;
        return new Clorus(energy);
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");

        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if(!plips.isEmpty()) {
            Direction moveDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, moveDir);
        } else if(energy >= 1) {
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, moveDir);
        } else {
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.MOVE, moveDir);
        }
    }
}
