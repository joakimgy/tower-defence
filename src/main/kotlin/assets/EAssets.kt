package assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.assets.getAsset
import ktx.assets.load

// texture atlas
enum class TextureAtlasAssets(val path: String) {
    TowerDefence("src/main/kotlin/assets/images/td.atlas"),
    BlackSmith("src/main/kotlin/assets/images/blacksmith.atlas")
}

inline fun AssetManager.load(asset: TextureAtlasAssets) = load<TextureAtlas>(asset.path)
inline operator fun AssetManager.get(asset: TextureAtlasAssets) = getAsset<TextureAtlas>(asset.path)
