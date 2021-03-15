package com.kata.tennis;



import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.kata.tennis.core.Game;
import com.kata.tennis.core.UserInterface;

import groovy.lang.GroovyClassLoader;

@SpringBootTest
class TennisApplicationTests {

	private final GroovyClassLoader loader;

	public TennisApplicationTests() {
		// TODO Auto-generated constructor stub
		loader = new GroovyClassLoader(this.getClass().getClassLoader());
	}

	@Test
	void contextLoads() {

		Class calcClass;
		try {
//			System.out.println(loader);
//			calcClass = loader.parseClass(
//					new File("src/test/java/com/kata/tennis/", "UserInterfaceSpec.groovy"));
//			GroovyObject calc = (GroovyObject) calcClass.newInstance();
//			calc.invokeMethod("setup", null);
//			
//			calcClass = loader.parseClass(
//					new File("src/test/java/com/kata/tennis/", "ScoreSpec.groovy"));
//			calc = (GroovyObject) calcClass.newInstance();
//			calc.invokeMethod("setup", null);
			
			Game game = new Game(new UserInterface(), "Guru", "Tanu");
	        
			int randomPlayerId = game.scoreRandomPoint();
			int actual = randomPlayerId == 1 ? 1 : 2;
			assertEquals(actual, randomPlayerId);
			
			
		} catch (CompilationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testScorePoint() {
		
		Game game = new Game(new UserInterface(), "Guru", "Tanu");
		
		List<String> possibleScores =
			Arrays.asList("Love - All","Fifteen - All","Thirty - All","Deuce","Fifteen - Love",
				 "Love - Fifteen","Thirty - Love","Love - Thirty","Forty - Love",
				 "Love - Forty","Thirty - Fifteen","Fifteen - Thirty","Forty - Fifteen",
				 "Fifteen - Forty","Forty - Thirty","Thirty - Forty",
		         "Advantage Guru","Advantage Tanu","Win for Guru","Win for Tanu");
		boolean hasPossibleScore = possibleScores.indexOf(game.getScore()) > -1;
		assertEquals(hasPossibleScore, true);
	}
}
