package com.simonag.simonag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simonag.simonag.model.Aktifitas;

import java.util.ArrayList;

/**
 * Created by diditsepiyanto on 6/13/17.
 */

public class AktifitasActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "id_program";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_program);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_program);
        setupRecyclerView(rv);
        showActionBar();
    }

    private void showActionBar() {
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setElevation(0);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(this,
                getRandomSublist()));
    }

    private ArrayList<Aktifitas> getRandomSublist() {
        ArrayList<Aktifitas> list = new ArrayList<>();
        list.add(new Aktifitas(0, "Instalasi Line Telepon", "Kapasitas", 100,100, 10, 20, "12 Agustus 2018", "Line"));
        list.add(new Aktifitas(0, "Instalasi Line Telepon", "Kapasitas", 100,100, 10, 20, "12 Agustus 2018", "Line"));
        list.add(new Aktifitas(0, "Instalasi Line Telepon", "Kapasitas", 100,100, 10, 20, "12 Agustus 2018", "Line"));
        list.add(new Aktifitas(0, "Instalasi Line Telepon", "Kapasitas", 100,100, 10, 20, "12 Agustus 2018", "Line"));
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<Aktifitas> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ArrayList<Aktifitas>  mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, ArrayList<Aktifitas> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_aktivitas, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //holder.mBoundString = mValues.get(position);
            //holder.mTextView.setText(mValues.get(position).getNama_bumn());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, AktifitasActivity.class);
                    intent.putExtra(AktifitasActivity.EXTRA_NAME, holder.mBoundString);

                    context.startActivity(intent);
                }
            });
            /*
            Glide.with(holder.mImageView.getContext())
                    .load(Cheeses.getRandomCheeseDrawable())
                    .fitCenter()
                    .into(holder.mImageView);*/
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
