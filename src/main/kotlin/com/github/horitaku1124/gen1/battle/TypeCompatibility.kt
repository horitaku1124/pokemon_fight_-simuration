package com.github.horitaku1124.gen1.battle

import com.github.horitaku1124.common.Type
import com.github.horitaku1124.gen1.model.Move
import com.github.horitaku1124.gen1.model.PokemonInBattle

object TypeCompatibility {
  fun calc(move: Move, defence: PokemonInBattle): Int {
    var ratio = 100
    defence.poke.types.forEach { opType ->
      when(move.type) {
        Type.FIRE -> {
          if (opType == Type.LEAF) {
            ratio *= 2
          }
          if (opType == Type.ROCK) {
            ratio /= 2
          }
        }
        Type.WATER -> {
          if (opType == Type.ROCK || opType == Type.GROUND ) {
            ratio *= 2
          }
        }
        Type.LEAF -> {
          if (opType == Type.WATER || opType == Type.ROCK || opType == Type.GROUND ) {
            ratio *= 2
          }
          if (opType == Type.FIRE) {
            ratio /= 2
          }
        }
        Type.NORMAL -> {
          if (opType == Type.ROCK) {
            ratio /= 2
          }
        }
        else -> {

        }
      }

    }
    return ratio
  }
}
