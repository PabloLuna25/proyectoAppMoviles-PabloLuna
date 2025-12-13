package Util

import Data.Reminder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pulsetasks.R

class ReminderAdapter(
    private val reminders: MutableList<Reminder>,
    private val onEdit: (Reminder) -> Unit,
    private val onDelete: (Reminder) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtTitle)
        val date: TextView = view.findViewById(R.id.txtDate)
        val time: TextView = view.findViewById(R.id.txtTime)
        val image: ImageView = view.findViewById(R.id.imgReminder)
        val menu: ImageView = view.findViewById(R.id.btnMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = reminders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reminder = reminders[position]

        holder.title.text = reminder.title
        holder.date.text = "${reminder.day}/${reminder.month}/${reminder.year}"
        holder.time.text = "%02d:%02d".format(reminder.hour, reminder.minute)

        if (!reminder.imageUrl.isNullOrEmpty()) {
            holder.image.visibility = View.VISIBLE
            Glide.with(holder.image.context)
                .load(reminder.imageUrl)
                .into(holder.image)
        }

        holder.menu.setOnClickListener {
            showPopup(it, reminder, onEdit, onDelete)
        }
    }

    private fun showPopup(
        view: View,
        reminder: Reminder,
        onEdit: (Reminder) -> Unit,
        onDelete: (Reminder) -> Unit
    ) {
        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(R.menu.reminder_menu, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_edit -> onEdit(reminder)
                R.id.action_delete -> onDelete(reminder)
            }
            true
        }
        popup.show()
    }
}