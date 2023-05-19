package edu.skku.cs.pa2

class MazeCell {
    var top: Boolean = false
    var bottom: Boolean = false
    var left: Boolean = false
    var right: Boolean = false
    var isPlayer: Boolean = false
    var isGoal: Boolean = false
    var isHint: Boolean = false

    override fun toString(): String {
        return "MazeCell(top=$top, bottom=$bottom, left=$left, right=$right, isPlayer=$isPlayer, isGoal=$isGoal, isHint=$isHint)"
    }


}