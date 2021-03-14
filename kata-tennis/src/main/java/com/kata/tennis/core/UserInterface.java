package com.kata.tennis.core;

public class UserInterface {

    public Boolean prompt(){
        System.out.println("Tennis Game starting............ ");
        return true;
    }

    public Boolean print(String toPrint){
        System.out.println(toPrint);
        return true;
    }

    public Boolean exit() {
        System.out.print("\nBye!\n\n");
        return true;
    }
}
