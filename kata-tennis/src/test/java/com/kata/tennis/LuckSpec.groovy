package com.kata.tennis

import org.springframework.boot.test.context.SpringBootTest

import spock.lang.Specification

@SpringBootTest
class LuckSpec extends Specification {

    def 'Should randomly decide between player one or two winning the point'() {
        given:
        Integer pointWinnerUserId = Luck.randomPointWinner()

        expect:
        pointWinnerUserId == 1 || pointWinnerUserId == 2
    }
}
