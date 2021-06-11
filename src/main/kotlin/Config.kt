object Config {
    const val enemiesToSpawn = 3
    const val buildingBlocksPerRound = 5

    const val MAP_SIZE_X = 23
    const val MAP_SIZE_Y = 13
    const val TILE_SIZE = 64f

    object Screen {
        const val viewportWidth = 1600f
        const val viewportHeight = 960f
    }

    object Player {
        const val health = 130f
    }

    object AttackTower {
        const val range = TILE_SIZE * 3
        const val damage = 20f
    }
}
