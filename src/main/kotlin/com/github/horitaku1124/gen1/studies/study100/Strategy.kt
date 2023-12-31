package com.github.horitaku1124.gen1.studies.study100

import com.github.horitaku1124.gen1.model.Move

interface Strategy {
  fun chooseMove(moves: List<Move>, turn: Int): Move
}
