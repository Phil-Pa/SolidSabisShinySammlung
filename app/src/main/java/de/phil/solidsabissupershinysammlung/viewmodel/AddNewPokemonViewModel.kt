package de.phil.solidsabissupershinysammlung.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.phil.solidsabissupershinysammlung.R
import de.phil.solidsabissupershinysammlung.ShinyPokemonApplication
import de.phil.solidsabissupershinysammlung.database.PokemonDao
import de.phil.solidsabissupershinysammlung.database.PokemonDatabase
import de.phil.solidsabissupershinysammlung.model.PokemonData

class AddNewPokemonViewModel(application: Application) : AndroidViewModel(application) {

    private val pokemonDao: PokemonDao =
        PokemonDatabase.instance(application.applicationContext).pokemonDao()

    fun pokemonNameExists(name: String): Boolean {
        val pokemonNames = PokemonDatabase.androidPokemonResources(getApplication()).getPokemonNames()
        for (pokemonName in pokemonNames)
            if (name == pokemonName)
                return true

        return false
    }

    fun getPokemonNames(): List<String> {
        return PokemonDatabase.androidPokemonResources(getApplication()).getPokemonNames()
    }

    fun validateInput(pokemonData: PokemonData): Pair<String?, PokemonData?> {

        val context = getApplication<Application>().applicationContext

        if (pokemonData.name.isEmpty() || pokemonData.name.isBlank())
            return Pair(context.resources.getString(R.string.error_empty_name), null)
        else if (!pokemonNameExists(pokemonData.name) && !pokemonData.name.endsWith(
                ShinyPokemonApplication.ALOLA_EXTENSION) &&
            !pokemonData.name.endsWith(ShinyPokemonApplication.GALAR_EXTENSION)
        )
            return Pair(
                "${pokemonData.name} " + context.resources.getString(R.string.error_is_not_a_pokemon),
                null
            )
        else if (pokemonData.encounterNeeded < 0)
            return Pair(context.resources.getString(R.string.error_encounter_lower_zero), null)

        val name = pokemonData.name
        val generation = PokemonDatabase.androidPokemonResources(getApplication()).getGenerationByName(name)
        val pokedexId = PokemonDatabase.androidPokemonResources(getApplication()).getPokedexIdByName(name)
        val internalId = pokemonDao.getMaxInternalId() + 1

        val validatedData = PokemonData(
            name,
            pokedexId,
            generation,
            pokemonData.encounterNeeded,
            pokemonData.huntMethod,
            pokemonData.pokemonEdition,
            pokemonData.tabIndex
        )
        validatedData.internalId = internalId

        return Pair(null, validatedData)
    }

    fun getPokedexIdByName(name: String): Int {
        return PokemonDatabase.androidPokemonResources(getApplication()).getPokedexIdByName(name)
    }

    fun getGenerationByName(name: String): Int {
        return PokemonDatabase.androidPokemonResources(getApplication()).getGenerationByName(name)
    }

    fun addPokemonToDatabase(pokemonData: PokemonData): Pair<Boolean, String> {

        val (message, data) = validateInput(pokemonData)

        return if (message == null && data != null) {
            pokemonData.internalId = pokemonDao.getMaxInternalId() + 1
            pokemonData.generation = PokemonDatabase.androidPokemonResources(getApplication()).getGenerationByName(pokemonData.name)
            pokemonData.pokedexId = PokemonDatabase.androidPokemonResources(getApplication()).getPokedexIdByName(pokemonData.name)
            pokemonDao.addPokemon(pokemonData)
            Pair(true, "${pokemonData.name} " + getApplication<Application>().applicationContext.resources.getString(R.string.message_has_been_added))
        } else {
            Pair(false, message!!)
        }
    }

}