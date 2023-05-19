package edu.skku.cs.pa2

data class MazeData(val name: String, val size: Int) {
    override fun toString(): String = "$name - size: $size"
}