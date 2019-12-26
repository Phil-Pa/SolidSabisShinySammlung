package de.phil.solidsabissupershinysammlung.model

enum class PokemonEdition(val value: Int) {
    XY(0),
    ORAS(1),
    SM(2),
    USUM(3),
    SWSH(4),
    GO(5),
    LETSGO(6);

    companion object {
        private val map = values().associateBy(PokemonEdition::ordinal)
        fun fromInt(type: Int) = map[type]

//        fun getPokemonEditionUpTo(pokemonEdition: PokemonEdition): List<PokemonEdition> {
//            return when (pokemonEdition) {
//                ORAS -> listOf(ORAS)
//                SM -> listOf(ORAS, SM)
//                USUM -> listOf(ORAS, SM, USUM)
//                SWSH -> listOf(ORAS, SM, USUM, SWSH)
//
//            }
//        }
    }
}