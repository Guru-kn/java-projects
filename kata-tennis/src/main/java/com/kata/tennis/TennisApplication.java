package com.kata.tennis;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kata.tennis.core.Game;
import com.kata.tennis.core.UserInterface;

@SpringBootApplication
public class TennisApplication {

	public static void main(String[] args) {
		SpringApplication.run(TennisApplication.class, args);
		UserInterface ui = new UserInterface();
        ui.prompt();
        
        Scanner sc = new Scanner(System.in);

        ui.print("Please enter player one name: ");
        String playerOneName = sc.nextLine();
        ui.print("Please enter player two name: ");
        String playerTwoName = sc.nextLine();
        ui.print("Press enter to start the game...");
        sc.nextLine();

        Game game = new Game(ui, playerOneName, playerTwoName);
        game.start();
        ui.exit();
	}
}
