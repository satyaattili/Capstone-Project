package in.mobileappdev.news.views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.mobileappdev.news.R;
import in.mobileappdev.news.utils.Constants;
import in.mobileappdev.news.utils.Utils;

/**
 * Created by satyanarayana.avv on 30-10-2016.
 */

public class ErrorBuilder {

    private Context mContext;
    private LinearLayout mErrorLayout;
    private ErrorClickListener mErrorClickListener;
    private ImageView mErrorImage;

    public ErrorBuilder(Context mContext, LinearLayout mErrorLayout,
                        ErrorClickListener mErrorClickListener) {
        this.mContext = mContext;
        this.mErrorLayout = mErrorLayout;
        this.mErrorClickListener = mErrorClickListener;
    }


    public void showError(String error, int type, boolean action) {

        if (Utils.isEmpty(error)) {
            error = Constants.EMPTY_STRING;
        }

        if (mErrorLayout != null) {
            View errorMessageView =
                    LayoutInflater.from(mContext).inflate(R.layout.layout_error, null, false);
            mErrorLayout.setVisibility(View.VISIBLE);
            TextView mErrorMessage = (TextView) errorMessageView.findViewById(R.id.error_messgae);
            mErrorImage = (ImageView) errorMessageView.findViewById(R.id.error_icon);
            TextView mBtnErrorClick = (TextView) errorMessageView.findViewById(R.id.btn_error_click);

            mErrorMessage.setText(error);
            mErrorImage.setContentDescription(error);

            if (action) {
                mBtnErrorClick.setVisibility(View.VISIBLE);
                mBtnErrorClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mErrorClickListener.onRetryClicked(view);
                    }
                });

            } else {
                mBtnErrorClick.setVisibility(View.GONE);
            }


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.gravity = Gravity.CENTER;

            mErrorLayout.removeAllViews();
            mErrorLayout.addView(errorMessageView, layoutParams);

        }

    }

    public void hideErrorLayout() {
        mErrorLayout.setVisibility(View.GONE);
    }
}
