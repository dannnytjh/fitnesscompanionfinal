package fitnesscompanion.com.View.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Adapter.MenuAdapter;
import fitnesscompanion.com.Database.UserDB;
import fitnesscompanion.com.Model.User;
import fitnesscompanion.com.R;
import fitnesscompanion.com.View.Home.BmiActivity;
import fitnesscompanion.com.View.Home.NewsActivity;

/**
 * Created by Soon Kok Fung
 */
@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment {

    @BindView(R.id.txtName) TextView txtName;
    @BindView(R.id.imageProfile) ImageView imageProfile;
    @BindView(R.id.btnEdit) ImageButton btnEdit;
    @BindView(R.id.workout_achievement) RelativeLayout achievement_button;
    @BindView(R.id.workout_ranking) RelativeLayout ranking_button;
    @BindView(R.id.workout_statistics) RelativeLayout statistics_button;
    @BindView(R.id.reminder_alarm) RelativeLayout reminder_button;
    @BindView(R.id.editProfile) TextView edit_Profile_button;

    private User user;


    private Context context;
    public ProfileFragment(Context context) {
        this.context = context;
    }

//    private View.OnClickListener onClickEdit = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            getActivity().finish();
//            startActivity(new Intent(context,ProfileActivity.class));
//        }
//    };
//    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//            getActivity().finish();
//            switch (position) {
//                case 0:
//                    startActivity(new Intent(context,AchievementActivity.class));
//                    break;
//                case 1:
//                    startActivity(new Intent(context,RankingActivity.class));
//                    break;
//                case 2:
//                    startActivity(new Intent(context,ReminderActivity.class));
//                    //startActivity(new Intent(context,SettingActivity.class));
//                    //reminder
//                    break;
//                case 3:
//                    startActivity(new Intent(context,TimeLineActivity.class));
//                    //startActivity(new Intent(context,SettingActivity.class));
//                    //graph
//                    break;
//                case 4:
//                    startActivity(new Intent(context,SettingActivity.class));
//                    break;
//            }
//        }
//    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new UserDB(context).getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        txtName.setText(user.getName());

        if (user.getImage().length() > 10) {
            imageProfile.setImageBitmap(user.getImageFromJSon());
        }
        //btnEdit.setOnClickListener(onClickEdit);
        //imageProfile.setOnClickListener(onClickEdit);
//        listViewMenu.setAdapter(new MenuAdapter(context, context.getResources().getStringArray(R.array.menu_profile)));
//        listViewMenu.setOnItemClickListener(onItemClickListener);

        edit_Profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, SettingActivity.class);
                startActivity(intent);
            }
        });



        achievement_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AchievementActivity.class);
                startActivity(intent);
            }
        });
        ranking_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RankingActivity.class);
                startActivity(intent);
            }
        });

        statistics_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, TimeLineActivity.class);
                startActivity(intent);
            }
        });

        reminder_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, ReminderActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
