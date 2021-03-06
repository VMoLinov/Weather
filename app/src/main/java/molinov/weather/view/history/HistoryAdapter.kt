package molinov.weather.view.history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_history_recycler_item.view.*
import molinov.weather.R
import molinov.weather.model.Weather
import molinov.weather.model.conditionsMap

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {

    private var data: List<Weather> = arrayListOf()

    fun setData(data: List<Weather>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_history_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        Log.d("data", data.toString())
        return data.size
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: Weather) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                Log.d("itemView", data.toString())
                itemView.recyclerViewItem.text =
                    String.format(
                        "%s %d %s %d %f",
                        data.city.city,
                        data.temperature,
                        conditionsMap[data.condition],
                        data.pressure_mm,
                        data.wind_speed
                    )
                itemView.setOnClickListener {
                    Toast.makeText(
                        itemView.context, "on click: ${data.city.city}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
