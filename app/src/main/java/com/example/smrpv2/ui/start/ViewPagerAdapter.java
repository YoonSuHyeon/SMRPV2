package com.example.smrpv2.ui.start;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.PagerAdapter;
import com.example.smrpv2.R;

/**
 * ViewPagerAdapter :
 * 기능 1. 단순한 ViewPager (이미지 슬라이드)
 * 기능 2. 클릭 시 다른 Fragment 또는 Activity 로 이동하는 ViewPager
 */
public class ViewPagerAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;

    final int FIRST_PAGE = 0;
    final int SECOND_PAGE = 1;
    final int THIRD_PAGE  = 2;
    final int CLICK_OK = 1;
    private int click =0;
    private int[] images;

    /**
     * 클릭되는 ViewPager
     * @param context
     * @param images 이미지 슬라이드
     * @param click ViewPager 클릭 여부 -> 클릭 허용 시 click에 1을 보냄(다른 Fragment 이동 가능)
     */
    public ViewPagerAdapter(Context context, int []images, int click) {
        this.images = images;
        this.context = context;
        this.click = click;
    }
    public ViewPagerAdapter(Context context, int []images)
    {   this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout)o);
    }
    @Override
    public @NonNull Object instantiateItem(@NonNull final ViewGroup container, final int position)
    {
        inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        View v = inflater.inflate(R.layout.pager, container, false);

        //초기화
        ImageView imageView = v.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        v.setOnClickListener(new View.OnClickListener() { //이미지를 클릭 했을때.
            @Override
            public void onClick(View v1) {

                if(click == CLICK_OK){
                    NavHostFragment navHostFragment =
                            (NavHostFragment) ((AppCompatActivity) context).getSupportFragmentManager()
                                    .findFragmentById(R.id.nav_host_fragment);
                    NavController navController = navHostFragment.getNavController();

                    // position : 클릭된 페이지 번호
                    if(position == FIRST_PAGE){
                        navController.navigate(R.id.action_nav_home_to_nav_medicine);
                    }
                    else if(position==SECOND_PAGE){
                        navController.navigate(R.id.action_nav_home_to_nav_report_notice);

                    }
                    else if(position == THIRD_PAGE){
                        navController.navigate(R.id.action_nav_home_to_nav_hospital);
                    }


                }
            }
        });
        container.addView(v);
        return v;
    }
}