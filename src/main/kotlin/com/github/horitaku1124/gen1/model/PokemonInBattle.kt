package com.github.horitaku1124.gen1.model

import com.github.horitaku1124.gen1.model.ChangeStatusMove.StatusChanged
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

/**
 * https://wiki.xn--rckteqa2e.com/wiki/%E3%82%B9%E3%83%86%E3%83%BC%E3%82%BF%E3%82%B9
 */
class PokemonInBattle(var poke: PokemonInBall) {
  var hitpoint: Int
  val hitpointMax: Int
  val attack: Int
  val defence: Int
  val special: Int
  val speed: Int
  val name: String

  private var attackModLevel = 0
  private var defenceModLevel = 0
  private var attackMod = 1f
  private var defenceMod = 1f
  private var specialMod = 1f

  private fun floor(d: Double): Int = d.toInt()
  private fun floor(d: Int): Int = d

  // TODO この辺PokemonInBallに移動
  init {
    // TODO これらの値はボール内で決まっている
    hitpointMax = realHitpoint(poke.level,
      poke.pokemon.baseStats.hitPoint,
      poke.individualValues.hitPoint,
      poke.effortValue.hitPoint
    )
    hitpoint = hitpointMax

    attack = realStatus(
      poke.level,
      poke.pokemon.baseStats.attack,
      poke.individualValues.attack,
      poke.effortValue.attack
    )
    defence = realStatus(
      poke.level,
      poke.pokemon.baseStats.defence,
      poke.individualValues.defence,
      poke.effortValue.defence
    )
    special = realStatus(
      poke.level,
      poke.pokemon.baseStats.special,
      poke.individualValues.special,
      poke.effortValue.special
    )
    speed = realStatus(
      poke.level,
      poke.pokemon.baseStats.speed,
      poke.individualValues.speed,
      poke.effortValue.speed
    )
    name = poke.pokemon.name
  }

  override fun toString(): String {
    return poke.pokemon.name + ": Lv." + poke.level + " "  + hitpoint + "/" + hitpointMax +
        " A:" + attack +
        " B:" + defence +
        " S:" + speed +
        " C:" + special
  }

  fun realStatus(レベル: Int, 種族値: Int, 個体値: Int, 努力値: Int): Int {
    // floor({(種族値+個体値)×2+min(63,floor(floor(1+√努力値)÷4))}×レベル÷100)+5
    return floor(
      (
        (種族値+個体値)*2+min(63,floor(floor(1+sqrt(努力値.toDouble()))/4))
      ) * レベル/100
        ) + 5
  }

  fun realHitpoint(レベル: Int, 種族値: Int, 個体値: Int, 努力値: Int): Int {
    // floor({(種族値+個体値)×2+min(63,floor(floor(1+√努力値)÷4))}×レベル÷100)+レベル+10
    return floor(
      (
          (種族値 + 個体値)*2+min(63,floor(floor(1+sqrt(努力値.toDouble()))/4))
          ) * レベル / 100
    ) +レベル+10
  }

  fun description(): String {
    return poke.pokemon.name + ": " + hitpoint + "/" + hitpointMax +
        " Attack:" + attack +
        " Defence:" + defence +
        " Speed:" + speed +
        " Special:" + special +
        "\n Base: " + poke.pokemon.baseStats+
        "\n Effort: " + poke.effortValue +
        "\n Individual: " + poke.individualValues + "\n"
  }

  fun reset() {
    hitpoint = hitpointMax
  }

  fun level(): Int {
    return poke.level
  }

  fun attackModBy(change: Int): StatusChanged {
    val before = attackModLevel
    attackModLevel += change
    if (attackModLevel > 6) {
      attackModLevel = 6
    }
    if (attackModLevel < -6) {
      attackModLevel = -6
    }

    attackMod = if (attackModLevel < 0) {
      (2.0 / (2 + abs(attackModLevel))).toFloat()
    } else {
      (2.0 + attackModLevel / 2).toFloat()
    }
    return StatusChanged(attackModLevel - before, attackModLevel)
  }

  fun defenceModLevel(change: Int): StatusChanged {
    val before = defenceModLevel
    defenceModLevel += change
    if (defenceModLevel > 6) {
      defenceModLevel = 6
    }
    if (defenceModLevel < -6) {
      defenceModLevel = -6
    }

    defenceMod = if (defenceModLevel < 0) {
      (2.0 / (2 + abs(defenceModLevel))).toFloat()
    } else {
      (2.0 + defenceModLevel / 2).toFloat()
    }
    return StatusChanged(defenceModLevel - before, defenceModLevel)
  }

  fun getModifiedAttack(isCritical: Boolean): Int {
    return if (isCritical) attack else (attack * attackMod).toInt()
  }
  fun getModifiedSpecial(isCritical: Boolean): Int {
    return if (isCritical) special else (special * specialMod).toInt()
  }
  fun getModifiedDefence(isCritical: Boolean): Int {
    return if (isCritical) defence else (defence * defenceMod).toInt()
  }

  var allEvents = hashMapOf<String, EventMove.Eventes> ()
  fun setEvent(eventKey: String, onEvent: EventMove.Eventes): Boolean {
    if (allEvents.containsKey(eventKey)) {
      return false
    }
    allEvents[eventKey] = onEvent
    return true
  }
  fun removeEvent(eventKey: String) {
    if (allEvents.containsKey(eventKey)) {
      allEvents.remove(eventKey)
    }
  }
  var condition = hashMapOf<String, String>()
}
