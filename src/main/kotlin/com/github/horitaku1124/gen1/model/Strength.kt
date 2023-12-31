package com.github.horitaku1124.gen1.model

class Strength {
  var attack: Int = 0
  var defence: Int = 0
  var special: Int = 0
  var speed: Int = 0
  var hitPoint: Int = 0

  override fun toString(): String {
    return String.format("Attack:%d Defence:%d Special:%d Speed:%d HitPoint:%d",
      attack, defence, special, speed, hitPoint
    )
  }
}
