package com.github.horitaku1124.gen1.battle

import com.github.horitaku1124.gen1.model.*
import com.github.horitaku1124.util.RandomUtil
import org.slf4j.LoggerFactory

/**
 * 第1世代（赤・緑・青・黄）
 * https://pokemon-wiki.net/%e3%83%80%e3%83%a1%e3%83%bc%e3%82%b8%e8%a8%88%e7%ae%97%e5%bc%8f
 * https://wiki.xn--rckteqa2e.com/wiki/%E3%83%80%E3%83%A1%E3%83%BC%E3%82%B8#.E7.AC.AC.E4.B8.80.E4.B8.96.E4.BB.A3
 * http://alain.hatenablog.com/entry/20140403/p1
 */
object Turn {
  private val logger = LoggerFactory.getLogger(Turn.javaClass)
  fun startTurn(player1: PokemonInBattle,
                player2: PokemonInBattle,
                player1Move: Move,
                player2Move: Move
                ): TurnResult {
    val player1IsFaster = player1.speed > player2.speed
    val (faster, slower) = if (player1IsFaster) Pair(player1,player2) else Pair(player2,player1)
    val (fasterMove, slowerMove) = if (player1IsFaster) Pair(player1Move,player2Move) else Pair(player2Move,player1Move)

    useMove(faster, slower, fasterMove)
    if (slower.hitpoint == 0) {
      logger.debug(" ${slower.name} is down")
      return TurnResult(!player1IsFaster, player1IsFaster)
    }
    useMove(slower, faster, slowerMove)
    if (faster.hitpoint == 0) {
      logger.debug(" ${faster.name} is down")
      return TurnResult(player1IsFaster, !player1IsFaster)
    }

    faster.allEvents.forEach{ev ->
      ev.value.onTurnEnd(faster, slower)
    }
    slower.allEvents.forEach{ev ->
      ev.value.onTurnEnd(slower, faster)
    }
    if (faster.hitpoint == 0) {
      logger.debug(" ${faster.name} is down")
      return TurnResult(player1IsFaster, !player1IsFaster)
    }
    if (slower.hitpoint == 0) {
      logger.debug(" ${slower.name} is down")
      return TurnResult(!player1IsFaster, player1IsFaster)
    }
    logger.debug("Me: {}", faster)
    logger.debug("Op: {}", slower)

    return TurnResult(player1Defeat = false, player2Defeat = false)
  }

  data class TurnResult (val player1Defeat: Boolean, val player2Defeat: Boolean)

  fun useMove(offence: PokemonInBattle, defence: PokemonInBattle, move: Move) {
    logger.trace(offence.name + "'s " + move.name)
    if (move is AttackMove) {
      attack(offence, defence, move)
    } else if (move is ChangeStatusMove) {
      if (RandomUtil.missed(move.accuracy)) {
        logger.trace(offence.name + "'s " + move.name + " missed")
        return
      }
      move.modify.statusChange(offence, defence)
    } else if (move is EventMove) {
      move.attach(offence, defence)
    }
  }

  fun attack(offence: PokemonInBattle, defence: PokemonInBattle, move: AttackMove) {
    val damage = Damage.calcDamage(offence, defence, move)
    if (damage.isMissed){
      logger.trace(offence.name + "'s " + move.name + " missed")
      return
    }
    Damage.applyDamage(defence, damage.amount)
    logger.trace(" " +
        (if(damage.isCritical) "Critical" else "") +
        (if(damage.compatibility > 100) " Effective" else "") +
        (if(damage.compatibility < 100) " Not effective" else "") +
        " ${damage.amount} damaged to " + defence.name + " " + defence.hitpoint + "/" + defence.hitpointMax)
    logger.trace(" ModifiedAttack=" + offence.getModifiedAttack(damage.isCritical) + " ModifiedDefence= " + defence.getModifiedDefence(damage.isCritical))
//      logger.trace(" A=" + (damage.toFloat() * move.attack * offence.getModifiedAttack() / defence.getModifiedDefence()))
  }
}
