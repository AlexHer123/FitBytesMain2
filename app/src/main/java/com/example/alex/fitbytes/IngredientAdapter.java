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

    public static class IngredientHolder
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
            //If we don't have a view that is being used create one, and make sure you create a
            //view holder along with it to save our view references to.
            holder = new IngredientHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pantry_list_row,parent,false);

            //Set our views to our view holder so that we no longer have to go back and use find view
            //by id every time we have a new row
            holder.name = (TextView) convertView.findViewById(R.id.ingredientListItemNameID);
            holder.amount = (TextView) convertView.findViewById(R.id.ingredientAmountListItemID);
            holder.measurement = (TextView) convertView.findViewById(R.id.ingredientMeasurementListItemID);

            //Use setTag to remember our view holder which is holding our references to our widgets
            convertView.setTag(holder);
        }
        else
        {
            holder = (IngredientHolder) convertView.getTag();
        }

        holder.name.setText(ingredients.get(position).getName());
        holder.amount.setText(String.valueOf(ingredients.get(position).getAmount()));
        switch(ingredients.get(position).getMeasurement()) {
            case CUP:
                holder.measurement.setText("cup");
                break;
            case GRAM:
                holder.measurement.setText("gram");
                break;
            case FLOZ:
                holder.measurement.setText("fl. oz.");
                break;
            case OZ:
                holder.measurement.setText("oz");
                break;
            case LB:
                holder.measurement.setText("lb");
                break;
            case TBSP:
                holder.measurement.setText("tbsp");
                break;
            case TSP:
                holder.measurement.setText("tsp");
                break;
            case NONE:
                holder.measurement.setText("");
                break;
            default:
                holder.measurement.setText("");
                break;
        }

        //Now that we modified the view to display appropriate data,
        //return it so it will be displayed.
        return convertView;
    }

    public Filter getFilter()
    {
        return new Filter()
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<IngredientItem> results = new ArrayList<>();
                if (orig == null)
                    orig = ingredients;
                if (constraint != null)
                {
                    if (orig != null && orig.size() > 0)
                    {
                        for (final IngredientItem g : orig)
                        {
                            if (g.getName().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
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