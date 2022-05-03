package funapps.`fun`.music.zarghtly

import MainViewModel
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import funapps.`fun`.music.zarghtly.databinding.FragmentMainBinding
import funapps.`fun`.music.zarghtly.dataclasses.Music
import funapps.music.zarghtly.adapters.MusicAdapter
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.io.InputStream
import kotlin.math.log

const val AD_UNIT_ID = "ca-app-pub-2020667111518171/6585379019"

class MainFragment : Fragment() {
    private var mInterstitialAd: InterstitialAd? = null
    private var mAdIsLoading: Boolean = false
    private lateinit var musicViewmodel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var madapter: MusicAdapter
    private lateinit  var inputStream:InputStream
   private  var downloadPath:String =""
    var musiclist: ArrayList<Music> = ArrayList()

    companion object {

        var firstAudio: File? = null
        var secondAudio: File? = null
        var thirdAudio: File? = null
        var fourthAudio: File? = null
        var fifthAudio: File? = null
        var sixthAudio: File? = null
        var seventhAudio: File? = null
        var eighthAudio: File? = null
        var ninethAudio: File? = null
        var tenthAudio: File? = null
        var eleventhAudio: File? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        saveaudiosToPhone()
        requireActivity().volumeControlStream = (AudioManager.STREAM_MUSIC)
        MobileAds.initialize(requireContext()) {}

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .build()
        )
        loadAd()
        super.onCreate(savedInstanceState)
    }

    private fun loadAd() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            requireActivity(), AD_UNIT_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("tag", adError?.message)
                    mInterstitialAd = null
                    mAdIsLoading = false
//                    val error = "domain: ${adError.domain}, code: ${adError.code}, " +
//                            "message: ${adError.message}"

                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("tag", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    mAdIsLoading = false
                }
            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        musicViewmodel = ViewModelProvider(this).get(MainViewModel::class.java)
        musicViewmodel.getMusicList()
        musiclist = musicViewmodel.musiclist
        madapter = MusicAdapter(musiclist, requireContext())
        binding.mainRecycler.let {
            it.adapter = madapter
            it.hasFixedSize()
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        }
        return binding.root
    }

    fun showad() {
        if (mInterstitialAd == null) {
            loadAd()
        } else if (mInterstitialAd != null) {

            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("ad", "the ad is dissmissed ")
                    super.onAdDismissedFullScreenContent()
                    requireActivity().finish()
                    mInterstitialAd = null
                    loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Log.d("ad", "the ad has failed ")

                    mInterstitialAd = null

                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("ad", "ad showed full screen ")

                }
            }

            mInterstitialAd?.show(requireActivity())
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showad()
                }


            }


        )
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        madapter.clearMediaPlayer()
        super.onDetach()
    }

//    private fun allowPermissionForFile() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//
////
////            if (ContextCompat.checkSelfPermission(requireContext(),
////                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
////            ) {
////                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
////
////            }else{
////                saveaudiosToPhone()
////
////            }
//            downloadPath =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/"
//
//        }else{
//
//        }
//
//    }


    fun saveaudiosToPhone() {
        try {
//             downloadPath =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/"

            firstAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC) , "zarghoutaegypt.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zarghoutaegypt)
            FileUtils.copyInputStreamToFile(inputStream, firstAudio)


            secondAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC),"zarhoutatunis.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zarhoutatunis)
            FileUtils.copyInputStreamToFile(inputStream, secondAudio)

            thirdAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "zarghoutamaroc.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zarghoutamaroc)
            FileUtils.copyInputStreamToFile(inputStream, thirdAudio)

            fourthAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "zarghoutapalestine.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zarghoutapalestine)
            FileUtils.copyInputStreamToFile(inputStream, fourthAudio)

            fifthAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "zargoutasoudan.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zargoutasoudan)
            FileUtils.copyInputStreamToFile(inputStream, fifthAudio)

            sixthAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "zarghoutalebanon.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zarghoutalebanon)
            FileUtils.copyInputStreamToFile(inputStream, sixthAudio)

            seventhAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC),"zarghoutaspecial.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zarghoutaspecial)
            FileUtils.copyInputStreamToFile(inputStream, seventhAudio)

            eighthAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "zarghoutakhalegeya.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zarghoutakhalegeya)
            FileUtils.copyInputStreamToFile(inputStream, eighthAudio)

            ninethAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "zahroutetelfarah.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zahroutetelfarah)
            FileUtils.copyInputStreamToFile(inputStream, ninethAudio)

            tenthAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "zarghoutagroup.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zarghoutagroup)
            FileUtils.copyInputStreamToFile(inputStream, tenthAudio)

            eleventhAudio = File(requireActivity()?.getExternalFilesDir(Environment.DIRECTORY_MUSIC),"zarghoutaspecial.mp3")
            inputStream = requireContext().resources.openRawResource(R.raw.zarghoutaspecial)
            FileUtils.copyInputStreamToFile(inputStream, eleventhAudio)
            Log.d("saveaudioteveth", "saveaudiosToPhone:${eleventhAudio.toString()} ")


        } catch (e: IOException) {
            Log.d("saveaudiotostorage", "saveaudiosToPhone:${e.toString()} ")
        }
    }
}


