package com.github.horitaku1124.gen1.battle

import com.github.horitaku1124.gen1.model.AttackMove
import com.github.horitaku1124.gen1.model.PokemonInBattle
import com.github.horitaku1124.util.RandomUtil
import com.github.horitaku1124.common.Type
import kotlin.math.roundToInt

/**
 * https://wiki.xn--rckteqa2e.com/wiki/%E3%83%80%E3%83%A1%E3%83%BC%E3%82%B8#.E7.AC.AC.E4.B8.80.E4.B8.96.E4.BB.A3
 */
object Damage {
  private val SpecialTypes = setOf(
    Type.FIRE, Type.WATER, Type.LEAF
  )

  data class DamageResult(var isMissed: Boolean, var amount: Int, var isCritical: Boolean, var compatibility: Int)

  fun calcDamage(offence: PokemonInBattle, defence: PokemonInBattle, move: AttackMove): DamageResult {
    if (RandomUtil.missed(move.accuracy)) {
      return DamageResult(true, 0, false, 0)
    }

    val isCritical = isCritical(offence.poke.pokemon.baseStats.speed)
    val criticalRate = if (isCritical) {
      2.0
    } else {
      1.0
    }
    val sameTypeRatio = if (offence.poke.types.contains(move.type)) 1.5 else 1.0
    val compatibility = TypeCompatibility.calc(move, defence)
    val isSpecial = SpecialTypes.contains(move.type)

    var damage:Int = (offence.level() * criticalRate * 2.toFloat() / 5 + 2).roundToInt()
    damage =  if (isSpecial) {
      (damage.toFloat() * move.attack * offence.getModifiedAttack(isCritical) / defence.getModifiedDefence(isCritical)).roundToInt()
    } else {
      (damage.toFloat() * move.attack * offence.getModifiedSpecial(isCritical) / defence.getModifiedSpecial(isCritical)).roundToInt()
    }

    damage = (damage.toFloat() / 50 + 2).roundToInt()
    damage = (damage * RandomUtil.getDamageRandom()).roundToInt()
    damage = (damage * sameTypeRatio).roundToInt()
    damage = (damage * compatibility / 100f).roundToInt()
    damage %= 1024
    return DamageResult(false, damage, isCritical, compatibility)
  }

  fun isCritical(speed: Int): Boolean {
    return (speed / 2) > RandomUtil.rand.nextInt(256)
  }

  fun applyDamage(defence: PokemonInBattle, amount: Int) {
    defence.hitpoint -= amount
    if (defence.hitpoint < 0) {
      defence.hitpoint = 0
    }
  }

  fun cureDamage(defence: PokemonInBattle, amount: Int) {
    defence.hitpoint += amount
    if (defence.hitpoint > defence.hitpointMax) {
      defence.hitpoint = defence.hitpointMax
    }
  }
}
