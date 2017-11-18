package net.hribi.fri;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.hribi.fri.Adapter.MountainFinalAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MountainFinal extends AppCompatActivity implements View.OnClickListener {
    private String url;
    private TextView getThere,directions;
    private ImagesFragment rf;
    private ViewPager vp;
    private List<String> imgUrl = new ArrayList<>();
    private  ImageView avatar,star;
    private boolean fav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mountain_final);

        url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        Toolbar tb = (Toolbar) findViewById(R.id.anim_toolbar);
        tb.setTitle(title);
        tb.setTitleTextColor(0xffffffff);

        getThere = (TextView) findViewById(R.id.text1);
        directions = (TextView) findViewById(R.id.desc);

        rf = new ImagesFragment();
        vp = (ViewPager) findViewById(R.id.image_pager);
        vp.setAdapter(rf);
        avatar = (ImageView) findViewById(R.id.avatar);
        star = (ImageView) findViewById(R.id.star);

        new FeedTask().execute();
    }

    @Override
    public void onClick(View view) {
        if(!fav){
            star.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.ic_star_rate_yellow_18px));
            fav= true;
        } else {
            star.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.ic_star_rate_black_18px));
            fav = false;
        }
    }

    class FeedTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Document doc;
            String rez = "";
            try {
                doc = Jsoup.connect(url).get();
                Elements links = doc.select("tr:nth-of-type(3) > td > p");
                rez = links.get(0).text();
                publishProgress(rez,"0");
                links = doc.select("tr:nth-of-type(5) > td > p");

                rez=links.get(0).text();
                publishProgress(rez,"1");

                links = doc.select("div#vsebina > table > tbody > tr > td > table:nth-of-type(2) > tbody > tr");
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
                publishProgress(rez,"2");

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
                getThere.setText(values[0]);
            } else if(progress == 1) {
                directions.setText(values[0]);
            } else {
                vp.setAdapter(rf);
                if (imgUrl.size() > 0) {
                    Picasso.with(getApplicationContext()).load(imgUrl.get(0)).fit().centerCrop().into(avatar);
                }
            }
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
