package com.github.horitaku1124.gen1.model

import com.github.horitaku1124.common.Type

/**
 * https://pokemon.g-takumi.com/pokemon/
 * https://yakkun.com/swsh/zukan/
 */
class Pokemon {
  /**
   * 名前
   */
  lateinit var name: String

  /**
   * タイプ
   */
  lateinit var types: Set<Type>

  /**
   * 種族値
   */
  lateinit var baseStats: Strength
}
