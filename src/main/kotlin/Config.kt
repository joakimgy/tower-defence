object Config {
    const val enemiesToSpawn = 3
    const val buildingBlocksPerRound = 5

    object Screen {
        const val viewportWidth = 1280f
        const val viewportHeight = 720f
    }

    const val MAP_SIZE_X = 23
    const val MAP_SIZE_Y = 13
    const val TILE_SIZE = 50f
    const val FONT_SIZE = 30


    object Player {
        const val health = 130f
    }

    object AttackTower {
        const val range = TILE_SIZE * 3
        const val damage = 20f
    }
}
