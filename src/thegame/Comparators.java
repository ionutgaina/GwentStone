package thegame;

import thegame.cards.Minion;

import java.util.Comparator;

public final class Comparators {

    /**
     * Comparator for minion health
     */
    public static class HealthComparator implements Comparator<Minion> {
        @Override
        public int compare(final Minion o1, final Minion o2) {
            return o1.getHealth() - o2.getHealth();
        }
    }

    /**
     * Comparator for minion AttackDamage
     */
    public static class AttackComparator implements Comparator<Minion> {
        @Override
        public int compare(final Minion o1, final Minion o2) {
            return o1.getAttackDamage() - o2.getAttackDamage();
        }
    }
}
