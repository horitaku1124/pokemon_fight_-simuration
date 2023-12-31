package com.github.horitaku1124.gen1.model

/**
 * @param level 現在のレベル
 * @param pokemon 所持ポケモン
 * @param individualValues 個体値 0~15の値
 * @param effortValue 努力値　0~65536の値
 */
class PokemonInBall(
  var level: Int,
  var pokemon: Pokemon,
  var individualValues: Strength,
  var effortValue: Strength,
  var moves: List<Move>
) {
  val types get() = pokemon.types

  // 全部設定するのが面倒なとき１ステータスでOKなようにする
  constructor(
    level: Int,
    pokemon: Pokemon,
    individualValues: Int,
    effortValue: Int,
    moves: List<Move>
  ) : this(level, pokemon, Strength().also {
    it.hitPoint = individualValues
    it.attack = individualValues
    it.defence = individualValues
    it.special = individualValues
    it.speed = individualValues
  }, Strength().also {
    it.hitPoint = effortValue
    it.attack = effortValue
    it.defence = effortValue
    it.special = effortValue
    it.speed = effortValue
  },
    moves
  )

  var name = this.pokemon.name

  fun resetIndividualValues(values: Strength) {
    this.individualValues = values
    var hp = 0
    if (values.attack % 2 == 1) {
      hp += 8
    }
    if (values.defence % 2 == 1) {
      hp += 4
    }
    if (values.speed % 2 == 1) {
      hp += 2
    }
    if (values.special % 2 == 1) {
      hp += 1
    }
    this.individualValues.hitPoint = hp
  }
}
