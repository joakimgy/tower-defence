package assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.assets.getAsset
import ktx.assets.load

// music
enum class MusicAssets(val path: String) {
    Hype("src/main/kotlin/assets/music/hype.mp3")
}

inline fun AssetManager.load(asset: MusicAssets) = load<Music>(asset.path)
inline operator fun AssetManager.get(asset: MusicAssets) = getAsset<Music>(asset.path)

// texture atlas
enum class TextureAtlasAssets(val path: String) {
    Map("src/main/kotlin/assets/images/map.atlas"),
    BlackSmith("src/main/kotlin/assets/images/blacksmith.atlas")
}

inline fun AssetManager.load(asset: TextureAtlasAssets) = load<TextureAtlas>(asset.path)
inline operator fun AssetManager.get(asset: TextureAtlasAssets) = getAsset<TextureAtlas>(asset.path)
