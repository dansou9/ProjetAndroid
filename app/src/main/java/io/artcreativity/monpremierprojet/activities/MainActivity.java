package io.artcreativity.monpremierprojet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.artcreativity.monpremierprojet.R;
import io.artcreativity.monpremierprojet.dao.DataBaseRoom;
import io.artcreativity.monpremierprojet.dao.ProductRoomDao;
import io.artcreativity.monpremierprojet.entities.Product;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    private final String TAG = MainActivity.class.getCanonicalName();
//
//    private TextInputEditText designationEditText;
//    private TextInputEditText descriptionEditText;
//    private TextInputEditText priceEditText;
//    private TextInputEditText quantityInStockEditText;
//    private TextInputEditText alertQuantityEditText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        designationEditText = findViewById(R.id.name);
//        descriptionEditText = findViewById(R.id.description);
//        priceEditText = findViewById(R.id.price);
//        quantityInStockEditText = findViewById(R.id.quantity_in_stock);
//        alertQuantityEditText = findViewById(R.id.alert_quantity);
////        findViewById(R.id.my_btn).setOnClickListener(new View.OnClickListener(){
////
////            @Override
////            public void onClick(View view) {
////                saveProduct(view);
////            }
////        });
//        findViewById(R.id.my_btn).setOnClickListener(this);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = new MenuInflater(this);
//        menuInflater.inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    public void saveProduct(View view) {
//        Log.d(TAG, "saveProduct: ");
//        Product product = new Product();
//        product.name = designationEditText.getText().toString();
//        product.description = descriptionEditText.getText().toString();
//        product.price = Double.parseDouble(priceEditText.getText().toString());
//        product.quantityInStock = Double.parseDouble(quantityInStockEditText.getText().toString());
//        product.alertQuantity = Double.parseDouble(alertQuantityEditText.getText().toString());
//        Log.e(TAG, "saveProduct: " + product);
//        Toast.makeText(getApplicationContext(), "J'ai clique", Toast.LENGTH_SHORT).show();
//
//        Intent intent = getIntent();
//        intent.putExtra("MY_PROD", product);
//        setResult(Activity.RESULT_OK, intent);
//        finish();
////        startActivity(intent);
//    }
//
//    @Override
//    public void onClick(View view) {
//        saveProduct(view);
//    }
private final String TAG = MainActivity.class.getCanonicalName();

    private TextInputEditText designationEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText priceEditText;
    private TextInputEditText quantityInStockEditText;
    private TextInputEditText alertQuantityEditText;
    private Button my_btn;

    private int INSERT_ACTION = 0;
    private int MODIFICATION_ACTION = 1;
//    private ProductDao productDao = new ProductDao(this);

    private ProductRoomDao productRoomDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        designationEditText = findViewById(R.id.name);
        descriptionEditText = findViewById(R.id.description);
        priceEditText = findViewById(R.id.price);
        quantityInStockEditText = findViewById(R.id.quantity_in_stock);
        alertQuantityEditText = findViewById(R.id.alert_quantity);
        my_btn = findViewById(R.id.my_btn);

        hasProductModificationAction();

    }

    @Override
    protected void onStart() {
        super.onStart();
        productRoomDao = DataBaseRoom.getInstance(this).productRoomDao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void saveProduct(int action, Integer id) {

        if (isEmptyInput(designationEditText, false)){
            Toast.makeText(getApplicationContext(), "Le nom de produit est vide", Toast.LENGTH_SHORT).show();
        }else if (isEmptyInput(descriptionEditText, false)){
            Toast.makeText(getApplicationContext(), "La description du produit est vide", Toast.LENGTH_SHORT).show();
        }else if (isEmptyInput(priceEditText, true)){
            Toast.makeText(getApplicationContext(), "Le prix du produit est invalid", Toast.LENGTH_SHORT).show();
        }else if (isEmptyInput(quantityInStockEditText, true)){
            Toast.makeText(getApplicationContext(), "Le quantité du produit est invalid", Toast.LENGTH_SHORT).show();
        }else if (isEmptyInput(alertQuantityEditText, true)){
            Toast.makeText(getApplicationContext(), "La quantité d'alert du produit est invalid", Toast.LENGTH_SHORT).show();
        }else if (Double.parseDouble(Objects.requireNonNull(alertQuantityEditText.getText()).toString()) > Double.parseDouble(Objects.requireNonNull(quantityInStockEditText.getText()).toString())){
            Toast.makeText(getApplicationContext(), "La quantité d'alert du produit est invalid", Toast.LENGTH_SHORT).show();
        }else {
            Product product = new Product();
            product.name = Objects.requireNonNull(designationEditText.getText()).toString();
            product.description = Objects.requireNonNull(descriptionEditText.getText()).toString();
            product.price = Double.parseDouble(Objects.requireNonNull(priceEditText.getText()).toString());
            product.quantityInStock = Double.parseDouble(Objects.requireNonNull(quantityInStockEditText.getText()).toString());
            product.alertQuantity = Double.parseDouble(Objects.requireNonNull(alertQuantityEditText.getText()).toString());
            Log.e(TAG, "saveProduct: " + product);
            Toast.makeText(getApplicationContext(), "Produit enregistré", Toast.LENGTH_SHORT).show();

            if (action == INSERT_ACTION){
                new Thread(() -> {
                    productRoomDao.insert(product);
                    product.id = productRoomDao.findByName(product.name, product.description).get(0).id;
                }).start();
            }else if (action == MODIFICATION_ACTION){
                product.id = id;
                new Thread(() -> productRoomDao.update(product)).start();
            }

            Intent intent = getIntent();
            intent.putExtra("product", product);
            setResult(RESULT_OK, intent);
            finish();

        }

    }

    public boolean isEmptyInput(TextInputEditText textInputEditText, boolean mustContainNumber){
        if (mustContainNumber){
            boolean isvalid;
            try {
                Double.parseDouble(Objects.requireNonNull(textInputEditText.getText()).toString());
                isvalid = true;
            }catch (NumberFormatException e){
                isvalid = false;
            }
            return Objects.requireNonNull(textInputEditText.getText()).toString().isEmpty() || textInputEditText.getText().toString().matches("^\\s+") || !isvalid;
        }else return Objects.requireNonNull(textInputEditText.getText()).toString().isEmpty() || textInputEditText.getText().toString().matches("^\\s+");

    }

    @Override
    public void onClick(View view) {

    }

    @SuppressLint("SetTextI18n")
    public void hasProductModificationAction(){
        Product product = (Product)getIntent().getSerializableExtra("product");
        if (product != null) {
            my_btn.setOnClickListener(view -> this.saveProduct(MODIFICATION_ACTION, product.id));
            my_btn.setText("Modifier");
            designationEditText.setText(product.name);
            descriptionEditText.setText(product.description);
            priceEditText.setText(String.valueOf(product.price));
            quantityInStockEditText.setText(String.valueOf(product.quantityInStock));
            alertQuantityEditText.setText(String.valueOf(product.alertQuantity));
        }else {
            my_btn.setOnClickListener(view -> this.saveProduct(INSERT_ACTION, null));
        }
    }
}