package thegame;

import thegame.cards.Minion;

import java.util.Comparator;

public class Comparators {
    public static class HealthComparator implements Comparator<Minion> {
        @Override
        public int compare(Minion o1, Minion o2) {
            return o1.getHealth() - o2.getHealth();
        }
    }
    public static class AttackComparator implements Comparator<Minion> {
        @Override
        public int compare(Minion o1, Minion o2) {
            return o1.getAttackDamage() - o2.getAttackDamage();
        }
    }
}
