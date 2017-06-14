/*package com.simonag.simonag.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.simonag.simonag.R;
import com.simonag.simonag.model.AktivitasModel;
import com.simonag.simonag.utils.EditHapusInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

*/
/*
public class DataAktivitasAdapter extends RecyclerView.Adapter<DataAktivitasAdapter.ViewHolder> {

    Context context;
    List<Aktifitas> aktivitasModels;
    EditHapusInterface listener;

    public DataAktivitasAdapter(Context context, List<Aktifitas> aktivitasModels, EditHapusInterface listener) {
        this.aktivitasModels = aktivitasModels;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aktivitas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Aktifitas aktivitas = aktivitasModels.get(position);
        holder.tvNama.setText(aktivitas.nama);
        holder.tvDuedate.setText(aktivitas.duedate);
        holder.tvTarget.setText(aktivitas.target);
        holder.tvRevenue.setText(aktivitas.revenue);
        holder.btnEdit.setImageDrawable(new IconicsDrawable(context)
                .icon(FontAwesome.Icon.faw_pencil_square)
                .color(Color.WHITE));
        holder.btnHapus.setImageDrawable(new IconicsDrawable(context)
                .icon(FontAwesome.Icon.faw_trash)
                .color(Color.WHITE));
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.edit(position);
            }
        });
        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.hapus(position);
            }
        });
        Drawable img;
        img = new IconicsDrawable(context)
                .icon(FontAwesome.Icon.faw_calendar)
                .color(ResourcesCompat.getColor(context.getResources(), R.color.primary, null))
                .actionBar();
        img.setBounds(0, 0, 50, 50);
        holder.tvDuedate.setCompoundDrawables(img, null, null, null);
        img = new IconicsDrawable(context)
                .icon(FontAwesome.Icon.faw_star)
                .color(ResourcesCompat.getColor(context.getResources(), R.color.primary, null))
                .actionBar();
        img.setBounds(0, 0, 50, 50);
        holder.tvTarget.setCompoundDrawables(img, null, null, null);
        img = new IconicsDrawable(context)
                .icon(FontAwesome.Icon.faw_money)
                .color(ResourcesCompat.getColor(context.getResources(), R.color.primary, null))
                .actionBar();
        img.setBounds(0, 0, 50, 50);
        holder.tvRevenue.setCompoundDrawables(img, null, null, null);
    }

    @Override
    public int getItemCount() {
        return aktivitasModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_nama)
        TextView tvNama;
        @BindView(R.id.tv_duedate)
        TextView tvDuedate;
        @BindView(R.id.tv_target)
        TextView tvTarget;
        @BindView(R.id.tv_revenue)
        TextView tvRevenue;
        @BindView(R.id.btn_edit)
        ImageView btnEdit;
        @BindView(R.id.btn_hapus)
        ImageView btnHapus;
        @BindView(R.id.cv_aktivitas)
        CardView cvAktivitas;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}*/
