package com.github.horitaku1124.gen1.data

import com.github.horitaku1124.common.Type.*
import com.github.horitaku1124.gen1.model.Pokemon
import com.github.horitaku1124.gen1.model.Strength

object PokemonData {
  val Bulbasaur = Pokemon().also { poke ->
    poke.name = "フシギダネ"
    poke.types = setOf(LEAF, POISON)
    poke.baseStats = Strength().also {
      it.hitPoint = 45
      it.attack = 49
      it.defence = 49
      it.special = 65
      it.speed = 45
    }
  }
  val Charmander = Pokemon().also { poke ->
    poke.name = "ヒトカゲ"
    poke.types = setOf(FIRE)
    poke.baseStats = Strength().also {
      it.hitPoint = 39
      it.attack = 52
      it.defence = 43
      it.special = 50
      it.speed = 65
    }
  }
  val Squirtle = Pokemon().also { poke ->
    poke.name = "ゼニガメ"
    poke.types = setOf(WATER)
    poke.baseStats = Strength().also {
      it.hitPoint = 44
      it.attack = 48
      it.defence = 65
      it.special = 50
      it.speed = 43
    }
  }
  val Geodude = Pokemon().also { poke ->
    poke.name = "イシツブテ"
    poke.types = setOf(ROCK, GROUND)
    poke.baseStats = Strength().also {
      it.hitPoint = 40
      it.attack = 80
      it.defence = 100
      it.special = 30
      it.speed = 20
    }
  }
}
