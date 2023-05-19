package edu.skku.cs.pa2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.runBlocking

class MazeActivity : AppCompatActivity() {
    private var turnCount = 0
    private var maze: Maze = Maze()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)
        val mazeName = intent.getStringExtra("mazeName")

        runBlocking {
            maze.loadMaze(mazeName!!)
            val mazeAdapter = MazeAdapter(
                this@MazeActivity, maze
            )
            renderMaze(mazeAdapter)
            Log.d("maze", maze.toString())
        }

        val leftButton: Button = findViewById(R.id.leftButton)
        val rightButton: Button = findViewById(R.id.rightButton)
        val upButton: Button = findViewById(R.id.upButton)
        val downButton: Button = findViewById(R.id.downButton)
        val turnTextView: TextView = findViewById(R.id.turnTextView)
        val hintButton: Button = findViewById(R.id.hintButton)

        hintButton.setOnClickListener {
            val hintCell = maze.findNextCell()
            val y = hintCell.first
            val x = hintCell.second
            if (y != -1 && x != -1) {
                maze.cells[y][x].isHint = true
                renderMaze(MazeAdapter(this@MazeActivity, maze))
            }
            hintButton.isEnabled = false
        }

        leftButton.setOnClickListener {
            if (maze.moveLeft()) {
                turnCount++
                if (maze.cells[maze.playerY][maze.playerX].isHint) {
                    maze.cells[maze.playerY][maze.playerX].isHint = false
                }
                renderMaze(MazeAdapter(this@MazeActivity, maze))
                turnTextView.text = "Turn: $turnCount"
            }
            if (maze.cells[maze.playerY][maze.playerX].isGoal) {
                Toast.makeText(this, "Finish!", Toast.LENGTH_SHORT).show()
            }
        }

        rightButton.setOnClickListener {
            if (maze.moveRight()) {
                turnCount++
                if (maze.cells[maze.playerY][maze.playerX].isHint) {
                    maze.cells[maze.playerY][maze.playerX].isHint = false
                }
                renderMaze(MazeAdapter(this@MazeActivity, maze))
                turnTextView.text = "Turn: $turnCount"
            }
            if (maze.cells[maze.playerY][maze.playerX].isGoal) {
                Toast.makeText(this, "Finish!", Toast.LENGTH_SHORT).show()
            }
        }

        upButton.setOnClickListener {
            if (maze.moveUp()) {
                turnCount++
                if (maze.cells[maze.playerY][maze.playerX].isHint) {
                    maze.cells[maze.playerY][maze.playerX].isHint = false
                }
                renderMaze(MazeAdapter(this@MazeActivity, maze))
                turnTextView.text = "Turn: $turnCount"
            }
            if (maze.cells[maze.playerY][maze.playerX].isGoal) {
                Toast.makeText(this, "Finish!", Toast.LENGTH_SHORT).show()
            }
        }

        downButton.setOnClickListener {
            if (maze.moveDown()) {
                turnCount++
                if (maze.cells[maze.playerY][maze.playerX].isHint) {
                    maze.cells[maze.playerY][maze.playerX].isHint = false
                }
                renderMaze(MazeAdapter(this@MazeActivity, maze))
                turnTextView.text = "Turn: $turnCount"
            }
            if (maze.cells[maze.playerY][maze.playerX].isGoal) {
                Toast.makeText(this, "Finish!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renderMaze(mazeAdapter: MazeAdapter) {
        val mazeGridView = findViewById<GridView>(R.id.mazeGridView)
        mazeGridView.numColumns = maze.size
        mazeGridView.adapter = mazeAdapter
        Log.d("count", mazeAdapter.count.toString())
    }

}