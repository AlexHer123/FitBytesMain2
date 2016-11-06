package com.example.alex.fitbytes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Austin on 11/5/2016.
 */

public class IngredientAdapter extends ArrayAdapter<IngredientItem> implements Filterable
{
    public Context context;
    public ArrayList<IngredientItem> ingredients;
    public ArrayList<IngredientItem> orig;

    public IngredientAdapter(Context context, ArrayList<IngredientItem> ingredients)
    {
        super(context,0,ingredients);
        this.context = context;
        this.ingredients = ingredients;
    }

    private class IngredientHolder
    {
        TextView name;
        TextView amount;
        TextView measurement;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Get the data item for this position
        IngredientItem it = getItem(position);
        IngredientHolder holder;

        //Check if an existing view is being reused, otherwise inflate a new view from custom row layout
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pantry_list_row,parent,false);
            holder = new IngredientHolder();
            holder.name = (TextView) convertView.findViewById(R.id.ingredientListItemNameID);
            holder.amount = (TextView) convertView.findViewById(R.id.ingredientAmountListItemID);
            holder.measurement = (TextView) convertView.findViewById(R.id.ingredientMeasurementListItemID);
            convertView.setTag(holder);
        }
        else
        {
            holder = (IngredientHolder) convertView.getTag();
        }

        holder.name.setText(ingredients.get(position).getName());
        holder.amount.setText(String.valueOf(ingredients.get(position).getAmount()));
        holder.measurement.setText(String.valueOf(ingredients.get(position).getMeasurement()));

        //Grab reference of views so we can populate them with specific note row data
        TextView ingredientName = (TextView) convertView.findViewById(R.id.ingredientListItemNameID);
        TextView ingredientAmount = (TextView) convertView.findViewById(R.id.ingredientAmountListItemID);
        TextView ingredientMeasurement = (TextView) convertView.findViewById(R.id.ingredientMeasurementListItemID);

        //Fill each new referenced view with data associated with note it's referencing
        ingredientName.setText(it.getName());
        ingredientAmount.setText(""+it.getAmount());
        switch(it.getMeasurement()) {
            case CUP:
                ingredientMeasurement.setText("cup");
                break;
            case GRAM:
                ingredientMeasurement.setText("gram");
                break;
            case FLOZ:
                ingredientMeasurement.setText("fl. oz.");
                break;
            case OZ:
                ingredientMeasurement.setText("oz");
                break;
            case LB:
                ingredientMeasurement.setText("lb");
                break;
            case TBSP:
                ingredientMeasurement.setText("tbsp");
                break;
            case TSP:
                ingredientMeasurement.setText("tsp");
                break;
            case NONE:
                ingredientMeasurement.setText("");
                break;
            default:
                ingredientMeasurement.setText("");
                break;
        }

        //Now that we modified the view to display appropriate data,
        //return it so it will be displayed.
        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<IngredientItem> results = new ArrayList<>();
                if (orig == null)
                    orig = ingredients;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final IngredientItem g : orig) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                ingredients = (ArrayList<IngredientItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return ingredients.size();
    }

    @Override
    public long getItemId(int position)
    {
        return ingredients.get(position).getIngredientID();
    }

    @Override
    public IngredientItem getItem(int position)
    {
        return ingredients.get(position);
    }
}