package com.github.horitaku1124.gen1.model

import com.github.horitaku1124.common.Type

class ChangeStatusMove(type: Type, name: String, powerPoint: Int, accuracy: Int, var modify: Modification) :
  Move(type, name, powerPoint, accuracy) {

  interface Modification {
    fun statusChange(offence: PokemonInBattle, defense: PokemonInBattle): StatusChanged
  }

  data class StatusChanged(val changedLevel: Int, val afterLevel: Int)
}
