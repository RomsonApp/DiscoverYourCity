package com.romsonapp.discoveryourcity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.romsonapp.discoveryourcity.R;
import com.romsonapp.discoveryourcity.model.Point;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.romsonapp.discoveryourcity.R.layout.cards_layout;

/**
 * Created by romson on 3/20/16.
 */
public class CardsAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<Point> points;

    public CardsAdapter(Context context, ArrayList<Point> points) {
        this.context = context;
        this.points = points;
    }


    @Override
    public int getCount() {
        return points.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            gridView = new View(context);

            gridView = inflater.inflate(R.layout.cards_layout, null);

            ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView);

            Log.d("log", String.valueOf(points.size()));
            Point point = points.get(position);
            imageView.setImageResource(R.drawable.ic_close_card_web);
            imageView.setContentDescription("status:" + String.valueOf(point.getStatus()) + ",id:" + String.valueOf(point.getId()));
            if (point.getStatus() == 1) {
                Picasso.with(context).load(point.getImage()).into(imageView);
            }
        } else {
            gridView = convertView;
        }

        return gridView;

    }


}
