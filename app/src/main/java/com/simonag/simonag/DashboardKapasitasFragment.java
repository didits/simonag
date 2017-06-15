package com.simonag.simonag;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.Dashboard;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardKapasitasFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_dashboard_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                ((MainActivity) getActivity()).db));
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<Dashboard> mValues;
        Activity c;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ArrayList<Dashboard>  mBoundString;


            public final View mView;
            @BindView(R.id.avatar)
            ImageView avatar;
            @BindView(android.R.id.text1)
            TextView text1;
            @BindView(android.R.id.text2)
            TextView text2;
            @BindView(android.R.id.progress)
            NumberProgressBar progress;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this, view);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + text1.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Activity context, ArrayList<Dashboard> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            this.c = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_dashboard, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            //holder.mBoundString = mValues.get(position);
            holder.text1.setText(mValues.get(position).getNama_bumn());
            holder.text2.setText(mValues.get(position).getPersentase_kapasitas()+" %");
            //progresssetProgress((int)mValues.get(position).getPersentase_kapasitas());
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    c.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.progress.incrementProgressBy(1);
                        }
                    });
                }
            }, 1000, 100);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mValues.get(position).getId_bumn()== Prefs.getInt(Config.ID_BUMN, 0)){
                    Context context = v.getContext();
                    context.startActivity(new Intent(context, ProgramActivity.class));}
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
