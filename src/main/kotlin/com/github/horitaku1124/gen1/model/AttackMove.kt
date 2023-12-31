package com.github.horitaku1124.gen1.model

import com.github.horitaku1124.common.Type
import com.github.horitaku1124.gen1.model.Move

class AttackMove(type: Type,
                 name: String,
                 powerPoint: Int,
                 accuracy: Int,
                 var attack: Int) : Move(type, name, powerPoint, accuracy)
