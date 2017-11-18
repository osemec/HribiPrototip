package net.hribi.fri;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.hribi.fri.Adapter.MountainFinalAdapter;
import net.hribi.fri.Helper.DataHribi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MountainDesc extends AppCompatActivity {
    private int startPos, curPos;
    private TextView desc;
    private List<String> imgUrl = new ArrayList<>();
    private List<String> routeName = new ArrayList<>();
    private List<String> routeTime = new ArrayList<>();
    private List<String> routeUrl = new ArrayList<>();
    private ImagesFragment rf;
    private ViewPager vp;
    private ImageView avatar;
    private DataHribi dh;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mountain_desc);
        startPos = getIntent().getIntExtra("startpos", -1);
        curPos = getIntent().getIntExtra("curpos", -1);
        dh = new DataHribi(startPos);

        Toolbar tb = (Toolbar) findViewById(R.id.anim_toolbar);
        tb.setTitle(dh.getName()[curPos]);

        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        rf = new ImagesFragment();
        vp = (ViewPager) findViewById(R.id.image_pager);
        vp.setAdapter(rf);

        rv = (RecyclerView) findViewById(R.id.routes);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        avatar = (ImageView) findViewById(R.id.avatar);
        desc = (TextView) findViewById(R.id.desc);
        new FeedTask().execute();
    }

    class FeedTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Document doc;
            String rez = "";
            try {
                String url = dh.getUrl(curPos);
                doc = Jsoup.connect(url).get();
                Elements links = doc.select("tr:nth-of-type(3) > td > p");
                rez = links.get(0).text();
                publishProgress(rez,"0");

                links = doc.select("div#vsebina > table:nth-of-type(2) > tbody > tr");
                boolean hit = false;
                for (Element e : links) {
                    if (hit) {
                        Elements slike = e.select("td > a[target]");
                        for (Element img : slike) {
                            String link = img.attr("abs:target");
                            int pos = link.indexOf("http:");
                            link = link.substring(pos, link.length());
                            imgUrl.add(link);
                        }
                        break;
                    }

                    Elements slike = e.select("td > b");
                    if (slike.size() > 0) {
                        if (slike.get(0).text().compareTo("Slike:") == 0) {
                            hit = true;
                        }
                    }
                }
                publishProgress(rez,"1");
                links = doc.select("div#vsebina > table:nth-of-type(3) > tbody > tr:nth-of-type(n+2)");
                for (Element e : links) {
                    Elements name = e.select("td:nth-of-type(1) > a[href]");
                    Elements time = e.select("td:nth-of-type(2) > a[href]");
                    if (name.text().length() == 0) {
                        break;
                    }
                    routeName.add(name.text());
                    routeTime.add(time.text());
                    routeUrl.add(name.attr("abs:href"));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return rez;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            int progress = Integer.parseInt(values[1]);
            if (progress == 0) {
                desc.setText(values[0]);
            } else {
                vp.setAdapter(rf);
                if (imgUrl.size() > 0) {
                    Picasso.with(getApplicationContext()).load(imgUrl.get(0)).fit().centerCrop().into(avatar);
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            MountainFinalAdapter mfa = new MountainFinalAdapter(getApplicationContext(), routeName, routeTime, routeUrl);
            rv.setAdapter(mfa);
        }
    }

    private class ImagesFragment extends PagerAdapter {

        @Override
        public int getCount() {
            return imgUrl.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.mountain_images, container, false);

            ImageView im = (ImageView) v.findViewById(R.id.image);
            Picasso.with(getApplicationContext()).load(imgUrl.get(position)).into(im);
            container.addView(v);
            return v;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
