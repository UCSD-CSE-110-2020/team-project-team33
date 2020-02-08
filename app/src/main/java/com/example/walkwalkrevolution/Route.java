package com.example.walkwalkrevolution;

public class Route {
    private String name;
    private String startLoc;
    private Walk walk;
    //Features eventually
    private boolean favorite;

    // Constructor
    public Route(String name, String startLocation, Walk walk){
        this.name = name;
        this.startLoc = startLocation;
        this.walk = walk;
    }

    // Name setter
    public void setName(String name){
        this.name = name;
    }

    // Name getter
    public String getName(){
        return this.name;
    }

    // startLoc setter
    public void setStartLoc(String startLoc) {
        this.startLoc = startLoc;
    }

    // startLoc getter
    public String setStartLoc() {
        return this.startLoc;
    }
    // favorite setter
    public void setFavorite(boolean fav) {
        this.favorite = fav;
    }

    // startLoc getter
    public boolean setFavorite() {
        return this.favorite;
    }

    // walk getter
    public Walk getWalk() {
        return this.walk;
    }
}
