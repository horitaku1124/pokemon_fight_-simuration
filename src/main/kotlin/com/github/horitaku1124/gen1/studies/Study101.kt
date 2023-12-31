package com.github.horitaku1124.gen1.studies

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.github.horitaku1124.gen1.battle.Turn
import com.github.horitaku1124.gen1.data.MoveData.Bubble
import com.github.horitaku1124.gen1.data.MoveData.DefenseCurl
import com.github.horitaku1124.gen1.data.MoveData.Ember
import com.github.horitaku1124.gen1.data.MoveData.Growl
import com.github.horitaku1124.gen1.data.MoveData.LeechSeed
import com.github.horitaku1124.gen1.data.MoveData.Scratch
import com.github.horitaku1124.gen1.data.MoveData.Tackle
import com.github.horitaku1124.gen1.data.MoveData.TailWhip
import com.github.horitaku1124.gen1.data.MoveData.VineWhip
import com.github.horitaku1124.gen1.data.PokemonData.Bulbasaur
import com.github.horitaku1124.gen1.data.PokemonData.Charmander
import com.github.horitaku1124.gen1.data.PokemonData.Geodude
import com.github.horitaku1124.gen1.data.PokemonData.Squirtle
import com.github.horitaku1124.gen1.model.Move
import com.github.horitaku1124.gen1.model.PokemonInBall
import com.github.horitaku1124.gen1.model.PokemonInBattle
import com.github.horitaku1124.gen1.model.Strength
import com.github.horitaku1124.util.RandomUtil
import org.slf4j.LoggerFactory

fun main() {
  val logger = LoggerFactory.getLogger("STDOUT")
  val tries = 1000
  val bulbasaurInBall = PokemonInBall(14, Bulbasaur, 1, 0, listOf(Tackle, Growl, LeechSeed, VineWhip))
  val charmanderInBall = PokemonInBall(14, Charmander, 1, 0, listOf(Scratch, Growl, Ember))
  val squirtleInBall = PokemonInBall(14, Squirtle, 1, 0, listOf(Tackle, TailWhip, Bubble))

  val geodudeInTakeshi = PokemonInBall(12, Geodude, 1, 0, listOf(Tackle, DefenseCurl))

  val root: Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
  root.level = Level.WARN

  val theirs = geodudeInTakeshi

  val myStrategy = fun (moves: List<Move>, turn: Int): Move {
    return moves[RandomUtil.chooseOne(moves.size)]
  }
  val theirStrategy = fun (moves: List<Move>, turn: Int): Move {
    return moves[RandomUtil.chooseOne(moves.size)]
  }

  for (mine1 in listOf(bulbasaurInBall, charmanderInBall, squirtleInBall)) {
    var wonRate = 0
    for (k in 0 until tries) {
      mine1.individualValues = Strength().also {
        it.hitPoint = RandomUtil.decideIndividual()
        it.attack = RandomUtil.decideIndividual()
        it.defence = RandomUtil.decideIndividual()
        it.special = RandomUtil.decideIndividual()
        it.speed = RandomUtil.decideIndividual()
      }
      theirs.individualValues = Strength().also {
        it.hitPoint = RandomUtil.decideIndividual()
        it.attack = RandomUtil.decideIndividual()
        it.defence = RandomUtil.decideIndividual()
        it.special = RandomUtil.decideIndividual()
        it.speed = RandomUtil.decideIndividual()
      }

      val me = PokemonInBattle(mine1)
      val opponent = PokemonInBattle(theirs)

      logger.debug("Me: {}", me)
      logger.debug("Op: {}", opponent)

      for (j in 0 until 15) {
        val result = Turn.startTurn(me, opponent, myStrategy(me.poke.moves, j), theirStrategy(opponent.poke.moves, j))
        if (result.player1Defeat) {
          logger.info(opponent.name + " won")
          break
        }
        if (result.player2Defeat) {
          logger.info(me.name + " won")
          wonRate++
          break
        }
      }
    }
    println(mine1.name + " won rate=" + (wonRate * 100f / tries).toInt() + "%")
  }
}
