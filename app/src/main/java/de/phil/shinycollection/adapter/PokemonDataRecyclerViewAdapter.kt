package de.phil.shinycollection.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.phil.shinycollection.R
import de.phil.shinycollection.activity.IPokemonListActivity
import de.phil.shinycollection.ShinyPokemonApplication
import de.phil.shinycollection.model.PokemonData
import de.phil.shinycollection.model.translateToCurrentLocaleLanguage
import kotlinx.android.synthetic.main.fragment_pokemondata.view.*

class PokemonDataRecyclerViewAdapter(
    private val mValues: MutableList<PokemonData>, private val activity: IPokemonListActivity
) : RecyclerView.Adapter<PokemonDataRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pokemondata, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        // "ID: "
        holder.mPokedexIdView.text = ("ID: ${item.pokedexId}")
        // "Name: "
        holder.mNameView.text = (item.name)
        // "Encounter: "

        if (item.encounterNeeded == ShinyPokemonApplication.ENCOUNTER_UNKNOWN)
            holder.mEggsNeededView.text = activity.getContext().resources.getString(R.string.encounter_unknown)
        else
            holder.mEggsNeededView.text = (activity.getContext().resources.getString(R.string.encounter_colon) + " " + item.encounterNeeded.toString())

        // "Methode: "
        val method: String = item.huntMethod.translateToCurrentLocaleLanguage()

        holder.mHuntMethodView.text = (activity.getContext().resources.getString(R.string.method_colon) + " " + method)

        Glide.with(activity.getContext())
            .load(item.getDownloadUrl())
            .placeholder(ContextCompat.getDrawable(activity.getContext(), R.drawable.placeholder_pokemon))
            .into(holder.mShinyImageView)

        with(holder.mView) {
            tag = item
            setOnLongClickListener{
                activity.onListEntryLongClick(it)
                true
            }
            setOnClickListener {
                activity.onListEntryClick(it.tag as PokemonData)
            }
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mPokedexIdView: TextView = mView.fragment_pokemondata_pokedex_id
        val mNameView: TextView = mView.fragment_pokemondata_name
        val mEggsNeededView: TextView = mView.fragment_pokemondata_eggs_needed
        val mHuntMethodView: TextView = mView.fragment_pokemondata_hunt_method
        val mShinyImageView: ImageView = mView.fragment_pokemondata_shiny_image
    }
}