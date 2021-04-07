package hu.alkfejl.model;

public enum PlayerSign {
    X('X'),
    O('O');

    private final Character value;

    PlayerSign(Character c) {
        this.value = c;
    }

    public char getValue(){
        return this.value;
    }
}
