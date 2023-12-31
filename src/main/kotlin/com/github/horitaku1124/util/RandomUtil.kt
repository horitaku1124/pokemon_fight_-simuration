package com.github.horitaku1124.util

import java.util.*

object RandomUtil {
  var rand: Random = Random()
  fun getDamageRandom(): Double {
    return rand.nextDouble() * 0.15 + 0.85
  }
  fun missed(accuracy: Int): Boolean {
    return rand.nextInt(256) > (accuracy * 255 / 100)
  }
  fun chooseOne(option: Int): Int {
    return rand.nextInt(option)
  }
  fun decideIndividual(): Int {
    return rand.nextInt(16)
  }
}
