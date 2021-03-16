package screen

data class GameState(
    var round: Int,
    var enemiesToSpawn: Int,
    var isBuilding: Boolean,
    var blocksRemaining: Int,
)
