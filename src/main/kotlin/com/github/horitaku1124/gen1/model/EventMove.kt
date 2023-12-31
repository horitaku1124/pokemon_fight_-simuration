package com.github.horitaku1124.gen1.model

import com.github.horitaku1124.common.Type


class EventMove(type: Type, name: String, powerPoint: Int, accuracy: Int, var eventListener: EventListener) :
  Move(type, name, powerPoint, accuracy) {
  interface EventListener {
    fun onAttach(offence: PokemonInBattle, defense: PokemonInBattle): Boolean
  }

  fun attach(offence: PokemonInBattle, defense: PokemonInBattle): Boolean {
    return eventListener.onAttach(offence, defense)
  }

  open class Eventes {
    open fun onTurnEnd(mySelf: PokemonInBattle, opponent: PokemonInBattle) {

    }
  }
}
