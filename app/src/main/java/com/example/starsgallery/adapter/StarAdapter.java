package com.example.starsgallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.starsgallery.R;
import com.example.starsgallery.beans.Star;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {
    private List<Star> stars;
    private List<Star> starsFull;
    private Context context;

    public StarAdapter(Context context, List<Star> stars) {
        this.context = context;
        this.stars = stars;
        this.starsFull = new ArrayList<>(stars);
    }

    @Override
    public StarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.star_item, parent, false);
        return new StarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StarViewHolder holder, int position) {
        Star s = stars.get(position);
        holder.name.setText(s.getName().toUpperCase());
        holder.rating.setRating(s.getRating());
        holder.tvId.setText(String.valueOf(s.getId()));

        String img = s.getImg();
        if (img != null && img.startsWith("http")) {
            Glide.with(context).load(img).into(holder.img);
        } else {
            int resId = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
            Glide.with(context).load(resId != 0 ? resId : R.drawable.star).into(holder.img);
        }

        holder.itemView.setOnClickListener(v -> {
            View popupView = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null);
            CircleImageView imgView = popupView.findViewById(R.id.img);
            RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);
            TextView idText = popupView.findViewById(R.id.idss);

            idText.setText(String.valueOf(s.getId()));
            
            if (img != null && img.startsWith("http")) {
                Glide.with(context).load(img).into(imgView);
            } else {
                int resId = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
                Glide.with(context).load(resId != 0 ? resId : R.drawable.star).into(imgView);
            }

            ratingBar.setRating(s.getRating());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Notez :")
                    .setMessage("Donner une note entre 1 et 5 :")
                    .setView(popupView)
                    .setPositiveButton("VALIDER", (dialog, which) -> {
                        float newRating = ratingBar.getRating();
                        s.setRating(newRating);
                        notifyItemChanged(holder.getAdapterPosition());
                    })
                    .setNegativeButton("ANNULER", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() { return stars.size(); }

    @Override
    public Filter getFilter() {
        return starFilter;
    }

    private Filter starFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Star> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(starsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Star item : starsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            stars.clear();
            stars.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class StarViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView name;
        RatingBar rating;
        TextView tvId;

        StarViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgStar);
            name = itemView.findViewById(R.id.tvName);
            rating = itemView.findViewById(R.id.rating);
            tvId = itemView.findViewById(R.id.tvId);
        }
    }
}