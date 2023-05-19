package edu.skku.cs.pa2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

class MazeListAdapter(context: Context, resource: Int, mazeData: List<MazeData>) :
    ArrayAdapter<MazeData>(context, resource, mazeData) {
    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.maze_entry, parent, false)

        val mazeNameTextView = view.findViewById<TextView>(R.id.maze_name)
        val mazeSizeTextView = view.findViewById<TextView>(R.id.maze_size)
        val mazeStartButton = view.findViewById<Button>(R.id.maze_start)

        val maze = getItem(position)

        mazeNameTextView.text = maze?.name
        mazeSizeTextView.text = maze?.size.toString()
        mazeStartButton.setOnClickListener {
            val intent = android.content.Intent(context, MazeActivity::class.java)
            intent.putExtra("mazeName", maze?.name)
            startActivity(context, intent, null)
        }

        return view
    }
}
