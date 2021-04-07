package hu.alkfejl.model;

public class Player {
    private final String name;
    private boolean isActive;
    private final PlayerSign sign;

    public Player(String name, boolean isActive, PlayerSign sign) {
        this.name = name;
        this.isActive = isActive;
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public PlayerSign getSign() {
        return sign;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
