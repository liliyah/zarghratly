package funapps.`fun`.music.zarghtly

import MainViewModel
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import funapps.`fun`.music.zarghtly.databinding.FragmentMainBinding
import funapps.`fun`.music.zarghtly.dataclasses.Music
import funapps.music.zarghtly.adapters.MusicAdapter
import kotlin.math.log

const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

class MainFragment : Fragment() {
    private var mInterstitialAd: InterstitialAd? = null
    private var mAdIsLoading: Boolean = false
    private lateinit var musicViewmodel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var   madapter:MusicAdapter

    var musiclist: ArrayList<Music> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().volumeControlStream=(AudioManager.STREAM_MUSIC)
        MobileAds.initialize(requireContext()) {}

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("ABCDEF012345"))
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
                    val error = "domain: ${adError.domain}, code: ${adError.code}, " +
                            "message: ${adError.message}"

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

fun showad(){
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :OnBackPressedCallback(true){
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


}