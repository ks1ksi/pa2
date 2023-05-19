package edu.skku.cs.pa2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
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
                this@MazeActivity,
                maze
            )
            renderMaze(mazeAdapter)
            Log.d("maze", maze.toString())
        }

        val leftButton: Button = findViewById(R.id.leftButton)
        val rightButton: Button = findViewById(R.id.rightButton)
        val upButton: Button = findViewById(R.id.upButton)
        val downButton: Button = findViewById(R.id.downButton)
        val turnTextView: TextView = findViewById(R.id.turnTextView)

        leftButton.setOnClickListener {
            if (maze.moveLeft()) {
                turnCount++
                renderMaze(MazeAdapter(this@MazeActivity, maze))
                turnTextView.text = "Turn: $turnCount"
            }
        }

        rightButton.setOnClickListener {
            if (maze.moveRight()) {
                turnCount++
                renderMaze(MazeAdapter(this@MazeActivity, maze))
                turnTextView.text = "Turn: $turnCount"
            }
        }

        upButton.setOnClickListener {
            if (maze.moveUp()) {
                turnCount++
                renderMaze(MazeAdapter(this@MazeActivity, maze))
                turnTextView.text = "Turn: $turnCount"
            }
        }

        downButton.setOnClickListener {
            if (maze.moveDown()) {
                turnCount++
                renderMaze(MazeAdapter(this@MazeActivity, maze))
                turnTextView.text = "Turn: $turnCount"
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