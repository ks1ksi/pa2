package edu.skku.cs.pa2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.view.marginBottom

class MazeAdapter(private val context: MazeActivity, private val maze: Maze) : BaseAdapter() {
    override fun getCount(): Int = maze.size * maze.size

    override fun getItem(position: Int): Any =
        maze.cells[position / maze.size][position % maze.size]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cell = getItem(position) as MazeCell
        val view =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.maze_cell, parent, false)
        val cellImageView = view.findViewById<ImageView>(R.id.cellImageView)
        val overlayImageView = view.findViewById<ImageView>(R.id.overlayImageView)

        val dp = 350 / maze.size
        val pixel = (dp * context.resources.displayMetrics.density + 0.5).toInt()
        val marginPixel = (3 * context.resources.displayMetrics.density + 0.5).toInt()

        cellImageView.layoutParams.height = pixel
        cellImageView.layoutParams.width = pixel

        val params = cellImageView.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0, 0, 0, 0)
        if (cell.bottom) {
            params.bottomMargin = marginPixel
            cellImageView.layoutParams.height -= marginPixel
        }
        if (cell.top) {
            params.topMargin = marginPixel
            cellImageView.layoutParams.height -= marginPixel
        }
        if (cell.left) {
            params.leftMargin = marginPixel
            cellImageView.layoutParams.width -= marginPixel
        }
        if (cell.right) {
            params.rightMargin = marginPixel
            cellImageView.layoutParams.width -= marginPixel
        }
        cellImageView.layoutParams = params

        if (cell.isPlayer) {
            overlayImageView.setImageResource(R.drawable.user)
            overlayImageView.visibility = View.VISIBLE
        } else if (cell.isGoal) {
            overlayImageView.setImageResource(R.drawable.goal)
            overlayImageView.visibility = View.VISIBLE
        } else if (cell.isHint) {
            overlayImageView.setImageResource(R.drawable.hint)
            overlayImageView.visibility = View.VISIBLE
        } else {
            overlayImageView.visibility = View.GONE
        }

        return view
    }
}
