package com.example.nikitasotnikov.inventoryapp;

/**
 * Created by NikitaSotnikov on 18.10.2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikitasotnikov.inventoryapp.data.ProductContract.ProductEntry;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.list_item_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.list_item_price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.list_item_quantity);
        ImageView productImageView = (ImageView) view.findViewById(R.id.list_item_image);
        ImageButton saleImageButton = (ImageButton) view.findViewById(R.id.list_item_sale_button);


        // Find the columns of product attributes that we're interested in
        int idIndex = cursor.getColumnIndex(ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the product attributes from the Cursor for the current product
        final String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);

        // Get Image if available
        byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE));
        Bitmap productImage = ImageHelper.convertBlobToBitmap(imageByteArray);

        // set Image
        productImageView.setImageBitmap(productImage);

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);
        //itemImageView.setImageResource(productImage);


        //Get data from cursor
        int id = cursor.getInt(idIndex);
        final Uri uri = Uri.parse(ProductEntry.CONTENT_URI + "/" + id);

        saleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues updateValues = new ContentValues();
                int productQuantityChange = Integer.parseInt(productQuantity);
                if (productQuantityChange - 1 < 0) {
                    Toast.makeText(context, context.getString(R.string.catalog_sell_one_item_change_quantity),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                updateValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantityChange - 1);
                int rowsAffected = context.getContentResolver().update(uri, updateValues, null, null);

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(context, context.getString(R.string.catalog_sell_failed_one_item),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(context, context.getString(R.string.catalog_sell_one_item) + " " + productName,
                            Toast.LENGTH_SHORT).show();
                }
                quantityTextView.setText(String.valueOf(productQuantityChange - 1));
            }
        });
    }
}

