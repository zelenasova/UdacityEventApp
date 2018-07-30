package peter.kalata.com.eventapp.ui.users;

import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.User;


public class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.iv_letter)
    ImageView ivLetter;

    private User user;
    private ColorGenerator generator = ColorGenerator.MATERIAL;

    UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(User user) {
        this.user = user;
        setupTexts();
    }

    private void setupTexts() {
        tvName.setText(user.getFullname());
        tvPhoneNumber.setText(user.getPhoneNumber());
        tvEmail.setText(user.getEmail());
        if (user.getFullname().length() > 1) {
            String letter = String.valueOf(user.getFullname().charAt(0));
            int color = generator.getColor(user.getFullname());
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, color);
            ivLetter.setImageDrawable(drawable);
        }

    }
}
