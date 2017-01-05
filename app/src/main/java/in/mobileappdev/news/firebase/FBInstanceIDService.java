package in.mobileappdev.news.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import in.mobileappdev.news.api.APIClient;
import in.mobileappdev.news.models.NewsArticlesListResponse;
import in.mobileappdev.news.models.TokenResponse;
import in.mobileappdev.news.utils.Constants;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by satyanarayana.avv on 03-01-2017.
 */


public class FBInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FBInstanceIDService";
    Subscription subscription;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: " + refreshedToken);
        saveToken(refreshedToken);
    }

    private void saveToken(@NonNull String token) {
        Log.d(TAG, "TOKEN : "+token);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            user.getEmail();
            subscription = APIClient.getInstance()
                    .saveToken(user.getDisplayName(), user.getEmail(), token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TokenResponse>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "In onCompleted()");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "In onError() " + e.getMessage());

                        }

                        @Override
                        public void onNext(TokenResponse response) {
                            Log.d(TAG, "In onNext()");

                        }
                    });
        }

    }
}
