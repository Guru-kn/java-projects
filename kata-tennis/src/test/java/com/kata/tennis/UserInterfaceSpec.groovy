package com.kata.tennis

import org.springframework.boot.test.context.SpringBootTest

import com.kata.tennis.core.UserInterface

import spock.lang.Specification

@SpringBootTest
class UserInterfaceSpec extends Specification {

    UserInterface ui

    def setup() {
        ui = new UserInterface()
    }

    def "User Interface should print prompt"(){
        expect:
        ui.prompt()
    }

    def "User Interface should print exit"(){
        expect:
        ui.exit()
    }

    def "User Interface should print random text"(){
        expect:
        ui.print("Random text")
    }
}
