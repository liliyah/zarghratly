import androidx.lifecycle.ViewModel
import funapps.`fun`.music.zarghtly.R
import funapps.`fun`.music.zarghtly.dataclasses.Music

class MainViewModel : ViewModel() {
    val musiclist: ArrayList<Music> = ArrayList()
    fun getMusicList() {
        musiclist.add(Music("زغروطه مصريه", R.raw.zarghoutaegypt))
        musiclist.add(Music("زغروطه تونسيه", R.raw.zarhoutatunis))
        musiclist.add(Music("زغروطه مغربيه", R.raw.zarghoutamaroc))
        musiclist.add(Music("زغروطه فلسطينيه", R.raw.zarghoutapalestine))
        musiclist.add(Music("زغروطه سودانيه", R.raw.zargoutasoudan))
        musiclist.add(Music("زغروطه لبنانيه", R.raw.zarghoutalebanon))
        musiclist.add(Music("زغروطه مميزة", R.raw.zarghoutaspecial))
        musiclist.add(Music("زغروطه خليجيه", R.raw.zarghoutakhalegeya))
        musiclist.add(Music("زغروطه الفرح", R.raw.zahroutetelfarah))
        musiclist.add(Music("زغروطه جماعيه", R.raw.zarghoutagroup))
        musiclist.add(Music("رغروطه خطيرة", R.raw.zarghoutaspecial))
    }
}