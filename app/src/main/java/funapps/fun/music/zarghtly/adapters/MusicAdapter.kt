package funapps.music.zarghtly.adapters

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import funapps.`fun`.music.zarghtly.R
import funapps.`fun`.music.zarghtly.databinding.RecyclerItemBinding
import funapps.`fun`.music.zarghtly.dataclasses.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.io.FileUtils.copyInputStreamToFile
import java.io.File


class MusicAdapter(val musiclist: ArrayList<Music>, private val context: Context) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {
    companion object {

        private var mediaPlayer: MediaPlayer? = null
        private var firstAudio: File? = null
        private var secondAudio: File? = null
        private var thirdAudio: File? = null
        private var fourthAudio: File? = null
        private var fifthAudio: File? = null
        private var sixthAudio: File? = null
        private var seventhAudio: File? = null
        private var eighthAudio: File? = null
        private var ninethAudio: File? = null
        private var tenthAudio: File? = null
        private var eleventhAudio: File? = null
        private lateinit var path: Uri
        var handler: Handler = Handler(Looper.getMainLooper())
        var runnable: Runnable = Runnable { }
    }

    init {

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC);
        saveaudiosToPhone()
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


                    runnable=object : Runnable {
                        override fun run() {
                            val  current_pos = mediaPlayer!!.getCurrentPosition();
                            binding.seekbar.setProgress(current_pos.toInt())

                            handler.postDelayed(this, 1000)
                        }
                    }
                    handler.postDelayed(runnable,0)

                    binding.btnPlay.setImageResource(R.drawable.pause)
                }

            }
            binding.btnShare.setOnClickListener {
                var position = adapterPosition
                when (position) {
                    0 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        firstAudio!!)
                    1 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        secondAudio!!)
                    2 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        thirdAudio!!)
                    3 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        fourthAudio!!)

                    4 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        fifthAudio!!)
                    5 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        sixthAudio!!)
                    6 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        seventhAudio!!)
                    7 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        eighthAudio!!)
                    8 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        ninethAudio!!)
                    9 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        tenthAudio!!)


                    10 -> path = FileProvider.getUriForFile(mcontext,
                        "funapps.fun.music.zarghtly.fileprovider",
                        eleventhAudio!!)

                    else -> {

                        path = FileProvider.getUriForFile(mcontext,
                            "funapps.fun.music.zarghtly.fileprovider",
                            firstAudio!!)

                    }
                }

                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_STREAM, path)

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                intent.type = "audio/mp3"
                mcontext.startActivity(Intent.createChooser(intent, "ارسل الى"))

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

    fun saveaudiosToPhone() {

        val downloadPath =
            Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).absolutePath + "/"
        var inputStream = context.resources.openRawResource(R.raw.zarghoutaegypt)
        firstAudio = File(downloadPath + "zarghoutaegypt.mp3")
        copyInputStreamToFile(inputStream, firstAudio)

        inputStream = context.resources.openRawResource(R.raw.zarhoutatunis)
        secondAudio = File(downloadPath + "zarhoutatunis.mp3")
        copyInputStreamToFile(inputStream, secondAudio)

        inputStream = context.resources.openRawResource(R.raw.zarghoutamaroc)
        thirdAudio = File(downloadPath + "zarghoutamaroc.mp3")
        copyInputStreamToFile(inputStream, thirdAudio)

        inputStream = context.resources.openRawResource(R.raw.zarghoutapalestine)
        fourthAudio = File(downloadPath + "zarghoutapalestine.mp3")
        copyInputStreamToFile(inputStream, fourthAudio)

        inputStream = context.resources.openRawResource(R.raw.zargoutasoudan)
        fifthAudio = File(downloadPath + "zargoutasoudan.mp3")
        copyInputStreamToFile(inputStream, fifthAudio)

        inputStream = context.resources.openRawResource(R.raw.zarghoutalebanon)
        sixthAudio = File(downloadPath + "zarghoutalebanon.mp3")
        copyInputStreamToFile(inputStream, sixthAudio)

        inputStream = context.resources.openRawResource(R.raw.zarghoutaspecial)
        seventhAudio = File(downloadPath + "zarghoutaspecial.mp3")
        copyInputStreamToFile(inputStream, seventhAudio)

        inputStream = context.resources.openRawResource(R.raw.zarghoutakhalegeya)
        eighthAudio = File(downloadPath + "zarghoutakhalegeya.mp3")
        copyInputStreamToFile(inputStream, eighthAudio)

        inputStream = context.resources.openRawResource(R.raw.zahroutetelfarah)
        ninethAudio = File(downloadPath + "zahroutetelfarah.mp3")
        copyInputStreamToFile(inputStream, ninethAudio)

        inputStream = context.resources.openRawResource(R.raw.zarghoutagroup)
        tenthAudio = File(downloadPath + "zarghoutagroup.mp3")
        copyInputStreamToFile(inputStream, tenthAudio)

        inputStream = context.resources.openRawResource(R.raw.zarghoutaspecial)
        eleventhAudio = File(downloadPath + "zarghoutaspecial.mp3")
        copyInputStreamToFile(inputStream, eleventhAudio)


    }

}



