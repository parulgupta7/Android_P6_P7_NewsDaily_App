package com.example.parul.newsdaily;

import android.content.Context;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsDailyAdapter extends ArrayAdapter<NewsDaily> {

    public NewsDailyAdapter(Context context, List<NewsDaily> newsDaily) {
        super(context, 0, newsDaily);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final NewsDaily newsDaily = getItem(position);

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_name);
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_content);
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text_view);

        sectionTextView.setText(newsDaily.getSectionName());
        titleTextView.setText(newsDaily.getTitle());
        dateTextView.setText(newsDaily.getDate().substring(0, 9));
        authorTextView.setText(newsDaily.getAuthor());

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                newIntent.setData(Uri.parse(newsDaily.getUrl()));
                getContext().startActivity(newIntent);
            }
        });
        return listItemView;
    }
}
