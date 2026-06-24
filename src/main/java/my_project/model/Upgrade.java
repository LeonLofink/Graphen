package my_project.model;

import KAGO_framework.model.abitur.datenstrukturen.ComparableContent;

public class Upgrade implements ComparableContent<Upgrade> {

    private double value; // Spectrum: -1.0 (Pure Health) to 1.0 (Pure Speed)
    private String name;
    private int cost;
    private boolean purchased;

    public Upgrade(double value) {
        this.value = value;
        this.purchased = false;
        this.cost = (int) (10 + (Math.abs(value) * 40)); // Cost scales up for specialized upgrades

        // Generate names based on where the value falls on the spectrum
        if (value == -1.0) {
            this.name = "Ultimate Titan Core (+50 Max HP)";
        } else if (value == 1.0) {
            this.name = "Hyperdrive Overclock (+50 Speed)";
        } else if (value < -0.3) {
            this.name = "Heavy Shielding (+HP / minor Speed)";
        } else if (value > 0.3) {
            this.name = "Lightweight Thrusters (+Speed / minor HP)";
        } else {
            this.name = "Balanced Matrix (+HP & +Speed)";
        }
    }

    public double getValue() { return value; }
    public String getName() { return name; }
    public int getCost() { return cost; }
    public boolean isPurchased() { return purchased; }
    public void buy() { this.purchased = true; }

    // --- NRW Framework ComparableContent Requirements ---
    @Override
    public boolean isLess(Upgrade pContent) {
        if (pContent == null) return false;
        return this.value < pContent.getValue();
    }

    @Override
    public boolean isEqual(Upgrade pContent) {
        if (pContent == null) return false;
        return Math.abs(this.value - pContent.getValue()) < 0.0001;
    }

    @Override
    public boolean isGreater(Upgrade pContent) {
        if (pContent == null) return false;
        return this.value > pContent.getValue();
    }
}