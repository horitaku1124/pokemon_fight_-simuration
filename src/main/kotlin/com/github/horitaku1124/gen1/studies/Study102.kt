package com.github.horitaku1124.gen1.studies

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.github.horitaku1124.gen1.battle.Turn
import com.github.horitaku1124.gen1.data.MoveData.DefenseCurl
import com.github.horitaku1124.gen1.data.MoveData.Ember
import com.github.horitaku1124.gen1.data.MoveData.Growl
import com.github.horitaku1124.gen1.data.MoveData.Scratch
import com.github.horitaku1124.gen1.data.MoveData.Tackle
import com.github.horitaku1124.gen1.data.PokemonData
import com.github.horitaku1124.gen1.model.Move
import com.github.horitaku1124.gen1.model.PokemonInBall
import com.github.horitaku1124.gen1.model.PokemonInBattle
import com.github.horitaku1124.gen1.model.Strength
import com.github.horitaku1124.gen1.studies.study100.MyStrategy
import com.github.horitaku1124.gen1.studies.study100.Strategy
import com.github.horitaku1124.util.RandomUtil
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

const val tries = 1000
const val maxTurn = 15

val logger = LoggerFactory.getLogger("STDOUT")
class Gene(size: Int) {
  val gene: Array<Int> = Array(size) { 0 }
}

fun trySimulation(
  mine1: PokemonInBall,
  theirs: PokemonInBall,
  myStrategy: Strategy,
  theirStrategy: Strategy,
) : Float{
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

    for (j in 0 until maxTurn) {
      val result = Turn.startTurn(
        me,
        opponent,
        myStrategy.chooseMove(me.poke.moves, j),
        theirStrategy.chooseMove(opponent.poke.moves, j)
      )
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
  return (wonRate * 100f / tries)
}

data class GeneResult(var score: Float, var id: Int, var gene: Gene)

fun intersect(parent1: Gene, parent2: Gene): Gene {
  val mixed = Gene(parent1.gene.size)
  for (i in 0 until mixed.gene.size) {
    if (i % 2 == 0) {
      mixed.gene[i] = parent1.gene[i]
    } else {
      mixed.gene[i] = parent2.gene[i]
    }
  }
  return mixed
}


val rand = Random()

fun nextGenerations(gene: Gene, num: Int): List<Gene> {
  val newGenes = arrayListOf<Gene>()
  for(i in 0 until num) {
    val newGene = Gene(gene.gene.size)
    for (j in 0 until gene.gene.size) {
      newGene.gene[j] = gene.gene[j]
    }
    newGene.gene[rand.nextInt(gene.gene.size)] = rand.nextInt(256)
    newGenes.add(newGene)
  }

  return newGenes
}

fun main() {
  val num = 20
  val charmanderInBall = PokemonInBall(14, PokemonData.Charmander, 1, 0, listOf(Scratch, Growl, Ember))
//  val charmanderInBall = PokemonInBall(8, PokemonData.Charmander, 1, 0, listOf(Scratch, Growl))

  val geodudeInTakeshi = PokemonInBall(12, PokemonData.Geodude, 1, 0, listOf(Tackle, DefenseCurl))

  val root: Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
  root.level = Level.WARN

  val geneRanking = arrayListOf<GeneResult>()


  val genes = arrayListOf<Gene>()
  for (i in 0 until num) {
    val gene = Gene(maxTurn)
    for (j in gene.gene.indices) {
      gene.gene[j] = rand.nextInt(256)
    }
    genes.add(gene)
  }

  val fw = Files.newBufferedWriter(Path.of("history.txt"))

  for (k in 0 until 100) {
    println("-------------------------")
    println("         Gen-" + (k+1))
    println("-------------------------")
    geneRanking.clear()
    for (i in 0 until num) {
      val gene = genes[i]
      val winRate = trySimulation(
        charmanderInBall,
        geodudeInTakeshi,
        MyStrategy(gene),
        object: Strategy {
          override fun chooseMove(moves: List<Move>, turn: Int): Move {
            return moves[RandomUtil.chooseOne(moves.size)]
          }
        },
      )
      geneRanking.add(GeneResult(winRate, i, gene))
    }
    geneRanking.sortByDescending { it.score }

    geneRanking.forEach {
      println("${it.id}:" + charmanderInBall.name + " won rate=" + it.score.toInt() + "%")
    }

    genes.clear()
    val nextGene = intersect(geneRanking[0].gene, geneRanking[1].gene)
    genes.addAll(
      nextGenerations(nextGene, num)
    )
    fw.write(geneRanking[0].score.toString() + "\n")
  }
  fw.close()
}
