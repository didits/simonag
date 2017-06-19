package com.simonag.simonag;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.DashboardKomisaris;
import com.simonag.simonag.utils.Config;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.String.format;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardAktivitasBUMNFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_dashboard_list, container, false);
        setupRecyclerView(rv);
        Log.d("get_data", ((MainActivityKomisaris) getActivity()).db.size()+"");
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                ((MainActivityKomisaris) getActivity()).db, Prefs.getInt(Config.FILTER_KOMISARIS, 0)));
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<DashboardKomisaris> mValues;
        Activity c;
        int nilai_tertinggi=0;
        int tipe = 0;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ArrayList<DashboardKomisaris> mBoundString;


            public final View mView;
            @BindView(R.id.avatar)
            ImageView avatar;
            @BindView(android.R.id.text1)
            TextView text1;
            @BindView(android.R.id.text2)
            TextView text2;
            @BindView(android.R.id.progress)
            NumberProgressBar progress;
            @BindView(R.id.progress_nilai)
            NumberProgressBar progress_nilai;

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

        public SimpleStringRecyclerViewAdapter(Activity context, ArrayList<DashboardKomisaris> items, int tipe) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            this.c = context;
            this.tipe = tipe;
            if(tipe == 0){
                for (DashboardKomisaris k:items){
                    if(nilai_tertinggi<k.getTotal_aktifitas())
                        nilai_tertinggi = k.getTotal_aktifitas();
                }
            }else {
                for (DashboardKomisaris k:items){
                    if(nilai_tertinggi<k.getTotal_rupiah())
                        nilai_tertinggi = k.getTotal_rupiah();
                }
            }

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
            holder.setIsRecyclable(false);
            holder.text1.setText(mValues.get(position).getNama_perusahaan());
            holder.text2.setVisibility(View.VISIBLE);

            double hasil=0;
            if(tipe==0){
                try {
                    hasil = (double) mValues.get(position).getTotal_aktifitas()/(double) nilai_tertinggi*100.0;
                }catch (Exception e){
                    Log.e("bind_error", e.toString());
                }
                holder.text2.setText(mValues.get(position).getTotal_aktifitas()+"");
            }else {
                try {
                    hasil = (double)mValues.get(position).getTotal_rupiah()/(double)nilai_tertinggi*100.0;
                }catch (Exception e){
                    Log.e("bind_error", e.toString());
                }
                holder.text2.setText("Rp. " + format("%,d", mValues.get(position).getTotal_rupiah()).replace(",", ".")+"");
            }
            holder.progress_nilai.setVisibility(View.INVISIBLE);
            holder.progress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
            holder.progress.setProgress((int)hasil);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent i = new Intent(context, AktifitasActivityKomisaris.class);
                    i.putExtra("KEY", "" + mValues.get(position).getId_perusahaan());
                    i.putExtra("NAMA_PERUSAHAAN", "" + mValues.get(position).getNama_perusahaan());
                    i.putExtra("ID_BUMN", "" + mValues.get(position).getId_perusahaan());
                    i.putExtra("GAMBAR_PERUSAHAAN", "" + mValues.get(position).getImage());
                    context.startActivity(i);

                }
            });
            String url = Config.URL_GAMBAR + mValues.get(position).getImage();

            Glide.with(holder.avatar.getContext())
                    .load(url)
                    .fitCenter()
                    .into(holder.avatar);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

}
