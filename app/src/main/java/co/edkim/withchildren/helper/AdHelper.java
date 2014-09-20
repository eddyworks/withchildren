package co.edkim.withchildren.helper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Edward on 2014-09-12.
 */
public class AdHelper {
    public static void setAdmobAd(AdView adView){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("7DDD06BDE922F9125E7B97721D387C5C").addTestDevice("9153DFD37EBE531E627C44DB5BFC15D6").build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }
}
