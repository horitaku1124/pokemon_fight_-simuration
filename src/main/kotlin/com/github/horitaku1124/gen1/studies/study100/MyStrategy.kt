package com.github.horitaku1124.gen1.studies.study100

import com.github.horitaku1124.gen1.model.Move
import com.github.horitaku1124.gen1.studies.Gene


class MyStrategy(var gene: Gene): Strategy {
  override fun chooseMove(moves: List<Move>, turn: Int): Move {
    return moves[gene.gene[turn] % moves.size]
  }
}
