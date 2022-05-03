package funapps.music.zarghtly.adapters

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import funapps.`fun`.music.zarghtly.MainFragment
import funapps.`fun`.music.zarghtly.R
import funapps.`fun`.music.zarghtly.databinding.RecyclerItemBinding
import funapps.`fun`.music.zarghtly.dataclasses.Music


class MusicAdapter(val musiclist: ArrayList<Music>, private val context: Context) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {
    companion object {

        private var mediaPlayer: MediaPlayer? = null

       private lateinit var path: Uri
        var handler: Handler = Handler(Looper.getMainLooper())
        var runnable: Runnable = Runnable { }
    }

    init {

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding: RecyclerItemBinding =
            RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val musicitem = musiclist[position]
        holder.bind(musicitem)
    }

    override fun getItemCount(): Int {
        return musiclist.size
    }

    class MusicViewHolder(private val binding: RecyclerItemBinding, private val mcontext: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Music) {
            binding.music = item

            binding.btnPlay.setOnClickListener {
                if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.stop()
                    mediaPlayer!!.reset()
                    mediaPlayer!!.release()
                    mediaPlayer = null
                    handler.removeCallbacks(runnable)
                    binding.seekbar.progress = 0
                    binding.btnPlay.setImageResource(R.drawable.ic_playwhite)
                } else {

                    mediaPlayer = MediaPlayer.create(mcontext, item.songuri)
                    binding.seekbar.max = mediaPlayer!!.duration
                    mediaPlayer!!.start()
                    mediaPlayer!!.isLooping = true


                    runnable = object : Runnable {
                        override fun run() {
                            val current_pos = mediaPlayer!!.getCurrentPosition();
                            binding.seekbar.setProgress(current_pos.toInt())

                            handler.postDelayed(this, 1000)
                        }
                    }
                    handler.postDelayed(runnable, 0)

                    binding.btnPlay.setImageResource(R.drawable.pause)
                }

            }
            binding.btnShare.setOnClickListener {


                var position = adapterPosition
                when (position) {
                    0 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.firstAudio!!)

                    1 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.secondAudio!!)
                    2 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.thirdAudio!!)
                    3 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.fourthAudio!!)

                    4 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.fifthAudio!!)
                    5 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.sixthAudio!!)
                    6 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.seventhAudio!!)
                    7 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.eighthAudio!!)
                    8 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.ninethAudio!!)
                    9 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.tenthAudio!!)


                    10 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        MainFragment.eleventhAudio!!)

                    else -> {

                        path = FileProvider.getUriForFile(mcontext,
                            "funapps.fun.music.zarghtly.fileprovider",
                            MainFragment.firstAudio!!)
                    }
                }
                try {

                    val targetShareIntents: MutableList<Intent> = ArrayList()
//                    var intent = Intent()

//                    intent.action = Intent.ACTION_SEND
//                    intent.putExtra(Intent.EXTRA_STREAM, path)
////                    intent.putExtra(Intent.EXTRA_STREAM,Uri.parse("android.resource://funapps.fun.music.zarghtly/"+R.raw.zaghroutamomayaza))
//
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                    intent.type = "audio/mp3"

                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type = "text/plain"
                    val resInfos: List<ResolveInfo> =
                        mcontext.getPackageManager().queryIntentActivities(shareIntent, 0)

                        if (!resInfos.isEmpty()) {
                            for (resInfo in resInfos) {
                                val packageName = resInfo.activityInfo.packageName
                                Log.d("Package Name", packageName)
                                if (packageName.contains("com.whatsapp") || packageName.contains("com.facebook.mlite") || packageName.contains(
                                        "com.facebook.orca")
                                ) {
                                    val mintent = Intent()
                                    mintent.setComponent(ComponentName(packageName,
                                        resInfo.activityInfo.name))

                                    mintent.action = Intent.ACTION_SEND
                                    mintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    mintent.type = "audio/mp3"
                                    mintent.putExtra(Intent.EXTRA_STREAM, path)
                                    mintent.setPackage(packageName)
                                    targetShareIntents.add(mintent)
                                }
                            }
                            if (!targetShareIntents.isEmpty()) {
                                val chooserIntent =
                                    Intent.createChooser(targetShareIntents.removeAt(0),
                                        "ارسل الى ")
                                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                                    targetShareIntents.toTypedArray())
                                mcontext.startActivity(chooserIntent)
                            } else {
                                Toast.makeText(mcontext, "تعذر مشاركه التطبيق", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }


                    } catch (e: Exception) {

                        Log.d("sharebutton", "share: ${e.toString()}")
                    }


//
                    //var intent = Intent()
//                    intent.action = Intent.ACTION_SEND
//                   intent.putExtra(Intent.EXTRA_STREAM, path)
////                    intent.putExtra(Intent.EXTRA_STREAM,Uri.parse("android.resource://funapps.fun.music.zarghtly/"+R.raw.zaghroutamomayaza))
//
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                    intent.type = "audio/mp3"
////
//                    intent= Intent.createChooser(intent, "ارسل الى")


//                    mcontext.startActivity(intent)



            }

            binding.seekbar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean,
                    ) {
                        if (fromUser) {
                            if (mediaPlayer != null) {

                                mediaPlayer!!.seekTo(progress)
                                binding.seekbar.setProgress(progress);
                            }
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        if (mediaPlayer != null) {
                            mediaPlayer!!.seekTo(binding.seekbar.getProgress());

                        }
                    }
                }
            )
        }
    }
    fun clearMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

}



