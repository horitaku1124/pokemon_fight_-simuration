package com.github.horitaku1124.gen1.data

import com.github.horitaku1124.common.Type.*
import com.github.horitaku1124.gen1.battle.Damage
import com.github.horitaku1124.gen1.model.AttackMove
import com.github.horitaku1124.gen1.model.PokemonInBattle
import com.github.horitaku1124.gen1.model.ChangeStatusMove
import com.github.horitaku1124.gen1.model.ChangeStatusMove.Modification
import com.github.horitaku1124.gen1.model.ChangeStatusMove.StatusChanged
import com.github.horitaku1124.gen1.model.EventMove
import org.slf4j.LoggerFactory

object MoveData {
  private val logger = LoggerFactory.getLogger(MoveData.javaClass)
  val Tackle = AttackMove(NORMAL, "たいあたり", 35, 95, 35)
  val Growl = ChangeStatusMove(NORMAL, "なきごえ", 30, 100, object: Modification {
    override fun statusChange(offence: PokemonInBattle, defense: PokemonInBattle): StatusChanged {
      val changed = defense.attackModBy(-1)
      if (changed.changedLevel == 0) {
        logger.debug(defense.name + "'s attack didn't fall")
      } else {
        logger.debug(defense.name + "'s attack fell to " + changed.afterLevel)
      }
      return changed
    }
  })
  val TailWhip = ChangeStatusMove(NORMAL, "しっぽをふる", 30,100,  object: Modification {
    override fun statusChange(offence: PokemonInBattle, defense: PokemonInBattle): StatusChanged {
      val changed = defense.defenceModLevel(-1)

      if (changed.changedLevel == 0) {
        logger.debug(defense.name + "'s defence didn't fall")
      } else {
        logger.debug(defense.name + "'s defence fell to " + changed.afterLevel)
      }
      return changed
    }
  })
  val DefenseCurl = ChangeStatusMove(NORMAL, "まるくなる", 40,100,  object: Modification {
    override fun statusChange(offence: PokemonInBattle, defense: PokemonInBattle): StatusChanged {
      val changed = offence.defenceModLevel(1)
      if (changed.changedLevel == 0) {
        logger.debug(offence.name + "'s defence didn't rise")
      } else {
        logger.debug(offence.name + "'s defence rose to " + changed.afterLevel)
      }
      return changed
    }
  })
  val Scratch = AttackMove(NORMAL, "ひっかく", 30, 100, 40)
  val VineWhip = AttackMove(LEAF, "つるのムチ", 10, 100, 35)
  val Ember = AttackMove(FIRE, "ひのこ", 25, 100, 40)
  val WaterGun = AttackMove(WATER, "みずでっぽう", 25, 100, 40)
  val Bubble = AttackMove(WATER, "あわ", 30, 100, 20)
  val LeechSeed = EventMove(LEAF, "やどりぎのタネ", 10, 90, object: EventMove.EventListener{
    override fun onAttach(offence: PokemonInBattle, defense: PokemonInBattle): Boolean {
      val setEvent = defense.setEvent("LeechSeed", object : EventMove.Eventes() {
        override fun onTurnEnd(mySelf: PokemonInBattle, opponent: PokemonInBattle) {
          if (mySelf.allEvents.containsKey("LeechSeed")) {
            val drainHp = mySelf.hitpointMax / 16
            Damage.applyDamage(mySelf, drainHp)
            Damage.cureDamage(opponent, drainHp)
            logger.debug(mySelf.name + "'s HP=" + drainHp + " to " + opponent.name)
            logger.debug(mySelf.name)
            logger.debug(opponent.name)
          }
        }
      })
      if (setEvent) {
        logger.debug(defense.name + " affected by LeechSeed")
      }
      return setEvent
    }
  })
}
